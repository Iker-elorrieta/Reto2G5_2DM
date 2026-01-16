package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import Modelo.Users;
import Vista.Login;
import Vista.Menu;
import Vista.Perfil;

public class ControladorMenu implements ActionListener {
    
    private Users user;
    private Menu menu;
    private Login login = new Login();
    private Perfil perfil = new Perfil();
    
    public ControladorMenu(Menu menu, Users user) {
        this.user = user;
        this.menu = menu;
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    	String comando = e.getActionCommand();
		switch (comando) {
		case "PERFIL":
			ControladorPerfil perfilCtrl = new ControladorPerfil(user, perfil);
			perfilCtrl.iniciarPerfil();
			menu.dispose();
			break;
		case "LOGOUT":
			ControladorLogin loginCtrl = new ControladorLogin(login);
			loginCtrl.iniciarLogin();
			menu.dispose();
			break;
		case "ALUMNOS":
			ControladorAlumnos alumnosCtrl = new ControladorAlumnos(user);
			alumnosCtrl.iniciarAlumnos();
			menu.dispose();
			break;
			/*if (campoUsername.getText().isEmpty() || campoPassword.getPassword().length == 0) {
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
				    ControladorMenu menuCtrl = new ControladorMenu(usuarioJson);
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
			break;*/
		}
		
    }

    public void iniciarMenu() {
        menu = new Menu(); // crea la ventana
        menu.setNombre(user.getNombre());
        System.out.println("user argazkia url: " + user.getArgazkiaUrl());
        if(user.getArgazkiaUrl() != null) {
        menu.setFoto(user.getArgazkiaUrl());
        }
        menu.setVisible(true);
    }
}
