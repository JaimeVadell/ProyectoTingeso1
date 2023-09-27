package com.usach.PT1.Services;

import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstudianteService {
    @Autowired
    EstudianteRepository estudianteRepository;

    public String CrearEstudiante(Estudiante estudiante){
        VerificadorRut verificadorRut = new VerificadorRut();
        String rutEstudiante = verificadorRut.validarRut(estudiante.getRut_estudiante());

        if(rutEstudiante.equals("")){
            throw new IllegalArgumentException("Rut Invalido");
        }
        if(estudianteRepository.existsById(rutEstudiante)){
            throw new IllegalArgumentException("Estudiante ya existe");
        }
        estudiante.setRut_estudiante(rutEstudiante);
        estudianteRepository.save(estudiante);
        return "Estudiante Creado";

        
    }
    public Optional<Estudiante> buscarEstudianteRutsinFormato(String rutEstudiante){
        Optional<Estudiante> estudianteOptional;
        VerificadorRut verificadorRut = new VerificadorRut();
        rutEstudiante = verificadorRut.validarRut(rutEstudiante);
        if(rutEstudiante.equals("")){
            estudianteOptional = Optional.empty();
            return estudianteOptional;
        }
        return estudianteRepository.findById(rutEstudiante);
    }




}
