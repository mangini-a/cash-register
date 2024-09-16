package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.formdev.flatlaf.FlatIntelliJLaf;

import ui.inventario.GestioneInventarioPanel;
import ui.scontrini.RegistrazioneScontriniPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel mainPanel;
	private CardLayout cardLayout;
	private JPanel registrazioneScontriniPanel;
	private JPanel gestioneInventarioPanel;

	public MainFrame() {
		// Imposta il titolo del frame principale e fai sì che l'app termini alla chiusura del frame
		setTitle("Smart Cash Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Imposta il tema IntelliJ di FlatLaf (Flat Look and Feel)
		FlatIntelliJLaf.setup();

		// Crea una barra posizionata in alto che ospiti i due menu previsti
		JMenuBar menuBar = new JMenuBar();
		JMenu scontriniMenu = new JMenu("Registrazione scontrini");
		JMenu inventarioMenu = new JMenu("Gestione inventario");

		// Crea le voci a cui si può accedere tramite il menu "Registrazione scontrini"
		JMenuItem registraScontrinoItem = new JMenuItem("Registra scontrino", new ImageIcon("../img/invoice-icon.png"));
		JMenuItem visualizzaScontriniItem = new JMenuItem("Visualizza scontrini", new ImageIcon("../img/read-icon.png"));

		// Crea le voci a cui si può accedere tramite il menu "Gestione inventario"
		JMenuItem aggiungiProdottoItem = new JMenuItem("Aggiungi prodotto", new ImageIcon("../img/create-icon.png"));
		JMenuItem eliminaProdottoItem = new JMenuItem("Elimina prodotto", new ImageIcon("../img/delete-icon.png"));
		JMenuItem modificaProdottoItem = new JMenuItem("Modifica prodotto", new ImageIcon("../img/update-icon.png"));
		JMenuItem visualizzaProdottiItem = new JMenuItem("Visualizza prodotti", new ImageIcon("../img/read-icon.png"));

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

		// Aggiunge al menu "Registrazione scontrini" le relative voci
		scontriniMenu.add(registraScontrinoItem);
		scontriniMenu.add(visualizzaScontriniItem);

		// Aggiunge al menu "Gestione inventario" le relative voci
		inventarioMenu.add(aggiungiProdottoItem);
		inventarioMenu.add(eliminaProdottoItem);
		inventarioMenu.add(modificaProdottoItem);
		inventarioMenu.add(visualizzaProdottiItem);
		
		// Aggiunge entrambi i menu alla menu bar
		menuBar.add(scontriniMenu);
		menuBar.add(inventarioMenu);
		setJMenuBar(menuBar);
		
		// Definisce il layout del container principale
		mainPanel = new JPanel();
		cardLayout = new CardLayout();
		mainPanel.setLayout(cardLayout);

		// Crea i due sotto-container di primo livello ed aggiungili a quello top-level
		registrazioneScontriniPanel = new RegistrazioneScontriniPanel(this);
		gestioneInventarioPanel = new GestioneInventarioPanel(this);
		mainPanel.add(registrazioneScontriniPanel, "Registra Scontrino");
		mainPanel.add(gestioneInventarioPanel, "Gestisci Inventario");

		// Aggiunge il container principale al layout
		add(mainPanel, BorderLayout.CENTER);

		// Sceglie automaticamente la dimensione del frame a seconda del suo contenuto
		pack();

		// Imposta una dimensione minima per garantire che il frame non risulti troppo piccolo
		setMinimumSize(new Dimension(800, 600));

		// Centra il frame sullo schermo
		setLocationRelativeTo(null);

		// Imposta l'icona del frame principale
		ImageIcon icona = new ImageIcon("../img/euro-icon.png");
		setIconImage(icona.getImage());

		// Visualizza il frame principale a video
		setVisible(true);
	}

	public static void main(String[] args) {
		// Crea e visualizza il frame, assicurando che la UI venga creata sull'Event Dispatch Thread (EDT)
		SwingUtilities.invokeLater(MainFrame::new);
	}
}
