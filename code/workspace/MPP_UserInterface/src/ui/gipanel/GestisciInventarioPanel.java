package ui.gipanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import ui.MainFrame;

@SuppressWarnings("serial")
public class GestisciInventarioPanel extends JPanel {

	public GestisciInventarioPanel(MainFrame mainFrame) {
		setLayout(new BorderLayout());

	    // Aggiunge questa schermata al pannello dei contenuti del frame principale
	    mainFrame.getContentPane().add(this, BorderLayout.CENTER);

	    // Add your components to the panel
	    // ...
	}
}
