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
				case "CONSEGUIR_ALUMNOS":
					String userJson = (String) ois.readObject();
					System.out.println("Servidor: Recibido comando CONSEGUIR_ALUMNOS");
					String alumnosJson = metodos.obtenerAlumnos(userJson);
					oos.writeObject(alumnosJson);
					oos.flush();
					break;
				case "CONSEGUIR_HORARIOS":
					Integer userId = (Integer) ois.read();
					System.out.println("Servidor: Recibido comando CONSEGUIR_HORARIOS");
					
					String horariosJson = metodos.obtenerHorarios(userId);
					oos.writeObject(horariosJson);
					oos.flush();
					break;
				case "CONSEGUIR_PROFESORES":
					String profesoresJson = metodos.obtenerProfesores();
					oos.writeObject(profesoresJson);
					oos.flush();
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
