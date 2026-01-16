package view;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

public class Perfil extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textNombre;
	private JTextField textApellidos;
	private JTextField textEmail;
	private JTextField textTelefono;
	private JTextField textDireccion;
	private JTextField textCargo;
	private JTextField textDepartamento;
	private JLabel lblProfileImage;
	private String currentUser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Perfil frame = new Perfil("Juan Pérez");
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
	public Perfil(String user) {
		this.currentUser = user;
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Perfil.class.getResource("/img/elorrieta.png")));
		setTitle("Perfil de Usuario - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 650);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Header with title
		JLabel lblTitle = new JLabel("Perfil de Usuario");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblTitle.setForeground(new Color(51, 102, 153));
		lblTitle.setBounds(50, 20, 300, 40);
		contentPane.add(lblTitle);
		
		// Profile image section
		JPanel profileImagePanel = new JPanel();
		profileImagePanel.setBackground(Color.WHITE);
		profileImagePanel.setBorder(new LineBorder(new Color(200, 200, 200), 2));
		profileImagePanel.setBounds(50, 80, 180, 200);
		contentPane.add(profileImagePanel);
		profileImagePanel.setLayout(null);
		
		lblProfileImage = new JLabel("");
		lblProfileImage.setBounds(10, 10, 160, 160);
		profileImagePanel.add(lblProfileImage);
		
		// Load default profile image
		ImageIcon profileIcon = new ImageIcon(Perfil.class.getResource("/img/ic_profile.png"));
		Image profileImage = profileIcon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
		ImageIcon scaledProfileIcon = new ImageIcon(profileImage);
		lblProfileImage.setIcon(scaledProfileIcon);
		
		JButton btnChangePhoto = new JButton("Cambiar Foto");
		btnChangePhoto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnChangePhoto.setBackground(new Color(70, 130, 180));
		btnChangePhoto.setForeground(Color.WHITE);
		btnChangePhoto.setBounds(10, 175, 160, 20);
		profileImagePanel.add(btnChangePhoto);
		
		// Personal Information Panel
		JPanel personalInfoPanel = new JPanel();
		personalInfoPanel.setBackground(Color.WHITE);
		personalInfoPanel.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153), 2), 
			"Información Personal", TitledBorder.LEADING, TitledBorder.TOP, 
			new Font("Tahoma", Font.BOLD, 14), new Color(51, 102, 153)));
		personalInfoPanel.setBounds(270, 80, 520, 200);
		contentPane.add(personalInfoPanel);
		personalInfoPanel.setLayout(null);
		
		// Name field
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNombre.setBounds(20, 30, 80, 25);
		personalInfoPanel.add(lblNombre);
		
		textNombre = new JTextField();
		textNombre.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textNombre.setBounds(110, 30, 180, 25);
		personalInfoPanel.add(textNombre);
		textNombre.setColumns(10);
		
		// Surname field
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblApellidos.setBounds(310, 30, 80, 25);
		personalInfoPanel.add(lblApellidos);
		
		textApellidos = new JTextField();
		textApellidos.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textApellidos.setBounds(390, 30, 110, 25);
		personalInfoPanel.add(textApellidos);
		textApellidos.setColumns(10);
		
		// Email field
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setBounds(20, 70, 80, 25);
		personalInfoPanel.add(lblEmail);
		
		textEmail = new JTextField();
		textEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textEmail.setBounds(110, 70, 270, 25);
		personalInfoPanel.add(textEmail);
		textEmail.setColumns(10);
		
		// Phone field
		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTelefono.setBounds(390, 70, 80, 25);
		personalInfoPanel.add(lblTelefono);
		
		textTelefono = new JTextField();
		textTelefono.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textTelefono.setBounds(390, 95, 110, 25);
		personalInfoPanel.add(textTelefono);
		textTelefono.setColumns(10);
		
		// Address field
		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDireccion.setBounds(20, 110, 80, 25);
		personalInfoPanel.add(lblDireccion);
		
		textDireccion = new JTextField();
		textDireccion.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textDireccion.setBounds(110, 110, 270, 25);
		personalInfoPanel.add(textDireccion);
		textDireccion.setColumns(10);
		
		// Professional Information Panel
		JPanel professionalInfoPanel = new JPanel();
		professionalInfoPanel.setBackground(Color.WHITE);
		professionalInfoPanel.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153), 2), 
			"Información Profesional", TitledBorder.LEADING, TitledBorder.TOP, 
			new Font("Tahoma", Font.BOLD, 14), new Color(51, 102, 153)));
		professionalInfoPanel.setBounds(50, 300, 740, 120);
		contentPane.add(professionalInfoPanel);
		professionalInfoPanel.setLayout(null);
		
		// Position field
		JLabel lblCargo = new JLabel("Cargo:");
		lblCargo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCargo.setBounds(20, 30, 80, 25);
		professionalInfoPanel.add(lblCargo);
		
		textCargo = new JTextField();
		textCargo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textCargo.setBounds(110, 30, 200, 25);
		professionalInfoPanel.add(textCargo);
		textCargo.setColumns(10);
		
		// Department field
		JLabel lblDepartamento = new JLabel("Departamento:");
		lblDepartamento.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDepartamento.setBounds(350, 30, 100, 25);
		professionalInfoPanel.add(lblDepartamento);
		
		textDepartamento = new JTextField();
		textDepartamento.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textDepartamento.setBounds(460, 30, 200, 25);
		professionalInfoPanel.add(textDepartamento);
		textDepartamento.setColumns(10);
		
		// User ID and creation date (read-only info)
		JLabel lblUsuarioId = new JLabel("ID Usuario: USR001");
		lblUsuarioId.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblUsuarioId.setForeground(Color.GRAY);
		lblUsuarioId.setBounds(20, 70, 150, 20);
		professionalInfoPanel.add(lblUsuarioId);
		
		JLabel lblFechaCreacion = new JLabel("Fecha de registro: 15/01/2024");
		lblFechaCreacion.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblFechaCreacion.setForeground(Color.GRAY);
		lblFechaCreacion.setBounds(200, 70, 200, 20);
		professionalInfoPanel.add(lblFechaCreacion);
		
		JLabel lblUltimoAcceso = new JLabel("Último acceso: 16/01/2026 09:30");
		lblUltimoAcceso.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblUltimoAcceso.setForeground(Color.GRAY);
		lblUltimoAcceso.setBounds(420, 70, 200, 20);
		professionalInfoPanel.add(lblUltimoAcceso);
		
		// Action buttons
		JButton btnGuardar = new JButton("Guardar Cambios");
		btnGuardar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGuardar.setBackground(new Color(34, 139, 34));
		btnGuardar.setForeground(Color.WHITE);
		btnGuardar.setBounds(200, 450, 150, 40);
		contentPane.add(btnGuardar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancelar.setBackground(new Color(108, 117, 125));
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setBounds(370, 450, 120, 40);
		contentPane.add(btnCancelar);
		
		JButton btnCambiarPassword = new JButton("Cambiar Contraseña");
		btnCambiarPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCambiarPassword.setBackground(new Color(255, 193, 7));
		btnCambiarPassword.setForeground(Color.BLACK);
		btnCambiarPassword.setBounds(510, 450, 180, 40);
		contentPane.add(btnCambiarPassword);
		
		// Back button
		JButton btnVolver = new JButton("← Volver al Menú");
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnVolver.setBackground(new Color(70, 130, 180));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setBounds(50, 520, 140, 35);
		contentPane.add(btnVolver);
		
		// Status label
		JLabel lblStatus = new JLabel("Estado: Activo");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStatus.setForeground(new Color(34, 139, 34));
		lblStatus.setBounds(650, 525, 100, 25);
		contentPane.add(lblStatus);
		
		// Initialize with sample data
		loadUserData();
		
		// Button Listeners
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Menu menu = new Menu(currentUser);
				menu.setVisible(true);
			}
		});
		
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Implement save functionality
				javax.swing.JOptionPane.showMessageDialog(null, 
					"Cambios guardados correctamente", 
					"Éxito", 
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Reload original data
				loadUserData();
				javax.swing.JOptionPane.showMessageDialog(null, 
					"Cambios cancelados", 
					"Información", 
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		btnCambiarPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Open password change dialog
				javax.swing.JOptionPane.showMessageDialog(null, 
					"Funcionalidad de cambio de contraseña próximamente", 
					"Información", 
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		btnChangePhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Implement photo change functionality
				javax.swing.JOptionPane.showMessageDialog(null, 
					"Funcionalidad de cambio de foto próximamente", 
					"Información", 
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	/**
	 * Load user data into the form fields
	 */
	private void loadUserData() {
		// Sample data - in real application this would come from database
		textNombre.setText("Juan");
		textApellidos.setText("Pérez García");
		textEmail.setText("juan.perez@elorrieta.com");
		textTelefono.setText("+34 123 456 789");
		textDireccion.setText("Calle Mayor, 123, 48001 Bilbao");
		textCargo.setText("Profesor de Informática");
		textDepartamento.setText("Departamento de Tecnología");
	}

}