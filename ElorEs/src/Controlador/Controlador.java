package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Modelo.EnviarDatos;
import Modelo.SocketCliente;

public class Controlador implements ActionListener {

	private JTextField campoUsername;
	private JPasswordField campoPassword;
	private SocketCliente socketCliente;
	private static final EnviarDatos enviarDatos = new EnviarDatos();

	public Controlador() {
		socketCliente = new SocketCliente();
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
			String username = (String) campoUsername.getText();
			String password = (String) campoPassword.getText();
			enviarDatos.login(username,password);
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
