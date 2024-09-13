package ui.inventario;

import java.awt.*;
import javax.swing.*;

import ui.MainFrame;

@SuppressWarnings("serial")
public class GestisciInventarioPanel extends JPanel {
	
	private JTabbedPane tabbedPane;
	private JPanel aggiungiProdottoPanel;
	private JPanel eliminaProdottoPanel;
	private JPanel modificaProdottoPanel;
	private JPanel visualizzaProdottiPanel;
	
	public GestisciInventarioPanel(MainFrame mainFrame) {
		setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

        aggiungiProdottoPanel = new AggiungiProdottoPanel(this);
        eliminaProdottoPanel = new EliminaProdottoPanel(this);
        modificaProdottoPanel = new ModificaProdottoPanel(this);
        visualizzaProdottiPanel = new VisualizzaProdottiPanel(this);

        tabbedPane.addTab("Aggiungi prodotto", aggiungiProdottoPanel);
        tabbedPane.addTab("Elimina prodotto", eliminaProdottoPanel);
        tabbedPane.addTab("Modifica prodotto", modificaProdottoPanel);
        tabbedPane.addTab("Visualizza prodotti", visualizzaProdottiPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }
}
