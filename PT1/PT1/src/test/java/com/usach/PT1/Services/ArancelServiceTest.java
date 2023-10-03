package com.usach.PT1.Services;

import com.usach.PT1.Models.*;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional

public class ArancelServiceTest {
    @Autowired
    ArancelService arancelService;
    @Autowired
    EstudianteService estudianteService;
    @Autowired
    PagoService pagoService;

    @Test
    void CrearMatriculaEstudiante(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2020, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<>());
        estudiante.setCuotas(new ArrayList<>());
        estudianteService.CrearEstudiante(estudiante);

        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);

        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);

        // Precio de Colegio privado(0%) con 4 cuotas y 1-2 anios(8%) de agreso = 1_500_000 - 1_500_000 * 0.08 = 1_380_000
        assertEquals(1_380_000, estudiante.getArancel().getMontoTotalArancel());


        estudianteService.eliminarEstudiante(estudiante);

    }

    @Test
    void CrearMatriculaEstudianteColegioPrivado(){
        // 3 Cuotas y 3-4 anios de egreso
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

        arancelService.crearMatricula(3, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        // Precio de Colegio privado con 3 cuotas y 3-4 anios de agreso = 1_500_000 - 1_500_000 * 0.03 = 1_440_000
        assertEquals(1_440_000, estudiante.getArancel().getMontoTotalArancel());
        assertEquals(480_000, estudiante.getDeuda().getPrecioCuota());

        estudianteService.eliminarEstudiante(estudiante);

    }

    @Test
    void CrearMatriculaEstudianteColegioPrivado2(){

        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2021, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        // 4 cuotas y 1-2 anios de egreso
        estudianteService.CrearEstudiante(estudiante);
        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        // Precio de Colegio privado con 4 cuotas y 1-2 anios de agreso = 1_500_000 - 1_500_000 * 0.08 = 1_380_000
        assertEquals(1_380_000, estudiante.getArancel().getMontoTotalArancel());
        assertEquals(345_000, estudiante.getDeuda().getPrecioCuota());
        estudianteService.eliminarEstudiante(estudiante);
    }

    @Test
    void CrearMatriculaEstudianteColegioPrivado3(){

        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2023, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        // 4 cuotas y <1 anios de egreso
        estudianteService.CrearEstudiante(estudiante);
        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        // Precio de Colegio privado con 4 cuotas y <1 anios de agreso = 1_500_000 - 1_500_000 * 0.15 = 1_275_000
        assertEquals(1_275_000, estudiante.getArancel().getMontoTotalArancel());
        assertEquals(318_750, estudiante.getDeuda().getPrecioCuota());
        estudianteService.eliminarEstudiante(estudiante);
    }
    @Test
    void CrearMatriculaEstudianteNumeroInvalidoCuotas(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2023, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        // 4 cuotas y <1 anios de egreso
        estudianteService.CrearEstudiante(estudiante);
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(5, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(0, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(-1, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(-1, estudiante.getRut_estudiante(), EMedioPago.CONTADO);
        });
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(1, estudiante.getRut_estudiante(), EMedioPago.CONTADO);
        });
    }
    @Test
    void CrearMatriculaEstudianteColegioPrivado5(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2023, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        // Sin cuotas
        arancelService.crearMatricula(0, estudiante.getRut_estudiante(), EMedioPago.CONTADO);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        //Pago contado 1_500_000 * 0.5 = 750_000
        assertEquals(750_000, estudiante.getArancel().getMontoTotalArancel());
        assertTrue(estudiante.getArancel().isEstadoDePagoArancel());


    }

    @Test
    void CrearMatriculaEstudianteColegioMunicipal(){
        // 3 Cuotas y 3-4 anios de egreso
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2020, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.MUNICIPAL)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);

        arancelService.crearMatricula(3, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);

        // Precio de Colegio municipal(20%) con 3 cuotas y 3-4 anios de agreso(4%) = 1_500_000 - 1_500_000 * 0.24 = 1_140_000
        assertEquals(1_140_000, estudiante.getArancel().getMontoTotalArancel());
        assertEquals(380_000, estudiante.getDeuda().getPrecioCuota());

        estudianteService.eliminarEstudiante(estudiante);

    }

    @Test
    void CrearMatriculaEstudianteColegioMunicipalCuotasInvalidas(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2020, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.MUNICIPAL)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(11, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(-1, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(0, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });

    }

    @Test
    void CrearMatriculaEstudianteColegioSubvencioado(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2023, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.SUBVENCIONADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        // 4 cuotas y <1 anios de egreso
        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        // Precio de Colegio subvencionado(10%) con 4 cuotas y <1 anios de agreso(15%) = 1_500_000 - 1_500_000 * 0.25 = 1_125_000
        assertEquals(1_125_000, estudiante.getArancel().getMontoTotalArancel());
        assertEquals(281_250, estudiante.getDeuda().getPrecioCuota());

    }
    @Test
    void CrearMatriculaEstudianteColegioSubvencioado2(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2023, 1, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.SUBVENCIONADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudiante.setPagos(new ArrayList<Pago>());
        estudiante.setCuotas(new ArrayList<Cuota>());
        estudianteService.CrearEstudiante(estudiante);
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(8, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(-1, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });
        assertThrows(Throwable.class, () -> {
            arancelService.crearMatricula(0, estudiante.getRut_estudiante(), EMedioPago.CUOTAS);
        });
    }




}
