package Controlador;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modelo.Users;
import Vista.GestionarReuniones;

public class ControladorGestionarReuniones implements ActionListener {
	
	private Window previousWindow;
	//private Users user;
	private GestionarReuniones ventana;
	

	public ControladorGestionarReuniones(Users user,Window previousWindow) {
	    this.previousWindow = previousWindow;
	    //this.user = user;
	}

	public void iniciarGestionarReuniones() {
		ventana = new GestionarReuniones(this);
		ventana.setVisible(true);
	}
	
	 public void actionPerformed(ActionEvent e) {
	        String comando = e.getActionCommand();
	        switch (comando) {
	        case "VOLVER":
	            if (previousWindow != null) {
	                previousWindow.setVisible(true);
	            }
	            ventana.dispose();
	            break;
			}
	 }

}
