package com.usach.PT1.Controllers;

import com.usach.PT1.Models.Cuota;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cuotas")
public class CuotasController {

    @Autowired
    EstudianteService estudianteService;

    @GetMapping("/ver-cuotas/{rutEstudiante}")
    public String verCuotas(@PathVariable String rutEstudiante, Model model) {
        Estudiante estudiante = estudianteService.buscarEstudianteRutsinFormato(rutEstudiante).orElse(null);

        if (estudiante == null) {
            throw new RuntimeException("Estudiante no encontrado");
        }
        // Obtener todas las cuotas del estudiante
        List<Cuota> cuotas = estudiante.getCuotas();

        // Ordenar las cuotas por fecha (las más cercanas primero)
        cuotas.sort((cuota1, cuota2) -> cuota1.getPlazoMaximoPago().compareTo(cuota2.getPlazoMaximoPago()));

        model.addAttribute("cuotas", cuotas);
        return "ver-cuotas"; // Cambia "ver-cuotas" al nombre de tu vista Thymeleaf
    }

}
