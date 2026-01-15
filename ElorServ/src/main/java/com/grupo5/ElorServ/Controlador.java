package com.grupo5.ElorServ;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Modelo.Metodos;
import Modelo.Users;

@RestController
@RequestMapping("/api")
public class Controlador {
    private Metodos metodos = new Metodos();

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        Users usuario = metodos.loginWeb(username, password);
        
        if (usuario != null) {
            return metodos.crearJson(usuario);
        } else {
            return "{\"error\": \"Usuario o contraseña incorrectos\"}";
        }
    }
    

}

