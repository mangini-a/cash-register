package ui;

import java.awt.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatIntelliJLaf;

import ui.gipanel.GestisciInventarioPanel;
import ui.rspanel.RegistraScontrinoPanel;
import ui.vspanel.VisualizzaScontriniPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel mainPanel;
	private RegistraScontrinoPanel registraScontrinoPanel;
	private GestisciInventarioPanel gestisciInventarioPanel;
	private VisualizzaScontriniPanel visualizzaScontriniPanel;

	public MainFrame() {
		setTitle("Store manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("../img/euro-icon.png").getImage());
        
        // Imposta il tema IntelliJ di FlatLaf
        FlatIntelliJLaf.setup();
        
        // Crea il pannello principale con un BorderLayout
        mainPanel = new JPanel(new BorderLayout());

        // Crea i pulsanti che consentono l'accesso alle tre schermate operative
        JButton registraScontrinoButton = new JButton("Registra un nuovo scontrino", new ImageIcon("../img/addinvoice-icon.png"));
        JButton gestisciInventarioButton = new JButton("Gestisci l'inventario", new ImageIcon("../img/inventory-icon.png"));
        JButton visualizzaScontriniButton = new JButton("Visualizza gli scontrini emessi", new ImageIcon("../img/viewinvoices-icon.png"));

        // Alla pressione del pulsante "Registra un nuovo scontrino", mostra il relativo pannello
        registraScontrinoButton.addActionListener(e -> {
            showPanel(registraScontrinoPanel);
        });

        // Alla pressione del pulsante "Gestisci l'inventario", mostra il relativo pannello
        gestisciInventarioButton.addActionListener(e -> {
            showPanel(gestisciInventarioPanel);
        });

        // Alla pressione del pulsante "Visualizza gli scontrini emessi", mostra il relativo pannello
        visualizzaScontriniButton.addActionListener(e -> {
            showPanel(visualizzaScontriniPanel);
        });
        
        // Crea il pannello che ospita i pulsanti con un FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registraScontrinoButton);
        buttonPanel.add(gestisciInventarioButton);
        buttonPanel.add(visualizzaScontriniButton);
        
        // Aggiunge il pannello che ospita i pulsanti a quello principale, posizionandolo in alto
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Crea le tre schermate operative, a cui si può accedere tramite un click sul relativo pulsante
        registraScontrinoPanel = new RegistraScontrinoPanel(this);
        gestisciInventarioPanel = new GestisciInventarioPanel(this);
        visualizzaScontriniPanel = new VisualizzaScontriniPanel(this);
        
        // Aggiunge la schermata iniziale (home page) al pannello principale
        showPanel(registraScontrinoPanel);

        // Posiziona il pannello principale al centro della schermata
        add(mainPanel, BorderLayout.CENTER);
		
		// Imposta una dimensione minima per garantire che il frame non risulti troppo piccolo
		setMinimumSize(new Dimension(800, 600));
		
		// Centra il frame sullo schermo
		setLocationRelativeTo(null);
		
		// Mostra il frame
		setVisible(true);
	}

	/*
	 * Gestisce la transizione tra una schermata e l'altra.
	 */
	private void showPanel(JPanel panel) {
		mainPanel.removeAll(); // Rimuove tutti i componenti dal pannello principale
        mainPanel.add(panel); // Aggiunge il pannello selezionato al pannello principale
        revalidate();
        repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainFrame::new);
	}
}
