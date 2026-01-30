package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.UserListTable;

public class Alumnos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private UserListTable userTable;
	private JTextField searchField;
	private JButton viewScheduleButton;
	private JLabel selectedUserLabel;

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
		JPanel topPanel = new JPanel(new FlowLayout());
		JLabel searchLabel = new JLabel("Buscar:");
		searchField = new JTextField(20);
		searchField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				userTable.filterUsers(searchField.getText());
			}
		});
		topPanel.add(searchLabel);
		topPanel.add(searchField);
		contentPane.add(topPanel, BorderLayout.NORTH);

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

		// Cargar datos de ejemplo
		loadSampleData();
	}

	private void loadSampleData() {
		// Datos de ejemplo de alumnos
		userTable.addUser(1, "Juan", "García López", "juan.garcia@elorrieta.com", "Alumno");
		userTable.addUser(2, "María", "Fernández Silva", "maria.fernandez@elorrieta.com", "Alumno");
		userTable.addUser(3, "Carlos", "Rodríguez Martín", "carlos.rodriguez@elorrieta.com", "Alumno");
		userTable.addUser(4, "Ana", "López González", "ana.lopez@elorrieta.com", "Alumno");
		userTable.addUser(5, "Pedro", "Sánchez Ruiz", "pedro.sanchez@elorrieta.com", "Alumno");
		userTable.addUser(6, "Laura", "Martín Díez", "laura.martin@elorrieta.com", "Alumno");
		userTable.addUser(7, "Diego", "Herrera Vega", "diego.herrera@elorrieta.com", "Alumno");
		userTable.addUser(8, "Carmen", "Jiménez Moreno", "carmen.jimenez@elorrieta.com", "Alumno");
		userTable.addUser(9, "Alberto", "Navarro Castillo", "alberto.navarro@elorrieta.com", "Alumno");
		userTable.addUser(10, "Isabel", "Torres Ramos", "isabel.torres@elorrieta.com", "Alumno");
		userTable.addUser(11, "Miguel", "Vargas Peña", "miguel.vargas@elorrieta.com", "Alumno");
		userTable.addUser(12, "Lucía", "Mendoza Cruz", "lucia.mendoza@elorrieta.com", "Alumno");
		userTable.addUser(13, "Rafael", "Ortega Luna", "rafael.ortega@elorrieta.com", "Alumno");
		userTable.addUser(14, "Sofía", "Aguilar Flores", "sofia.aguilar@elorrieta.com", "Alumno");
		userTable.addUser(15, "Andrés", "Castro Herrera", "andres.castro@elorrieta.com", "Alumno");

		// Algunos profesores de ejemplo para mostrar la funcionalidad mixta
		userTable.addUser(101, "Dr. José", "Pérez Docente", "jose.perez@elorrieta.com", "Profesor");
		userTable.addUser(102, "Dra. Elena", "Morales Catedrática", "elena.morales@elorrieta.com", "Profesor");
	}

}