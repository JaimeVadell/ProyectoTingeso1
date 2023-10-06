package com.usach.PT1.Services;

import com.usach.PT1.Models.ETipoColegio;
import com.usach.PT1.Models.Estudiante;
import jakarta.transaction.Transactional;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertThrows;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest
@Transactional
public class PruebaServiceTest {

    @Autowired
    EstudianteService estudianteService;
    @Autowired
    PruebaService pruebaService;

    @Test
    public void registrarPrueba() throws IOException, URISyntaxException {
        Estudiante estudiante1 = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        Estudiante estudiante2 = Estudiante.builder()
                .rut_estudiante("8.773.084-0")
                .nombre("Maria Jose")
                .apellido("Bofill")
                .anioEgreso(LocalDate.of(2021, 12, 1))
                .nombreColegio("Colegio Ejemplo2")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(2000,7,20))
                .build();

        Estudiante estudiante3 = Estudiante.builder()
                .rut_estudiante("19.664.581-0")
                .nombre("Ximena")
                .apellido("Lara")
                .anioEgreso(LocalDate.of(2022, 12, 1))
                .nombreColegio("Colegio Ejemplo2")
                .tipoColegio(ETipoColegio.MUNICIPAL)
                .fechaNacimiento(LocalDate.of(1968,7,20))
                .build();
        estudiante1.setPruebas(new ArrayList<>());
        estudiante2.setPruebas(new ArrayList<>());
        estudiante3.setPruebas(new ArrayList<>());
        estudianteService.CrearEstudiante(estudiante1);
        estudianteService.CrearEstudiante(estudiante2);
        estudianteService.CrearEstudiante(estudiante3);
        MultipartFile filePruebas = obtenerArchivoDesdeRutaLocal("pruebasExcel/examen1.xlsx");
        pruebaService.RevisarDocumentoExamen(filePruebas);
        //cargar pruebas duplicadas
        assertThrows(Throwable.class, () -> {
            pruebaService.RevisarDocumentoExamen(filePruebas);
        });
        estudiante1 = estudianteService.buscarEstudianteRutsinFormato("20107536K").orElse(null);
        Assertions.assertEquals(1, estudiante1.getPruebas().size());

    }
    private MultipartFile obtenerArchivoDesdeRutaLocal(String filename) throws IOException, URISyntaxException {

        File file = getFileFromResource(filename);

        // Verificar que el archivo existe
        if (!file.exists()) {
            throw new IllegalArgumentException("El archivo no existe en la ruta proporcionada.");
        }

        // Crear un flujo de entrada desde el archivo
        FileInputStream input = new FileInputStream(file);

        // Crear un objeto MultipartFile a partir del flujo de entrada y el nombre del archivo


        return new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
    }
    private File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }
    }

    @Test
    void pruebasInvalidas() throws IOException, URISyntaxException {
        Estudiante estudiante1 = Estudiante.builder()
                .rut_estudiante("20.107.536-K")
                .nombre("Jaime")
                .apellido("Vadell")
                .anioEgreso(LocalDate.of(2019, 12, 1))
                .nombreColegio("Colegio Ejemplo")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(1999, 2, 6))
                .build();
        Estudiante estudiante2 = Estudiante.builder()
                .rut_estudiante("8.773.084-0")
                .nombre("Maria Jose")
                .apellido("Bofill")
                .anioEgreso(LocalDate.of(2021, 12, 1))
                .nombreColegio("Colegio Ejemplo2")
                .tipoColegio(ETipoColegio.PRIVADO)
                .fechaNacimiento(LocalDate.of(2000,7,20))
                .build();

        Estudiante estudiante3 = Estudiante.builder()
                .rut_estudiante("19.664.581-0")
                .nombre("Ximena")
                .apellido("Lara")
                .anioEgreso(LocalDate.of(2022, 12, 1))
                .nombreColegio("Colegio Ejemplo2")
                .tipoColegio(ETipoColegio.MUNICIPAL)
                .fechaNacimiento(LocalDate.of(1968,7,20))
                .build();
        estudiante1.setPruebas(new ArrayList<>());
        estudiante2.setPruebas(new ArrayList<>());
        estudiante3.setPruebas(new ArrayList<>());
        estudianteService.CrearEstudiante(estudiante1);
        estudianteService.CrearEstudiante(estudiante2);
        estudianteService.CrearEstudiante(estudiante3);
        MultipartFile filePruebas = obtenerArchivoDesdeRutaLocal("pruebasExcel/pruebasInvalidasRutRepetido.xlsx");

        MultipartFile finalFilePruebas = filePruebas;
        assertThrows(Throwable.class, () -> {
            pruebaService.RevisarDocumentoExamen(finalFilePruebas);
        });
        filePruebas = obtenerArchivoDesdeRutaLocal("pruebasExcel/pruebaInvalida4Columnas.xlsx");
        MultipartFile finalFilePruebas1 = filePruebas;
        assertThrows(Throwable.class, () -> {
            pruebaService.RevisarDocumentoExamen(finalFilePruebas1);
        });
        filePruebas = obtenerArchivoDesdeRutaLocal("pruebasExcel/pruebaInvalidaEstudianteInexistente.xlsx");
        MultipartFile finalFilePruebas2 = filePruebas;
        assertThrows(Throwable.class, () -> {
            pruebaService.RevisarDocumentoExamen(finalFilePruebas2);
        });
        filePruebas = obtenerArchivoDesdeRutaLocal("pruebasExcel/pruebaInvalidaFechaFueraDeRango.xlsx");
        MultipartFile finalFilePruebas3 = filePruebas;
        assertThrows(Throwable.class, () -> {
            pruebaService.RevisarDocumentoExamen(finalFilePruebas3);
        });
        filePruebas = obtenerArchivoDesdeRutaLocal("pruebasExcel/pruebaInvalidaFechaIncorrecta.xlsx");
        MultipartFile finalFilePruebas4 = filePruebas;
        assertThrows(Throwable.class, () -> {
            pruebaService.RevisarDocumentoExamen(finalFilePruebas4);
        });

        filePruebas = obtenerArchivoDesdeRutaLocal("pruebasExcel/pruebaInvalidaPuntajeFueraDeRango.xlsx");
        MultipartFile finalFilePruebas5 = filePruebas;
        assertThrows(Throwable.class, () -> {
            pruebaService.RevisarDocumentoExamen(finalFilePruebas5);
        });
        filePruebas = obtenerArchivoDesdeRutaLocal("pruebasExcel/pruebaInvalidaRutInvalido.xlsx");
        MultipartFile finalFilePruebas6 = filePruebas;
        assertThrows(Throwable.class, () -> {
            pruebaService.RevisarDocumentoExamen(finalFilePruebas6);
        });
    }


}
