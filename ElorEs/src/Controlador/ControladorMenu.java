package Controlador;

import Modelo.Users;
import Vista.Menu;

public class ControladorMenu {
	
	private Users user;
	private Menu menu;
	public ControladorMenu(Users user) {
		this.user=user;
		
	}
	
	public void iniciarMenu() {
		menu = new Menu();
		menu.setVisible(true);
	}

}
