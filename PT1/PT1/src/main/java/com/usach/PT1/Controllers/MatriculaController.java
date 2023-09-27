package com.usach.PT1.Controllers;

import com.usach.PT1.Models.EPago;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Services.EstudianteService;
import com.usach.PT1.Services.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/matricula")
public class MatriculaController {

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    MatriculaService matriculaService;

    @GetMapping("/crear/{rutEstudiante}")
    public String mostrarFormularioMatricula(@PathVariable String rutEstudiante, Model model) {
        // Recupera el estudiante por su rut (puedes implementar esto en tu servicio)
        Optional<Estudiante> estudianteOptional = estudianteService.buscarEstudianteRutsinFormato(rutEstudiante);
        if (estudianteOptional.isEmpty()){
            throw new RuntimeException("Error al crear la matrícula");
        }
        Estudiante estudiante = estudianteOptional.get();
        model.addAttribute("estudiante", estudiante);
        return "crear-matricula";
    }

    @PostMapping("/crear")
    public String crearMatricula(@RequestParam("numeroCuotas") int numeroCuotas,
                                 @RequestParam("pago") String tipoPago,
                                 @RequestParam("rut") String rutEstudiante,
                                 Model model) {
        //System.out.println(numeroCuotas + tipoPago + "" + rutEstudiante);
        EPago tipoPagoEnum = EPago.valueOf(tipoPago);
        matriculaService.crearMatricula(numeroCuotas, rutEstudiante, tipoPagoEnum);
        return "redirect:/estudiante/listar-estudiantes";
    }
}
