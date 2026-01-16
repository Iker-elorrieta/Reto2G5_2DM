package Controlador;

import Vista.Reuniones;

public class ControladorReuniones {

    private Reuniones ventana;

    public ControladorReuniones() {
        ventana = new Reuniones();
    }

    public void iniciarReuniones() {
        ventana.setVisible(true);
    }
}
