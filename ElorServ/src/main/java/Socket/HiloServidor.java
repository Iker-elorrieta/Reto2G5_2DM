package Socket;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Modelo.Metodos;
import Modelo.Users;

public class HiloServidor extends Thread {

	private Metodos metodos = new Metodos();
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public HiloServidor(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush(); 

			ois = new ObjectInputStream(socket.getInputStream());


			while (true) {
				Object obj = ois.readObject();

				if (!(obj instanceof String)) {
					System.out.println("Objeto inesperado");
					break;
				}

				String comando = (String) obj;

				switch (comando) {
				case "LOGIN":
					String username = (String) ois.readObject();
					String password = (String) ois.readObject();

					Users u = metodos.loginCliente(username, password);
					String respuesta = metodos.crearJson(u);
					oos.writeObject(respuesta);
					oos.flush();
					break;
				case "OBTENER_ALUMNOS":
					Integer userId = (Integer) ois.readObject();
					String alumnosJson = metodos.obtenerAlumnosPorProfesor(userId);
					oos.writeObject(alumnosJson);
				default:
					break;
				}
			}

		} catch (EOFException e) {
			System.out.println("Clience cerro conexion");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (oos != null)
					oos.close();
				if (socket != null && !socket.isClosed())
					socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * private boolean estaConectado() { return socket != null && !socket.isClosed()
	 * && socket.isConnected(); }
	 */
}
