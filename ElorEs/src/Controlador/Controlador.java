package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Modelo.Request;
import Modelo.Response;
import Modelo.SocketCliente;

public class Controlador implements ActionListener {

	private JTextField usernameField;
	private JPasswordField passwordField;
	private SocketCliente socketCliente;

	public Controlador() {
		socketCliente = new SocketCliente();
	}

	public void setFields(JTextField usernameField, JPasswordField passwordField) {
		this.usernameField = usernameField;
		this.passwordField = passwordField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();

		switch (comando) {
		case "LOGIN":
			login();
			break;

		default:
			System.out.println("Comando no reconocido: " + comando);
		}
	}

	private void login() {
		Request request = new Request();
		request.setHeader("LOGIN");

		HashMap<String, Object> data = new HashMap<>();
		data.put("username", usernameField.getText());
		data.put("password", new String(passwordField.getPassword()));

		request.setData(data);

		Response response = socketCliente.enviarRequest(request);

		System.out.println("Respuesta del servidor: " + response.getStatus() + " - " + response.getMessage());

		if ("OK".equals(response.getStatus())) {
			System.out.println("Login exitoso: " + response.getMessage());
		} else {
			System.out.println("Error en login: " + response.getMessage());
		}
	}

	public void cerrarConexion() {
		if (socketCliente != null) {
			socketCliente.cerrarConexion();
		}
	}
}
