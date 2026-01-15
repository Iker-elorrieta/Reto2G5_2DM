package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Modelo.EnviarDatos;
import Modelo.SocketCliente;
import Modelo.Users;

public class Controlador implements ActionListener {

	private JTextField campoUsername;
	private JPasswordField campoPassword;
	private SocketCliente socketCliente;
	private static EnviarDatos enviarDatos = null;

	public Controlador() {
		try {
			socketCliente = new SocketCliente();

			enviarDatos = new EnviarDatos(socketCliente.getOut(), socketCliente.getIn());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void camposLogin(JTextField campoUsername, JPasswordField campoPassword) {
		this.campoUsername = campoUsername;
		this.campoPassword = campoPassword;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		switch (comando) {
		case "LOGIN":
			String username = campoUsername.getText();
			String password = new String(campoPassword.getPassword());
			System.out.println("Controlador: " + username + " " + password);
			String jsonUsuario = enviarDatos.login(username, password);
			Users usuarioJson = enviarDatos.leerJson(jsonUsuario, Users.class);
			
			System.out.println("Usuario recibido: " + usuarioJson.getUsername() + " " + usuarioJson.getPassword() + usuarioJson.getId());

			break;

		default:
			System.out.println("Comando no reconocido: " + comando);
		}
	}

	public void cerrarConexion() {
		if (socketCliente != null) {
			socketCliente.cerrarConexion();
		}
	}
}
