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

	public ModificaProdottoPanel(GestisciInventarioPanel gestisciInventarioPanel) {
		this.dataService = new DataService();
		setLayout(new GridLayout(6, 2, 5, 5));

		JLabel lblProdotto = new JLabel("Seleziona prodotto:");
		add(lblProdotto);

		// Carica le descrizioni dei prodotti nella combo box
		String[] descrizioniProdotti = caricaDescrizioniProdotti();
		prodottiComboBox = new JComboBox<>(descrizioniProdotti);
		add(prodottiComboBox);

		prodottiComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					caricaDettagliProdotto((String) prodottiComboBox.getSelectedItem());
				}
			}
		});

		JLabel lblNomeProdotto = new JLabel("Nuovo nome:");
		add(lblNomeProdotto);

		nomeProdottoField = new JTextField();
		add(nomeProdottoField);
		nomeProdottoField.setColumns(10);

		JLabel lblPrezzoProdotto = new JLabel("Nuovo prezzo:");
		add(lblPrezzoProdotto);

		prezzoProdottoField = new JTextField();
		add(prezzoProdottoField);
		prezzoProdottoField.setColumns(10);

		JLabel lblQtaProdotto = new JLabel("Nuova quantità:");
		add(lblQtaProdotto);

		qtaProdottoField = new JTextField();
		add(qtaProdottoField);
		qtaProdottoField.setColumns(10);

		JLabel lblDescrizioneProdotto = new JLabel("Nuova descrizione:");
		add(lblDescrizioneProdotto);

		descrizioneProdottoField = new JTextField();
		add(descrizioneProdottoField);
		descrizioneProdottoField.setColumns(10);

		JButton btnModificaProdotto = new JButton("Modifica Prodotto");
		add(btnModificaProdotto);

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
		ProdottoRecord prodotto = dataService.getProdotto(prodottoSelezionato);

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
			JOptionPane.showMessageDialog(this, "Non è stato possibile modificare il prodotto.", "Errore", JOptionPane.ERROR_MESSAGE);
		}
	}
}
