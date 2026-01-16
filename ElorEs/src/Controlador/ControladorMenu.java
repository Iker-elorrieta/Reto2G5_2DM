package Controlador;

import Modelo.Users;
import Vista.Menu;

public class ControladorMenu {
    
    private Users user;
    private Menu menu;

    public ControladorMenu(Users user) {
        this.user = user;
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
