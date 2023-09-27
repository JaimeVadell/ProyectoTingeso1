package com.usach.PT1.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Services.EstudianteService;
import com.usach.PT1.Services.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {
    @Autowired
    EstudianteService estudianteService;
    @Autowired
    MatriculaService matriculaService;
    @Autowired
    EstudianteRepository estudianteRepository;


    @GetMapping("/crear-estudiante")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "crear-estudiante"; // Nombre del archivo HTML para el formulario de creación de estudiantes
    }

    @PostMapping("/crear-estudiante")
    public String crearEstudiante(@ModelAttribute Estudiante estudiante) {
        estudianteService.CrearEstudiante(estudiante);
        return "redirect:/estudiante/listar-estudiantes"; // Redirige a la página de lista de estudiantes después de la creación exitosa
    }

    @GetMapping("/listar-estudiantes")
    public String listarEstudiantes(Model model) {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        model.addAttribute("estudiantes", estudiantes);
        return "listar-estudiantes"; // Nombre del archivo HTML para la lista de estudiantes
    }



}
