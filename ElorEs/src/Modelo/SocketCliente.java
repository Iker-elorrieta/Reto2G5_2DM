package Modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketCliente {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public SocketCliente() {
		try {
			socket = new Socket("localhost", 6767);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			System.out.println("Conectado al servidor en localhost:6767");
		} catch (IOException e) {
			System.err.println("Error al conectar con el servidor: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public Response enviarRequest(Request request) {
		try {
			// Verificar si la conexión sigue activa
			if (socket == null || socket.isClosed() || !socket.isConnected()) {
				System.err.println("CONEXIÓN PERDIDA: El socket está cerrado o desconectado");
				return new Response("ERROR", "Conexión perdida con el servidor", null);
			}

			out.writeObject(request);
			out.flush();

			Response response = (Response) in.readObject();
			return response;

		} catch (IOException e) {
			System.err.println("ERROR DE CONEXIÓN: La conexión con el servidor se ha perdido");
			System.err.println("Detalles: " + e.getMessage());
			e.printStackTrace();
			cerrarConexion();
			return new Response("ERROR", "Error de conexión - Servidor desconectado", null);
		} catch (ClassNotFoundException e) {
			System.err.println("Error al leer la respuesta del servidor: " + e.getMessage());
			e.printStackTrace();
			return new Response("ERROR", "Error al procesar respuesta del servidor", null);
		}
	}

	public void cerrarConexion() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
			System.out.println("Conexión cerrada correctamente");
		} catch (IOException e) {
			System.err.println("Error al cerrar la conexión: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean estaConectado() {
		return socket != null && !socket.isClosed() && socket.isConnected();
	}
}
