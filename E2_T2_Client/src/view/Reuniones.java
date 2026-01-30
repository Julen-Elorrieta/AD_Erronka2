package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import utils.Meetings;

public class Reuniones extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaMeetings;
	private DefaultTableModel tableModel;
	private JButton btnRefrescar;
	private JButton btnVerDetalles;
	private JButton btnFiltrarProfesor;
	private JButton btnFiltrarAlumno;
	private JButton btnMostrarTodas;
	private JLabel lblEstado;
	private JLabel lblTotal;
	private JComboBox<String> comboFiltroEstado;
	private JTextField txtBuscar;
	private Meetings meetingClient;
	private String user;
	private List<Meetings.MeetingData> reunionesActuales;


	/**
	 * Create the frame.
	 * @param user 
	 */
	public Reuniones(String user) {
		meetingClient = new Meetings();
		
		this.user = user;

		setTitle("Gestión de Reuniones - Elorrieta");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		// Panel superior - Título y controles
		JPanel panelSuperior = crearPanelSuperior();
		contentPane.add(panelSuperior, BorderLayout.NORTH);

		// Panel central - Tabla de reuniones
		JPanel panelCentral = crearPanelTabla();
		contentPane.add(panelCentral, BorderLayout.CENTER);

		// Panel inferior - Acciones
		JPanel panelInferior = crearPanelInferior();
		contentPane.add(panelInferior, BorderLayout.SOUTH);

		// Cargar datos iniciales
		cargarTodasReuniones();
	}

	/**
	 * Crea el panel superior con título y filtros
	 */
	private JPanel crearPanelSuperior() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		// Título
		JLabel lblTitulo = new JLabel("Gestión de Reuniones");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblTitulo.setForeground(new Color(51, 102, 153));
		panel.add(lblTitulo, BorderLayout.NORTH);

		// Panel de filtros
		JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

		// Búsqueda por texto
		JLabel lblBuscar = new JLabel("Buscar:");
		txtBuscar = new JTextField(15);
		txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				filtrarReuniones();
			}
		});

		// Filtro por estado
		JLabel lblFiltroEstado = new JLabel("Estado:");
		comboFiltroEstado = new JComboBox<>();
		comboFiltroEstado.setModel(
				new DefaultComboBoxModel<>(new String[] { "Todos", "Pendiente", "Aceptada", "Denegada", "Conflicto" }));
		comboFiltroEstado.addActionListener(e -> filtrarReuniones());

		btnRefrescar = new JButton("🔄 Actualizar");
		btnRefrescar.addActionListener(e -> cargarTodasReuniones());

		btnMostrarTodas = new JButton("Todas las Reuniones");
		btnMostrarTodas.addActionListener(e -> cargarTodasReuniones());

		panelFiltros.add(lblBuscar);
		panelFiltros.add(txtBuscar);
		panelFiltros.add(lblFiltroEstado);
		panelFiltros.add(comboFiltroEstado);
		panelFiltros.add(btnRefrescar);
		panelFiltros.add(btnMostrarTodas);

		panel.add(panelFiltros, BorderLayout.CENTER);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnVolver.setBackground(new Color(108, 117, 125));
		panelFiltros.add(btnVolver);
		
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Menu menu = new Menu(user);
				menu.setVisible(true);
			}
		});

		// Panel de estado
		JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblEstado = new JLabel("Listo");
		lblTotal = new JLabel("Total: 0 reuniones");
		lblTotal.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelEstado.add(lblEstado);
		panelEstado.add(new JLabel(" | "));
		panelEstado.add(lblTotal);
		panel.add(panelEstado, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Crea el panel con la tabla de reuniones
	 */
	private JPanel crearPanelTabla() {
		JPanel panel = new JPanel(new BorderLayout());

		// Crear tabla
		String[] columnas = { "ID", "Título", "Estado", "Profesor ID", "Alumno ID", "Fecha", "Hora", "Aula", "Asunto" };

		tableModel = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Tabla no editable
			}
		};

		tablaMeetings = new JTable(tableModel);
		tablaMeetings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaMeetings.setRowHeight(25);
		tablaMeetings.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
		tablaMeetings.getTableHeader().setBackground(new Color(70, 130, 180));
		tablaMeetings.getTableHeader().setForeground(Color.WHITE);

		// Ajustar anchos de columnas
		tablaMeetings.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
		tablaMeetings.getColumnModel().getColumn(1).setPreferredWidth(150); // Título
		tablaMeetings.getColumnModel().getColumn(2).setPreferredWidth(80); // Estado
		tablaMeetings.getColumnModel().getColumn(3).setPreferredWidth(80); // Prof ID
		tablaMeetings.getColumnModel().getColumn(4).setPreferredWidth(80); // Alum ID
		tablaMeetings.getColumnModel().getColumn(5).setPreferredWidth(100); // Fecha
		tablaMeetings.getColumnModel().getColumn(6).setPreferredWidth(60); // Hora
		tablaMeetings.getColumnModel().getColumn(7).setPreferredWidth(60); // Aula
		tablaMeetings.getColumnModel().getColumn(8).setPreferredWidth(200); // Asunto

		JScrollPane scrollPane = new JScrollPane(tablaMeetings);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Crea el panel inferior con botones de acción
	 */
	private JPanel crearPanelInferior() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		// Primera fila de botones
		JPanel panelBotones1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

		btnVerDetalles = new JButton("📋 Ver Detalles");
		btnVerDetalles.setEnabled(false);
		btnVerDetalles.addActionListener(e -> verDetallesReunion());

		btnFiltrarProfesor = new JButton("👨‍🏫 Filtrar por Profesor");
		btnFiltrarProfesor.addActionListener(e -> filtrarPorProfesor());

		btnFiltrarAlumno = new JButton("👨‍🎓 Filtrar por Alumno");
		btnFiltrarAlumno.addActionListener(e -> filtrarPorAlumno());

		panelBotones1.add(btnVerDetalles);
		panelBotones1.add(btnFiltrarProfesor);
		panelBotones1.add(btnFiltrarAlumno);

		// Segunda fila - información
		JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel lblInfo = new JLabel("Selecciona una reunión para ver sus detalles");
		lblInfo.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblInfo.setForeground(Color.GRAY);
		panelInfo.add(lblInfo);

		panel.add(panelBotones1);
		panel.add(panelInfo);

		// Listener para habilitar botón de detalles
		tablaMeetings.getSelectionModel().addListSelectionListener(e -> {
			btnVerDetalles.setEnabled(tablaMeetings.getSelectedRow() != -1);
		});

		return panel;
	}

	/**
	 * Carga todas las reuniones desde el servidor
	 */
	private void cargarTodasReuniones() {
		btnRefrescar.setEnabled(false);
		lblEstado.setText("Cargando reuniones...");
		lblEstado.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				reunionesActuales = meetingClient.obtenerTodasReuniones();

				EventQueue.invokeLater(() -> {
					actualizarTabla(reunionesActuales);
					lblEstado.setText("✓ Reuniones cargadas correctamente");
					lblEstado.setForeground(new Color(0, 128, 0));
					lblTotal.setText("Total: " + reunionesActuales.size() + " reuniones");
					btnRefrescar.setEnabled(true);

					if (reunionesActuales.isEmpty()) {
						JOptionPane.showMessageDialog(this, "No se encontraron reuniones en el sistema.", "Sin datos",
								JOptionPane.INFORMATION_MESSAGE);
					}
				});

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					lblEstado.setText("❌ Error al cargar");
					lblEstado.setForeground(Color.RED);
					btnRefrescar.setEnabled(true);

					JOptionPane.showMessageDialog(this, "Error al conectar con el servidor:\n" + e.getMessage(),
							"Error de Conexión", JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	/**
	 * Filtra reuniones por profesor
	 */
	private void filtrarPorProfesor() {
		String input = JOptionPane.showInputDialog(this, "Introduce el ID del profesor:", "Filtrar por Profesor",
				JOptionPane.QUESTION_MESSAGE);

		if (input != null && !input.trim().isEmpty()) {
			try {
				long profesorId = Long.parseLong(input.trim());
				cargarReunionesPorProfesor(profesorId);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "El ID debe ser un número válido", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Carga reuniones de un profesor específico
	 */
	private void cargarReunionesPorProfesor(long profesorId) {
		lblEstado.setText("Cargando reuniones del profesor " + profesorId + "...");
		lblEstado.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				reunionesActuales = meetingClient.obtenerReunionesPorProfesor(profesorId);

				EventQueue.invokeLater(() -> {
					actualizarTabla(reunionesActuales);
					lblEstado.setText("✓ Reuniones del profesor " + profesorId);
					lblEstado.setForeground(new Color(0, 128, 0));
					lblTotal.setText("Total: " + reunionesActuales.size() + " reuniones");

					if (reunionesActuales.isEmpty()) {
						JOptionPane.showMessageDialog(this,
								"No se encontraron reuniones para el profesor con ID: " + profesorId, "Sin resultados",
								JOptionPane.INFORMATION_MESSAGE);
					}
				});

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					lblEstado.setText("❌ Error al cargar");
					lblEstado.setForeground(Color.RED);
					JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	/**
	 * Filtra reuniones por alumno
	 */
	private void filtrarPorAlumno() {
		String input = JOptionPane.showInputDialog(this, "Introduce el ID del alumno:", "Filtrar por Alumno",
				JOptionPane.QUESTION_MESSAGE);

		if (input != null && !input.trim().isEmpty()) {
			try {
				long alumnoId = Long.parseLong(input.trim());
				cargarReunionesPorAlumno(alumnoId);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "El ID debe ser un número válido", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Carga reuniones de un alumno específico
	 */
	private void cargarReunionesPorAlumno(long alumnoId) {
		lblEstado.setText("Cargando reuniones del alumno " + alumnoId + "...");
		lblEstado.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				reunionesActuales = meetingClient.obtenerReunionesPorAlumno(alumnoId);

				EventQueue.invokeLater(() -> {
					actualizarTabla(reunionesActuales);
					lblEstado.setText("✓ Reuniones del alumno " + alumnoId);
					lblEstado.setForeground(new Color(0, 128, 0));
					lblTotal.setText("Total: " + reunionesActuales.size() + " reuniones");

					if (reunionesActuales.isEmpty()) {
						JOptionPane.showMessageDialog(this,
								"No se encontraron reuniones para el alumno con ID: " + alumnoId, "Sin resultados",
								JOptionPane.INFORMATION_MESSAGE);
					}
				});

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					lblEstado.setText("❌ Error al cargar");
					lblEstado.setForeground(Color.RED);
					JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	/**
	 * Filtra las reuniones según los criterios actuales
	 */
	private void filtrarReuniones() {
		if (reunionesActuales == null)
			return;

		String textoBusqueda = txtBuscar.getText().toLowerCase().trim();
		String estadoFiltro = (String) comboFiltroEstado.getSelectedItem();

		List<Meetings.MeetingData> reunionesFiltradas = reunionesActuales.stream().filter(r -> {
			// Filtro por texto
			boolean coincideTexto = textoBusqueda.isEmpty()
					|| (r.getTitulo() != null && r.getTitulo().toLowerCase().contains(textoBusqueda))
					|| (r.getAsunto() != null && r.getAsunto().toLowerCase().contains(textoBusqueda))
					|| (r.getAula() != null && r.getAula().toLowerCase().contains(textoBusqueda));

			// Filtro por estado
			boolean coincideEstado = estadoFiltro.equals("Todos") || r.getEstadoEspañol().equals(estadoFiltro);

			return coincideTexto && coincideEstado;
		}).toList();

		actualizarTabla(reunionesFiltradas);
		lblTotal.setText("Mostrando: " + reunionesFiltradas.size() + " de " + reunionesActuales.size() + " reuniones");
	}

	/**
	 * Actualiza la tabla con las reuniones proporcionadas
	 */
	private void actualizarTabla(List<Meetings.MeetingData> reuniones) {
		tableModel.setRowCount(0);

		for (Meetings.MeetingData reunion : reuniones) {
			Object[] fila = { reunion.getIdReunion(), reunion.getTitulo(), reunion.getEstadoEspañol(),
					reunion.getProfesorId(), reunion.getAlumnoId(), reunion.getFechaSoloFecha(),
					reunion.getFechaSoloHora(), reunion.getAula(), reunion.getAsunto() };
			tableModel.addRow(fila);
		}
	}

	/**
	 * Muestra los detalles de la reunión seleccionada
	 */
	private void verDetallesReunion() {
		int selectedRow = tablaMeetings.getSelectedRow();
		if (selectedRow == -1)
			return;

		Long idReunion = (Long) tableModel.getValueAt(selectedRow, 0);

		// Buscar la reunión en la lista actual
		Meetings.MeetingData reunion = reunionesActuales.stream().filter(r -> r.getIdReunion().equals(idReunion))
				.findFirst().orElse(null);

		if (reunion == null)
			return;

		// Crear panel de detalles
		JPanel panelDetalles = new JPanel(new GridLayout(10, 2, 10, 10));
		panelDetalles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panelDetalles.add(new JLabel("ID Reunión:"));
		panelDetalles.add(new JLabel(reunion.getIdReunion().toString()));

		panelDetalles.add(new JLabel("Título:"));
		panelDetalles.add(new JLabel(reunion.getTitulo()));

		panelDetalles.add(new JLabel("Estado:"));
		panelDetalles.add(new JLabel(reunion.getEstadoEspañol() + " / " + reunion.getEstadoEus()));

		panelDetalles.add(new JLabel("Profesor ID:"));
		panelDetalles.add(new JLabel(reunion.getProfesorId() != null ? reunion.getProfesorId().toString() : "N/A"));

		panelDetalles.add(new JLabel("Alumno ID:"));
		panelDetalles.add(new JLabel(reunion.getAlumnoId() != null ? reunion.getAlumnoId().toString() : "N/A"));

		panelDetalles.add(new JLabel("Fecha y Hora:"));
		panelDetalles.add(new JLabel(reunion.getFechaFormateada()));

		panelDetalles.add(new JLabel("Aula:"));
		panelDetalles.add(new JLabel(reunion.getAula()));

		panelDetalles.add(new JLabel("Centro:"));
		panelDetalles.add(new JLabel(reunion.getIdCentro()));

		panelDetalles.add(new JLabel("Asunto:"));
		JLabel lblAsunto = new JLabel("<html>" + reunion.getAsunto() + "</html>");
		panelDetalles.add(lblAsunto);

		JOptionPane.showMessageDialog(this, panelDetalles, "Detalles de la Reunión", JOptionPane.INFORMATION_MESSAGE);
	}
}