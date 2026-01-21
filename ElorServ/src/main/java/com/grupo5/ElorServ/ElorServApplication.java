package com.grupo5.ElorServ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Socket.SocketServer;

@SpringBootApplication
public class ElorServApplication {
	
	public static void main(String[] args) {
		//Iniciar Springboot
		SpringApplication.run(ElorServApplication.class, args);
		
		//Iniciar Socket
		SocketServer SocketServer = new SocketServer();
		SocketServer.iniciar();
	}

}
