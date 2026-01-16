package view;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Color;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu("Profe");
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
	public Menu(String user) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FirstView.class.getResource("/img/elorrieta.png")));
		setTitle("Menu - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Header area with welcome message and profile button
		JLabel lblWelcome = new JLabel("");
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblWelcome.setForeground(new Color(51, 102, 153));
		lblWelcome.setBounds(50, 30, 600, 50);
		contentPane.add(lblWelcome);
		lblWelcome.setText("Bienvenido, " + user + "!");
		
		JButton btnPerfil = new JButton("");
		btnPerfil.setBackground(new Color(51, 102, 153));
		ImageIcon originalIcon = new ImageIcon(Menu.class.getResource("/img/ic_profile.png"));
		Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		btnPerfil.setIcon(scaledIcon);
		btnPerfil.setBounds(738, 11, 36, 33);
		contentPane.add(btnPerfil);
		
		// Main menu buttons - centered layout in 2x2 grid
		JButton btnHorario = new JButton("Horario");
		btnHorario.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnHorario.setBackground(new Color(70, 130, 180));
		btnHorario.setForeground(Color.WHITE);
		btnHorario.setBounds(200, 150, 170, 80);
		contentPane.add(btnHorario);
		
		JButton btnConsulta = new JButton("Consulta");
		btnConsulta.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnConsulta.setBackground(new Color(70, 130, 180));
		btnConsulta.setForeground(Color.WHITE);
		btnConsulta.setBounds(430, 150, 170, 80);
		contentPane.add(btnConsulta);
		
		JButton btnReuniones = new JButton("Reuniones");
		btnReuniones.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnReuniones.setBackground(new Color(70, 130, 180));
		btnReuniones.setForeground(Color.WHITE);
		btnReuniones.setBounds(200, 270, 170, 80);
		contentPane.add(btnReuniones);
		
		JButton btnAlumnos = new JButton("Alumnos");
		btnAlumnos.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAlumnos.setBackground(new Color(70, 130, 180));
		btnAlumnos.setForeground(Color.WHITE);
		btnAlumnos.setBounds(430, 270, 170, 80);
		contentPane.add(btnAlumnos);
		
		// Logo/school image centered at bottom
		JLabel lblSchoolLogo = new JLabel("");
		ImageIcon logoIcon = new ImageIcon(Menu.class.getResource("/img/elorrieta.png"));
		Image logoImage = logoIcon.getImage().getScaledInstance(150, 90, Image.SCALE_SMOOTH);
		ImageIcon scaledLogoIcon = new ImageIcon(logoImage);
		lblSchoolLogo.setIcon(scaledLogoIcon);
		lblSchoolLogo.setBounds(325, 390, 150, 90);
		contentPane.add(lblSchoolLogo);
		
		// Logout button positioned at bottom right
		JButton btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogout.setBackground(new Color(220, 53, 69));
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBounds(684, 510, 90, 40);
		contentPane.add(btnLogout);
		
		// Button Listeners
		
		btnHorario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnConsulta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Consultas consultas = new Consultas(user);
				consultas.setVisible(true);
			}
		});
		
		btnReuniones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// TODO: Certify logout in backend
				dispose();
				FirstView login = new FirstView();
				login.setVisible(true);
			}
		});
	}

}