package com.grupo5.ElorServ;

import java.util.ArrayList;

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
    public Users login(@RequestParam String username, @RequestParam String password) {
        return metodos.login(username, password);
    }
    
    @GetMapping
    public ArrayList<Users> getAllUsers() {
    	return metodos.getAllUsers();
	}
}

