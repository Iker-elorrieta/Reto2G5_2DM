package com.grupo5.ElorServ;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Modelo.Users;

@RestController
@RequestMapping("/api")
public class Controlador {

	
	@GetMapping
	public Users login(String user, String password) {
		
		Users usuario = new Users();
		usuario.setUsername(user);
		usuario.setPassword(password);
		return usuario;
		
	}
	
}
