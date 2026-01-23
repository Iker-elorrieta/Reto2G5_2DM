package com.grupo5.ElorServ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import Modelo.Metodos;
import Modelo.Users;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class Controlador {
	private Metodos metodos = new Metodos();

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // todos los endpoints
				.allowedOrigins("http://localhost:4200").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowCredentials(true);

	}

	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
		Users usuario = metodos.loginWeb(username, password);

		if (usuario != null) {
			return ResponseEntity.ok(metodos.crearJson(usuario));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("{\"error\": \"Usuario o contraseña incorrectos\"}");
		}
	}

	@GetMapping("/centros")
	public ResponseEntity<String> getCentros() {
		String json = null;
		try {
			File file = new File("EuskadiLatLon.json");
			json = Files.readString(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(json);
	}
	
	//TODO: TOTAL ALUMNOS, PROFESORES, REUNIONES DEL DIA DE HOY
	@GetMapping("/stats")
	public ResponseEntity<String> getStats() {
		String json = metodos.obtenerStats();
		if (json == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"No se pudieron obtener las estadísticas\"}");
		}else {
		return ResponseEntity.ok(json);
		}
	}
	
	//TODO: HORARIOS PROFESORES Y ALUMNOS
	@GetMapping("/horarios")
	public ResponseEntity<String> getHorarios(@RequestParam Integer id) {
		String json = metodos.obtenerHorarios(id);
		if (json == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"No se pudieron obtener los horarios\"}");
		}else {
		return ResponseEntity.ok(json);
		}
	}
	@GetMapping("/users")
	public ResponseEntity<String> getUsers() {
		String json = metodos.obtenerUsers();
		if (json == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"No se pudieron obtener los usuarios\"}");
		}else {
		return ResponseEntity.ok(json);
		}
	}

}
