package ui.inventario;

import java.awt.*;
import javax.swing.*;

import jooq.DataService;

@SuppressWarnings("serial")
public class AggiungiProdottoPanel extends JPanel {

	private JTextField nomeField;
	private JTextField prezzoField;
	private JTextField qtaField;
	private JTextField descrizioneField;
	private JButton aggiungiButton;
	private DataService dataService;

	public AggiungiProdottoPanel(GestioneInventarioPanel gestisciInventarioPanel) {
		this.dataService = new DataService();
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// Nome del prodotto
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		add(new JLabel("Nome:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		nomeField = new JTextField(20);
		add(nomeField, gbc);

		// Prezzo del prodotto
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Prezzo (€):"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		prezzoField = new JTextField(20);
		add(prezzoField, gbc);

		// Quantità del prodotto
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

		// Descrizione del prodotto
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Descrizione:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		descrizioneField = new JTextField(20);
		add(descrizioneField, gbc);

		// Pulsante "Aggiungi"
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		aggiungiButton = new JButton("Aggiungi", new ImageIcon("../img/create-icon.png"));
		aggiungiButton.addActionListener(e -> aggiungiProdotto());
		add(aggiungiButton, gbc);
	}

	private void aggiungiProdotto() {
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
		} catch (NumberFormatException e) {
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
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Quantità non valida. Inserisci un numero intero.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the quantity is invalid
		}

		try {
			// Handle the product addition using the underlying business logic
			dataService.aggiungiProdotto(nome, prezzo, qta, descrizione);
			JOptionPane.showMessageDialog(this, "Prodotto aggiunto con successo!", "Operazione riuscita",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Non è stato possibile aggiungere il prodotto.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}

		// Clear the fields after adding the product
		nomeField.setText("");
		prezzoField.setText("");
		qtaField.setText("");
		descrizioneField.setText("");
	}
}
