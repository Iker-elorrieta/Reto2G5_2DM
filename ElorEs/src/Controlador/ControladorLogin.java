package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Modelo.EnviarDatos;
import Modelo.SocketCliente;
import Modelo.Users;
import Vista.Login;
import Vista.Menu;


public class ControladorLogin implements ActionListener {

	private JTextField campoUsername;
	private JPasswordField campoPassword;
	private JLabel lblMensaje;
	private SocketCliente socketCliente;
	private  EnviarDatos enviarDatos = null;
	private Login ventanaLogin;
	private Menu menu;
	
	public ControladorLogin(Login ventanaLogin) {
		this.ventanaLogin = ventanaLogin;
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

	public void setLblMensaje(JLabel lblMensaje) {
		this.lblMensaje = lblMensaje;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		switch (comando) {
		case "LOGIN":
			if (campoUsername.getText().isEmpty() || campoPassword.getPassword().length == 0) {
				String faltan = "";
				if (campoUsername.getText().isEmpty())
					faltan += "Username ";
				if (campoPassword.getPassword().length == 0)
					faltan += "Contraseña";
				lblMensaje.setText("Falta: " + faltan);
				return;
			}

			String username = campoUsername.getText();
			String password = new String(campoPassword.getPassword());

			try {
				MessageDigest md = MessageDigest.getInstance("SHA");
				byte dataBytes[] = password.getBytes();
				md.update(dataBytes);
				byte resumen[] = md.digest();
				String passwordCifrada = new String(resumen);

				String jsonUsuario = enviarDatos.login(username, passwordCifrada);
				Users usuarioJson = enviarDatos.leerJson(jsonUsuario, Users.class);

				if (usuarioJson != null) {
					lblMensaje.setText("Login exitoso!");
				    ControladorMenu menuCtrl = new ControladorMenu(menu, usuarioJson);
				    menuCtrl.iniciarMenu();
					ventanaLogin.dispose();
				} else {
					lblMensaje.setText("Usuario o contraseña incorrectos");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				lblMensaje.setText("Error al cifrar contraseña");
				return;
			}
			break;
		}
	}
	
	public void iniciarLogin() {
		ventanaLogin.setVisible(true);
	}

	public void cerrarConexion() {
		if (socketCliente != null) {
			socketCliente.cerrarConexion();
		}
	}
}
