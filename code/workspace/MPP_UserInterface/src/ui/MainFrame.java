package ui;

import java.awt.*;
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
		initLookAndFeel();
		createMenu();
		createMainPanel();
		addComponents();
		setupLayout();
		pack();
		setMinimumSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initLookAndFeel() {
		FlatIntelliJLaf.setup();
	}

	private void createMenu() {
		// Crea una barra posizionata in alto che ospiti i tre menu previsti
		JMenuBar menuBar = new JMenuBar();
		JMenu scontriniMenu = new JMenu("Registrazione scontrini");
		JMenu inventarioMenu = new JMenu("Gestione inventario");

		// Crea le voci a cui si può accedere tramite il menu "Registrazione scontrini"
		JMenuItem registraScontrinoItem = new JMenuItem("Registra scontrino", new ImageIcon("../img/invoice-icon.png"));
		JMenuItem visualizzaScontriniItem = new JMenuItem("Visualizza scontrini",
				new ImageIcon("../img/read-icon.png"));

		// Crea le voci a cui si può accedere tramite il menu "Gestione inventario"
		JMenuItem aggiungiProdottoItem = new JMenuItem("Aggiungi prodotto", new ImageIcon("../img/create-icon.png"));
		JMenuItem eliminaProdottoItem = new JMenuItem("Elimina prodotto", new ImageIcon("../img/delete-icon.png"));
		JMenuItem modificaProdottoItem = new JMenuItem("Modifica prodotto", new ImageIcon("../img/update-icon.png"));
		JMenuItem visualizzaProdottiItem = new JMenuItem("Visualizza prodotti", new ImageIcon("../img/read-icon.png"));

		registraScontrinoItem.addActionListener(e -> {
			cardLayout.show(mainPanel, "Registra Scontrino");
			((JTabbedPane) registrazioneScontriniPanel.getComponent(0)).setSelectedIndex(0);
		});

		visualizzaScontriniItem.addActionListener(e -> {
			cardLayout.show(mainPanel, "Registra Scontrino");
			((JTabbedPane) registrazioneScontriniPanel.getComponent(0)).setSelectedIndex(1);
		});

		aggiungiProdottoItem.addActionListener(e -> {
			cardLayout.show(mainPanel, "Gestisci Inventario");
			((JTabbedPane) gestioneInventarioPanel.getComponent(0)).setSelectedIndex(0);
		});

		eliminaProdottoItem.addActionListener(e -> {
			cardLayout.show(mainPanel, "Gestisci Inventario");
			((JTabbedPane) gestioneInventarioPanel.getComponent(0)).setSelectedIndex(1);
		});

		modificaProdottoItem.addActionListener(e -> {
			cardLayout.show(mainPanel, "Gestisci Inventario");
			((JTabbedPane) gestioneInventarioPanel.getComponent(0)).setSelectedIndex(2);
		});

		visualizzaProdottiItem.addActionListener(e -> {
			cardLayout.show(mainPanel, "Gestisci Inventario");
			((JTabbedPane) gestioneInventarioPanel.getComponent(0)).setSelectedIndex(3);
		});

		scontriniMenu.add(registraScontrinoItem);
		scontriniMenu.add(visualizzaScontriniItem);

		inventarioMenu.add(aggiungiProdottoItem);
		inventarioMenu.add(eliminaProdottoItem);
		inventarioMenu.add(modificaProdottoItem);
		inventarioMenu.add(visualizzaProdottiItem);

		menuBar.add(scontriniMenu);
		menuBar.add(inventarioMenu);
		
		// Ensures that the menu bar takes up the full width of the frame
		menuBar.setPreferredSize(new Dimension(getWidth(), menuBar.getPreferredSize().height));
		setJMenuBar(menuBar);
	}
	
	private void createMainPanel() {
		mainPanel = new JPanel();
		cardLayout = new CardLayout();
		mainPanel.setLayout(cardLayout);
	}
	
	private void addComponents() {
		registrazioneScontriniPanel = new RegistrazioneScontriniPanel(this);
		gestioneInventarioPanel = new GestioneInventarioPanel(this);

		mainPanel.add(registrazioneScontriniPanel, "Registra Scontrino");
		mainPanel.add(gestioneInventarioPanel, "Gestisci Inventario");

		add(mainPanel, BorderLayout.CENTER);
	}

	private void setupLayout() {
		setTitle("Store manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("../img/euro-icon.png").getImage());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainFrame::new);
	}
}
