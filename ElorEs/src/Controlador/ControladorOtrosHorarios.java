package Controlador;

import Vista.OtrosHorarios;

public class ControladorOtrosHorarios {

    private OtrosHorarios ventana;

    public ControladorOtrosHorarios() {
        ventana = new OtrosHorarios();
    }

    public void iniciarOtrosHorarios() {
        ventana.setVisible(true);
    }
}
