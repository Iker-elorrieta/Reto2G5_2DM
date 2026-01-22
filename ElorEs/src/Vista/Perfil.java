package Vista;

import java.awt.*;
import java.io.InputStream;
import java.net.URI;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Perfil extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Labels
    private JLabel lblFoto;
    private JLabel lblNombre;
    private JLabel lblApellidos;
    private JLabel lblDni;
    private JLabel lblEmail;
    private JLabel lblTelefono1;
    private JLabel lblTelefono2;
    private JLabel lblDireccion;

    // Imagen original
    private Image imgOriginal;

    // Botón Volver
    private JButton btnVolver;


    public Perfil() {
        setTitle("Perfil - ???"  );
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 400);

        // ===== CONTENT PANE =====
        contentPane = new JPanel();
        Estilos.panelFondo(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // ===== HEADER =====
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        Estilos.header(header);
        header.setPreferredSize(new Dimension(0, 100));
        contentPane.add(header, BorderLayout.NORTH);

        // Foto del profesor
        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(80, 80));
        header.add(lblFoto);

        // Nombre completo
        lblNombre = new JLabel();
        Estilos.labelHeader(lblNombre);
        header.add(lblNombre);

        // ===== BODY =====
        JPanel body = new JPanel();
        Estilos.body(body);
        body.setLayout(new GridBagLayout());
        contentPane.add(body, BorderLayout.CENTER);

        int fila = 0;

        // ===== Apellidos =====
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.insets = new Insets(10, 20, 10, 20);
        gbcLabel.anchor = GridBagConstraints.WEST;
        gbcLabel.gridx = 0;
        gbcLabel.gridy = fila;
        JLabel lApellidos = new JLabel("Apellidos: ");
        Estilos.labelNormal(lApellidos);
        body.add(lApellidos, gbcLabel);

        GridBagConstraints gbcValue = (GridBagConstraints) gbcLabel.clone();
        gbcValue.gridx = 1;
        lblApellidos = new JLabel();
        Estilos.labelNormal(lblApellidos);
        body.add(lblApellidos, gbcValue);
        fila++;

        // ===== DNI =====
        gbcLabel = new GridBagConstraints();
        gbcLabel.insets = new Insets(10, 20, 10, 20);
        gbcLabel.anchor = GridBagConstraints.WEST;
        gbcLabel.gridx = 0;
        gbcLabel.gridy = fila;
        JLabel lDni = new JLabel("DNI: ");
        Estilos.labelNormal(lDni);
        body.add(lDni, gbcLabel);

        gbcValue = (GridBagConstraints) gbcLabel.clone();
        gbcValue.gridx = 1;
        lblDni = new JLabel();
        Estilos.labelNormal(lblDni);
        body.add(lblDni, gbcValue);
        fila++;

        // ===== Email =====
        gbcLabel = new GridBagConstraints();
        gbcLabel.insets = new Insets(10, 20, 10, 20);
        gbcLabel.anchor = GridBagConstraints.WEST;
        gbcLabel.gridx = 0;
        gbcLabel.gridy = fila;
        JLabel lEmail = new JLabel("Email: ");
        Estilos.labelNormal(lEmail);
        body.add(lEmail, gbcLabel);

        gbcValue = (GridBagConstraints) gbcLabel.clone();
        gbcValue.gridx = 1;
        lblEmail = new JLabel();
        Estilos.labelNormal(lblEmail);
        body.add(lblEmail, gbcValue);
        fila++;

        // ===== Teléfono 1 =====
        gbcLabel = new GridBagConstraints();
        gbcLabel.insets = new Insets(10, 20, 10, 20);
        gbcLabel.anchor = GridBagConstraints.WEST;
        gbcLabel.gridx = 0;
        gbcLabel.gridy = fila;
        JLabel lTel1 = new JLabel("Teléfono 1: ");
        Estilos.labelNormal(lTel1);
        body.add(lTel1, gbcLabel);

        gbcValue = (GridBagConstraints) gbcLabel.clone();
        gbcValue.gridx = 1;
        lblTelefono1 = new JLabel();
        Estilos.labelNormal(lblTelefono1);
        body.add(lblTelefono1, gbcValue);
        fila++;

        // ===== Teléfono 2 =====
        gbcLabel = new GridBagConstraints();
        gbcLabel.insets = new Insets(10, 20, 10, 20);
        gbcLabel.anchor = GridBagConstraints.WEST;
        gbcLabel.gridx = 0;
        gbcLabel.gridy = fila;
        JLabel lTel2 = new JLabel("Teléfono 2: ");
        Estilos.labelNormal(lTel2);
        body.add(lTel2, gbcLabel);

        gbcValue = (GridBagConstraints) gbcLabel.clone();
        gbcValue.gridx = 1;
        lblTelefono2 = new JLabel();
        Estilos.labelNormal(lblTelefono2);
        body.add(lblTelefono2, gbcValue);
        fila++;

        // ===== Dirección =====
        gbcLabel = new GridBagConstraints();
        gbcLabel.insets = new Insets(10, 20, 10, 20);
        gbcLabel.anchor = GridBagConstraints.WEST;
        gbcLabel.gridx = 0;
        gbcLabel.gridy = fila;
        JLabel lDir = new JLabel("Dirección: ");
        Estilos.labelNormal(lDir);
        body.add(lDir, gbcLabel);

        gbcValue = (GridBagConstraints) gbcLabel.clone();
        gbcValue.gridx = 1;
        lblDireccion = new JLabel();
        Estilos.labelNormal(lblDireccion);
        body.add(lblDireccion, gbcValue);
        fila++;

        // ===== BOTÓN VOLVER =====
        btnVolver = new JButton("Volver");
        Estilos.botonPrimario(btnVolver);
        contentPane.add(btnVolver, BorderLayout.SOUTH);
    }

    // ===== GETTERS =====
    public JPanel getContentPane() {return contentPane;}
    public JLabel getLblNombre() { return lblNombre; }
    public JLabel getLblApellidos() { return lblApellidos; }
    public JLabel getLblDni() { return lblDni; }
    public JLabel getLblEmail() { return lblEmail; }
    public JLabel getLblTelefono1() { return lblTelefono1; }
    public JLabel getLblTelefono2() { return lblTelefono2; }
    public JLabel getLblDireccion() { return lblDireccion; }
    public JLabel getLblFoto() { return lblFoto; }
    public JButton getBtnVolver() { return btnVolver; }

    // ===== MÉTODO PÚBLICO PARA CARGAR IMAGEN =====
    public void cargarImagen(String linkFoto) {
        try {
            Image imgTemp = null;

            if (linkFoto != null && !linkFoto.isEmpty()) {
                try {
                    URI uri = new URI(linkFoto);
                    try (InputStream in = uri.toURL().openStream()) {
                        imgTemp = ImageIO.read(in);
                    }
                } catch (Exception e) {
                    System.out.println("No se pudo cargar la foto desde link: " + e.getMessage());
                }
            }

            if (imgTemp == null) {
                try (InputStream defaultStream = getClass().getResourceAsStream("/unknown.png")) {
                    imgTemp = ImageIO.read(defaultStream);
                }
            }

            imgOriginal = imgTemp;

            if (imgOriginal != null && lblFoto != null) {
                int ancho = lblFoto.getPreferredSize().width;
                int alto = lblFoto.getPreferredSize().height;
                Image imgEscalada = imgOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(imgEscalada));
            }

        } catch (Exception e) {
            System.out.println("Error cargando la imagen: " + e.getMessage());
        }
    }
    
    public void setTitleUsuario(String nombre, String apellidos) {
        setTitle("Perfil - " + nombre + " " + apellidos);
    }
}
