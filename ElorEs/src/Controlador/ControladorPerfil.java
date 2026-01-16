package Controlador;

import Modelo.Users;
import Vista.Perfil;

public class ControladorPerfil {
	
	private Perfil perfil;
	private Users user;
	public ControladorPerfil(Users user, Perfil perfil) {
		this.perfil = new Perfil();
		this.user = user;
	}
	
	public void iniciarPerfil() {
		perfil.setVisible(true);
		perfil.setLocationRelativeTo(null);
	}

}
