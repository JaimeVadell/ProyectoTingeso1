package com.usach.PT1.Services;

import com.usach.PT1.Models.*;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Repositories.MatriculaRepository;
import com.usach.PT1.Repositories.PagoRepository;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Repository
public class MatriculaService {
    @Autowired
    MatriculaRepository matriculaRepository;

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    PagoRepository pagoRepository;

    @Autowired
    DeudaService deudaService;

    public String crearMatricula(int numeroCuotas, String rutEstudiante, EPago pago){
        VerificadorRut verificadorRut = new VerificadorRut();
        rutEstudiante = verificadorRut.validarRut(rutEstudiante);

        if(rutEstudiante.equals("")){
            throw new IllegalArgumentException("Rut Invalido");
        }

        if(numeroCuotas > 10 || numeroCuotas < 0 || (numeroCuotas < 0 && pago == EPago.CONTADO)){
            throw new IllegalArgumentException("Numero Cuotas Invalidas");
        }

        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(rutEstudiante);
        if(!estudianteOptional.isPresent()){
            throw new IllegalArgumentException("Estudiante no existe");
        }
        if (estudianteOptional.get().getMatricula() != null){
            throw new IllegalArgumentException("Estudiante ya tiene matricula");
        }
        else{
            Estudiante estudiante = estudianteOptional.get();
            ETipoColegio tipoColegio = estudiante.getTipoColegio();

            LocalDate anioEgreso = estudiante.getAnioEgreso();
            LocalDate fechaActual = LocalDate.now();
            long diferenciaEnDias = ChronoUnit.DAYS.between(anioEgreso, fechaActual);
            double aniosDesdeEgreso = diferenciaEnDias / 365.25;
            boolean estadoMatricula = false;
            int MontoMatricula = 1_570_000;
            int Descuento = 0;
            if (pago == EPago.CUOTAS) {
                if (tipoColegio == ETipoColegio.MUNICIPAL) {
                    if (numeroCuotas > 10) {
                        return "Numero de cuotas invalido para colegio MUNICIPAL";
                    }
                    Descuento += 20;
                }
                else if (tipoColegio == ETipoColegio.SUBVENCIONADO) {
                    if (numeroCuotas > 7) {
                        return "Numero de cuotas invalido para colegio SUBVENCIONADO";
                    }
                    Descuento += 10;
                }
                else if (tipoColegio == ETipoColegio.PRIVADO) {
                    if (numeroCuotas > 4) {
                        return "Numero de cuotas invalido para colegio PARTICULAR";
                    }
                }
                if(aniosDesdeEgreso < 1){
                    Descuento += 15;
                }
                else if(aniosDesdeEgreso <= 2){
                    Descuento += 8;
                }
                else if(aniosDesdeEgreso <=4){
                    Descuento += 4;
                }
            }
            else if(pago == EPago.CONTADO){
                Descuento += 50;
                Pago pagoContado = Pago.builder()
                        .fechaPago(fechaActual)
                        .montoPagado(MontoMatricula - (MontoMatricula * Descuento / 100))
                        .estudiante(estudiante)
                        .build();
                pagoRepository.save(pagoContado);
                List<Pago> pagos = estudiante.getPagos();
                pagos.add(pagoContado);
                estudiante.setPagos(pagos);
                estudianteRepository.save(estudiante);
                estadoMatricula = true;
            }

            int precioMatricula = MontoMatricula - (MontoMatricula * Descuento / 100);
            Matricula matricula = Matricula.builder()
                    .estadoMatricula(estadoMatricula)
                    .fechaMatricula(fechaActual)
                    .montoMatricula(precioMatricula)
                    .numeroCuotas(numeroCuotas)
                    .pago(pago)
                    .estudiante(estudiante)
                    .build();
            matriculaRepository.save(matricula);
            estudiante.setMatricula(matricula);
            estudianteRepository.save(estudiante);
            int deuda = precioMatricula;
            if(pago == EPago.CONTADO){
                deuda = 0;
            }
            if(numeroCuotas == 0){
                deudaService.CrearDeudaEstudiante(deuda, numeroCuotas, 0, estudiante);
            }
            else if(numeroCuotas > 0){
                deudaService.CrearDeudaEstudiante(deuda, numeroCuotas, deuda/numeroCuotas, estudiante);

            }
            return "Matricula Creada";
        }
    }

    public Optional<Matricula> obtenerMatriculaPorRut(String rutEstudiante) {
        Optional<Matricula> matriculaOptional = Optional.empty();
        VerificadorRut verificadorRut = new VerificadorRut();
        rutEstudiante = verificadorRut.validarRut(rutEstudiante);

        if(rutEstudiante.equals("")){
            return matriculaOptional;
        }

        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(rutEstudiante);
        if(!estudianteOptional.isPresent() || estudianteOptional.get().getMatricula() == null){
            return matriculaOptional;
        }
        else{
            Estudiante estudiante = estudianteOptional.get();
            Matricula matricula = estudiante.getMatricula();
            return Optional.of(matricula);
        }
    }






}
