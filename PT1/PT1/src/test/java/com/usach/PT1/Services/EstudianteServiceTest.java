package com.usach.PT1.Services;

import com.usach.PT1.Models.ETipoColegio;
import com.usach.PT1.Models.Estudiante;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class EstudianteServiceTest {

    @Autowired
    EstudianteService estudianteService;

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
        estudianteService.CrearEstudiante(estudiante);
        Estudiante estudianteTest = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        Assertions.assertNotNull(estudianteTest);
        Assertions.assertEquals("Jaime", estudianteTest.getNombre());
        estudianteService.eliminarEstudiante(estudiante);

    }

    @Test
    void crearEstudianteSinFechaNacimiento() {
        //Sin fecha de nacimiento
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        assertThrows(Throwable.class, () -> {
            estudianteService.CrearEstudiante(estudiante);
        });


    }

    @Test
    void CrearEstudianteExistente(){
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
        Estudiante estudianteTest = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        Assertions.assertNotNull(estudianteTest);
        Assertions.assertEquals("Jaime", estudianteTest.getNombre());
        assertThrows(Throwable.class, () -> {
            estudianteService.CrearEstudiante(estudiante);
        });
        estudianteService.eliminarEstudiante(estudiante);

    }

    @Test
    void CrearEstudianteRutErroneo(){
        //Estudiante rut Erroneo
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-1")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        assertThrows(Throwable.class, () -> {
            estudianteService.CrearEstudiante(estudiante);
        });
    }

    @Test
    void CrearEstudianteSinColegio(){
        //Sin nombre Colegio
        Estudiante estudiante = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        assertThrows(Throwable.class, () -> {
            estudianteService.CrearEstudiante(estudiante);
        });
    }
}
