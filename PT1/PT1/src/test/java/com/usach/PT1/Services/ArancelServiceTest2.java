package com.usach.PT1.Services;

import com.usach.PT1.Models.EMedioPago;
import com.usach.PT1.Models.ETipoColegio;
import com.usach.PT1.Models.Estudiante;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ArancelServiceTest2 {
    @Autowired
    ArancelService arancelService;
    @Autowired
    EstudianteService estudianteService;
    @Autowired
    PagoService pagoService;


    @Test
    void CalcularUnMesDeAtraso(){
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

        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS); // 1_380_000 // Precio Cuota = 345_000
        arancelService.reCalcularArancel(LocalDate.of(2023, 11, 25));
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        assertEquals(355_350, estudiante.getCuotas().get(0).getMontoCuota());
        assertEquals(355_350, estudiante.getCuotas().get(1).getMontoCuota());
        assertEquals(355_350, estudiante.getCuotas().get(2).getMontoCuota());
        assertEquals(355_350, estudiante.getCuotas().get(3).getMontoCuota());

    }
    @Test
    void CalcularDosMesesDeAtraso(){
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

        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS); // 1_380_000 // Precio Cuota = 345_000
        arancelService.reCalcularArancel(LocalDate.of(2023, 12, 25));
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        assertEquals(365_700, estudiante.getCuotas().get(0).getMontoCuota());
        assertEquals(365_700, estudiante.getCuotas().get(1).getMontoCuota());
        assertEquals(365_700, estudiante.getCuotas().get(2).getMontoCuota());
        assertEquals(365_700, estudiante.getCuotas().get(3).getMontoCuota());

    }
    @Test
    void CalcularTresMesesAtraso(){
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

        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS); // 1_380_000 // Precio Cuota = 345_000
        arancelService.reCalcularArancel(LocalDate.of(2024, 1, 25));
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        assertEquals(376_050, estudiante.getCuotas().get(0).getMontoCuota());
        assertEquals(376_050, estudiante.getCuotas().get(1).getMontoCuota());
        assertEquals(376_050, estudiante.getCuotas().get(2).getMontoCuota());
        assertEquals(376_050, estudiante.getCuotas().get(3).getMontoCuota());

    }
    @Test
    void CalcularCuatroMesesAtraso(){
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

        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS); // 1_380_000 // Precio Cuota = 345_000
        arancelService.reCalcularArancel(LocalDate.of(2024, 2, 25));
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        assertEquals(396_750, estudiante.getCuotas().get(0).getMontoCuota());
        assertEquals(396_750, estudiante.getCuotas().get(1).getMontoCuota());
        assertEquals(396_750, estudiante.getCuotas().get(2).getMontoCuota());
        assertEquals(396_750, estudiante.getCuotas().get(3).getMontoCuota());

    }

    @Test
    void CalcularMesAtrasoPagoMesAtraso(){
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

        arancelService.crearMatricula(4, estudiante.getRut_estudiante(), EMedioPago.CUOTAS); // 1_380_000 // Precio Cuota = 345_000
        estudiante = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        System.out.println(estudiante.getCuotas().get(1).getPlazoMaximoPago());
        arancelService.reCalcularArancel(LocalDate.of(2023, 11, 25));
        pagoService.pagarCuota("20.107.536-K");

         arancelService.reCalcularArancel(LocalDate.of(2023, 12, 25));
        System.out.println("hola");

        //System.out.println(estudiante.getCuotas().get(1).getMontoCuota());


    }


}
