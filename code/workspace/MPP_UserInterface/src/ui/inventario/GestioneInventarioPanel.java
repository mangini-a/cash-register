package ui.inventario;

import java.awt.*;
import javax.swing.*;

import ui.MainFrame;

@SuppressWarnings("serial")
public class GestioneInventarioPanel extends JPanel {
	
	private JTabbedPane tabbedPane;
	private JPanel aggiungiProdottoPanel;
	private JPanel eliminaProdottoPanel;
	private JPanel modificaProdottoPanel;
	private JPanel visualizzaProdottiPanel;
	
	public GestioneInventarioPanel(MainFrame mainFrame) {
		setLayout(new BorderLayout());

		// Crea il pannello a schede
		tabbedPane = new JTabbedPane();

		// Crea le schede a cui il pannello consente l'accesso
        aggiungiProdottoPanel = new AggiungiProdottoPanel(this);
        eliminaProdottoPanel = new EliminaProdottoPanel(this);
        modificaProdottoPanel = new ModificaProdottoPanel(this);
        visualizzaProdottiPanel = new VisualizzaProdottiPanel(this);
        
        // Aggiungi le schede al pannello precedentemente creato
        tabbedPane.addTab("Aggiungi prodotto", aggiungiProdottoPanel);
        tabbedPane.addTab("Elimina prodotto", eliminaProdottoPanel);
        tabbedPane.addTab("Modifica prodotto", modificaProdottoPanel);
        tabbedPane.addTab("Visualizza prodotti", visualizzaProdottiPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }
}
