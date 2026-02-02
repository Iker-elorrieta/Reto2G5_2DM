package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import Modelo.EnviarDatos;
import Modelo.SocketCliente;
import Modelo.Users;
import Vista.GestionarReuniones;

public class ControladorGestionarReuniones implements ActionListener {

	private Users user;
	private GestionarReuniones ventana;
	private EnviarDatos enviarDatos = null;
	private SocketCliente socketCliente;

	public ControladorGestionarReuniones(Users user) {
		this.user = user;

		// 1. Inicializamos conexión
		try {
			socketCliente = new SocketCliente();
			enviarDatos = new EnviarDatos(socketCliente.getOut(), socketCliente.getIn());
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con el servidor.");
		}

		// 2. Creamos la ventana UNA sola vez
		ventana = new GestionarReuniones(this);
	}

	public void iniciarGestionarReuniones() {
		// 3. Cargamos los datos antes de mostrar
		mostrarReuniones();
		ventana.setVisible(true);
		ventana.setLocationRelativeTo(null); // Centrar en pantalla
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();

		switch (comando) {
		case "VOLVER":
			ventana.dispose();
			ControladorReuniones controladorReuniones = new ControladorReuniones(user);
			controladorReuniones.iniciarReuniones();
			break;

		case "ACEPTAR_REUNION":
			aceptarReunion();
			break;

		case "DENEGAR_REUNION":
			denegarReunion();
			break;
		}
	}

	public void mostrarReuniones() {
		// 1. Obtener datos del servidor
		ArrayList<Modelo.Reuniones> listaReuniones = enviarDatos.datosReuniones(user);

		// 2. Obtener modelo de la tabla
		DefaultTableModel modelo = ventana.getModeloGestion();

		// 3. Limpiar tabla (por si recargamos)
		modelo.setRowCount(0);

		// Formato para la fecha
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		// 4. Rellenar tabla
		if (listaReuniones != null) {
			for (Modelo.Reuniones r : listaReuniones) {

				// Evitar NullPointerExceptions si faltan datos
				String fechaStr = (r.getFecha() != null) ? sdf.format(r.getFecha()) : "Sin fecha";

				// Obtener nombres seguros
				String nombreSolicitante = "Desconocido";
				if (r.getUsersByAlumnoId() != null) {
					nombreSolicitante = r.getUsersByAlumnoId().getNombre() + " "
							+ r.getUsersByAlumnoId().getApellidos();
				}

				String nombreProfesor = (r.getUsersByProfesorId() != null)
						? r.getUsersByProfesorId().getNombre() + " " + r.getUsersByProfesorId().getApellidos()
						: "Sin asignar";

				Object[] fila = { 
					r.getIdReunion(), // ID (Columna 0)
					r.getTitulo(), // Título
					r.getAsunto(), // Asunto
					nombreSolicitante, // Solicitante (Alumno)
					nombreProfesor, // Profesor
					fechaStr, // Fecha formateada
					r.getAula(), // Aula
					r.getEstado() // Estado
				};

				modelo.addRow(fila);
			}
		}
	}

	private void aceptarReunion() {
		Integer idSeleccionado = ventana.getIdReunionSeleccionada();
		if (idSeleccionado == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una reunión para aceptar.");
			return;
		}

		// Obtener el estado actual de la reunión seleccionada
		String estadoActual = obtenerEstadoReunion(idSeleccionado);
		
		// Validar que el estado sea PENDIENTE
		if (estadoActual == null || !estadoActual.equalsIgnoreCase("PENDIENTE")) {
			JOptionPane.showMessageDialog(ventana, 
				"Solo se pueden aceptar reuniones en estado PENDIENTE.\nEstado actual: " + estadoActual,
				"Acción no permitida",
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		System.out.println("Aceptando reunión ID: " + idSeleccionado);
		boolean actualizarReunion = enviarDatos.actualizarEstadoReunion(idSeleccionado, "ACEPTADA");
		
		if (actualizarReunion) {
			JOptionPane.showMessageDialog(ventana, "Reunión aceptada correctamente.");
			System.out.println("Reunión aceptada: " + actualizarReunion);
			mostrarReuniones(); // Refrescar tabla
		} else {
			JOptionPane.showMessageDialog(ventana, "Error al aceptar la reunión.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void denegarReunion() {
		Integer idSeleccionado = ventana.getIdReunionSeleccionada();
		if (idSeleccionado == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una reunión para denegar.");
			return;
		}

		// Obtener el estado actual de la reunión seleccionada
		String estadoActual = obtenerEstadoReunion(idSeleccionado);
		
		// Validar que el estado sea PENDIENTE
		if (estadoActual == null || !estadoActual.equalsIgnoreCase("PENDIENTE")) {
			JOptionPane.showMessageDialog(ventana, 
				"Solo se pueden denegar reuniones en estado PENDIENTE.\nEstado actual: " + estadoActual,
				"Acción no permitida",
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		System.out.println("Denegando reunión ID: " + idSeleccionado);
		boolean actualizarReunion = enviarDatos.actualizarEstadoReunion(idSeleccionado, "DENEGADA");
		
		if (actualizarReunion) {
			JOptionPane.showMessageDialog(ventana, "Reunión denegada correctamente.");
			System.out.println("Reunión denegada: " + actualizarReunion);
			mostrarReuniones(); // Refrescar tabla
		} else {
			JOptionPane.showMessageDialog(ventana, "Error al denegar la reunión.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Obtiene el estado actual de una reunión desde la tabla
	 */
	private String obtenerEstadoReunion(Integer idReunion) {
		DefaultTableModel modelo = ventana.getModeloGestion();
		
		for (int i = 0; i < modelo.getRowCount(); i++) {
			Object id = modelo.getValueAt(i, 0); // Columna 0 = ID
			
			if (id != null && id.toString().equals(idReunion.toString())) {
				Object estado = modelo.getValueAt(i, 7); // Columna 7 = Estado
				return (estado != null) ? estado.toString() : null;
			}
		}
		
		return null;
	}
}