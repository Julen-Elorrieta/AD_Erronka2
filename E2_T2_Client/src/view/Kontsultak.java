package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import utils.AstekoOrdutegia;

public class Kontsultak extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JPanel edukiPanel;
    private final AstekoOrdutegia ordutegiaTabla;
    private final String erabiltzailea;

    public Kontsultak(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(
            Kontsultak.class.getResource("/img/elorrieta.png")));
        setTitle("Kontsultak - EE Software");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        
        edukiPanel = new JPanel();
        edukiPanel.setBackground(new Color(240, 240, 240));
        edukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        edukiPanel.setLayout(new BorderLayout());
        setContentPane(edukiPanel);

        edukiPanel.add(sortuGoiburua(), BorderLayout.NORTH);

        ordutegiaTabla = new AstekoOrdutegia();
        ordutegiaTabla.ezarriOrdutegiIzenburua(erabiltzailea + "ren Ordutegia");
        edukiPanel.add(ordutegiaTabla, BorderLayout.CENTER);

        kargatuAdibideOrdutegia();
    }

    private JPanel sortuGoiburua() {
        JPanel goiburuPanel = new JPanel();
        goiburuPanel.setBackground(new Color(240, 240, 240));
        goiburuPanel.setLayout(null);
        goiburuPanel.setPreferredSize(new java.awt.Dimension(0, 80));

        JLabel izenburua = new JLabel("Ordutegien Kontsulta - " + erabiltzailea);
        izenburua.setFont(new Font("Tahoma", Font.BOLD, 24));
        izenburua.setForeground(new Color(51, 102, 153));
        izenburua.setBounds(50, 20, 600, 40);
        goiburuPanel.add(izenburua);

        JButton btnAtzera = new JButton("Atzera");
        btnAtzera.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAtzera.setBackground(new Color(108, 117, 125));
        btnAtzera.setForeground(Color.WHITE);
        btnAtzera.setBounds(780, 25, 80, 30);
        btnAtzera.addActionListener(e -> {
            dispose();
            new Menua(erabiltzailea).setVisible(true);
        });
        goiburuPanel.add(btnAtzera);

        return goiburuPanel;
    }

    private void kargatuAdibideOrdutegia() {
        ordutegiaTabla.ezarriGelaxkaEdukia(1, 0, "Matematika");
        ordutegiaTabla.ezarriGelaxkaKolorea(1, 0, AstekoOrdutegia.KLASE_ARRUNTA);
        
        ordutegiaTabla.ezarriGelaxkaEdukia(2, 0, "Historia");
        ordutegiaTabla.ezarriGelaxkaKolorea(2, 0, AstekoOrdutegia.KLASE_ARRUNTA);
        
        ordutegiaTabla.ezarriGelaxkaEdukia(3, 1, "Programazioa");
        ordutegiaTabla.ezarriGelaxkaKolorea(3, 1, AstekoOrdutegia.KLASE_ARRUNTA);
        
        ordutegiaTabla.ezarriGelaxkaEdukia(4, 2, "Bilera - Gurasoek");
        ordutegiaTabla.ezarriGelaxkaKolorea(4, 2, AstekoOrdutegia.BILERA_PROGRAMATUA);
        
        ordutegiaTabla.ezarriGelaxkaEdukia(5, 3, "Bilera - Taldea");
        ordutegiaTabla.ezarriGelaxkaKolorea(5, 3, AstekoOrdutegia.BILERA_PROGRAMATUA);
        
        ordutegiaTabla.ezarriGelaxkaEdukia(2, 4, "Bilera Bertan Behera");
        ordutegiaTabla.ezarriGelaxkaKolorea(2, 4, AstekoOrdutegia.BILERA_EZEZTUA);
        
        ordutegiaTabla.ezarriGelaxkaEdukia(1, 2, "Libre");
        ordutegiaTabla.ezarriGelaxkaKolorea(1, 2, AstekoOrdutegia.DENBORA_LIBRE);
        
        ordutegiaTabla.ezarriGelaxkaEdukia(4, 0, "Ingelesa");
        ordutegiaTabla.ezarriGelaxkaKolorea(4, 0, AstekoOrdutegia.KLASE_ARRUNTA);
        
        ordutegiaTabla.ezarriGelaxkaEdukia(5, 1, "Zientziak");
        ordutegiaTabla.ezarriGelaxkaKolorea(5, 1, AstekoOrdutegia.KLASE_ARRUNTA);
    }
}