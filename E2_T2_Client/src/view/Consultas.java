package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utils.WeekScheduleTable;

public class Consultas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private WeekScheduleTable scheduleTable;

	/**
	 * Create the frame.
	 */
	public Consultas(String user) {

		setIconImage(Toolkit.getDefaultToolkit().getImage(Consultas.class.getResource("/img/elorrieta.png")));
		setTitle("Consultas - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 650);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		// Header panel
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(240, 240, 240));
		headerPanel.setLayout(null);
		headerPanel.setPreferredSize(new java.awt.Dimension(0, 80));
		contentPane.add(headerPanel, BorderLayout.NORTH);

		JLabel lblTitle = new JLabel("Consulta de Horarios - " + user);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblTitle.setForeground(new Color(51, 102, 153));
		lblTitle.setBounds(50, 20, 600, 40);
		headerPanel.add(lblTitle);

		// Back button
		JButton btnVolver = new JButton("Volver");
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnVolver.setBackground(new Color(108, 117, 125));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setBounds(780, 25, 80, 30);
		headerPanel.add(btnVolver);

		// Schedule table
		scheduleTable = new WeekScheduleTable();
		scheduleTable.setScheduleTitle("Horario de " + user);
		contentPane.add(scheduleTable, BorderLayout.CENTER);

		// Load sample data
		loadSampleSchedule();

		// Button listener
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Menu menu = new Menu(user);
				menu.setVisible(true);
			}
		});
	}

	/**
	 * Carga datos de ejemplo en el horario
	 */
	private void loadSampleSchedule() {
		// Ejemplo de horario con diferentes tipos de actividades

		// Clases normales
		scheduleTable.setCellContent(1, 0, "Matemáticas");
		scheduleTable.setCellColor(1, 0, WeekScheduleTable.NORMAL_CLASS);

		scheduleTable.setCellContent(2, 0, "Historia");
		scheduleTable.setCellColor(2, 0, WeekScheduleTable.NORMAL_CLASS);

		scheduleTable.setCellContent(3, 1, "Programación");
		scheduleTable.setCellColor(3, 1, WeekScheduleTable.NORMAL_CLASS);

		// Reuniones programadas
		scheduleTable.setCellContent(4, 2, "Reunión - Padres");
		scheduleTable.setCellColor(4, 2, WeekScheduleTable.MEETING_SCHEDULED);

		scheduleTable.setCellContent(5, 3, "Reunión - Equipo");
		scheduleTable.setCellColor(5, 3, WeekScheduleTable.MEETING_SCHEDULED);

		// Reunión cancelada
		scheduleTable.setCellContent(2, 4, "Reunión Cancelada");
		scheduleTable.setCellColor(2, 4, WeekScheduleTable.MEETING_CANCELLED);

		// Tiempo libre
		scheduleTable.setCellContent(1, 2, "Libre");
		scheduleTable.setCellColor(1, 2, WeekScheduleTable.FREE_TIME);

		// Más clases
		scheduleTable.setCellContent(4, 0, "Inglés");
		scheduleTable.setCellColor(4, 0, WeekScheduleTable.NORMAL_CLASS);

		scheduleTable.setCellContent(5, 1, "Ciencias");
		scheduleTable.setCellColor(5, 1, WeekScheduleTable.NORMAL_CLASS);
	}

}