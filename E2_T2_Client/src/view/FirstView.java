package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.CheckLogin;
import utils.SessionManager;

public class FirstView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userField;
	private JPasswordField passwordField;
	private String user;
	private String password;

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

	public FirstView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FirstView.class.getResource("/img/elorrieta.png")));
		setTitle("Login - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Title label
		JLabel lblTitle = new JLabel("Sistema de Gestión Escolar");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblTitle.setForeground(new Color(51, 102, 153));
		lblTitle.setBounds(200, 30, 400, 50);
		contentPane.add(lblTitle);

		// Logo section
		JLabel lblLogo = new JLabel();
		lblLogo.setOpaque(true);
		lblLogo.setBackground(Color.WHITE);
		lblLogo.setBounds(300, 100, 200, 120);
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/elorrieta.png"));
		Image logoImage = logoIcon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(logoImage));
		contentPane.add(lblLogo);

		// Login form section
		JLabel lblNewLabel = new JLabel("Erabiltzailea:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setForeground(new Color(51, 102, 153));
		lblNewLabel.setBounds(250, 260, 120, 25);
		contentPane.add(lblNewLabel);

		userField = new JTextField();
		userField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		userField.setBounds(250, 285, 300, 35);
		userField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
				javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		contentPane.add(userField);
		userField.setColumns(10);

		JLabel lblPasahitza = new JLabel("Pasahitza:");
		lblPasahitza.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPasahitza.setForeground(new Color(51, 102, 153));
		lblPasahitza.setBounds(250, 340, 120, 25);
		contentPane.add(lblPasahitza);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField.setBounds(250, 365, 300, 35);
		passwordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
				javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		contentPane.add(passwordField);

		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnLogin.setBackground(new Color(70, 130, 180));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBounds(350, 430, 100, 45);
		btnLogin.setFocusPainted(false);
		contentPane.add(btnLogin);

		// Footer info
		JLabel lblFooter = new JLabel("Elorrieta Erreka Mari BHI");
		lblFooter.setFont(new Font("Tahoma", Font.ITALIC, 12));
		lblFooter.setForeground(new Color(102, 102, 102));
		lblFooter.setBounds(320, 520, 200, 20);
		contentPane.add(lblFooter);

		// Button Listener
		// En FirstView.java, actualizar el ActionListener del botón login:

		btnLogin.addActionListener(new ActionListener() {
		    @SuppressWarnings("deprecation")
		    public void actionPerformed(ActionEvent e) {
		        user = userField.getText();
		        password = passwordField.getText();
		        
		        btnLogin.setEnabled(false);
		        btnLogin.setText("Validando...");
		        
		        new Thread(() -> {
		            CheckLogin checkLogin = new CheckLogin();
		            CheckLogin.LoginResponse response = checkLogin.validarLogin(user, password);
		            
		            EventQueue.invokeLater(() -> {
		                if (response.isExitoso()) {
		                    // Guardar sesión
		                    SessionManager.getInstance().iniciarSesion(response.getUsuario());
		                    
		                    System.out.println("Login exitoso: " + response.getUsuario().getNombreCompleto());
		                    dispose();
		                    Menu menu = new Menu(response.getUsuario().getUsername());
		                    menu.setVisible(true);
		                } else {
		                    btnLogin.setEnabled(true);
		                    btnLogin.setText("Login");
		                    
		                    javax.swing.JOptionPane.showMessageDialog(
		                        FirstView.this,
		                        response.getMensaje(),
		                        "Error de Login",
		                        javax.swing.JOptionPane.ERROR_MESSAGE
		                    );
		                }
		            });
		        }).start();
		    }
		});

	}
}