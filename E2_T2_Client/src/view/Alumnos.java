package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.GetAllUsers;
import utils.UserListTable;

public class Alumnos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private UserListTable userTable;
	private JTextField searchField;
	private JButton viewScheduleButton;
	private JButton refreshButton;
	private JLabel selectedUserLabel;
	private JLabel statusLabel;
	private JButton btnVolver;
	private String currentUser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Alumnos frame = new Alumnos();
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
	public Alumnos() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setTitle("Gestión de Alumnos - Elorrieta");

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		// Crear la tabla de usuarios
		userTable = new UserListTable("Alumnos");
		contentPane.add(userTable, BorderLayout.CENTER);

		// Panel superior con búsqueda
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel searchLabel = new JLabel("Buscar:");
		searchField = new JTextField(20);
		searchField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				userTable.filterUsers(searchField.getText());
			}
		});
		
		refreshButton = new JButton("🔄 Actualizar");
		refreshButton.addActionListener(e -> cargarAlumnosDesdeServidor());
		
		statusLabel = new JLabel("Listo");
		
		topPanel.add(searchLabel);
		topPanel.add(searchField);
		topPanel.add(refreshButton);
		topPanel.add(statusLabel);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		btnVolver = new JButton("Volver");
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnVolver.setBackground(new Color(108, 117, 125));
		topPanel.add(btnVolver);
		
		// Button listener
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Menu menu = new Menu(currentUser);
				menu.setVisible(true);
			}
		});

		// Panel inferior con acciones
		JPanel bottomPanel = new JPanel(new FlowLayout());
		selectedUserLabel = new JLabel("Ningún alumno seleccionado");
		viewScheduleButton = new JButton("Ver Horario");
		viewScheduleButton.setEnabled(false);

		viewScheduleButton.addActionListener(e -> {
			Object[] userInfo = userTable.getSelectedUserInfo();
			if (userInfo != null) {
				// Aquí se abriría la ventana del horario del alumno seleccionado
				javax.swing.JOptionPane.showMessageDialog(this,
						"Abriendo horario de: " + userInfo[1] + " " + userInfo[2] + "\nEmail: " + userInfo[3],
						"Horario del Alumno", javax.swing.JOptionPane.INFORMATION_MESSAGE);
			}
		});

		bottomPanel.add(selectedUserLabel);
		bottomPanel.add(viewScheduleButton);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		// Configurar listener para selección de usuarios
		userTable.addUserSelectionListener((userId, userName, userType) -> {
			selectedUserLabel.setText("Alumno seleccionado: " + userName + " (ID: " + userId + ")");
			viewScheduleButton.setEnabled(true);
		});

		// Cargar datos reales desde el servidor
		cargarAlumnosDesdeServidor();
	}

	/**
	 * Carga los alumnos desde el servidor Socket
	 */
	private void cargarAlumnosDesdeServidor() {
		// Deshabilitar botón mientras carga
		refreshButton.setEnabled(false);
		statusLabel.setText("Cargando alumnos...");
		
		// Ejecutar en hilo separado para no bloquear la UI
		new Thread(() -> {
			try {
				GetAllUsers client = new GetAllUsers();
				
				// Obtener solo alumnos (tipoId = 4)
				List<GetAllUsers.UserData> alumnos = client.obtenerUsuariosPorTipo(4);
				
				// Actualizar UI en el hilo de eventos
				EventQueue.invokeLater(() -> {
					// Limpiar tabla
					userTable.clearUsers();
					
					// Agregar alumnos a la tabla
					for (GetAllUsers.UserData alumno : alumnos) {
						userTable.addUser(
							alumno.getId().intValue(),
							alumno.getNombre(),
							alumno.getApellidos(),
							alumno.getEmail(),
							alumno.getTipoNombre()
						);
					}
					
					// Actualizar estado
					statusLabel.setText("✓ " + alumnos.size() + " alumnos cargados");
					refreshButton.setEnabled(true);
					
					if (alumnos.isEmpty()) {
						javax.swing.JOptionPane.showMessageDialog(
							this,
							"No se encontraron alumnos en el sistema.\n" +
							"Verifica que el servidor esté ejecutándose.",
							"Sin datos",
							javax.swing.JOptionPane.WARNING_MESSAGE
						);
					}
				});
				
			} catch (Exception e) {
				// Manejar error en el hilo de eventos
				EventQueue.invokeLater(() -> {
					statusLabel.setText("❌ Error al cargar");
					refreshButton.setEnabled(true);
					
					javax.swing.JOptionPane.showMessageDialog(
						this,
						"Error al conectar con el servidor:\n" + e.getMessage() +
						"\n\nVerifica que el servidor esté ejecutándose en el puerto 6000.",
						"Error de Conexión",
						javax.swing.JOptionPane.ERROR_MESSAGE
					);
					
					// Cargar datos de ejemplo como fallback
					loadSampleData();
				});
			}
		}).start();
	}

	/**
	 * Datos de ejemplo (fallback)
	 */
	private void loadSampleData() {
		userTable.clearUsers();
		userTable.addUser(1, "Juan", "García López", "juan.garcia@elorrieta.com", "Alumno");
		userTable.addUser(2, "María", "Fernández Silva", "maria.fernandez@elorrieta.com", "Alumno");
		userTable.addUser(3, "Carlos", "Rodríguez Martín", "carlos.rodriguez@elorrieta.com", "Alumno");
		userTable.addUser(4, "Ana", "López González", "ana.lopez@elorrieta.com", "Alumno");
		userTable.addUser(5, "Pedro", "Sánchez Ruiz", "pedro.sanchez@elorrieta.com", "Alumno");
		statusLabel.setText("⚠ Datos de ejemplo (sin conexión)");
	}
}