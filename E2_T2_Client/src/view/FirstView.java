package view;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;

public class FirstView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FirstView frame = new FirstView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FirstView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FirstView.class.getResource("/img/elorrieta.png")));
		setTitle("Login - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 763, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		userField = new JTextField();
		userField.setBounds(265, 222, 206, 20);
		contentPane.add(userField);
		userField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(265, 283, 206, 20);
		contentPane.add(passwordField);
		
		JLabel lblLogo = new JLabel();
		lblLogo.setOpaque(true);
		lblLogo.setBackground(Color.WHITE);
		lblLogo.setBounds(158, 63, 392, 109);
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/elorrieta.png"));
		Image logoImage = logoIcon.getImage().getScaledInstance(lblLogo.getWidth(), lblLogo.getHeight(), Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(logoImage));
		contentPane.add(lblLogo);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(319, 359, 89, 23);
		contentPane.add(btnLogin);
		
		JLabel lblNewLabel = new JLabel("Erabiltzailea:");
		lblNewLabel.setBounds(265, 197, 108, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblPasahitza = new JLabel("Pasahitza:");
		lblPasahitza.setBounds(265, 258, 108, 14);
		contentPane.add(lblPasahitza);

	}
}
