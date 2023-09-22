package com.usach.PT1.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {

    @GetMapping("/test")
    public String hello(){
        return "Hello World Not Secured";
    }

    
}
