package com.usach.PT1.Model;

import com.usach.PT1.Models.ETipoColegio;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Repositories.EstudianteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class EstudianteTest {

    @Autowired
    EstudianteRepository estudianteRepository;

    @Test
    void crearEstudiante(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudianteRepository.save(estudiante);
        Estudiante estudianteCreado = estudianteRepository.findById(estudiante.getRut_estudiante()).orElse(null);
        assertNotNull(estudianteCreado);
        assertEquals("Jaime", estudianteCreado.getNombre());
        estudianteRepository.delete(estudiante);

    }

    @Test
    void eliminarEstudiante(){
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        estudianteRepository.save(estudiante);
        Estudiante estudianteCreado = estudianteRepository.findById(estudiante.getRut_estudiante()).orElse(null);
        assertNotNull(estudianteCreado);
        estudianteRepository.delete(estudiante);
        Estudiante estudianteEliminado = estudianteRepository.findById(estudiante.getRut_estudiante()).orElse(null);
        assertNull(estudianteEliminado);
    }
}
