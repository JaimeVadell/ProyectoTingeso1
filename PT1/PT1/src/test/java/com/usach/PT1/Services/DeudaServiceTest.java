package com.usach.PT1.Services;

import com.usach.PT1.Models.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
public class DeudaServiceTest {
    @Autowired
    EstudianteService estudianteService;
    @Autowired
    DeudaService deudaService;
    @Autowired
    ArancelService arancelService;

    @Test
    void CrearDeudaEstudiante(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudianteService.CrearEstudiante(estudiante);
        deudaService.CrearDeudaEstudiante(1_440_000, 4, 360_000, estudiante);
        assertEquals(1_440_000, estudiante.getDeuda().getMontoDeuda());
        assertEquals(4, estudiante.getDeuda().getCuotasRestantes());
        assertEquals(0, estudiante.getDeuda().getCuotasConRetrasoHistorico());
        assertEquals(360_000, estudiante.getDeuda().getPrecioCuotaInicial());
        assertEquals(360_000, estudiante.getDeuda().getPrecioCuota());
        assertEquals(0, estudiante.getDeuda().getCuotasConRetraso());


    }

    @Test
    void ActualiazarDeuda(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudianteService.CrearEstudiante(estudiante);
        deudaService.CrearDeudaEstudiante(1_440_000, 4, 360_000, estudiante);
        deudaService.actualizarDeuda(estudiante, 360_000);
        assertEquals(1_080_000, estudiante.getDeuda().getMontoDeuda());
        assertEquals(3, estudiante.getDeuda().getCuotasRestantes());
        assertEquals(0, estudiante.getDeuda().getCuotasConRetrasoHistorico());
        assertEquals(360_000, estudiante.getDeuda().getPrecioCuotaInicial());
        assertEquals(360_000, estudiante.getDeuda().getPrecioCuota());
        assertEquals(0, estudiante.getDeuda().getCuotasConRetraso());

    }

    @Test
    void ActualiazarDeudaConRetraso(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudianteService.CrearEstudiante(estudiante);
        deudaService.CrearDeudaEstudiante(1_440_000, 4, 360_000, estudiante);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        estudiante.getDeuda().setCuotasConRetraso(1);
        estudiante.getDeuda().setCuotasConRetrasoHistorico(1);
        estudianteService.actualizarEstudiante(estudiante);

        deudaService.actualizarDeuda(estudiante, 360_000);
        assertEquals(1_080_000, estudiante.getDeuda().getMontoDeuda());
        assertEquals(3, estudiante.getDeuda().getCuotasRestantes());
        assertEquals(1, estudiante.getDeuda().getCuotasConRetrasoHistorico());
        assertEquals(360_000, estudiante.getDeuda().getPrecioCuotaInicial());
        assertEquals(360_000, estudiante.getDeuda().getPrecioCuota());
        assertEquals(0, estudiante.getDeuda().getCuotasConRetraso());
    }

    @Test
    void FinaliazarDeuda(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2020, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        arancelService.crearMatricula(1,"20.107.536-K", EMedioPago.CUOTAS);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        deudaService.actualizarDeuda(estudiante, 1_440_000);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        assertEquals(0, estudiante.getDeuda().getMontoDeuda());
        assertEquals(0, estudiante.getDeuda().getCuotasRestantes());
        assertTrue(estudiante.getArancel().isEstadoDePagoArancel());

    }

    @Test
    void CuotaInvalidaDeuda(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2020, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudianteService.CrearEstudiante(estudiante);
        deudaService.CrearDeudaEstudiante(1_440_000, 4, 360_000, estudiante);
        assertThrows(Throwable.class, () -> {
            deudaService.actualizarDeuda(estudiante, 1_000_000);
        });
    }

}
