package ui.scontrini;

import java.awt.*;
import javax.swing.*;

import ui.MainFrame;

@SuppressWarnings("serial")
public class RegistrazioneScontriniPanel extends JPanel {

	private JTabbedPane tabbedPane;
	private RegistraScontrinoPanel registraScontrinoPanel;
	private VisualizzaScontriniPanel visualizzaScontriniPanel;

	public RegistrazioneScontriniPanel(MainFrame mainFrame) {
		setLayout(new BorderLayout());

		// Crea il pannello a schede
		tabbedPane = new JTabbedPane();

		// Crea le schede a cui il pannello consente l'accesso
		registraScontrinoPanel = new RegistraScontrinoPanel();
		visualizzaScontriniPanel = new VisualizzaScontriniPanel();

		// Aggiungi le schede al pannello precedentemente creato
		tabbedPane.addTab("Registra scontrino", registraScontrinoPanel);
		tabbedPane.addTab("Visualizza scontrini", visualizzaScontriniPanel);

		add(tabbedPane, BorderLayout.CENTER);
	}
}
