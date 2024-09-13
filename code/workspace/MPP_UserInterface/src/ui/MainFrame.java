package ui;

import javax.swing.*;

import ui.inventario.GestisciInventarioPanel;
import ui.scontrini.RegistraScontrinoPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel gestisciInventarioPanel;
    private JPanel registraScontrinoPanel;

	public MainFrame() {
		setTitle("Minimarket Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
		
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        gestisciInventarioPanel = new GestisciInventarioPanel(this);
        registraScontrinoPanel = new RegistraScontrinoPanel(this);

        mainPanel.add(gestisciInventarioPanel, "Gestisci Inventario");
        mainPanel.add(registraScontrinoPanel, "Registra Scontrino");

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));

        JButton gestisciInventarioButton = new JButton("Gestisci Inventario");
        JButton registraScontrinoButton = new JButton("Registra Scontrino");
        
        gestisciInventarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Gestisci Inventario");
            }
        });

        registraScontrinoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Registra Scontrino");
            }
        });

        buttonPanel.add(gestisciInventarioButton);
        buttonPanel.add(registraScontrinoButton);

        add(buttonPanel, BorderLayout.WEST);

        setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
	}
}
