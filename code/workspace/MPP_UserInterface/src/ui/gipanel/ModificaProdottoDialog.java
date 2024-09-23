package ui.gipanel;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

import inventory.ProductService;
import jooq.DataService;

@SuppressWarnings("serial")
public class ModificaProdottoDialog extends JDialog {

	private JTextField nomeField;
	private JTextField descrizioneField;
	private JTextField qtaField;
	private JTextField prezzoField;
	private JButton confermaButton;
	private JButton annullaButton;
	private ProductService productService;

	public ModificaProdottoDialog(JFrame parentFrame, DataService dataService, String nomeAttuale, String descrizioneAttuale,
			int qtaAttuale, float prezzoAttuale) {
		super(parentFrame, "Modifica le informazioni relative a: " + nomeAttuale, true);
		this.productService = ProductService.getInstance(dataService);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// Crea il campo per l'inserimento del nuovo nome del prodotto
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		add(new JLabel("Nuovo nome:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		nomeField = new JTextField(nomeAttuale);
		add(nomeField, gbc);

		// Crea il campo per l'inserimento della nuova descrizione del prodotto
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Nuova descrizione:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		descrizioneField = new JTextField(descrizioneAttuale);
		add(descrizioneField, gbc);

		// Crea il campo per l'inserimento della nuova quantità del prodotto
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Nuova quantità:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		qtaField = new JTextField(String.valueOf(qtaAttuale));
		add(qtaField, gbc);

		// Crea il campo per l'inserimento del nuovo prezzo del prodotto
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Nuovo prezzo (€):"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		prezzoField = new JTextField(String.valueOf(prezzoAttuale));
		add(prezzoField, gbc);

		// Crea un pannello separato per i pulsanti "Conferma" ed "Annulla"
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// Crea il pulsante "Conferma" e lo aggiunge al pannello inferiore
		confermaButton = new JButton("Conferma", new ImageIcon("../img/yes.png"));
		confermaButton.addActionListener(e -> {
			String nuovoNome = nomeField.getText();
		    String nuovoPrezzoText = prezzoField.getText();
		    String nuovaQtaText = qtaField.getText();
		    String nuovaDescrizione = descrizioneField.getText();
		    try {
				modificaProdotto(nomeAttuale, nuovoNome, nuovoPrezzoText, nuovaQtaText, nuovaDescrizione);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		buttonPanel.add(confermaButton);

		// Crea il pulsante "Annulla" e lo aggiunge al pannello inferiore
		annullaButton = new JButton("Annulla", new ImageIcon("../img/no.png"));
		annullaButton.addActionListener(e -> dispose());
		buttonPanel.add(annullaButton);

		// Aggiunge il pannello inferiore a quello principale
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2; // Occupa ambo le colonne a disposizione
		gbc.anchor = GridBagConstraints.CENTER;
		add(buttonPanel, gbc);

		// Set preferred and minimum sizes for the dialog
		setPreferredSize(new Dimension(400, 250));
		setResizable(false);

		// Pack the dialog to ensure all components are laid out properly
		pack();

		// Get the location of the parent frame
		Point parentLocation = parentFrame.getLocationOnScreen();

		// Set the location of the dialog to be centered relative to the parent frame
		int x = parentLocation.x + (parentFrame.getWidth() - getWidth()) / 2;
		int y = parentLocation.y + (parentFrame.getHeight() - getHeight()) / 2;
		setLocation(x, y);
	}

	private void modificaProdotto(String nomeAttuale, String nuovoNome, String nuovoPrezzoText, String nuovaQtaText, String nuovaDescrizione) throws SQLException {
		// Sostituisce eventuali virgole con dei punti per gestire entrambi i separatori decimali
		nuovoPrezzoText = nuovoPrezzoText.replace(",", ".");

		// Ottiene il nuovo prezzo del prodotto sotto forma di numero reale
		float nuovoPrezzo = 0.0f;

		try {
			nuovoPrezzo = Float.parseFloat(nuovoPrezzoText);
			if (nuovoPrezzo <= 0) {
				JOptionPane.showMessageDialog(this, "Nuovo prezzo non valido. Inserisci un numero positivo.", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return; // Esce dal metodo se il nuovo prezzo risulta negativo o nullo
			}
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Nuovo prezzo non valido. Inserisci un numero reale.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Esce dal metodo se il nuovo prezzo è costituito da un valore non valido
		}

		// Ottiene la nuova quantità del prodotto sotto forma di numero intero
		int nuovaQta = 0;

		try {
			nuovaQta = Integer.parseInt(nuovaQtaText);
			if (nuovaQta <= 0) {
				JOptionPane.showMessageDialog(this, "Nuova quantità non valida. Inserisci un numero positivo.", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return; // Esce dal metodo se la nuova quantità non risulta positiva
			}
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Nuova quantità non valida. Inserisci un numero intero.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Esce dal metodo se la nuova quantità è costituita da un valore non valido
		}
		
		// Modifica il prodotto agendo sulla relativa tabella nel database, chiudendo automaticamente la connessione al termine
		boolean esito = productService.update(nomeAttuale, nuovoNome, nuovoPrezzo, nuovaQta, nuovaDescrizione);
		
		if (esito) {
			JOptionPane.showMessageDialog(this, "Prodotto modificato con successo!", "Operazione riuscita",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Non è stato possibile modificare il prodotto.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
