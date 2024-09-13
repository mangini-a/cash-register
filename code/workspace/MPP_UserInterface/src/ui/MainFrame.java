package ui;

import javax.swing.*;

import ui.inventario.GestisciInventarioPanel;
import ui.scontrini.RegistraScontrinoPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel mainPanel;
	private CardLayout cardLayout;
	private JPanel productSectionPanel;
	private JPanel cartSectionPanel;

	public MainFrame() {
		setTitle("Minimarket Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Crea una menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu productMenu = new JMenu("Gestione inventario");
		JMenu cartMenu = new JMenu("Registrazione scontrini");

		JMenuItem addProductItem = new JMenuItem("Aggiungi prodotto", new ImageIcon("../img/create-icon.png"));
		JMenuItem deleteProductItem = new JMenuItem("Elimina prodotto", new ImageIcon("../img/delete-icon.png"));
		JMenuItem updateProductItem = new JMenuItem("Modifica prodotto", new ImageIcon("../img/update-icon.png"));
		JMenuItem viewProductsItem = new JMenuItem("Visualizza prodotti", new ImageIcon("../img/read-icon.png"));
		JMenuItem cartItem = new JMenuItem("Registra scontrino", new ImageIcon("../img/invoice-icon.png"));

		addProductItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Gestisci Inventario");
				((JTabbedPane) productSectionPanel.getComponent(0)).setSelectedIndex(0);
			}
		});

		deleteProductItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Gestisci Inventario");
				((JTabbedPane) productSectionPanel.getComponent(0)).setSelectedIndex(1);
			}
		});

		updateProductItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Gestisci Inventario");
				((JTabbedPane) productSectionPanel.getComponent(0)).setSelectedIndex(2);
			}
		});

		viewProductsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Gestisci Inventario");
				((JTabbedPane) productSectionPanel.getComponent(0)).setSelectedIndex(3);
			}
		});

		cartItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainPanel, "Registra Scontrino");
			}
		});

		productMenu.add(addProductItem);
		productMenu.add(deleteProductItem);
		productMenu.add(updateProductItem);
		productMenu.add(viewProductsItem);
		cartMenu.add(cartItem);

		menuBar.add(productMenu);
		menuBar.add(cartMenu);
		setJMenuBar(menuBar);

		mainPanel = new JPanel();
		cardLayout = new CardLayout();
		mainPanel.setLayout(cardLayout);

		productSectionPanel = new GestisciInventarioPanel(this);
		cartSectionPanel = new RegistraScontrinoPanel(this);

		mainPanel.add(productSectionPanel, "Gestisci Inventario");
		mainPanel.add(cartSectionPanel, "Registra Scontrino");

		add(mainPanel, BorderLayout.CENTER);

		// Sceglie automaticamente la dimensione del frame a seconda del suo contenuto
		pack();

		// Imposta una dimensione minima per garantire che il frame non risulti troppo
		// piccolo
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
