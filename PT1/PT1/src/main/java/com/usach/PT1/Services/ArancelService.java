package com.usach.PT1.Services;

import com.usach.PT1.Models.*;
import com.usach.PT1.Repositories.*;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class ArancelService {
    @Autowired
    ArancelRepository arancelRepository;

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    PagoRepository pagoRepository;

    @Autowired
    DeudaService deudaService;

    @Autowired
    CuotasService cuotasService;

    @Autowired
    DeudaRepository deudaRepository;

    @Autowired
    CuotasRepository cuotasRepository;

    public String crearMatricula(int numeroCuotas, String rutEstudiante, EMedioPago pago){
        //Verificaciones de rut y numero de cuotas
        VerificadorRut verificadorRut = new VerificadorRut();
        rutEstudiante = verificadorRut.validarRut(rutEstudiante);

        if(rutEstudiante.equals("")){
            throw new IllegalArgumentException("Rut Invalido");
        }

        if(numeroCuotas > 10 || numeroCuotas < 0 || (numeroCuotas < 0 && pago == EMedioPago.CONTADO)){
            throw new IllegalArgumentException("Numero Cuotas Invalidas");
        }

        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(rutEstudiante);
        if(!estudianteOptional.isPresent()){
            throw new IllegalArgumentException("Estudiante no existe");
        }
        if (estudianteOptional.get().getArancel() != null){
            throw new IllegalArgumentException("Estudiante ya tiene arancel");
        }

        //Creacion de arancel
        else{
            Estudiante estudiante = estudianteOptional.get();
            ETipoColegio tipoColegio = estudiante.getTipoColegio();

            LocalDate anioEgreso = estudiante.getAnioEgreso();
            LocalDate fechaActual = LocalDate.now();
            long diferenciaEnDias = ChronoUnit.DAYS.between(anioEgreso, fechaActual);
            double aniosDesdeEgreso = diferenciaEnDias / 365.25;
            boolean estadoArancel = false;
            int arancelEstudio = 1_500_000;
            int precioMatricula = 70_000;


            // Calculo descuento arancel en base a tipo de colegio para pago en cuotas
            int Descuento = 0;
            if (pago == EMedioPago.CUOTAS) {
                if (tipoColegio == ETipoColegio.MUNICIPAL) {
                    Descuento += 20;
                }
                else if (tipoColegio == ETipoColegio.SUBVENCIONADO) {
                    if (numeroCuotas > 7) {
                        throw new RuntimeException("Numero de cuotas invalido para colegio SUBVENCIONADO");
                    }
                    Descuento += 10;
                }
                else if (tipoColegio == ETipoColegio.PRIVADO) {
                    if (numeroCuotas > 4) {
                        throw new RuntimeException("Numero de cuotas invalido para colegio PRIVADO");
                    }
                }
                //Agregar descuentos en base a anios desde egreso
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
            //Caso de pago al contado
            else if(pago == EMedioPago.CONTADO){
                Descuento += 50;
                // Pago total del arancel
                Pago pagoContado = Pago.builder()
                        .fechaPago(fechaActual)
                        .montoPagado(arancelEstudio - (arancelEstudio * Descuento / 100))
                        .estudiante(estudiante)
                        .tipoPago(ETipoPago.CUOTA_ARANCEL)
                        .build();
                pagoRepository.save(pagoContado);
                List<Pago> pagos = estudiante.getPagos();
                pagos.add(pagoContado);
                estudiante.setPagos(pagos);
                estudianteRepository.save(estudiante);
                estadoArancel = true;
            }
            //Pago de matricula
            pagarMatricula(estudiante, precioMatricula);

            int precioTotal = arancelEstudio - ((arancelEstudio * Descuento) / 100);
            Arancel arancel = Arancel.builder()
                    .estadoDePagoArancel(estadoArancel)
                    .fechaCreacionArancel(fechaActual)
                    .montoTotalArancel(precioTotal)
                    .numeroCuotas(numeroCuotas)
                    .pago(pago)
                    .estudiante(estudiante)
                    .build();
            arancelRepository.save(arancel);
            estudiante.setArancel(arancel);
            estudianteRepository.save(estudiante);
            int deuda = precioTotal;
            if(pago == EMedioPago.CONTADO){
                deuda = 0;
            }
            if(numeroCuotas == 0){
                return "Arancel Creado";
            }


            deudaService.CrearDeudaEstudiante(deuda, numeroCuotas, deuda/numeroCuotas, estudiante);

            cuotasService.generarCuotasArancel(arancel, deuda/numeroCuotas);

            return "Arancel Creado";
        }
    }


    private void pagarMatricula(Estudiante estudiante, int precioMatricula){
        Pago pago = Pago.builder()
                .fechaPago(LocalDate.now())
                .montoPagado(precioMatricula)
                .tipoPago(ETipoPago.MATRICULA)
                .estudiante(estudiante)
                .build();
        List<Pago> pagosEstudiante = estudiante.getPagos();
        pagosEstudiante.add(pago);
        estudiante.setPagos(pagosEstudiante);
        estudianteRepository.save(estudiante);
        pagoRepository.save(pago);

    }
    public Optional<Arancel> obtenerArancelPorRut(String rutEstudiante) {
        Optional<Arancel> arancelOptional = Optional.empty();
        VerificadorRut verificadorRut = new VerificadorRut();
        rutEstudiante = verificadorRut.validarRut(rutEstudiante);

        if(rutEstudiante.equals("")){
            return arancelOptional;
        }

        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(rutEstudiante);
        if(!estudianteOptional.isPresent() || estudianteOptional.get().getArancel() == null){
            return arancelOptional;
        }
        else{
            Estudiante estudiante = estudianteOptional.get();
            Arancel arancel = estudiante.getArancel();
            return Optional.of(arancel);
        }
    }


    public void arancelPagado(Estudiante estudiante) {
        Arancel arancel = estudiante.getArancel();
        arancel.setEstadoDePagoArancel(true);
        arancelRepository.save(arancel);
    }


    public void reCalcularArancel(){
        List <Estudiante> estudiantes = estudianteRepository.findAll();
        for(Estudiante estudiante: estudiantes) {

            if(!estudiante.getArancel().isEstadoDePagoArancel() ||!estudiante.getCuotas().isEmpty()) {
                List<Cuota> cuotasEstudiante = estudiante.getCuotas();
                // Ordenar cuotas por fecha de vencimiento
                Collections.sort(cuotasEstudiante, Comparator.comparing(Cuota::getPlazoMaximoPago));
                Optional<Cuota> cuotaMasCercana = cuotasEstudiante.stream()
                        .filter(cuota -> !cuota.isPagada())
                        .findFirst();
                if (cuotaMasCercana.isEmpty()) {
                    continue;
                }
                LocalDate fechaActual = LocalDate.now();
                LocalDate fechaVencimiento = cuotaMasCercana.get().getPlazoMaximoPago();
                if(fechaActual.isAfter(fechaVencimiento)){
                    int diferenciaEnDias = (int) ChronoUnit.DAYS.between(fechaActual, fechaVencimiento);
                    int mesesDeRetrasoActual = (diferenciaEnDias / 30) +1;
                    int mesesDeRetrasoSistema = estudiante.getDeuda().getCuotasConRetraso();
                    if(mesesDeRetrasoSistema >= mesesDeRetrasoActual){
                        continue;
                    }
                    if(mesesDeRetrasoActual == 1){actuliazarDeudasyCuotas(estudiante, 3);}
                    else if(mesesDeRetrasoActual == 2){actuliazarDeudasyCuotas(estudiante, 6);}
                    else if(mesesDeRetrasoActual == 3){actuliazarDeudasyCuotas(estudiante, 9);}
                    else{
                        actuliazarDeudasyCuotas(estudiante, 15);
                    }



                }
            }
        }
    }

    public void actuliazarDeudasyCuotas(Estudiante estudiante, int interes){
        Deuda deuda = estudiante.getDeuda();
        int nuevoPrecioCuota = (deuda.getPrecioCuota() * interes) / 100;
        deuda.setPrecioCuota(nuevoPrecioCuota);
        deuda.setMontoDeuda(deuda.getCuotasRestantes() * nuevoPrecioCuota);
        deuda.setCuotasConRetraso(deuda.getCuotasConRetraso() + 1);
        deuda.setCuotasConRetrasoHistorico(deuda.getCuotasConRetrasoHistorico() + 1);
        deudaRepository.save(deuda);

        // Actuliazar cuotas
        List<Cuota> cuotas = estudiante.getCuotas();
        for(Cuota cuota: cuotas){
            if(!cuota.isPagada()){
                cuota.setMontoCuota(nuevoPrecioCuota);
                cuotasRepository.save(cuota);
            }
        }

    }
}
