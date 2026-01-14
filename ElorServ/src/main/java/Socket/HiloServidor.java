package Socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Modelo.Metodos;
import Modelo.Request;
import Modelo.Response;
import Modelo.Users;

public class HiloServidor extends Thread {

	private Metodos metodos = new Metodos();
	private Socket socket;

	public HiloServidor(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

			Object mensaje;

			while ((mensaje = in.readObject()) != null) {
				System.out.println("Recibido objeto: " + mensaje);

				Object respuesta = procesarRequest(mensaje);

				out.writeObject(respuesta);
				out.flush();
			}

		} catch (EOFException e) {
			System.out.println("Cliente desconectado normalmente");
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error en conexión: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Object procesarRequest(Object mensaje) {
		try {
			if (!(mensaje instanceof Request)) {
				return new Response("ERROR", "Formato de mensaje inválido", null);
			}

			Request request = (Request) mensaje;
			String header = request.getHeader();

			switch (header) {
			case "LOGIN":
				return login(request);

			default:
				return new Response("ERROR", "Header no válido: " + header, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new Response("ERROR", "Error al procesar request: " + e.getMessage(), null);
		}
	}

	private Response login(Request request) {

		String username = (String) request.getData().get("username");
		String password = (String) request.getData().get("password");

		System.out.println("Intentando login con username: " + username + " y password: " + password);

		if (username == null || password == null) {
			System.out.println("Username o password es null");
			return new Response("ERROR", "Username y password son requeridos", null);
		}

		Users user = metodos.login(username, password);

		if (user != null) {
			return new Response("OK", "Login exitoso", user);
		} else {
			return new Response("ERROR", "Usuario o contraseña incorrectos", null);
		}
	}

}
