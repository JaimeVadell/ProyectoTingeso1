package com.usach.PT1.Controllers;

import com.usach.PT1.Models.Cuota;
import com.usach.PT1.Models.ETipoPago;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Pago;
import com.usach.PT1.Repositories.CuotasRepository;
import com.usach.PT1.Repositories.PagoRepository;
import com.usach.PT1.Services.CuotasService;
import com.usach.PT1.Services.EstudianteService;
import com.usach.PT1.Services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    CuotasRepository cuotasRepository;

    @Autowired
    PagoService pagoService;

    @Autowired
    PagoRepository pagoRepository;

    @GetMapping("/ver-pagos/{rutEstudiante}")
    public String verPagosEstudiante(@PathVariable String rutEstudiante, Model model) {
        Estudiante estudiante = estudianteService.buscarEstudianteRutsinFormato(rutEstudiante).orElse(null);
        if (estudiante == null) {
            throw new RuntimeException("Estudiante no encontrado");
        }

        // Recupera los pagos asociados al estudiante por su RUT
        List<Pago> pagos = estudiante.getPagos();
        List<Pago> pagosTest = pagoRepository.findByEstudiante(estudiante);
        // Obtener de Pago Test solo los pagos que hayan sido cuota de arancel
        pagosTest.removeIf(pago -> pago.getTipoPago() == ETipoPago.MATRICULA);
        //System.out.println(pagosTest);
        // Ordena los pagos por la fecha de pago (asumiendo que la fecha de pago es LocalDate)
        Collections.sort(pagos, Comparator.comparing(Pago::getFechaPago));


        // Agrega los pagos ordenados al modelo para mostrarlos en la vista
        model.addAttribute("pagos", pagosTest);

        return "ver-pagos"; // Nombre de la vista que mostrará los pagos
    }

    @GetMapping("/pagar-cuota/{rutEstudiante}")
    public String pagarCuota(@PathVariable String rutEstudiante) {

        pagoService.pagarCuota(rutEstudiante);

        return "redirect:/cuotas/ver-cuotas/" + rutEstudiante; // Redirige de vuelta a la página de pagos del estudiante
    }


}
