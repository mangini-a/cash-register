package ui.inventario;

import javax.swing.*;

import jooq.DataService;

import java.awt.*;

@SuppressWarnings("serial")
public class AggiungiProdottoPanel extends JPanel {

	private JTextField nomeProdottoField;
	private JTextField prezzoProdottoField;
	private JTextField qtaProdottoField;
	private JTextField descrizioneProdottoField;
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
		nomeProdottoField = new JTextField(20);
		add(nomeProdottoField, gbc);

		// Prezzo del prodotto
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Prezzo (€):"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		prezzoProdottoField = new JTextField(20);
		add(prezzoProdottoField, gbc);

		// Quantità del prodotto
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Quantità:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		qtaProdottoField = new JTextField(20);
		add(qtaProdottoField, gbc);
		
		// Descrizione del prodotto
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Descrizione:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		descrizioneProdottoField = new JTextField(20);
		add(descrizioneProdottoField, gbc);
		
		// Pulsante "Aggiungi prodotto"
		gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
		JButton btnAggiungiProdotto = new JButton("Aggiungi prodotto", new ImageIcon("../img/create-icon.png"));
		add(btnAggiungiProdotto, gbc);

		btnAggiungiProdotto.addActionListener(e -> aggiungiProdotto());
	}

	private void aggiungiProdotto() {
		String nomeProdotto = nomeProdottoField.getText();
		String prezzoProdottoText = prezzoProdottoField.getText();
		String qtaProdottoText = qtaProdottoField.getText();
		String descrizioneProdotto = descrizioneProdottoField.getText();

		// Replace commas with periods to handle both decimal separators
		prezzoProdottoText = prezzoProdottoText.replace(",", ".");

		// Parse product price as float
		float prezzoProdotto = 0.0f;

		try {
			prezzoProdotto = Float.parseFloat(prezzoProdottoText);
			if (prezzoProdotto <= 0) {
				JOptionPane.showMessageDialog(this, "Prezzo del prodotto non valido. Inserisci un numero positivo.",
						"Errore", JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the price is not positive
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Prezzo del prodotto non valido. Inserisci un numero reale.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the price is invalid
		}

		// Parse product quantity as integer
		int qtaProdotto = 0;

		try {
			qtaProdotto = Integer.parseInt(qtaProdottoText);
			if (qtaProdotto <= 0) {
				JOptionPane.showMessageDialog(this, "Quantità del prodotto non valida. Inserisci un numero positivo.",
						"Errore", JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the quantity is not positive
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Quantità del prodotto non valida. Inserisci un numero intero.",
					"Errore", JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the quantity is invalid
		}

		// Handle the product addition using the underlying business logic
		dataService.aggiungiProdotto(nomeProdotto, prezzoProdotto, qtaProdotto, descrizioneProdotto);

		// Show a confirmation message
		JOptionPane.showMessageDialog(this, "Prodotto aggiunto con successo!", "Operazione riuscita",
				JOptionPane.INFORMATION_MESSAGE);

		// Clear the fields after adding the product
		nomeProdottoField.setText("");
		prezzoProdottoField.setText("");
		qtaProdottoField.setText("");
		descrizioneProdottoField.setText("");
	}
}
