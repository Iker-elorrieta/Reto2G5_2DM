package com.grupo5.ElorServ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Modelo.Metodos;
import Modelo.Tipos;
import Modelo.Users;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class Controlador {

    private final Metodos metodos;

    public Controlador(Metodos metodos) {
        this.metodos = metodos;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String username,
            @RequestParam String password) {

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
        try {
            File file = new File("EuskadiLatLon.json");
            return ResponseEntity.ok(Files.readString(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"No se pudo leer el archivo\"}");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStats() {
        String json = metodos.obtenerStats();
        return json != null
                ? ResponseEntity.ok(json)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"No se pudieron obtener las estadísticas\"}");
    }

    @GetMapping("/horarios/{id}")
    public ResponseEntity<String> getHorarios(@PathVariable Integer id) {

        Tipos tipo = metodos.obtenerTipoPorUserId(id);

        if (tipo == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"No se pudo obtener el tipo de usuario\"}");
        }

        if ("profesor".equals(tipo.getName()) || "irakaslea".equals(tipo.getNameEu())) {
            return ResponseEntity.ok(metodos.obtenerHorariosProfesor(id));
        }

        if ("alumno".equals(tipo.getName()) || "ikaslea".equals(tipo.getNameEu())) {
            return ResponseEntity.ok(metodos.obtenerHorariosAlumno(id));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("{\"error\": \"Acceso denegado\"}");
    }

    @GetMapping("/users")
    public ResponseEntity<String> getUsers() {
        String json = metodos.obtenerUsers();
        return json != null
                ? ResponseEntity.ok(json)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"No se pudieron obtener los usuarios\"}");
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody Users user) {
        return metodos.createUser(user)
                ? ResponseEntity.ok("{\"message\": \"Usuario creado correctamente\"}")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"No se pudo crear el usuario\"}");
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Integer id,
            @RequestBody Users user) {

        return metodos.updateUser(id, user)
                ? ResponseEntity.ok("{\"message\": \"Usuario actualizado correctamente\"}")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"No se pudo actualizar el usuario\"}");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        return metodos.deleteUser(id)
                ? ResponseEntity.ok("{\"message\": \"Usuario eliminado correctamente\"}")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"No se pudo eliminar el usuario\"}");
    }

    @GetMapping("/reuniones/{id}")
    public ResponseEntity<String> getReuniones(@PathVariable Integer id) {
        String json = metodos.obtenerReuniones(id);
        return json != null
                ? ResponseEntity.ok(json)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"No se pudieron obtener las reuniones\"}");
    }
}
