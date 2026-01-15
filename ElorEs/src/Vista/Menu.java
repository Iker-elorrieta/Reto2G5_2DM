package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 758, 557);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// ===== HEADER =====
		JPanel header = new JPanel(new BorderLayout());
		header.setPreferredSize(new Dimension(0, 70));
		header.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		// Título
		JLabel lblTitulo = new JLabel("MENÚ DEL PROFESOR", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
		header.add(lblTitulo, BorderLayout.CENTER);

		// Panel para botones a la derecha
		JPanel panelBotones = new JPanel();
		panelBotones.setOpaque(false); // para que no tape el fondo del header
		panelBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

		JButton btnPerfil = new JButton("Perfil");
		JButton btnLogout = new JButton("Desconectarse");

		panelBotones.add(btnPerfil);
		panelBotones.add(btnLogout);

		// Añadir panel de botones al header
		header.add(panelBotones, BorderLayout.EAST);

		// Añadir header al contentPane
		contentPane.add(header, BorderLayout.NORTH);

	}
}
