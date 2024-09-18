package ui.gipanel;

import java.awt.*;
import javax.swing.*;

import jooq.DataService;

@SuppressWarnings("serial")
public class AggiungiProdottoDialog extends JDialog {

	private JTextField nomeField;
	private JTextField descrizioneField;
	private JTextField qtaField;
	private JTextField prezzoField;
	private JButton confermaButton;
	private JButton annullaButton;

	public AggiungiProdottoDialog(JFrame parentFrame) {
		super(parentFrame, "Aggiungi un nuovo prodotto", true);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// Crea il campo per l'inserimento del nome del prodotto
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		add(new JLabel("Nome:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		nomeField = new JTextField(20);
		add(nomeField, gbc);

		// Crea il campo per l'inserimento della descrizione del prodotto
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Descrizione:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		descrizioneField = new JTextField(20);
		add(descrizioneField, gbc);

		// Crea il campo per l'inserimento della quantità del prodotto
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Quantità:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		qtaField = new JTextField(20);
		add(qtaField, gbc);
		
		// Crea il campo per l'inserimento del prezzo del prodotto
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Prezzo (€):"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		prezzoField = new JTextField(20);
		add(prezzoField, gbc);
		
		// Crea un pannello separato per i pulsanti "Conferma" ed "Annulla"
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// Crea il pulsante "Conferma" e lo aggiunge al pannello inferiore
		confermaButton = new JButton("Conferma", new ImageIcon("../img/yes.png"));
		confermaButton.addActionListener(e -> aggiungiProdotto());
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

	private void aggiungiProdotto() {
		// Get form data
		String nome = nomeField.getText();
		String prezzoText = prezzoField.getText();
		String qtaText = qtaField.getText();
		String descrizione = descrizioneField.getText();

		// Replace commas with periods to handle both decimal separators
		prezzoText = prezzoText.replace(",", ".");

		// Parse product price as float
		float prezzo = 0.0f;

		try {
			prezzo = Float.parseFloat(prezzoText);
			if (prezzo <= 0) {
				JOptionPane.showMessageDialog(this, "Prezzo non valido. Inserisci un numero positivo.", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the price is not positive
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Prezzo non valido. Inserisci un numero reale.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the price is invalid
		}

		// Parse product quantity as integer
		int qta = 0;

		try {
			qta = Integer.parseInt(qtaText);
			if (qta <= 0) {
				JOptionPane.showMessageDialog(this, "Quantità non valida. Inserisci un numero positivo.", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the quantity is not positive
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Quantità non valida. Inserisci un numero intero.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the quantity is invalid
		}

		// Aggiunge il prodotto alla relativa tabella nel database, chiudendo
		// automaticamente la connessione al termine
		try {
			try (DataService dataService = new DataService()) {
				dataService.aggiungiProdotto(nome, prezzo, qta, descrizione);
			} // La connessione viene chiusa qui

			// Mostra un messaggio di conferma
			JOptionPane.showMessageDialog(this, "Prodotto aggiunto con successo!", "Operazione riuscita",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Non è stato possibile aggiungere il prodotto.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}

		// Close dialog
		dispose();
	}
}
