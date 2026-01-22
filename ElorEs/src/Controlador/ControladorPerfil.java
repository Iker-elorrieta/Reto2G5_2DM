package Controlador;

import Modelo.Users;
import Vista.Perfil;
import Vista.Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorPerfil implements ActionListener {

    private Perfil perfil;
    private Users user;
    private Menu menu;

    public ControladorPerfil(Users user, Menu menu) {
        this.user = user;
        this.menu = menu;
        this.perfil = new Perfil(); // crea la ventana sin pasar nada

         // llena los labels

        perfil.getBtnVolver().addActionListener(this);
    }

    private void cargarDatosUsuario() {
        if (user == null) {
            System.out.println("ERROR: user es null en ControladorPerfil");
            return;
        }
        perfil.getLblNombre().setText(user.getNombre());
        perfil.getLblApellidos().setText(user.getApellidos());
        perfil.getLblDni().setText(user.getDni());
        perfil.getLblEmail().setText(user.getEmail());
        perfil.getLblTelefono1().setText(user.getTelefono1());
        perfil.getLblTelefono2().setText(user.getTelefono2());
        perfil.getLblDireccion().setText(user.getDireccion());

        perfil.cargarImagen(user.getArgazkiaUrl());
        
        perfil.setTitleUsuario(user.getNombre(), user.getApellidos());
    }


    public void iniciarPerfil() {
        cargarDatosUsuario(); 
        perfil.setLocationRelativeTo(null);
        perfil.setVisible(true);
        if (menu != null) menu.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == perfil.getBtnVolver()) {
            perfil.dispose();
            if (menu != null) menu.setVisible(true);
        }
    }
}