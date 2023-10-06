package com.usach.PT1.Services;

import com.usach.PT1.Models.*;
import com.usach.PT1.Repositories.CuotasRepository;
import com.usach.PT1.Repositories.DeudaRepository;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Repositories.PagoRepository;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {
    @Autowired
    EstudianteRepository estudianteRepository;
    @Autowired
    DeudaRepository deudaRepository;
    @Autowired
    PagoRepository PagoRepository;
    @Autowired
    CuotasRepository cuotasRepository;
    @Autowired
    DeudaService deudaService;
    @Autowired
    EstudianteService estudianteService;


    public void pagarCuota(String rutEstudiante) {
        Estudiante estudiante = estudianteService.buscarEstudianteRutsinFormato(rutEstudiante).orElse(null);
        if(estudiante == null){
            throw new IllegalArgumentException("Estudiante no existe");
        }
        List<Cuota> cuotasEstudianteSinPagar = cuotasRepository.findByEstudianteAndPagadaIsFalseOrderByPlazoMaximoPagoAsc(estudiante);
        if (cuotasEstudianteSinPagar.isEmpty()) {
            throw new IllegalArgumentException("Estudiante no tiene cuotas pendientes");
        }
        Cuota cuotaCorrespondiente = cuotasEstudianteSinPagar.get(0);
        // Generar Pago
        Pago pago = Pago.builder()
                .estudiante(estudiante)
                .tipoPago(ETipoPago.CUOTA_ARANCEL)
                .fechaPago(LocalDate.now())
                .montoPagado(cuotaCorrespondiente.getMontoCuota())
                .cuotaPagada(cuotaCorrespondiente)
                .build();
        List<Pago> pagosEstudiante = estudiante.getPagos();
        pagosEstudiante.add(pago);
        estudiante.setPagos(pagosEstudiante);
        //Guardar Cambios al estudiante
        estudianteRepository.save(estudiante);
        //Guardar Pago
        PagoRepository.save(pago);
        //Actuliazar estado de cuota
        cuotaCorrespondiente.setPagada(true);
        cuotaCorrespondiente.setPago(pago);
        cuotasRepository.save(cuotaCorrespondiente);
        deudaService.actualizarDeuda(estudiante, cuotaCorrespondiente.getMontoCuota());

    }
}