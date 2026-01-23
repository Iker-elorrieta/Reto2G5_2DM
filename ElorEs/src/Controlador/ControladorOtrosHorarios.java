
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Modelo.EnviarDatos;
import Modelo.SocketCliente;
import Modelo.Users;
import Vista.Menu;
import Vista.OtrosHorarios;

public class ControladorOtrosHorarios implements ActionListener {

    private OtrosHorarios ventana;
    private Users user;
    private SocketCliente socketCliente;
    private static EnviarDatos enviarDatos = null;
    private Menu menu;
    private ArrayList<Users> profesores; // Store the full list

    public ControladorOtrosHorarios(Users user) {
        ventana = new OtrosHorarios(this);
        this.user = user;
        try {
            socketCliente = new SocketCliente();
            enviarDatos = new EnviarDatos(socketCliente.getOut(), socketCliente.getIn());
            obtenerProfesores();
            // Set up the action listener for table selection
            ventana.setTableSelectionActionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        switch (comando) {
            case "VOLVER":
                ventana.setVisible(false);
                ControladorMenu controladorMenu = new ControladorMenu(menu, user);
                controladorMenu.iniciarMenu();
                break;
            case "PROFESOR_SELECCIONADO":
                int selectedRow = ventana.getTablaProfesores().getSelectedRow();
                if (selectedRow != -1 && profesores != null && selectedRow < profesores.size()) {
                    Users selectedUser = profesores.get(selectedRow);
                    ControladorHorarios controladorHorarios = new ControladorHorarios(selectedUser, ventana);
                    controladorHorarios.iniciarHorarios();
                    ventana.setVisible(false);
                }
                break;
        }
    }

    public void iniciarOtrosHorarios() {
        ventana.setVisible(true);
    }

    public void obtenerProfesores() {
        profesores = enviarDatos.conseguirProfesores(); // Save the list

        // Clear previous rows
        ventana.getModeloProfesores().setRowCount(0);

        // Add each professor to the table
        for (Users prof : profesores) {
            ventana.getModeloProfesores().addRow(new Object[]{
                prof.getNombre(),
                prof.getApellidos(),
                prof.getEmail()
            });
        }
    }
}
