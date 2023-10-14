package com.usach.PT1.Services;

import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Reembolso;
import com.usach.PT1.Repositories.ReembolsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReembolsoService {
    @Autowired
    ReembolsoRepository reembolsoRepository;
    @Autowired
    EstudianteService estudianteService;

    public void anadirActualizarReembolsoEstudiante(Estudiante estudiante, int montoReembolso ){
        if (reembolsoRepository.findByEstudiante(estudiante).isPresent()){
            Reembolso reembolso = reembolsoRepository.findByEstudiante(estudiante).get();
            reembolso.setMontoReembolso(reembolso.getMontoReembolso() + montoReembolso);
            reembolsoRepository.save(reembolso);
        }
        else {
            Reembolso reembolso = Reembolso.builder()
                    .estudiante(estudiante)
                    .montoReembolso(montoReembolso)
                    .build();
            reembolsoRepository.save(reembolso);
        }
    }

    public void reclamarReembolso(Reembolso reembolso){
        reembolso.setMontoReembolso(0);
        reembolsoRepository.save(reembolso);
    }


    public Optional<Reembolso> obtenerReembolsoRutEstudiante(String rutEstudiante){
        Estudiante estudiante = estudianteService.buscarEstudianteRutsinFormato(rutEstudiante).orElse(null);
        if(estudiante == null){
            throw new IllegalArgumentException("Estudiante no encontrado");
        }
        return reembolsoRepository.findByEstudiante(estudiante);

    }

}
