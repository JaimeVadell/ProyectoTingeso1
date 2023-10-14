package com.usach.PT1.Controllers;


import com.usach.PT1.Models.Reembolso;
import com.usach.PT1.Services.ReembolsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reembolso")
public class ReembolsoController {

    @Autowired
    ReembolsoService reembolsoService;

    @GetMapping("/ver-reembolso/{rutEstudiante}")
    public String verReembolso(@PathVariable String rutEstudiante, Model model) {

        Reembolso reembolso = reembolsoService.obtenerReembolsoRutEstudiante(rutEstudiante).orElse(null);
        if (reembolso != null) {
            model.addAttribute("reembolso", reembolso);
        }
        return "ver-reembolso";
    }


    @GetMapping("/reclamar-reembolso/{rutEstudiante}")
    public String reclamarReembolso(@PathVariable String rutEstudiante, Model model) {
        Reembolso reembolso = reembolsoService.obtenerReembolsoRutEstudiante(rutEstudiante).orElse(null);
        if (reembolso != null) {
            reembolsoService.reclamarReembolso(reembolso);

        }
        return "redirect:/estudiante/listar-estudiantes";
    }

}
