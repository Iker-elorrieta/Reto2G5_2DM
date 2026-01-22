// Java
package Vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controlador.ControladorAlumnos;

import java.awt.*;

import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

public class Alumnos extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel lblFoto;
	private JLabel lblBienvenido;
	private Image imgOriginal;

	private JTable table;
	private DefaultTableModel model;

	private JButton btnActualizar;
	private JButton btnVolver;

	private final String[] defaultCols = new String[] { "ID", "Nombre", "Apellido", "Email", "Curso" };

	// Java
	public Alumnos(ControladorAlumnos controlador) {
		setTitle("ElorEs - Alumnos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);

		contentPane = new JPanel(new BorderLayout());
		Estilos.panelFondo(contentPane);
		setContentPane(contentPane);

		// Table setup
		model = new DefaultTableModel(new Object[0][0], defaultCols) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scroll = new JScrollPane(table);
		contentPane.add(scroll, BorderLayout.CENTER);

		// Footer with action buttons
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		footer.setOpaque(false);

		btnActualizar = new JButton("Actualizar");
		btnVolver = new JButton("Volver");

		Estilos.botonMenu(btnActualizar);
		Estilos.botonMenu(btnVolver);

		footer.add(btnActualizar);
		footer.add(btnVolver);

		contentPane.add(footer, BorderLayout.SOUTH);
		
		btnVolver.setActionCommand("VOLVER");
		btnVolver.addActionListener(controlador);

		btnActualizar.setActionCommand("ACTUALIZAR");
		btnActualizar.addActionListener(controlador);

	}

	// ====== Methods for controller to set data / listeners ======

	public void setNombre(String nombreUsuario) {
		lblBienvenido.setText("Bienvenido - " + nombreUsuario);
	}

	public void setFoto(String linkFoto) {
		try {
			Image imgTemp = null;
			if (linkFoto != null && !linkFoto.isEmpty()) {
				try {
					URI uri = new URI(linkFoto);
					try (InputStream in = uri.toURL().openStream()) {
						imgTemp = ImageIO.read(in);
					}
				} catch (Exception ignored) {
					// Fall through to default
				}
			}
			if (imgTemp == null) {
				cargarImagenDefault();
				return;
			}
			imgOriginal = imgTemp;
			actualizarFotoRedonda();
		} catch (Exception e) {
			cargarImagenDefault();
		}
	}

	private void cargarImagenDefault() {
		try (InputStream defaultStream = getClass().getResourceAsStream("/unknown.png")) {
			if (defaultStream != null) {
				imgOriginal = ImageIO.read(defaultStream);
				actualizarFotoRedonda();
			}
		} catch (Exception e) {
			System.out.println("Error cargando la imagen default: " + e.getMessage());
		}
	}

	private void actualizarFotoRedonda() {
		if (imgOriginal != null) {
			int size = Math.min(lblFoto.getWidth(), lblFoto.getHeight());
			if (size <= 0)
				return;

			float ratioImg = (float) imgOriginal.getWidth(null) / imgOriginal.getHeight(null);
			int newWidth, newHeight;
			if (ratioImg > 1) {
				newWidth = size;
				newHeight = (int) (size / ratioImg);
			} else {
				newHeight = size;
				newWidth = (int) (size * ratioImg);
			}

			Image scaledImg = imgOriginal.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = circleBuffer.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setClip(new Ellipse2D.Float(0, 0, size, size));
			int x = (size - newWidth) / 2;
			int y = (size - newHeight) / 2;
			g2.drawImage(scaledImg, x, y, null);
			g2.dispose();
			lblFoto.setIcon(new ImageIcon(circleBuffer));
		}
	}

	// Replace whole table data with given rows. Each row should match defaultCols
	// order.
	public void setAlumnos(Object[][] rows) {
		Objects.requireNonNull(rows);
		model.setDataVector(rows, defaultCols);
	}

	// Convenience: set columns if needed (keeps same model instance)
	public void setColumnNames(String[] cols) {
		model.setColumnIdentifiers(cols);
	}

	public JTable getTablaAlumnos() {
		return table;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getBtnActualizar() {
		return btnActualizar;
	}

	public JButton getBtnVolver() {
		return btnVolver;
	}
}
