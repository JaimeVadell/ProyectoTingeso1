package com.usach.PT1.Controllers;

import com.usach.PT1.Models.ETipoPago;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Pago;
import com.usach.PT1.Models.Prueba;
import com.usach.PT1.Repositories.EstudianteRepository;
import com.usach.PT1.Services.EstudianteService;
import com.usach.PT1.Utils.VerificadorRut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/resumen-estudiante")
public class ResumenEstudiante {
    @Autowired
    EstudianteRepository estudianteRepository;
    @Autowired
    EstudianteService estudianteService;

    @GetMapping("/{rutEstudiante}")
    public String mostrarResumenEstudiante(@PathVariable String rutEstudiante, Model model) {
        Estudiante estudiante = estudianteService.buscarEstudianteRutsinFormato(rutEstudiante).orElse(null);

        if (estudiante == null) {
            throw new IllegalArgumentException("Estudiante no encontrado");
        }
        String rutEstudianteFormateado = estudiante.getRut_estudiante(); // Mostrar en la vista
        String nombreCompletoEstudiante = estudiante.getNombre() + " " + estudiante.getApellido(); // Mostrar en la vista

        List<Prueba> pruebasEstudiante = estudiante.getPruebas();
        int numeroExamenesRendidos = pruebasEstudiante.size(); // Mostrar en la vista
        int sumaPuntajePruebas = 0;
        for (Prueba prueba : pruebasEstudiante) {
            sumaPuntajePruebas += prueba.getPuntaje();
        }
        int promedioPuntajePruebas = Math.round(((float) sumaPuntajePruebas) / pruebasEstudiante.size()); // Mostrar en la vista

        int MontoTotalArancelaPagar = estudiante.getArancel().getMontoTotalArancel(); // Mostrar en la vista
        String TipoPago = estudiante.getArancel().getPago().toString(); // Mostrar en la vista
        int numeroCuotasPactadas = estudiante.getArancel().getNumeroCuotas(); // Mostrar en la vista
        int numeroCuotasPagas = (int) estudiante.getPagos()
                .stream()
                .filter(pago -> pago.getTipoPago() == ETipoPago.CUOTA_ARANCEL)
                .count();; // Mostrar en la vista

        int MontoTotalPagado = estudiante.getPagos().stream().filter(pago -> pago.getTipoPago().equals(ETipoPago.CUOTA_ARANCEL)).mapToInt(Pago::getMontoPagado).sum(); // Mostrar en la vista
        LocalDate fechaUltimoPago = estudiante.getPagos().stream().filter(pago -> pago.getTipoPago().equals(ETipoPago.CUOTA_ARANCEL))
                .map(Pago::getFechaPago).max(LocalDate::compareTo).orElse(null); // Mostrar en la vista
        int SaldoPorPagar;
        int numeroCuotasConRetrasoActual;
        int numeroCuotasConRetrasoHistorico;
        if (estudiante.getDeuda() == null){
            SaldoPorPagar = 0;
            numeroCuotasConRetrasoActual = 0;
            numeroCuotasConRetrasoHistorico = 0;
        }
        else{
            SaldoPorPagar = estudiante.getDeuda().getMontoDeuda(); // Mostrar en la vista
            numeroCuotasConRetrasoActual = estudiante.getDeuda().getCuotasConRetraso(); // Mostrar en la vista
            numeroCuotasConRetrasoHistorico = estudiante.getDeuda().getCuotasConRetrasoHistorico(); // Mostrar en la vista
        }

        // Agregar al model
        model.addAttribute("rutEstudianteFormateado", rutEstudianteFormateado);
        model.addAttribute("nombreCompletoEstudiante", nombreCompletoEstudiante);
        model.addAttribute("numeroExamenesRendidos", numeroExamenesRendidos);
        model.addAttribute("promedioPuntajePruebas", promedioPuntajePruebas);
        model.addAttribute("MontoTotalArancelaPagar", MontoTotalArancelaPagar);
        model.addAttribute("TipoPago", TipoPago);
        model.addAttribute("numeroCuotasPactadas", numeroCuotasPactadas);
        model.addAttribute("numeroCuotasPagas", numeroCuotasPagas);
        model.addAttribute("MontoTotalPagado", MontoTotalPagado);
        model.addAttribute("fechaUltimoPago", fechaUltimoPago);
        model.addAttribute("SaldoPorPagar", SaldoPorPagar);
        model.addAttribute("numeroCuotasConRetrasoActual", numeroCuotasConRetrasoActual);
        model.addAttribute("numeroCuotasConRetrasoHistorico", numeroCuotasConRetrasoHistorico);

        return "resumen-estudiante"; // Nombre de la vista que mostrará el resumen

    }

}
