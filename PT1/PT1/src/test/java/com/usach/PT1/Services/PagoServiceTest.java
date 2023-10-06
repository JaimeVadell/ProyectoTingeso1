package com.usach.PT1.Services;

import com.usach.PT1.Models.*;
import com.usach.PT1.Repositories.CuotasRepository;
import com.usach.PT1.Repositories.PagoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
public class PagoServiceTest {
    @Autowired
    EstudianteService estudianteService;
    @Autowired
    PagoService pagoService;
    @Autowired
    ArancelService arancelService;
    @Autowired
    PagoRepository pagoRepository;
    @Autowired
    CuotasRepository cuotasRepository;

/*    @Test
    void CrearPago(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        arancelService.crearMatricula(4,"20.107.536-K", EMedioPago.CUOTAS);
        pagoService.RealiazarPago(360_000, "20.107.536-K");
        List<Pago> pagosEstudiante = pagoRepository.findByEstudiante(estudiante);
        Pago PagoMasAntiguo = pagosEstudiante.stream()
                .filter(pago -> pago.getTipoPago() == ETipoPago.CUOTA_ARANCEL) // Filtrar por cuotas de arancel
                .map(Pago::getCuotaPagada) // Obtener la cuota asociada a cada pago
                .min(Comparator.comparing(Cuota::getPlazoMaximoPago)).orElse(null).getPago();
        assertEquals(360_000, PagoMasAntiguo.getMontoPagado());

        // Pago invalido por monto
        assertThrows(Throwable.class, () -> {
            pagoService.RealiazarPago(0, "20.107.536-K");
        });
        assertThrows(Throwable.class, () -> {
            pagoService.RealiazarPago(123, "20.107.536-K");
        });

    }

    @Test
    void CrearPagoInvalido(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        arancelService.crearMatricula(0,"20.107.536-K", EMedioPago.CONTADO);
        assertThrows(Throwable.class, () -> {
            pagoService.RealiazarPago(360_000, "20.107.536-1");
        });
        assertThrows(Throwable.class, () -> {
            pagoService.RealiazarPago(0, "20.107.536-K");
        });

    }*/


    @Test
    void PagarCuota(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        arancelService.crearMatricula(4,"20.107.536-K", EMedioPago.CUOTAS);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        pagoService.pagarCuota("20.107.536-K");
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        List<Pago> pagosEstudiante = estudiante.getPagos();

        // Estudiante invalido
        assertThrows(Throwable.class, () -> {
            pagoService.pagarCuota("20.107.536-1");
        });

    }

    @Test
    void PagarCuotaEstudianteSin(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        arancelService.crearMatricula(0,"20.107.536-K", EMedioPago.CONTADO);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        assertThrows(Throwable.class, () -> {
            pagoService.pagarCuota("20.107.536-K");
        });

    }
}
