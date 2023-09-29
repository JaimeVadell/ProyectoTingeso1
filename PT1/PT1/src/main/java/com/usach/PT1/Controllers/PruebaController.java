package com.usach.PT1.Controllers;

import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Prueba;
import com.usach.PT1.Services.EstudianteService;
import com.usach.PT1.Services.PruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/prueba")
public class PruebaController {

    @Autowired
    EstudianteService estudianteService;
    @Autowired
    PruebaService pruebaService;

    @GetMapping("/subir-prueba")
    public String mostrarFormularioSubirPrueba() {
        return "subir-prueba"; // Nombre de tu página HTML que contiene el formulario
    }

    @PostMapping("/guardar-prueba")
    public String subirPrueba(@RequestParam("archivo") MultipartFile archivo) {
        // Verificar si se ha seleccionado un archivo
        if (!archivo.isEmpty()) {
            pruebaService.RevisarDocumentoExamen(archivo);
        }
        return "redirect:/estudiante/listar-estudiantes"; // Redirige a la página de subir prueba en caso de que no se haya seleccionado un archivo
    }

    @GetMapping("/ver-pruebas/{rutEstudiante}")
    public String verPruebasEstudiante(@PathVariable String rutEstudiante, Model model) {
        Estudiante estudiante = estudianteService.buscarEstudianteRutsinFormato(rutEstudiante).orElse(null);

        if (estudiante == null) {
            throw new IllegalArgumentException("Estudiante no encontrado");
        }

        List<Prueba> pruebas = estudiante.getPruebas();


        Collections.sort(pruebas, Comparator.comparing(Prueba::getDiaPrueba));


        model.addAttribute("estudiante", estudiante);
        model.addAttribute("pruebas", pruebas);

        return "ver-pruebas"; // Nombre de la vista que mostrará los pagos
    }

}
