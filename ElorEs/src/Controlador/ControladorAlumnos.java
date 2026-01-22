package Controlador;

import Vista.Alumnos;

public class ControladorAlumnos {

    private Alumnos ventana;

    public ControladorAlumnos() {
        ventana = new Alumnos();
    }

    public void iniciarAlumnos() {
        ventana.setVisible(true);
    }
}
