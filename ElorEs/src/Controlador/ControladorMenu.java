package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modelo.Users;
import Vista.Login;
import Vista.Menu;

public class ControladorMenu implements ActionListener {

	private Users user;
	private Menu menu;
	private Login login = new Login();

	public ControladorMenu(Menu menu, Users user) {
		this.user = user;
		this.menu = menu;

	}

	public void actionPerformed(ActionEvent e) {

		String comando = e.getActionCommand();
		switch (comando) {
		case "PERFIL":
			ControladorPerfil perfilCtrl = new ControladorPerfil(user, menu);
			perfilCtrl.iniciarPerfil();
			menu.setVisible(false);
			break;
		case "LOGOUT":
			ControladorLogin loginCtrl = new ControladorLogin(login);
			loginCtrl.iniciarLogin();
			menu.dispose();
			break;
		case "ALUMNOS":
			ControladorAlumnos alumnosCtrl = new ControladorAlumnos(user);
			alumnosCtrl.iniciarAlumnos();
			menu.setVisible(false); // no lo cerramos, solo lo escondemos
			break;

		case "HORARIOS":
			ControladorHorarios horariosCtrl = new ControladorHorarios();
			horariosCtrl.iniciarHorarios();
			menu.setVisible(false);
			break;

		case "OTROS-HORARIOS":
			ControladorOtrosHorarios otrosCtrl = new ControladorOtrosHorarios();
			otrosCtrl.iniciarOtrosHorarios();
			menu.setVisible(false);
			break;

		case "REUNIONES":
			ControladorReuniones reunionesCtrl = new ControladorReuniones();
			reunionesCtrl.iniciarReuniones();
			menu.setVisible(false);
			break;

		}

	}

	public void iniciarMenu() {
		menu = new Menu(this); // crea la ventana
		menu.setNombre(user.getNombre());
		System.out.println("user argazkia url: " + user.getArgazkiaUrl());
		if (user.getArgazkiaUrl() != null) {
			menu.setFoto(user.getArgazkiaUrl());
		}
		menu.setVisible(true);
	}
}
