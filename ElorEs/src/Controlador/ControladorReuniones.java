package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import Modelo.EnviarDatos;
import Modelo.Horarios;
import Modelo.SocketCliente;
import Modelo.Users;
import Vista.Menu;
import Vista.Reuniones;

public class ControladorReuniones implements ActionListener {

    private Reuniones ventana;
    private SocketCliente socketCliente;
    private Users user;
    private Menu menu;
    private EnviarDatos enviarDatos = null;

    public ControladorReuniones(Users user) {
        this.user = user;
        ventana = new Reuniones(this);
        try {
            socketCliente = new SocketCliente();
            enviarDatos = new EnviarDatos(socketCliente.getOut(), socketCliente.getIn());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mostrarHorarioYReuniones();
        ventana.setVisible(true);
    }

    public void iniciarReuniones() {
        ventana.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        switch (comando) {
        case "VOLVER":
            ventana.setVisible(false);
            ControladorMenu controladorMenu = new ControladorMenu(menu, user);
            controladorMenu.iniciarMenu();
            break;
        case "IR_GESTION_REUNIONES":
            ventana.setVisible(false);
            ControladorGestionarReuniones controladorGestion = new ControladorGestionarReuniones(user);
            controladorGestion.iniciarGestionarReuniones();
            break;
        }
    }

    public void mostrarHorarioYReuniones() {
        // 1. Obtener datos
        ArrayList<Horarios> horario = enviarDatos.datosHorarios(user);
        ArrayList<Modelo.Reuniones> reuniones = enviarDatos.datosReuniones(user);
        DefaultTableModel modelo = ventana.getModeloReuniones();

        // 2. Limpiar tabla (respetando la columna de horas)
        for (int i = 0; i < modelo.getRowCount(); i++) {
            for (int j = 1; j < modelo.getColumnCount(); j++) {
                modelo.setValueAt("", i, j);
            }
        }

        // Mapeo manual de Días a Columnas
        Map<String, Integer> diaColumna = new HashMap<>();
        diaColumna.put("LUNES", 1);
        diaColumna.put("MARTES", 2);
        diaColumna.put("MIÉRCOLES", 3);
        diaColumna.put("MIERCOLES", 3);
        diaColumna.put("JUEVES", 4);
        diaColumna.put("VIERNES", 5);

        // 3. PINTAR HORARIO DE CLASES
        if (horario != null) {
            for (Horarios h : horario) {
                String dia = h.getDia().toUpperCase();
                Integer col = diaColumna.get(dia);
                int row = h.getHora() - 1; // La BBDD guarda 1, la tabla empieza en 0

                if (col != null && row >= 0 && row < modelo.getRowCount()) {
                    String value = h.getModulos().getNombre();
                    if (h.getAula() != null) value += " (" + h.getAula() + ")";
                    
                    // Si hay solapamiento de clases (dos clases misma hora)
                    String current = (String) modelo.getValueAt(row, col);
                    if (current != null && !current.isEmpty()) {
                        value = current + " / " + value;
                    }
                    modelo.setValueAt(value, row, col);
                }
            }
        }

        // --- 4. PINTAR REUNIONES (LÓGICA MODIFICADA) ---
        if (reuniones != null) {
            for (Modelo.Reuniones r : reuniones) {
                if (r.getFecha() == null) continue;

                LocalDateTime fechaHora = r.getFecha().toInstant()
                                          .atZone(ZoneId.systemDefault())
                                          .toLocalDateTime();

                // Obtener Columna (Día)
                int col = fechaHora.getDayOfWeek().getValue(); 
                if (col > 5) continue; // Ignoramos fines de semana

                // Obtener Fila (Hora). Asumiendo 08:00 AM es la fila 0.
                int row = fechaHora.getHour() - 8; 

                if (row >= 0 && row < modelo.getRowCount()) {
                    
                    String contenidoClase = (String) modelo.getValueAt(row, col);
                    String asuntoReunion = (r.getAsunto() != null) ? r.getAsunto() : "Sin Asunto";
                    String estado = (r.getEstado() != null) ? r.getEstado().toUpperCase() : "PENDIENTE";
                    
                    // --- LÓGICA DE PRIORIDAD Y VISUALIZACIÓN ---
                    
                    // Verificamos si hay clase en esa celda
                    if (contenidoClase != null && !contenidoClase.isEmpty()) {
                        
                        // CASO 1: HAY CLASE Y ESTÁ PENDIENTE -> CONFLICTO
                        if (estado.equals("PENDIENTE") || estado.equals("ZAIN")) {
                            
                            String textoConflicto = "<html><center>"
                                    + "<b>CONFLICTO</b><br>"
                                    + "R: " + asuntoReunion + "<br>"
                                    + "C: " + contenidoClase
                                    + "</center></html>";
                            
                            modelo.setValueAt(textoConflicto, row, col);
                            
                        } else {
                            // CASO 2: HAY CLASE PERO ESTÁ ACEPTADA/DENEGADA
                            // Mostramos el estado de la reunión (Tiene prioridad visual sobre la clase)
                            modelo.setValueAt(estado + ": " + asuntoReunion, row, col);
                        }
                        
                    } else {
                        // CASO 3: NO HAY CLASE (Solo muestra Reunión)
                        modelo.setValueAt(estado + ": " + asuntoReunion, row, col);
                    }
                }
            }
        }
    }
}