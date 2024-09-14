package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import ui.inventario.GestioneInventarioPanel;
import ui.scontrini.RegistrazioneScontriniPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel mainPanel;
	private CardLayout cardLayout;
	private JPanel gestioneInventarioPanel;
	private JPanel registrazioneScontriniPanel;

	public MainFrame() {
		setTitle("Minimarket Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Crea una barra posizionata in alto che ospiti i due menu previsti
		JMenuBar menuBar = new JMenuBar();
		JMenu inventarioMenu = new JMenu("Gestione inventario");
		JMenu scontriniMenu = new JMenu("Registrazione scontrini");
		
		// Crea le voci a cui si può accedere tramite il menu "Gestione inventario"
		JMenuItem aggiungiProdottoItem = new JMenuItem("Aggiungi prodotto", new ImageIcon("../img/create-icon.png"));
		JMenuItem eliminaProdottoItem = new JMenuItem("Elimina prodotto", new ImageIcon("../img/delete-icon.png"));
		JMenuItem modificaProdottoItem = new JMenuItem("Modifica prodotto", new ImageIcon("../img/update-icon.png"));
		JMenuItem visualizzaProdottiItem = new JMenuItem("Visualizza prodotti", new ImageIcon("../img/read-icon.png"));
		
		// Crea le voci a cui si può accedere tramite il menu "Registrazione scontrini"
		JMenuItem registraScontrinoItem = new JMenuItem("Registra scontrino", new ImageIcon("../img/invoice-icon.png"));
		JMenuItem visualizzaScontriniItem = new JMenuItem("Visualizza scontrini", new ImageIcon("../img/read-icon.png"));

		aggiungiProdottoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Gestisci Inventario");
				((JTabbedPane) gestioneInventarioPanel.getComponent(0)).setSelectedIndex(0);
			}
		});

		eliminaProdottoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Gestisci Inventario");
				((JTabbedPane) gestioneInventarioPanel.getComponent(0)).setSelectedIndex(1);
			}
		});

		modificaProdottoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Gestisci Inventario");
				((JTabbedPane) gestioneInventarioPanel.getComponent(0)).setSelectedIndex(2);
			}
		});

		visualizzaProdottiItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Gestisci Inventario");
				((JTabbedPane) gestioneInventarioPanel.getComponent(0)).setSelectedIndex(3);
			}
		});

		registraScontrinoItem.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Registra Scontrino");
                ((JTabbedPane) registrazioneScontriniPanel.getComponent(0)).setSelectedIndex(0);
            }
		});
		
		visualizzaScontriniItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Registra Scontrino");
                ((JTabbedPane) registrazioneScontriniPanel.getComponent(0)).setSelectedIndex(1);
            }
        });

		inventarioMenu.add(aggiungiProdottoItem);
		inventarioMenu.add(eliminaProdottoItem);
		inventarioMenu.add(modificaProdottoItem);
		inventarioMenu.add(visualizzaProdottiItem);
		
		scontriniMenu.add(registraScontrinoItem);
		scontriniMenu.add(visualizzaScontriniItem);

		menuBar.add(inventarioMenu);
		menuBar.add(scontriniMenu);
		setJMenuBar(menuBar);

		mainPanel = new JPanel();
		cardLayout = new CardLayout();
		mainPanel.setLayout(cardLayout);

		gestioneInventarioPanel = new GestioneInventarioPanel(this);
		registrazioneScontriniPanel = new RegistrazioneScontriniPanel(this);

		mainPanel.add(gestioneInventarioPanel, "Gestisci Inventario");
		mainPanel.add(registrazioneScontriniPanel, "Registra Scontrino");

		add(mainPanel, BorderLayout.CENTER);

		// Sceglie automaticamente la dimensione del frame a seconda del suo contenuto
		pack();

		// Imposta una dimensione minima per garantire che il frame non risulti troppo piccolo
		setMinimumSize(new Dimension(800, 600));

		// Centra il frame sullo schermo
		setLocationRelativeTo(null);

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
