package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
	/**
	 * Create the frame.
	 */

	public GestionarReuniones(ControladorGestionarReuniones controlador) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500); // Un poco más ancha para ver los datos
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		Estilos.panelFondo(contentPane);
		setContentPane(contentPane);

		// --- 1. HEADER (Título y Volver) ---
		JPanel panelTop = new JPanel(new BorderLayout());
		Estilos.header(panelTop); // Usamos el estilo header con borde inferior

		btnVolver = new JButton("Volver al Calendario");
		Estilos.botonHeader(btnVolver);
		btnVolver.setActionCommand("VOLVER");
		btnVolver.addActionListener(controlador);

		JLabel lblTitulo = new JLabel("GESTIÓN DE REUNIONES", SwingConstants.CENTER);
		Estilos.labelTitulo(lblTitulo);

		// Panel auxiliar para equilibrar el layout
		JPanel panelVacio = new JPanel();
		panelVacio.setOpaque(false);
		panelVacio.setPreferredSize(new Dimension(150, 10));

		panelTop.add(btnVolver, BorderLayout.WEST);
		panelTop.add(lblTitulo, BorderLayout.CENTER);
		panelTop.add(panelVacio, BorderLayout.EAST);

		contentPane.add(panelTop, BorderLayout.NORTH);

		// --- 2. TABLA CENTRAL ---
		// Columnas basadas en tus atributos
		String[] columnas = { "ID", // idReunion (Oculto o visible, útil para la lógica)
				"Título", // titulo
				"Asunto", // asunto
				"Solicitante", // usersByAlumnoId (Nombre)
				"Profesor", // usersByProfesorId (Nombre)
				"Fecha", // fecha
				"Aula", // aula
				"Estado" // estado
		};

		modeloGestion = new DefaultTableModel(null, columnas) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Hacemos que la tabla no sea editable directamente
			}
		};

		tablaGestion = new JTable(modeloGestion);
		tablaGestion.setRowHeight(30);
		tablaGestion.setFont(new Font("Roboto", Font.PLAIN, 12));
		tablaGestion.setBackground(new Color(50, 65, 90));
		tablaGestion.setForeground(Color.WHITE);
		tablaGestion.setSelectionBackground(new Color(90, 150, 200));
		tablaGestion.setSelectionForeground(Color.WHITE);

		// Estilo del Header de la tabla
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
		Estilos.body(panelBotones); // Fondo del cuerpo

		btnCrear = new JButton("Nueva Reunión (+)");
		Estilos.botonPrimario(btnCrear);
		btnCrear.setBackground(new Color(46, 139, 87)); // Un verde para diferenciar "Crear"
		btnCrear.setActionCommand("NUEVA_REUNION");
		btnCrear.addActionListener(controlador);

		btnAceptar = new JButton("Aceptar Solicitud");
		Estilos.botonPrimario(btnAceptar);
		btnAceptar.setActionCommand("ACEPTAR_REUNION");
		btnAceptar.addActionListener(controlador);

		btnDenegar = new JButton("Denegar Solicitud");
		Estilos.botonPrimario(btnDenegar);
		btnDenegar.setBackground(new Color(205, 92, 92)); // Un rojo suave para "Denegar"
		btnDenegar.setActionCommand("DENEGAR_REUNION");
		btnDenegar.addActionListener(controlador);

		panelBotones.add(btnCrear);
		panelBotones.add(Box.createHorizontalStrut(30)); // Espacio separador
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

	// Método útil para obtener el ID de la reunión seleccionada
	public Integer getIdReunionSeleccionada() {
		int fila = tablaGestion.getSelectedRow();
		if (fila == -1)
			return null;
		// Asumiendo que el ID está en la columna 0
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
