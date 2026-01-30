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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import utils.Profile;
import utils.SessionManager;

public class Perfil extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textNombre;
	private JTextField textApellidos;
	private JTextField textEmail;
	private JTextField textUsername;
	private JTextField textDNI;
	private JTextField textTelefono1;
	private JTextField textTelefono2;
	private JTextField textDireccion;
	private JTextField textTipoUsuario;
	private JLabel lblProfileImage;
	private JLabel lblUsuarioId;
	private JLabel lblStatus;
	private String currentUser;
	
	private Profile profileClient;
	private Profile.UserProfileData perfilActual;

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
		this.profileClient = new Profile();

		setIconImage(Toolkit.getDefaultToolkit().getImage(Perfil.class.getResource("/img/elorrieta.png")));
		setTitle("Perfil de Usuario - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
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
		try {
			ImageIcon profileIcon = new ImageIcon(Perfil.class.getResource("/img/ic_profile.png"));
			Image profileImage = profileIcon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
			ImageIcon scaledProfileIcon = new ImageIcon(profileImage);
			lblProfileImage.setIcon(scaledProfileIcon);
		} catch (Exception e) {
			System.err.println("No se pudo cargar la imagen de perfil");
		}

		JButton btnChangePhoto = new JButton("Cambiar Foto");
		btnChangePhoto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnChangePhoto.setBackground(new Color(70, 130, 180));
		btnChangePhoto.setForeground(Color.WHITE);
		btnChangePhoto.setBounds(10, 175, 160, 20);
		profileImagePanel.add(btnChangePhoto);

		// Personal Information Panel
		JPanel personalInfoPanel = new JPanel();
		personalInfoPanel.setBackground(Color.WHITE);
		personalInfoPanel.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153), 2), "Información Personal",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 14), new Color(51, 102, 153)));
		personalInfoPanel.setBounds(270, 80, 580, 240);
		contentPane.add(personalInfoPanel);
		personalInfoPanel.setLayout(null);

		// Username field
		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUsername.setBounds(20, 30, 100, 25);
		personalInfoPanel.add(lblUsername);

		textUsername = new JTextField();
		textUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textUsername.setEditable(false);
		textUsername.setBackground(new Color(245, 245, 245));
		textUsername.setBounds(130, 30, 180, 25);
		personalInfoPanel.add(textUsername);

		// DNI field
		JLabel lblDNI = new JLabel("DNI:");
		lblDNI.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDNI.setBounds(330, 30, 60, 25);
		personalInfoPanel.add(lblDNI);

		textDNI = new JTextField();
		textDNI.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textDNI.setBounds(400, 30, 150, 25);
		personalInfoPanel.add(textDNI);

		// Name field
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNombre.setBounds(20, 70, 100, 25);
		personalInfoPanel.add(lblNombre);

		textNombre = new JTextField();
		textNombre.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textNombre.setBounds(130, 70, 180, 25);
		personalInfoPanel.add(textNombre);

		// Surname field
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblApellidos.setBounds(330, 70, 80, 25);
		personalInfoPanel.add(lblApellidos);

		textApellidos = new JTextField();
		textApellidos.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textApellidos.setBounds(400, 70, 150, 25);
		personalInfoPanel.add(textApellidos);

		// Email field
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setBounds(20, 110, 100, 25);
		personalInfoPanel.add(lblEmail);

		textEmail = new JTextField();
		textEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textEmail.setEditable(false);
		textEmail.setBackground(new Color(245, 245, 245));
		textEmail.setBounds(130, 110, 420, 25);
		personalInfoPanel.add(textEmail);

		// Phone 1 field
		JLabel lblTelefono1 = new JLabel("Teléfono 1:");
		lblTelefono1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTelefono1.setBounds(20, 150, 100, 25);
		personalInfoPanel.add(lblTelefono1);

		textTelefono1 = new JTextField();
		textTelefono1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textTelefono1.setBounds(130, 150, 180, 25);
		personalInfoPanel.add(textTelefono1);

		// Phone 2 field
		JLabel lblTelefono2 = new JLabel("Teléfono 2:");
		lblTelefono2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTelefono2.setBounds(330, 150, 80, 25);
		personalInfoPanel.add(lblTelefono2);

		textTelefono2 = new JTextField();
		textTelefono2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textTelefono2.setBounds(400, 150, 150, 25);
		personalInfoPanel.add(textTelefono2);

		// Address field
		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDireccion.setBounds(20, 190, 100, 25);
		personalInfoPanel.add(lblDireccion);

		textDireccion = new JTextField();
		textDireccion.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textDireccion.setBounds(130, 190, 420, 25);
		personalInfoPanel.add(textDireccion);

		// Professional Information Panel
		JPanel professionalInfoPanel = new JPanel();
		professionalInfoPanel.setBackground(Color.WHITE);
		professionalInfoPanel.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153), 2),
				"Información del Sistema", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 14),
				new Color(51, 102, 153)));
		professionalInfoPanel.setBounds(50, 340, 800, 100);
		contentPane.add(professionalInfoPanel);
		professionalInfoPanel.setLayout(null);

		// User Type field
		JLabel lblTipo = new JLabel("Tipo de Usuario:");
		lblTipo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTipo.setBounds(20, 30, 120, 25);
		professionalInfoPanel.add(lblTipo);

		textTipoUsuario = new JTextField();
		textTipoUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textTipoUsuario.setEditable(false);
		textTipoUsuario.setBackground(new Color(245, 245, 245));
		textTipoUsuario.setBounds(150, 30, 200, 25);
		professionalInfoPanel.add(textTipoUsuario);

		// User ID (read-only info)
		lblUsuarioId = new JLabel("ID Usuario: ---");
		lblUsuarioId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUsuarioId.setForeground(new Color(70, 130, 180));
		lblUsuarioId.setBounds(20, 65, 200, 20);
		professionalInfoPanel.add(lblUsuarioId);

		// Action buttons
		JButton btnRefrescar = new JButton("🔄 Recargar Datos");
		btnRefrescar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRefrescar.setBackground(new Color(70, 130, 180));
		btnRefrescar.setForeground(Color.WHITE);
		btnRefrescar.setBounds(150, 470, 160, 40);
		contentPane.add(btnRefrescar);

		JButton btnGuardar = new JButton("💾 Guardar Cambios");
		btnGuardar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnGuardar.setBackground(new Color(34, 139, 34));
		btnGuardar.setForeground(Color.WHITE);
		btnGuardar.setBounds(330, 470, 180, 40);
		contentPane.add(btnGuardar);

		JButton btnCambiarPassword = new JButton("🔒 Cambiar Contraseña");
		btnCambiarPassword.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCambiarPassword.setBackground(new Color(255, 193, 7));
		btnCambiarPassword.setForeground(Color.BLACK);
		btnCambiarPassword.setBounds(530, 470, 200, 40);
		contentPane.add(btnCambiarPassword);

		// Back button
		JButton btnVolver = new JButton("← Volver al Menú");
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnVolver.setBackground(new Color(70, 130, 180));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setBounds(50, 550, 150, 35);
		contentPane.add(btnVolver);

		// Status label
		lblStatus = new JLabel("Cargando datos...");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStatus.setForeground(Color.BLUE);
		lblStatus.setBounds(250, 555, 400, 25);
		contentPane.add(lblStatus);

		// Button Listeners
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Menu menu = new Menu(currentUser);
				menu.setVisible(true);
			}
		});

		btnRefrescar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarDatosUsuario();
			}
		});

		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: Implementar guardado en servidor
				JOptionPane.showMessageDialog(null, 
					"Funcionalidad de guardado próximamente.\n" +
					"Actualmente el servidor solo permite lectura de datos.",
					"Información",
					JOptionPane.INFORMATION_MESSAGE);
			}
		});

		btnCambiarPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, 
					"Funcionalidad de cambio de contraseña próximamente",
					"Información", 
					JOptionPane.INFORMATION_MESSAGE);
			}
		});

		btnChangePhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, 
					"Funcionalidad de cambio de foto próximamente",
					"Información", 
					JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Cargar datos del usuario al iniciar
		cargarDatosUsuario();
	}

	/**
	 * Carga los datos del usuario desde el servidor
	 */
	private void cargarDatosUsuario() {
		lblStatus.setText("Cargando datos del perfil...");
		lblStatus.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				// Obtener ID del usuario actual desde la sesión
				Long userId = SessionManager.getInstance().getUserId();
				
				if (userId == null) {
					EventQueue.invokeLater(() -> {
						lblStatus.setText("❌ Error: No hay sesión activa");
						lblStatus.setForeground(Color.RED);
						JOptionPane.showMessageDialog(this,
							"No se pudo obtener la información del usuario.\n" +
							"Por favor, inicia sesión nuevamente.",
							"Error de Sesión",
							JOptionPane.ERROR_MESSAGE);
						
						// Cargar datos de ejemplo como fallback
						loadSampleData();
					});
					return;
				}

				// Obtener datos del perfil
				perfilActual = profileClient.obtenerPerfilUsuario(userId);

				if (perfilActual != null) {
					// Obtener nombre del tipo de usuario
					String tipoNombre = profileClient.obtenerNombreTipo(perfilActual.getTipoId());
					
					EventQueue.invokeLater(() -> {
						actualizarCamposPerfil(perfilActual, tipoNombre);
						lblStatus.setText("✓ Datos cargados correctamente");
						lblStatus.setForeground(new Color(0, 128, 0));
					});
				} else {
					EventQueue.invokeLater(() -> {
						lblStatus.setText("❌ No se encontró el usuario");
						lblStatus.setForeground(Color.RED);
						JOptionPane.showMessageDialog(this,
							"No se encontraron datos para el usuario actual.",
							"Usuario No Encontrado",
							JOptionPane.WARNING_MESSAGE);
						
						loadSampleData();
					});
				}

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					lblStatus.setText("❌ Error al cargar datos");
					lblStatus.setForeground(Color.RED);
					
					JOptionPane.showMessageDialog(this,
						"Error al conectar con el servidor:\n" + e.getMessage() +
						"\n\nVerifica que el servidor esté ejecutándose.",
						"Error de Conexión",
						JOptionPane.ERROR_MESSAGE);
					
					// Cargar datos de ejemplo como fallback
					loadSampleData();
				});
			}
		}).start();
	}

	/**
	 * Actualiza los campos del formulario con los datos del perfil
	 */
	private void actualizarCamposPerfil(Profile.UserProfileData perfil, String tipoNombre) {
		textUsername.setText(perfil.getUsername());
		textNombre.setText(perfil.getNombre());
		textApellidos.setText(perfil.getApellidos());
		textEmail.setText(perfil.getEmail());
		textDNI.setText(perfil.getDni());
		textTelefono1.setText(perfil.getTelefono1());
		textTelefono2.setText(perfil.getTelefono2());
		textDireccion.setText(perfil.getDireccion());
		textTipoUsuario.setText(tipoNombre != null ? tipoNombre : perfil.getTipoNombre());
		
		lblUsuarioId.setText("ID Usuario: " + perfil.getId());
		
		// TODO: Cargar imagen de perfil si existe argazkiaUrl
		if (perfil.getArgazkiaUrl() != null && !perfil.getArgazkiaUrl().isEmpty()) {
			System.out.println("URL de foto de perfil: " + perfil.getArgazkiaUrl());
		}
	}

	/**
	 * Carga datos de ejemplo (fallback)
	 */
	private void loadSampleData() {
		textUsername.setText("usuario.ejemplo");
		textNombre.setText("Usuario");
		textApellidos.setText("de Ejemplo");
		textEmail.setText("usuario@elorrieta.com");
		textDNI.setText("12345678A");
		textTelefono1.setText("+34 123 456 789");
		textTelefono2.setText("");
		textDireccion.setText("Calle Ejemplo, 123");
		textTipoUsuario.setText("Usuario");
		lblUsuarioId.setText("ID Usuario: ---");
		lblStatus.setText("⚠ Mostrando datos de ejemplo (sin conexión)");
		lblStatus.setForeground(new Color(255, 140, 0));
	}
}