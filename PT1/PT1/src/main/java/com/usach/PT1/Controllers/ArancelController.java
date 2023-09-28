package com.usach.PT1.Controllers;

import com.usach.PT1.Models.Arancel;
import com.usach.PT1.Models.EMedioPago;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Services.EstudianteService;
import com.usach.PT1.Services.ArancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/arancel")
public class ArancelController {

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    ArancelService arancelService;

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
        EMedioPago tipoPagoEnum = EMedioPago.valueOf(tipoPago);
        arancelService.crearMatricula(numeroCuotas, rutEstudiante, tipoPagoEnum);
        return "redirect:/estudiante/listar-estudiantes";
    }

    @GetMapping("/ver-arancel/{rutEstudiante}")
    public String verArancel(@PathVariable String rutEstudiante, Model model) {
        // Recupera el estudiante por su rut
        Estudiante estudiante = estudianteService.buscarEstudianteRutsinFormato(rutEstudiante).orElse(null);

        if (estudiante == null) {
            throw new RuntimeException("Estudiante no encontrado");
        }

        // Obtén el arancel del estudiante
        Arancel arancel = estudiante.getArancel();
        if (arancel == null) {
            throw new RuntimeException("Arancel no encontrado para el estudiante");
        }

        model.addAttribute("arancel", arancel);
        return "ver-arancel"; // Nombre de la plantilla Thymeleaf para mostrar el arancel
    }
}
