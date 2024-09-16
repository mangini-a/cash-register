package ui.inventario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;

@SuppressWarnings("serial")
public class ModificaProdottoPanel extends JPanel {

	private JComboBox<String> prodottiComboBox;
	private JTextField nomeProdottoField;
	private JTextField prezzoProdottoField;
	private JTextField qtaProdottoField;
	private JTextField descrizioneProdottoField;
	private DataService dataService;

	public ModificaProdottoPanel(GestioneInventarioPanel gestisciInventarioPanel) {
		this.dataService = new DataService();
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// Selezione del prodotto da modificare
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		add(new JLabel("Seleziona prodotto:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		// Carica le descrizioni dei prodotti nella combo box
		String[] descrizioniProdotti = caricaDescrizioniProdotti();
		prodottiComboBox = new JComboBox<>(descrizioniProdotti);
		add(prodottiComboBox, gbc);

		prodottiComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					caricaDettagliProdotto((String) prodottiComboBox.getSelectedItem());
				}
			}
		});

		// Nuovo nome del prodotto
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Nuovo nome:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		nomeProdottoField = new JTextField(20);
		add(nomeProdottoField, gbc);

		// Nuovo prezzo del prodotto
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Nuovo prezzo:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		prezzoProdottoField = new JTextField(20);
		add(prezzoProdottoField, gbc);

		// Nuova quantità del prodotto
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Nuova quantità:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		qtaProdottoField = new JTextField(20);
		add(qtaProdottoField, gbc);

		// Nuova descrizione del prodotto
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Nuova descrizione:"), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		descrizioneProdottoField = new JTextField(20);
		add(descrizioneProdottoField, gbc);

		// Pulsante "Modifica Prodotto"
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JButton btnModificaProdotto = new JButton("Modifica prodotto", new ImageIcon("../img/update-icon.png"));
		add(btnModificaProdotto, gbc);

		btnModificaProdotto.addActionListener(e -> modificaProdotto());
	}

	private String[] caricaDescrizioniProdotti() {
		// Recupera le descrizioni dei prodotti dal database usando jOOQ
		List<ProdottoRecord> prodotti = dataService.getProdotti();
		String[] descrizioniProdotti = new String[prodotti.size()];
		for (int i = 0; i < prodotti.size(); i++) {
			descrizioniProdotti[i] = prodotti.get(i).getDescrizione();
		}
		return descrizioniProdotti;
	}

	private void caricaDettagliProdotto(String prodottoSelezionato) {
		// Recupera i dettagli del prodotto selezionato dal database usando jOOQ
		ProdottoRecord prodotto = dataService.getProdottoByDescrizione(prodottoSelezionato);

		if (prodotto != null) {
			// Popola i campi di testo con i dettagli correnti del prodotto (pre-modifica)
			nomeProdottoField.setText(prodotto.getNome());
			prezzoProdottoField.setText(String.valueOf(prodotto.getPrezzo()));
			qtaProdottoField.setText(String.valueOf(prodotto.getQtadisponibile()));
			descrizioneProdottoField.setText(prodotto.getDescrizione());
		}
	}

	private void modificaProdotto() {
		// Recupera il prodotto selezionato dall'utente attraverso la combo box
		String prodottoSelezionato = (String) prodottiComboBox.getSelectedItem();
		String nuovoNomeProdotto = nomeProdottoField.getText();
		String nuovoPrezzoProdottoText = prezzoProdottoField.getText();
		String nuovaQtaProdottoText = qtaProdottoField.getText();
		String nuovaDescrizioneProdotto = descrizioneProdottoField.getText();

		// Sostituisci la virgola eventualmente inserita con un punto, per accettare
		// entrambi i separatori decimali
		nuovoPrezzoProdottoText = nuovoPrezzoProdottoText.replace(",", ".");

		// Estrai il nuovo prezzo del prodotto come un valore in virgola mobile
		float nuovoPrezzoProdotto = 0.0f;

		try {
			nuovoPrezzoProdotto = Float.parseFloat(nuovoPrezzoProdottoText);
			if (nuovoPrezzoProdotto <= 0) {
				JOptionPane.showMessageDialog(this, "Prezzo del prodotto non valido. Inserisci un numero positivo.",
						"Errore", JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the price is not positive
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Prezzo del prodotto non valido. Inserisci un numero reale.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the price is invalid
		}

		// Estrai la nuova quantità del prodotto come un valore intero
		int nuovaQtaProdotto = 0;

		try {
			nuovaQtaProdotto = Integer.parseInt(nuovaQtaProdottoText);
			if (nuovaQtaProdotto <= 0) {
				JOptionPane.showMessageDialog(this, "Quantità del prodotto non valida. Inserisci un numero positivo.",
						"Errore", JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the quantity is not positive
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Quantità del prodotto non valida. Inserisci un numero intero.",
					"Errore", JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the quantity is invalid
		}

		// Gestisci la modifica del prodotto in questione mediante la Business Logic
		// (classe DataService)
		int rowsAffected = dataService.modificaProdotto(nuovoNomeProdotto, nuovoPrezzoProdotto, nuovaQtaProdotto,
				nuovaDescrizioneProdotto, prodottoSelezionato);

		if (rowsAffected > 0) {
			System.out.println("Prodotto modificato con successo.");

			// Mostra a video un messaggio di conferma
			JOptionPane.showMessageDialog(this, "Prodotto modificato con successo!", "Operazione riuscita",
					JOptionPane.INFORMATION_MESSAGE);

			// Resetta i campi di inserimento dopo aver modificato il prodotto
			nomeProdottoField.setText("");
			prezzoProdottoField.setText("");
			qtaProdottoField.setText("");
			descrizioneProdottoField.setText("");
		} else {
			JOptionPane.showMessageDialog(this, "Non è stato possibile modificare il prodotto.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
