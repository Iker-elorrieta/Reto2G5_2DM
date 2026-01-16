package Vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Estilos {

	private Estilos() {} 

	// ===== BOTONES =====
	public static void botonMenu(JButton b) {
	    b.setFont(new Font("Roboto", Font.PLAIN, 16));
	    b.setPreferredSize(new Dimension(260, 45));
	    b.setBackground(new Color(70, 130, 180));
	    b.setForeground(Color.WHITE);
	    b.setFocusPainted(false);
	    b.setBorder(new EmptyBorder(10, 20, 10, 20));
	    b.setAlignmentX(JButton.CENTER_ALIGNMENT);
	}

	public static void botonPrimario(JButton b) {
	    b.setFont(new Font("Roboto", Font.PLAIN, 14));
	    b.setBackground(new Color(70, 130, 180));
	    b.setForeground(Color.WHITE);
	    b.setFocusPainted(false);
	    b.setBorder(new EmptyBorder(8, 15, 8, 15));
	}

	public static void botonHeader(JButton b) {
	    b.setFont(new Font("Roboto", Font.PLAIN, 14));
	    b.setBackground(new Color(70, 130, 180));
	    b.setForeground(Color.WHITE);
	    b.setFocusPainted(false);
	    b.setBorder(new EmptyBorder(5, 15, 5, 15));
	}

	// ===== PANEL =====
	public static void panelFondo(JPanel p) {
	    p.setBackground(new Color(30, 45, 70)); // azul oscuro de fondo
	}

	public static void header(JPanel p) {
	    p.setBackground(new Color(20, 35, 70));
	    p.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE)); // borde inferior blanco
	}

	public static void body(JPanel p) {
	    p.setBackground(new Color(25, 45, 80));
	}

	// ===== LABELS =====
	public static void labelTitulo(JLabel l) {
	    l.setFont(new Font("Roboto", Font.BOLD, 20));
	    l.setForeground(Color.WHITE);
	}

	public static void labelNormal(JLabel l) {
	    l.setFont(new Font("Roboto", Font.PLAIN, 14));
	    l.setForeground(Color.WHITE);
	}

	public static void labelHeader(JLabel l) {
	    l.setFont(new Font("Roboto", Font.BOLD, 20));
	    l.setForeground(Color.WHITE);
	}

	public static void labelError(JLabel l) {
	    l.setFont(new Font("Roboto", Font.PLAIN, 12));
	    l.setForeground(new Color(255, 100, 100));
	}

	// ===== INPUTS =====
	public static void input(JTextField t) {
	    t.setFont(new Font("Roboto", Font.PLAIN, 14));
	    t.setBackground(new Color(50, 65, 90));
	    t.setForeground(Color.WHITE);
	    t.setBorder(new EmptyBorder(5, 10, 5, 10));
	}

	public static void input(JPasswordField t) {
	    t.setFont(new Font("Roboto", Font.PLAIN, 14));
	    t.setBackground(new Color(50, 65, 90));
	    t.setForeground(Color.WHITE);
	    t.setBorder(new EmptyBorder(5, 10, 5, 10));
	}
}
