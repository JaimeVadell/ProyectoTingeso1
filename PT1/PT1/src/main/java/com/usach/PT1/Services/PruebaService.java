package com.usach.PT1.Services;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Prueba;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Repositories.PruebaRepository;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class PruebaService {
    @Autowired
    PruebaRepository pruebaRepository;
    @Autowired
    EstudianteRepository estudianteRepository;

    public String AsignarPruebaEstudiante(Prueba prueba, String rutEstudiante){
        VerificadorRut verificadorRut = new VerificadorRut();
        rutEstudiante = verificadorRut.validarRut(rutEstudiante);
        if(rutEstudiante.equals("")){
            throw new IllegalArgumentException("Rut Invalido");
        }
        if(!estudianteRepository.existsById(rutEstudiante)){
            throw new IllegalArgumentException("Estudiante no existe");
        }
        LocalDate fechaPruebaActual = prueba.getDiaPrueba();
        int anioPruebaActual = fechaPruebaActual.getYear();
        int mesPruebaActual = fechaPruebaActual.getMonthValue();

        Estudiante estudiante = estudianteRepository.findById(rutEstudiante).get();
        List <Prueba> pruebasEstudiante = estudiante.getPruebas();
        for(Prueba pruebaEstudiante: pruebasEstudiante){
            LocalDate fechaPruebaAntigua = pruebaEstudiante.getDiaPrueba();
            int anioPruebaAntigua = fechaPruebaAntigua.getYear();
            int mesPruebaAntigua = fechaPruebaAntigua.getMonthValue();
            if (anioPruebaActual == anioPruebaAntigua && mesPruebaActual == mesPruebaAntigua){
                return "Prueba Invalida: El alumno ya rindio una prueba en este mes";
            }
        }
        prueba.setEstudiante(estudiante);
        pruebaRepository.save(prueba);
        pruebasEstudiante.add(prueba);
        estudiante.setPruebas(pruebasEstudiante);
        estudianteRepository.save(estudiante);
        return "Prueba Asignada";

    }

    @Data
    @Builder
    private static class PuntajeFecha {
        private int puntaje;
        private LocalDate fecha;
    }


    public void RevisarDocumentoExamen(MultipartFile archivo) {


        //String filePath = "C:\\Users\\Jaime\\OneDrive - usach.cl\\Documents\\2023\\Semestre 2\\TINGESO\\Ejemplo Pruebas\\examen1.xlsx";
        try {
            InputStream fileInputStream = archivo.getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Suponiendo que tu hoja de datos está en la primera pestaña

            Map<String, PuntajeFecha> estudianteMap = new HashMap<>();
            LocalDate fechaBase = null;
            String rutEstudiante;
            int lastRowNum = sheet.getLastRowNum();

            // Iniciar un índice para recorrer las filas
            int rowIndex = 0;
            for (Row row : sheet) {
                Cell c = row.getCell(0);
                if (row == null || c == null || c.getCellType() == CellType.BLANK) {
                    break; // Salir del bucle cuando se alcance la última fila
                }

                if (row.getPhysicalNumberOfCells() != 3) {
                    System.err.println("Error: La fila no tiene 3 columnas.");
                    continue; // Saltar esta fila si no tiene 3 columnas
                }

                String rut = row.getCell(0).getStringCellValue();
                rutEstudiante = VerificadorRut.validarRut(rut);
                if (rutEstudiante.equals("")) {
                    throw  new IllegalArgumentException("Error: Rut inválido - " + rut);

                }

                if (!estudianteRepository.existsById(rutEstudiante)) {
                    throw new IllegalArgumentException("Error: Estudiante no existe - " + rut);
                }

                Date fechaFormatoDate = row.getCell(1).getDateCellValue();
                LocalDate fecha = fechaFormatoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                System.out.println(fecha);

                int puntaje = (int) row.getCell(2).getNumericCellValue();

                // Verificar que el rut no se repita
                if (estudianteMap.containsKey(rut)) {
                    throw  new IllegalArgumentException("Error: Estudiante duplicado - Rut: " + rut);

                }

                // Verificar que el puntaje esté en el rango correcto
                if (puntaje < 0 || puntaje > 1000) {
                    throw new IllegalArgumentException("Error: Puntaje fuera de rango - Rut: " + rut + ", Puntaje: " + puntaje);
                }

                // Verificar que las fechas sean iguales
                if (fechaBase == null) {
                    fechaBase = fecha;
                } else if (!fecha.equals(fechaBase)) {
                    throw  new IllegalArgumentException("Error: Fechas diferentes - Rut: " + rut);
                }
                PuntajeFecha puntajeFechaEstudiante = PuntajeFecha.builder()
                        .fecha(fecha)
                        .puntaje(puntaje)
                        .build();
                estudianteMap.put(rut, puntajeFechaEstudiante);
            }

            fileInputStream.close();
            workbook.close();
            guardarPruebasEstudiante(estudianteMap);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void guardarPruebasEstudiante(Map<String, PuntajeFecha> estudianteMap) {
        for (Map.Entry<String, PuntajeFecha> entry : estudianteMap.entrySet()) {
            String rutEstudiante = entry.getKey();
            PuntajeFecha puntajeFecha = entry.getValue();
            int puntaje = puntajeFecha.getPuntaje();
            LocalDate fecha = puntajeFecha.getFecha();
            rutEstudiante = VerificadorRut.validarRut(rutEstudiante);
            Estudiante estudiante = estudianteRepository.findById(rutEstudiante).orElse(null);
            if(estudiante == null) {
                System.err.println("Error: Estudiante no existe - Rut: " + rutEstudiante);
                continue;
            }
            Prueba prueba = Prueba.builder()
                    .diaPrueba(fecha)
                    .puntaje(puntaje)
                    .estudiante(estudiante)
                    .build();
            pruebaRepository.save(prueba);
            List<Prueba> pruebasEstudiante = estudiante.getPruebas();
            pruebasEstudiante.add(prueba);
            estudiante.setPruebas(pruebasEstudiante);
            estudianteRepository.save(estudiante);
        }
        System.out.println("Pruebas guardadas exitosamente");
    }

}
