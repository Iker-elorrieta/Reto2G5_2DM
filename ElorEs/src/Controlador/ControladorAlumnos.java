package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import Modelo.EnviarDatos;
import Modelo.SocketCliente;
import Modelo.Users;
import Vista.Alumnos;
import Vista.Menu;

public class ControladorAlumnos implements ActionListener {

	private Alumnos ventana;
	private Menu menu;
	private Users user;
	private SocketCliente socketCliente;
	private  EnviarDatos enviarDatos = null;

	public ControladorAlumnos(Users user) {
		ventana = new Alumnos(this);
		this.user = user;
		try {
			socketCliente = new SocketCliente();

			enviarDatos = new EnviarDatos(socketCliente.getOut(), socketCliente.getIn());
			
			mostrarAlumnos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void iniciarAlumnos() {
		ventana.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		switch (comando) {
		case "VOLVER":
			ControladorMenu controladorMenu = new ControladorMenu(menu,user);
			controladorMenu.iniciarMenu();
			ventana.dispose();
			break;
		case "ACTUALIZAR":
			mostrarAlumnos();
			break;
			
			
		}
	}
	
	public void mostrarAlumnos() {
		ArrayList<Users> alumnos = enviarDatos.conseguirAlumnos(user);
		  DefaultTableModel model = (DefaultTableModel) ventana.getTable().getModel();
		    model.setRowCount(0);

		    for (Users alumno : alumnos) {
		        Object[] row = {
		            alumno.getDni(),
		            alumno.getNombre(),
		            alumno.getApellidos(),
		            alumno.getDireccion(),
		            alumno.getTelefono1()
		        };
		        model.addRow(row);
		    }
		
	}
	
}
