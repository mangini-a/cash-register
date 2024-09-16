package ui.scontrini;

import java.awt.*;
import javax.swing.*;

import ui.MainFrame;

@SuppressWarnings("serial")
public class RegistrazioneScontriniPanel extends JPanel {

	private JTabbedPane tabbedPane;
	private JPanel registraScontrinoPanel;
	private JPanel visualizzaScontriniPanel;

	public RegistrazioneScontriniPanel(MainFrame mainFrame) {
		// Imposta il layout manager per questo container di primo livello
		setLayout(new BorderLayout());

		// Crea il sotto-container e le schede a cui consente l'accesso
		tabbedPane = new JTabbedPane();
		registraScontrinoPanel = new RegistraScontrinoPanel(this);
		visualizzaScontriniPanel = new VisualizzaScontriniPanel(this);

		// Aggiunge le schede al sotto-container precedentemente creato
		tabbedPane.addTab("Registra scontrino", registraScontrinoPanel);
		tabbedPane.addTab("Visualizza scontrini", visualizzaScontriniPanel);

		// Aggiunge il sotto-container al layout
		add(tabbedPane, BorderLayout.CENTER);
	}
}
