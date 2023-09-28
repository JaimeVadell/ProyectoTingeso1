package com.usach.PT1.Services;

import com.usach.PT1.Models.Arancel;
import com.usach.PT1.Models.Cuota;
import com.usach.PT1.Models.Deuda;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Repositories.CuotasRepository;
import com.usach.PT1.Repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CuotasService {

    @Autowired
    CuotasRepository cuotasRepository;

    @Autowired
    EstudianteRepository estudianteRepository;

    public void generarCuotasArancel(Arancel arancel, int precioCuota) {
        Estudiante estudiante = arancel.getEstudiante();
        List<Cuota> cuotasEstudiante = estudiante.getCuotas();
        int cuotas = arancel.getNumeroCuotas();
        LocalDate fechaActual = LocalDate.now();
        LocalDate plazoMaximoPago;
        int diaDelMes = fechaActual.getDayOfMonth();

        // Compara si el día del mes actual es mayor que 10
        if (diaDelMes >=5) {
            //Desde el proximo mes
            plazoMaximoPago = LocalDate.of(fechaActual.getYear(), fechaActual.getMonth().plus(1), 10);
            System.out.println("La fecha actual es posterior al día 15 del mes.");
        } else {
            //Mes actual
            plazoMaximoPago= LocalDate.of(fechaActual.getYear(), fechaActual.getMonth(), 10);
        }

        for(int i = 0; i < cuotas; i++){
            Cuota cuota = Cuota.builder()
                    .montoCuota(precioCuota)
                    .plazoMaximoPago(plazoMaximoPago)
                    .estudiante(estudiante)
                    .pagada(false)
                    .build();
            cuotasRepository.save(cuota);
            cuotasEstudiante.add(cuota);
            plazoMaximoPago = plazoMaximoPago.plusMonths(1);
            plazoMaximoPago = LocalDate.of(plazoMaximoPago.getYear(), plazoMaximoPago.getMonth(), 10);
        }
        estudiante.setCuotas(cuotasEstudiante);
        estudianteRepository.save(estudiante);

    }
}
