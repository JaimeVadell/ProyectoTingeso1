package com.usach.PT1.Services;

import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Prueba;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Repositories.PruebaRepository;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

}
