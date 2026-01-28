package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.ControladorAlumnos;

import java.awt.*;

public class Alumnos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JButton btnActualizar;
    private JButton btnVolver;

    public Alumnos(ControladorAlumnos controlador) {
        setTitle("ElorEs - Alumnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400);

        contentPane = new JPanel(new BorderLayout());
        Estilos.panelFondo(contentPane);
        setContentPane(contentPane);

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(0, 60));
        Estilos.header(header);
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        JLabel lblTitulo = new JLabel("Listado de Alumnos");
        Estilos.labelHeader(lblTitulo);
        header.add(lblTitulo);
        contentPane.add(header, BorderLayout.NORTH);

        // ===== TABLE =====
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        Estilos.body(panelTabla);

        String[] columnas = {"DNI", "Nombre", "Apellidos", "Dirección", "Teléfono1"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
           
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(50, 65, 90));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        scrollPane.getViewport().setBackground(new Color(25, 45, 80));
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        contentPane.add(panelTabla, BorderLayout.CENTER);

        // ===== BOTONES =====
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));

        btnActualizar = new JButton("Actualizar");
        Estilos.botonPrimario(btnActualizar);

        btnVolver = new JButton("Volver");
        Estilos.botonPrimario(btnVolver);

        panelBotones.add(btnActualizar);
        panelBotones.add(btnVolver);

        contentPane.add(panelBotones, BorderLayout.SOUTH);
        btnVolver.setActionCommand("VOLVER");
        btnVolver.addActionListener(controlador);
    }

    // Getters for controller usage
    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JTable getTable() {
        return table;
    }
}
