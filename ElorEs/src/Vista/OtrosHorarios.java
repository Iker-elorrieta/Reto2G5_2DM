
// OtrosHorarios.java
package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Controlador.ControladorOtrosHorarios;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OtrosHorarios extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaProfesores;
    private DefaultTableModel modeloProfesores;
    private JButton btnVolver;

    public OtrosHorarios(ControladorOtrosHorarios controlador) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        Estilos.panelFondo(contentPane);
        setContentPane(contentPane);

        // Top panel with "Volver" button
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Estilos.header(panelTop);
        btnVolver = new JButton("Volver");
        Estilos.botonHeader(btnVolver);
        panelTop.add(btnVolver);
        contentPane.add(panelTop, BorderLayout.NORTH);

        // Table setup
        String[] columnas = {"Nombre", "Apellidos", "Email"};
        modeloProfesores = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProfesores = new JTable(modeloProfesores);
        tablaProfesores.setFont(new Font("Roboto", Font.PLAIN, 14));
        tablaProfesores.setRowHeight(28);
        tablaProfesores.setBackground(new Color(50, 65, 90));
        tablaProfesores.setForeground(Color.WHITE);

        JTableHeader header = tablaProfesores.getTableHeader();
        header.setFont(new Font("Roboto", Font.BOLD, 16));
        header.setBackground(new Color(20, 35, 70));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tablaProfesores);
        scrollPane.getViewport().setBackground(new Color(50, 65, 90));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        btnVolver.setActionCommand("VOLVER");
        btnVolver.addActionListener(controlador);
    }

    public JTable getTablaProfesores() {
        return tablaProfesores;
    }

    public DefaultTableModel getModeloProfesores() {
        return modeloProfesores;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }
    
    
    public void setTableSelectionActionListener(ActionListener listener) {
        tablaProfesores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaProfesores.getSelectedRow() != -1) {
                listener.actionPerformed(
                    new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "PROFESOR_SELECCIONADO")
                );
            }
        });
    }

}
