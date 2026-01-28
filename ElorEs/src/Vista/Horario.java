// Horario.java
package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Controlador.ControladorHorarios;

import java.awt.*;

public class Horario extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaHorario;
    private DefaultTableModel modeloHorario;
    private JButton btnVolver;

    private static final String[] DIAS = {"LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES"};
    private static final int HORAS = 6;

    public Horario(ControladorHorarios controlador) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400);
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        Estilos.panelFondo(contentPane);
        setContentPane(contentPane);

        // Top panel with "Volver" button
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Estilos.panelFondo(panelTop);
        btnVolver = new JButton("Volver");
        Estilos.botonPrimario(btnVolver);
        panelTop.add(btnVolver);
        contentPane.add(panelTop, BorderLayout.NORTH);

        String[] columnas = new String[DIAS.length + 1];
        columnas[0] = "Hora";
        System.arraycopy(DIAS, 0, columnas, 1, DIAS.length);

        String[][] datos = new String[HORAS][DIAS.length + 1];
        for (int i = 0; i < HORAS; i++) {
            datos[i][0] = String.valueOf(i + 1);
            for (int j = 1; j < columnas.length; j++) {
                datos[i][j] = "";
            }
        }

        modeloHorario = new DefaultTableModel(datos, columnas) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaHorario = new JTable(modeloHorario);
        tablaHorario.setRowHeight(40);
        tablaHorario.setFont(new Font("Roboto", Font.PLAIN, 14));
        tablaHorario.setBackground(new Color(50, 65, 90));
        tablaHorario.setForeground(Color.WHITE);

        JTableHeader header = tablaHorario.getTableHeader();
        header.setFont(new Font("Roboto", Font.BOLD, 16));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tablaHorario);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        btnVolver.setActionCommand("VOLVER");
        btnVolver.addActionListener(controlador);
    }

    public DefaultTableModel getModeloHorario() {
        return modeloHorario;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }
}
