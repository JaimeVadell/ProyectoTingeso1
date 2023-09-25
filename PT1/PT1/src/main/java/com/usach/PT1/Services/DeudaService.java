package com.usach.PT1.Services;

import com.usach.PT1.Models.Deuda;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Repositories.DeudaRepository;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeudaService {
    @Autowired
    DeudaRepository deudaRepository;

    @Autowired
    EstudianteRepository estudianteRepository;

    public void CrearDeudaEstudiante(int montoDeuda, int cuotasRestantes, int precioCuota, Estudiante estudiante){
        Deuda deuda = Deuda.builder()
                .montoDeuda(montoDeuda)
                .CuotasRestantes(cuotasRestantes)
                .precioCuota(precioCuota)
                .estudiante(estudiante)
                .cuotasConRetraso(0)
                .build();
        estudiante.setDeuda(deuda);
        deudaRepository.save(deuda);
        estudianteRepository.save(estudiante);
    }

}
