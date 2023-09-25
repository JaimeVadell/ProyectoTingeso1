package com.usach.PT1.Services;

import com.usach.PT1.Models.Deuda;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Pago;
import com.usach.PT1.Repositories.DeudaRepository;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Repositories.PagoRepository;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagoService {
    @Autowired
    EstudianteRepository estudianteRepository;
    @Autowired
    DeudaRepository deudaRepository;
    @Autowired
    PagoRepository PagoRepository;

    public void RealiazarPago(int montoPagoEstudiante, String rutEstudiante) {
        VerificadorRut verificadorRut = new VerificadorRut();
        rutEstudiante = verificadorRut.validarRut(rutEstudiante);
        if (rutEstudiante.equals("")) {
            throw new IllegalArgumentException("Rut Invalido");
        }
        if (montoPagoEstudiante <= 0) {
            throw new IllegalArgumentException("Monto Invalido");
        }
        Estudiante estudiante = estudianteRepository.findById(rutEstudiante).get();
        if (estudiante.getMatricula() == null) {
            throw new IllegalArgumentException("Estudiante no tiene matricula");
        }
        if (estudiante.getDeuda().getPrecioCuota() == 0 || estudiante.getDeuda().getCuotasRestantes() == 0) {
            throw new IllegalArgumentException("Estudiante no tiene deuda");
        }
        int cuotaMensual = estudiante.getDeuda().getPrecioCuota();
        if (montoPagoEstudiante != cuotaMensual) {
            throw new IllegalArgumentException("Monto no corresponde a cuota");
        }
        Pago pago = Pago.builder()
                .montoPagado(montoPagoEstudiante)
                .estudiante(estudiante)
                .fechaPago(LocalDate.now())
                .build();

        // Actualizar Pagos
        List<Pago> pagosEstudiante = estudiante.getPagos();
        pagosEstudiante.add(pago);
        estudiante.setPagos(pagosEstudiante);
        estudianteRepository.save(estudiante);
        PagoRepository.save(pago);

        // Actualizar Deuda
        Deuda deuda = estudiante.getDeuda();
        deuda.setCuotasRestantes(deuda.getCuotasRestantes() -1);
        deuda.setMontoDeuda(deuda.getMontoDeuda() - montoPagoEstudiante);
        deudaRepository.save(deuda);
    }
}
