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
	private static EnviarDatos enviarDatos = null;
	
	private ArrayList<Users> listaAlumnos;
    
    public ControladorAlumnos(Users user) {
    	try {
			socketCliente = new SocketCliente();

			enviarDatos = new EnviarDatos(socketCliente.getOut(), socketCliente.getIn());
		} catch (Exception e) {
			e.printStackTrace();
		}
        ventana = new Alumnos(this);
        this.user = user;
        getListaAlumnos();
        
    }

    public void iniciarAlumnos() {
        ventana.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    	String comando = e.getActionCommand();
		switch (comando) {
		case "VOLVER":
			ControladorMenu perfilMenu = new ControladorMenu(menu,user);
			perfilMenu.iniciarMenu();
			ventana.setVisible(false);
			break;
		case "ACTUALIZAR":
			getListaAlumnos();
			break;
		}
    }
    
    public void getListaAlumnos() {
    	 listaAlumnos = enviarDatos.obtenerAlumnos(user);
    	 if (listaAlumnos != null && !listaAlumnos.isEmpty()) {
       	  String[] columnNames = {"Nombre", "Apellido", "Email", "DNI", "Dirección", "Teléfono"};
       	    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

       	    for (Users users : listaAlumnos) {
       	        Object[] row = {
       	            users.getNombre(),
       	            users.getApellidos(),
       	            users.getEmail(),
       	            users.getDni(),
       	            users.getDireccion(),
       	            users.getTelefono1()
       	        };
       	        model.addRow(row);
       	    }

       	    // Assuming ventana has a JTable named 'tablaAlumnos'
       	    ventana.getTablaAlumnos().setModel(model);
       } else {
           System.out.println("No students found or list is null.");
       }
    }
    
    
}
