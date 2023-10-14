package com.usach.PT1.Services;

import com.usach.PT1.Models.ETipoColegio;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Reembolso;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ReembolsoServiceTest {
    @Autowired
    ReembolsoService reembolsoService;
    @Autowired
    EstudianteService estudianteService;

    @Test
    public void testCrearReembolso() {
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
        reembolsoService.anadirActualizarReembolsoEstudiante(estudiante, 50_000);
        Reembolso reembolso = reembolsoService.obtenerReembolsoRutEstudiante(estudiante.getRut_estudiante()).orElse(null);
        assertEquals(50_000, reembolso.getMontoReembolso());
    }
    @Test
    public void testReclamarReembolso() {
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
        reembolsoService.anadirActualizarReembolsoEstudiante(estudiante, 50_000);
        Reembolso reembolso = reembolsoService.obtenerReembolsoRutEstudiante(estudiante.getRut_estudiante()).orElse(null);
        reembolsoService.reclamarReembolso(reembolso);
        reembolso = reembolsoService.obtenerReembolsoRutEstudiante(estudiante.getRut_estudiante()).orElse(null);
        assertEquals(0, reembolso.getMontoReembolso());
    }


}
