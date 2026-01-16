package Controlador;

import Vista.Horario;

public class ControladorHorarios {

    private Horario ventana;

    public ControladorHorarios() {
        ventana = new Horario();
    }

    public void iniciarHorarios() {
        ventana.setVisible(true);
    }
}
