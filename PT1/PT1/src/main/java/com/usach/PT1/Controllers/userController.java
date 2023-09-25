package com.usach.PT1.Controllers;

import com.usach.PT1.Models.EPago;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Matricula;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Services.EstudianteService;
import com.usach.PT1.Services.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class userController {
    @Autowired
    EstudianteService EstudianteService;
    @Autowired
    MatriculaService MatriculaService;
    @Autowired
    EstudianteRepository estudianteRepository;


    @GetMapping("/test")
    public String hello(){
        return "Hello World Not Secured";
    }

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
}
