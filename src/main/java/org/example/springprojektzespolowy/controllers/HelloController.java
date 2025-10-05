package org.example.springprojektzespolowy.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/public/hello")
    ResponseEntity<String> hello(){
        return ResponseEntity.ok("hello");
    }


}
