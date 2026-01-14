package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private Controlador controlador = new Controlador();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 551, 391);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(191, 103, 143, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsername.setBounds(191, 78, 143, 14);
        contentPane.add(lblUsername);

        JLabel lblContraseña = new JLabel("Contraseña");
        lblContraseña.setHorizontalAlignment(SwingConstants.CENTER);
        lblContraseña.setBounds(191, 166, 143, 14);
        contentPane.add(lblContraseña);

        passwordField = new JPasswordField();
        passwordField.setBounds(191, 191, 143, 20);
        contentPane.add(passwordField);

        JButton btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setActionCommand("LOGIN");
        btnLogin.addActionListener(controlador);
        btnLogin.setBounds(208, 254, 113, 23);
        contentPane.add(btnLogin);

        controlador.setFields(textField, passwordField);
    }
}
