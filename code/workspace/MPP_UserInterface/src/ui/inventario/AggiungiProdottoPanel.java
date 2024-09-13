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

	public AggiungiProdottoPanel(GestisciInventarioPanel gestisciInventarioPanel) {
		this.dataService = new DataService();
		setLayout(new GridLayout(5, 2, 5, 5));

		JLabel lblNomeProdotto = new JLabel("Nome:");
		add(lblNomeProdotto);

		nomeProdottoField = new JTextField();
		add(nomeProdottoField);
		nomeProdottoField.setColumns(10);

		JLabel lblPrezzoProdotto = new JLabel("Prezzo:");
		add(lblPrezzoProdotto);

		prezzoProdottoField = new JTextField();
		add(prezzoProdottoField);
		prezzoProdottoField.setColumns(10);

		JLabel lblQtaProdotto = new JLabel("Quantità:");
		add(lblQtaProdotto);

		qtaProdottoField = new JTextField();
		add(qtaProdottoField);
		qtaProdottoField.setColumns(10);

		JLabel lblDescrizioneProdotto = new JLabel("Descrizione:");
		add(lblDescrizioneProdotto);

		descrizioneProdottoField = new JTextField();
		add(descrizioneProdottoField);
		descrizioneProdottoField.setColumns(10);

		JButton btnAggiungiProdotto = new JButton("Aggiungi Prodotto");
		add(btnAggiungiProdotto);

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
