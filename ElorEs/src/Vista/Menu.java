package Vista;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Labels que se asignarán desde el controlador
    private JLabel lblFoto;
    private JLabel lblBienvenido;

    // Variable para guardar la imagen original
    private Image imgOriginal;

    public Menu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 555, 445);

        // ===== CONTENT PANE =====
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        Estilos.panelFondo(contentPane);
        setContentPane(contentPane);

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 70));
        Estilos.header(header);

        // ===== IZQUIERDA: FOTO + BIENVENIDO =====
        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelIzq.setOpaque(false);

        // Foto
        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(50, 50));
        cargarImagenDefault(); // Cargar foto default al inicio
        panelIzq.add(lblFoto);

        // Listener para redimensionar la imagen cuando cambie el tamaño del label
        lblFoto.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarFotoRedonda();
            }
        });

        // Texto Bienvenido
        lblBienvenido = new JLabel("Bienvenido - ");
        Estilos.labelHeader(lblBienvenido);
        panelIzq.add(lblBienvenido);

        header.add(panelIzq, BorderLayout.WEST);

        // ===== DERECHA: BOTONES =====
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        JButton btnPerfil = new JButton("Perfil");
        Estilos.botonHeader(btnPerfil);
        JButton btnLogout = new JButton("Desconectarse");
        Estilos.botonHeader(btnLogout);
        panelBotones.add(btnPerfil);
        panelBotones.add(btnLogout);

        // Reemplazo de FocusTraversalOnArray
        panelBotones.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {
           
			private static final long serialVersionUID = 1L;

			@Override
            public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
                if (aComponent == btnPerfil) return btnLogout;
                else return btnPerfil;
            }

            @Override
            public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
                if (aComponent == btnLogout) return btnPerfil;
                else return btnLogout;
            }

            @Override
            public Component getDefaultComponent(Container focusCycleRoot) {
                return btnPerfil;
            }
        });
        panelBotones.setFocusTraversalPolicyProvider(true);

        header.add(panelBotones, BorderLayout.EAST);

        contentPane.add(header, BorderLayout.NORTH);

        // ===== BODY =====
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        Estilos.body(body);
        contentPane.add(body, BorderLayout.CENTER);

        // Botones del body
        JButton btnConsultarAlumnos = new JButton("Consultar Alumnos");
        JButton btnHorarios = new JButton("Consultar Horarios");
        JButton btnOtrosHorarios = new JButton("Consultar Otros Horarios");
        JButton btnReuniones = new JButton("Gestionar Reuniones");

        // Aplicar estilos
        Estilos.botonMenu(btnConsultarAlumnos);
        Estilos.botonMenu(btnHorarios);
        Estilos.botonMenu(btnOtrosHorarios);
        Estilos.botonMenu(btnReuniones);

        // Alineación y separación
        btnConsultarAlumnos.setAlignmentX(JButton.CENTER_ALIGNMENT);
        btnHorarios.setAlignmentX(JButton.CENTER_ALIGNMENT);
        btnOtrosHorarios.setAlignmentX(JButton.CENTER_ALIGNMENT);
        btnReuniones.setAlignmentX(JButton.CENTER_ALIGNMENT);

        body.add(Box.createVerticalGlue());
        body.add(btnConsultarAlumnos);
        body.add(Box.createVerticalStrut(20));
        body.add(btnHorarios);
        body.add(Box.createVerticalStrut(20));
        body.add(btnOtrosHorarios);
        body.add(Box.createVerticalStrut(20));
        body.add(btnReuniones);
        body.add(Box.createVerticalGlue());
    }

    // ===== MÉTODOS PARA ASIGNAR DATOS DESDE EL CONTROLADOR =====
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
                } catch (Exception e) {
                    System.out.println("No se pudo cargar la foto desde link, se usará default: " + e.getMessage());
                }
            }

            if (imgTemp == null) {
                cargarImagenDefault();
                return;
            }

            imgOriginal = imgTemp;
            actualizarFotoRedonda();

        } catch (Exception e) {
            System.out.println("Error al cargar la foto: " + e.getMessage());
            cargarImagenDefault();
        }
    }

    private void cargarImagenDefault() {
        try (InputStream defaultStream = getClass().getResourceAsStream("/unknown.png")) {
            imgOriginal = ImageIO.read(defaultStream);
            actualizarFotoRedonda();
        } catch (Exception e) {
            System.out.println("Error cargando la imagen default: " + e.getMessage());
        }
    }

    private void actualizarFotoRedonda() {
        if (imgOriginal != null) {
            int size = Math.min(lblFoto.getWidth(), lblFoto.getHeight());
            if (size <= 0) return;

            // Escalar manteniendo proporción
            float ratioImg = (float) imgOriginal.getWidth(null) / imgOriginal.getHeight(null);
            int newWidth, newHeight;

            if (ratioImg > 1) { // imagen más ancha
                newWidth = size;
                newHeight = (int) (size / ratioImg);
            } else {
                newHeight = size;
                newWidth = (int) (size * ratioImg);
            }

            Image scaledImg = imgOriginal.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            // Crear imagen redonda
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
}
