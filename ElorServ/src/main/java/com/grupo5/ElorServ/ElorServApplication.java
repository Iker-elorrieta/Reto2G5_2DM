package com.grupo5.ElorServ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import Socket.SocketServer;

@SpringBootApplication
@ComponentScan(basePackages = {"com.grupo5.ElorServ", "Socket", "Modelo"})
public class ElorServApplication {
	
	public static void main(String[] args) {
        // Iniciar Spring Boot y GUARDAR el contexto
        ConfigurableApplicationContext context = SpringApplication.run(ElorServApplication.class, args);

        // Obtener el SocketServer desde Spring (NO con new!)
        SocketServer socketServer = context.getBean(SocketServer.class);

        // Iniciar en un hilo nuevo
        new Thread(() -> socketServer.iniciar()).start();
    }

}
