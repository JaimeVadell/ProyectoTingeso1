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
public class CuotaServiceTest  {
    @Autowired
    EstudianteService estudianteService;
    @Autowired
    ArancelService arancelService;



    @Test
    void CrearCuota(){
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
        assertEquals(345_000, estudiante.getCuotas().get(0).getMontoCuota());
        assertEquals(345_000, estudiante.getCuotas().get(1).getMontoCuota());
        assertEquals(345_000, estudiante.getCuotas().get(2).getMontoCuota());
        assertEquals(345_000, estudiante.getCuotas().get(3).getMontoCuota());


    }
}
