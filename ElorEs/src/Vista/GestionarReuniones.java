package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import Controlador.ControladorGestionarReuniones;

public class GestionarReuniones extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

    private JTable tablaGestion;
    private DefaultTableModel modeloGestion;
    
    // Botones
    private JButton btnVolver;
    private JButton btnCrear;
    private JButton btnAceptar;
    private JButton btnDenegar;

	public GestionarReuniones(ControladorGestionarReuniones controlador) {
        setTitle("ElorEs - Gestión de Reuniones");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500); 
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		Estilos.panelFondo(contentPane);
		setContentPane(contentPane);

		// --- 1. HEADER ---
		JPanel panelTop = new JPanel(new BorderLayout());
		Estilos.header(panelTop); 

		btnVolver = new JButton("Volver al Calendario");
		Estilos.botonHeader(btnVolver);
		btnVolver.setActionCommand("VOLVER");
		btnVolver.addActionListener(controlador);

		JLabel lblTitulo = new JLabel("GESTIÓN DE REUNIONES", SwingConstants.CENTER);
		Estilos.labelTitulo(lblTitulo);

		JPanel panelVacio = new JPanel();
		panelVacio.setOpaque(false);
		panelVacio.setPreferredSize(new Dimension(150, 10));

		panelTop.add(btnVolver, BorderLayout.WEST);
		panelTop.add(lblTitulo, BorderLayout.CENTER);
		panelTop.add(panelVacio, BorderLayout.EAST);

		contentPane.add(panelTop, BorderLayout.NORTH);

		// --- 2. TABLA CENTRAL ---
		String[] columnas = { "ID", "Título", "Asunto", "Solicitante", "Profesor", "Fecha", "Aula", "Estado" };

		modeloGestion = new DefaultTableModel(null, columnas) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; 
			}
		};

		tablaGestion = new JTable(modeloGestion);
		tablaGestion.setRowHeight(30);
		tablaGestion.setFont(new Font("Roboto", Font.PLAIN, 12));
		tablaGestion.setBackground(new Color(50, 65, 90));
		tablaGestion.setForeground(Color.WHITE);
		tablaGestion.setSelectionBackground(new Color(90, 150, 200));
		tablaGestion.setSelectionForeground(Color.WHITE);

		// Estilo del Header
		JTableHeader header = tablaGestion.getTableHeader();
		header.setFont(new Font("Roboto", Font.BOLD, 14));
		header.setBackground(new Color(70, 130, 180));
		header.setForeground(Color.WHITE);

		// Scroll
		JScrollPane scrollPane = new JScrollPane(tablaGestion);
		scrollPane.getViewport().setBackground(new Color(30, 45, 70));
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// --- 3. PANEL INFERIOR (Acciones) ---
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
		Estilos.body(panelBotones); 

		btnCrear = new JButton("Nueva Reunión (+)");
		Estilos.botonPrimario(btnCrear);
		btnCrear.setBackground(new Color(46, 139, 87)); 
		btnCrear.setActionCommand("NUEVA_REUNION");
		btnCrear.addActionListener(controlador);

		btnAceptar = new JButton("Aceptar Solicitud");
		Estilos.botonPrimario(btnAceptar);
		btnAceptar.setActionCommand("ACEPTAR_REUNION");
		btnAceptar.addActionListener(controlador);

		btnDenegar = new JButton("Denegar Solicitud");
		Estilos.botonPrimario(btnDenegar);
		btnDenegar.setBackground(new Color(205, 92, 92)); 
		btnDenegar.setActionCommand("DENEGAR_REUNION");
		btnDenegar.addActionListener(controlador);

		// --- LÓGICA DE ACTIVAR/DESACTIVAR BOTONES ---
		
		// 1. Estado inicial: Desactivados
		btnAceptar.setEnabled(false);
		btnDenegar.setEnabled(false);

		// 2. Listener para detectar selección en la tabla
		tablaGestion.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// Evitar eventos duplicados (solo actuar cuando termina la selección)
				if (!e.getValueIsAdjusting()) {
					boolean hayFilaSeleccionada = tablaGestion.getSelectedRow() != -1;
					
					// Activar o desactivar según si hay fila seleccionada
					btnAceptar.setEnabled(hayFilaSeleccionada);
					btnDenegar.setEnabled(hayFilaSeleccionada);
				}
			}
		});

		panelBotones.add(btnCrear);
		panelBotones.add(Box.createHorizontalStrut(30)); 
		panelBotones.add(btnAceptar);
		panelBotones.add(btnDenegar);

		contentPane.add(panelBotones, BorderLayout.SOUTH);
	}

	// --- GETTERS ---
	public DefaultTableModel getModeloGestion() {
		return modeloGestion;
	}

	public JTable getTablaGestion() {
		return tablaGestion;
	}

	public Integer getIdReunionSeleccionada() {
		int fila = tablaGestion.getSelectedRow();
		if (fila == -1)
			return null;
		
		Object id = modeloGestion.getValueAt(fila, 0);
		if (id instanceof Integer)
			return (Integer) id;
		try {
			return Integer.parseInt(id.toString());
		} catch (Exception e) {
			return null;
		}
	}
}