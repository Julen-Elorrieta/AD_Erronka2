package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Ordutegiak extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JPanel edukiPanel;

    public Ordutegiak() {
        setTitle("Ordutegia - EE Software");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        
        edukiPanel = new JPanel();
        edukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(edukiPanel);
    }
}