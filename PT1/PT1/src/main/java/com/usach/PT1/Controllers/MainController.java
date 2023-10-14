package com.usach.PT1.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String mostrarPaginaPrincipal() {
        return "index";
    }

}
