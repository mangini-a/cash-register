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
	private CardLayout cardLayout; // CardLayout per gestire le tre schermate operative
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

        // Alla pressione di un pulsante, mostra il relativo pannello
        registraScontrinoButton.addActionListener(e -> showPanel("Registra"));
        gestisciInventarioButton.addActionListener(e -> showPanel("Gestisci"));
        visualizzaScontriniButton.addActionListener(e -> showPanel("Visualizza"));
        
        // Crea il pannello che ospita i pulsanti con un FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registraScontrinoButton);
        buttonPanel.add(gestisciInventarioButton);
        buttonPanel.add(visualizzaScontriniButton);
        
        // Aggiunge il pannello che ospita i pulsanti a quello principale, posizionandolo in alto
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Crea un CardLayout per le schermate operative
        cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout); // Pannello per le card (schede)

        // Inizializza le tre schermate operative, a cui si può accedere tramite un click sul relativo pulsante
        registraScontrinoPanel = new RegistraScontrinoPanel(this);
        gestisciInventarioPanel = new GestisciInventarioPanel(this);
        visualizzaScontriniPanel = new VisualizzaScontriniPanel(this);
        
        // Aggiunge le schermate operative (a cui sono associati nomi univoci) al pannello per le card
        cardPanel.add(registraScontrinoPanel, "Registra");
        cardPanel.add(gestisciInventarioPanel, "Gestisci");
        cardPanel.add(visualizzaScontriniPanel, "Visualizza");
        
        // Aggiunge il pannello per le card a quello principale
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Mostra la schermata iniziale
        showPanel("Registra");

        // Imposta le proprietà del frame
        add(mainPanel);
		pack();
		setMinimumSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/*
	 * Gestisce la transizione tra una schermata e l'altra.
	 */
	private void showPanel(String panelName) {
		cardLayout.show((Container) mainPanel.getComponent(1), panelName); // Mostra la schermata selezionata mediante il proprio nome
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainFrame::new);
	}
}
