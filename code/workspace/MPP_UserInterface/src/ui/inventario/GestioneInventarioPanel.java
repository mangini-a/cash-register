package ui.inventario;

import java.awt.*;
import javax.swing.*;

import ui.MainFrame;

@SuppressWarnings("serial")
public class GestioneInventarioPanel extends JPanel {
	
	private JTabbedPane tabbedPane;
	private JPanel aggiungiProdottoPanel;
	private JPanel visualizzaProdottiPanel;
	
	public GestioneInventarioPanel(MainFrame mainFrame) {
		// Imposta il layout manager per questo container di primo livello
		setLayout(new BorderLayout());

		// Crea il sotto-container e le schede a cui consente l'accesso
		tabbedPane = new JTabbedPane();
        aggiungiProdottoPanel = new AggiungiProdottoPanel(this);
        visualizzaProdottiPanel = new VisualizzaProdottiPanel(this);
        
        // Aggiunge le schede al sotto-container precedentemente creato
        tabbedPane.addTab("Aggiungi prodotto", aggiungiProdottoPanel);
        tabbedPane.addTab("Visualizza prodotti", visualizzaProdottiPanel);

        // Aggiunge il sotto-container al layout
        add(tabbedPane, BorderLayout.CENTER);
    }
}
