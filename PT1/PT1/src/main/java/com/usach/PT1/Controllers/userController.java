package com.usach.PT1.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.usach.PT1.Models.EPago;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Matricula;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Services.EstudianteService;
import com.usach.PT1.Services.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class userController {
    @Autowired
    EstudianteService EstudianteService;
    @Autowired
    MatriculaService MatriculaService;
    @Autowired
    EstudianteRepository estudianteRepository;



    @PostMapping("/CrearEstudiante")
    public void CrearEstudianteControlador(@RequestBody Estudiante estudiante, int numero){
        System.out.println(EstudianteService.CrearEstudiante(estudiante));
        System.out.println(MatriculaService.crearMatricula(3,estudiante.getRut_estudiante(), EPago.CUOTAS));

    }

    @GetMapping ("/matricula")
    public void matricula(){
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById("20107536-K");
        Estudiante estudiante = estudianteOptional.get();
        Matricula matricula = estudiante.getMatricula();
        System.out.println(matricula.getNumeroCuotas());

    }

    @GetMapping("/crear-estudiante")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "crear-estudiante"; // Nombre del archivo HTML para el formulario de creación de estudiantes
    }

    @PostMapping("/crear-estudiante")
    public String crearEstudiante(@ModelAttribute Estudiante estudiante) {
        EstudianteService.CrearEstudiante(estudiante);
        return "redirect:/listar-estudiantes"; // Redirige a la página de lista de estudiantes después de la creación exitosa
    }

    @GetMapping("/listar-estudiantes")
    public String listarEstudiantes(Model model) {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        model.addAttribute("estudiantes", estudiantes);
        return "listar-estudiantes"; // Nombre del archivo HTML para la lista de estudiantes
    }




}
