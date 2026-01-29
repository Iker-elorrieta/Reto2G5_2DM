package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Controlador.ControladorReuniones;

import java.awt.*;

public class Reuniones extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaReuniones;
    private DefaultTableModel modeloReuniones;
    private JButton btnVolver;

    private static final String[] DIAS = {"LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES"};
    private static final int HORAS = 6;

    public Reuniones(ControladorReuniones controlador) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 500);
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        Estilos.panelFondo(contentPane);
        setContentPane(contentPane);

        // --- PANEL SUPERIOR ---
        JPanel panelTop = new JPanel(new BorderLayout());
        Estilos.panelFondo(panelTop);
        panelTop.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Estilos.panelFondo(panelBoton);
        
        btnVolver = new JButton("Volver");
        Estilos.botonPrimario(btnVolver);
        btnVolver.setActionCommand("VOLVER");
        btnVolver.addActionListener(controlador);
        panelBoton.add(btnVolver);
        
        JButton btnGestionar = new JButton("Gestionar Reuniones");
        Estilos.botonPrimario(btnGestionar);
        btnGestionar.setActionCommand("IR_GESTION_REUNIONES"); 
        btnGestionar.addActionListener(controlador);
        panelBoton.add(btnGestionar);

        JLabel lblTitulo = new JLabel("SOLICITUDES DE REUNIONES", SwingConstants.CENTER);
        Estilos.labelTitulo(lblTitulo);
        
        panelTop.add(panelBoton, BorderLayout.WEST);
        panelTop.add(lblTitulo, BorderLayout.CENTER);
        
        JPanel panelVacio = new JPanel();
        Estilos.panelFondo(panelVacio);
        panelVacio.setPreferredSize(new Dimension(250, 10)); 
        panelTop.add(panelVacio, BorderLayout.EAST);

        contentPane.add(panelTop, BorderLayout.NORTH);

        // --- TABLA ---
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

        modeloReuniones = new DefaultTableModel(datos, columnas) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaReuniones = new JTable(modeloReuniones);
        // Aumentamos altura para soportar HTML (3 líneas en conflictos)
        tablaReuniones.setRowHeight(65); 
        tablaReuniones.setFont(new Font("Roboto", Font.BOLD, 11));
        tablaReuniones.setBackground(new Color(50, 65, 90));
        tablaReuniones.setForeground(Color.WHITE);
        tablaReuniones.setGridColor(new Color(30, 45, 70));

        // --- RENDERIZADO PERSONALIZADO (AQUÍ ESTÁ EL CAMBIO DE COLOR) ---
        tablaReuniones.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                setHorizontalAlignment(SwingConstants.CENTER);
                String texto = (value != null) ? ((String) value).toUpperCase() : "";
                
                // Si está seleccionada la fila, respetar el azul de selección
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                    return c;
                }

                // COLUMNA DE HORAS
                if (column == 0) {
                    c.setBackground(new Color(40, 55, 80));
                    c.setForeground(Color.LIGHT_GRAY);
                    return c;
                }

                // --- LÓGICA DE COLORES DE ESTADOS ---
                if (!texto.isEmpty()) {
                    
                    if (texto.contains("CONFLICTO")) {
                        // GRIS
                        c.setBackground(Color.LIGHT_GRAY); 
                        c.setForeground(Color.BLACK);
                    } 
                    else if (texto.contains("ACEPTADA") || texto.contains("ONARTUA")) {
                        // VERDE
                        c.setBackground(new Color(46, 204, 113)); 
                        c.setForeground(Color.BLACK);
                    } 
                    else if (texto.contains("DENEGADA") || texto.contains("UKATUA")) {
                        // ROJO
                        c.setBackground(new Color(231, 76, 60)); 
                        c.setForeground(Color.WHITE);
                    } 
                    else if (texto.contains("PENDIENTE") || texto.contains("ZAIN")) {
                        // NARANJA
                        c.setBackground(new Color(243, 156, 18)); 
                        c.setForeground(Color.BLACK);
                    } 
                    else {
                        // **AQUÍ ESTÁ EL CAMBIO**: 
                        // CLASE NORMAL (SIN REUNIÓN) -> MANTENER AZUL OSCURO DEL TEMA
                        c.setBackground(new Color(50, 65, 90));
                        c.setForeground(Color.WHITE);
                    }
                } 
                else {
                    // CELDA VACÍA
                    c.setBackground(new Color(50, 65, 90));
                    c.setForeground(Color.WHITE);
                }

                return c;
            }
        });

        // Estilo Header
        JTableHeader header = tablaReuniones.getTableHeader();
        header.setFont(new Font("Roboto", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaReuniones);
        scrollPane.getViewport().setBackground(new Color(30, 45, 70)); 
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        // --- LEYENDA ---
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        Estilos.panelFondo(panelLeyenda);
        
        panelLeyenda.add(crearEtiquetaLeyenda("Pendiente", new Color(243, 156, 18), Color.BLACK));
        panelLeyenda.add(crearEtiquetaLeyenda("Aceptada", new Color(46, 204, 113), Color.BLACK));
        panelLeyenda.add(crearEtiquetaLeyenda("Denegada", new Color(231, 76, 60), Color.WHITE));
        panelLeyenda.add(crearEtiquetaLeyenda("Conflicto", Color.LIGHT_GRAY, Color.BLACK));
        
        contentPane.add(panelLeyenda, BorderLayout.SOUTH);
    }

    private JLabel crearEtiquetaLeyenda(String texto, Color fondo, Color letra) {
        JLabel lbl = new JLabel(" " + texto + " ");
        lbl.setOpaque(true);
        lbl.setBackground(fondo);
        lbl.setForeground(letra);
        lbl.setFont(new Font("Roboto", Font.BOLD, 12));
        lbl.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return lbl;
    }

    public DefaultTableModel getModeloReuniones() {
        return modeloReuniones;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }
}