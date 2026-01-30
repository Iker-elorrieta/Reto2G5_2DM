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

		case "ACEPTAR_REUNION": // Coincide con btnAceptar en Vista
			aceptarReunion();
			break;

		case "DENEGAR_REUNION": // Coincide con btnDenegar en Vista
			denegarReunion();
			break;

		case "NUEVA_REUNION": // Coincide con btnCrear en Vista
			// Aquí iría la lógica para abrir ventana de crear
			JOptionPane.showMessageDialog(ventana, "Funcionalidad de Crear Reunión pendiente.");
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

				// Obtener nombres seguros (asumiendo que Users tiene getNombre y getApellidos)
				String nombreSolicitante = "Desconocido";
				if (r.getUsersByAlumnoId() != null) {
					nombreSolicitante = r.getUsersByAlumnoId().getNombre() + " "
							+ r.getUsersByAlumnoId().getApellidos();
				} else if (r.getUsersByProfesorId() != null && user.getId() != r.getUsersByProfesorId().getId()) {
					// Si yo soy el alumno, el solicitante podría ser el profesor en algunos casos,
					// depende de tu lógica
				}

				String nombreProfesor = (r.getUsersByProfesorId() != null)
						? r.getUsersByProfesorId().getNombre() + " " + r.getUsersByProfesorId().getApellidos()
						: "Sin asignar";

				Object[] fila = { r.getIdReunion(), // ID (Columna 0)
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

	// Métodos placeholder para las acciones
	private void aceptarReunion() {
		Integer idSeleccionado = ventana.getIdReunionSeleccionada();
		if (idSeleccionado == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una reunión para aceptar.");
			return;
		}
		System.out.println("Aceptando reunión ID: " + idSeleccionado);
		boolean actualizarReunion = enviarDatos.actualizarEstadoReunion(idSeleccionado, "ACEPTADA");
		System.out.println("Reunión aceptada: " + actualizarReunion);
		mostrarReuniones(); // Refrescar tabla
	}

	private void denegarReunion() {
		Integer idSeleccionado = ventana.getIdReunionSeleccionada();
		if (idSeleccionado == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una reunión para denegar.");
			return;
		}
		System.out.println("Denegando reunión ID: " + idSeleccionado);
		boolean actualizarReunion = enviarDatos.actualizarEstadoReunion(idSeleccionado, "DENEGADA");
		System.out.println("Reunión denegada: " + actualizarReunion);
		mostrarReuniones();
	}
}