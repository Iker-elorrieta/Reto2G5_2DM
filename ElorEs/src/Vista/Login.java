package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.ControladorLogin;

import java.awt.*;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textUsername;
	private JPasswordField password;
	private ControladorLogin controlador = new ControladorLogin(this);

	public Login() {
		setTitle("ElorEs - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 500);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(30, 50, 30, 50));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		Estilos.panelFondo(contentPane);
		setContentPane(contentPane);

		// ===== LOGO =====
		

		JLabel lblLogo = new JLabel();
		lblLogo.setIcon(new ImageIcon(getClass().getResource("/logo.png")));
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLogo.setPreferredSize(new Dimension(100, 100));
		
		contentPane.add(lblLogo);
		contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

		// ===== TÍTULO =====
		JLabel lblTitulo = new JLabel("INICIO DE SESIÓN");
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		Estilos.labelTitulo(lblTitulo);
		contentPane.add(lblTitulo);
		contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

		// ===== USUARIO =====
		JLabel lblUsername = new JLabel("Usuario");
		lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
		Estilos.labelNormal(lblUsername);
		contentPane.add(lblUsername);
		contentPane.add(Box.createRigidArea(new Dimension(0,5)));

		textUsername = new JTextField();
		textUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		Estilos.input(textUsername);
		contentPane.add(textUsername);
		contentPane.add(Box.createRigidArea(new Dimension(0, 15)));

		// ===== CONTRASEÑA =====
		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
		Estilos.labelNormal(lblContraseña);
		contentPane.add(lblContraseña);
		contentPane.add(Box.createRigidArea(new Dimension(0,5)));

		password = new JPasswordField();
		password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		Estilos.input(password);
		contentPane.add(password);
		contentPane.add(Box.createRigidArea(new Dimension(0, 15)));

		// ===== MENSAJE ERROR =====
		JLabel lblPassIncorrecta = new JLabel("");
		lblPassIncorrecta.setAlignmentX(Component.CENTER_ALIGNMENT);
		Estilos.labelError(lblPassIncorrecta);
		contentPane.add(lblPassIncorrecta);
		contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

		// ===== BOTÓN LOGIN =====
		JButton btnLogin = new JButton("Iniciar Sesión");
		btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		Estilos.botonMenu(btnLogin);
		btnLogin.setActionCommand("LOGIN");
		btnLogin.addActionListener(controlador);
		contentPane.add(btnLogin);

		// ===== CONTROLADOR =====
		controlador.camposLogin(textUsername, password);
		controlador.setLblMensaje(lblPassIncorrecta);
	}
}
