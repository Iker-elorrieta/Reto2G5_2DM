package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import Modelo.EnviarDatos;
import Modelo.Horarios;
import Modelo.SocketCliente;
import Modelo.Users;
import Vista.Horario;
import Vista.Menu;

public class ControladorHorarios implements ActionListener {

	private Horario ventana;
	private Menu menu;
	private SocketCliente socketCliente;
	private static EnviarDatos enviarDatos = null;
	private Users user;

	public ControladorHorarios(Users user) {
		this.user = user;
		ventana = new Horario(this);
		try {
			socketCliente = new SocketCliente();

			enviarDatos = new EnviarDatos(socketCliente.getOut(), socketCliente.getIn());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void iniciarHorarios() {
		ventana.setVisible(true);
		mostrarHorarios();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		switch (comando) {
		case "VOLVER":
			ControladorMenu controladorMenu = new ControladorMenu(menu,user);
			controladorMenu.iniciarMenu();
			ventana.dispose();
			break;
		}
	}
	
	// ControladorHorarios.java
	public void mostrarHorarios() {
	    ArrayList<Horarios> horario = enviarDatos.datosHorarios(user);
	    DefaultTableModel modelo = ventana.getModeloHorario();

	    // Clear previous data (except hour column)
	    for (int i = 0; i < modelo.getRowCount(); i++) {
	        for (int j = 1; j < modelo.getColumnCount(); j++) {
	            modelo.setValueAt("", i, j);
	        }
	    }

	    // Map day names to column indices
	    java.util.Map<String, Integer> diaColumna = new java.util.HashMap<>();
	    diaColumna.put("LUNES", 1);
	    diaColumna.put("MARTES", 2);
	    diaColumna.put("MIÉRCOLES", 3);
	    diaColumna.put("MIERCOLES", 3); // For both spellings
	    diaColumna.put("JUEVES", 4);
	    diaColumna.put("VIERNES", 5);

	    for (Horarios h : horario) {
	        String dia = h.getDia().toUpperCase();
	        Integer col = diaColumna.get(dia);
	        int row = h.getHora() - 1;

	        if (col != null && row >= 0 && row < modelo.getRowCount()) {
	            String value = h.getModulos().getNombre();
	            if (h.getAula() != null && !h.getAula().isEmpty()) {
	                value += " (" + h.getAula() + ")";
	            }
	            String current = (String) modelo.getValueAt(row, col);
	            if (current != null && !current.isEmpty()) {
	                value = current + ", " + value;
	            }
	            modelo.setValueAt(value, row, col);
	        }
	    }
	}



}
