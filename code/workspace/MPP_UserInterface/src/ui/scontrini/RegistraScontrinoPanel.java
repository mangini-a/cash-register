package ui.scontrini;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.ScontrinoRecord;

@SuppressWarnings("serial")
public class RegistraScontrinoPanel extends JPanel {

	private JTextField prodottoField;
	private JSpinner qtaProdottoSpinner;
	private JTable cartTable;
	private DefaultTableModel tableModel;
	private JButton aggiungiAlCarrelloButton;
	private JButton generaScontrinoButton;
	private List<ProdottoRecord> prodotti;
	private List<ProdottoNelCarrello> prodottiNelCarrello;
	private DataService dataService;
	private InvoiceService invoiceService;

	public RegistraScontrinoPanel() {
		this.dataService = new DataService();
		this.invoiceService = new InvoiceService();
		setLayout(new BorderLayout());

		// Carica i prodotti dal database
		prodotti = dataService.getProdotti();
		prodottiNelCarrello = new ArrayList<>();

		// Crea il campo di testo per la selezione dei prodotti (con auto-completamento)
		prodottoField = new JTextField(20);
		prodottoField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = prodottoField.getText();
				if (!text.isEmpty()) {
					List<String> suggestions = getProductSuggestions(text);
					if (!suggestions.isEmpty()) {
						prodottoField.setText(suggestions.get(0));
						prodottoField.setSelectionStart(text.length());
						prodottoField.setSelectionEnd(suggestions.get(0).length());
					}
				}
			}

		});

		// Crea lo spinner per selezionare la quantità di prodotto desiderata dal cliente
		qtaProdottoSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

		// Crea il pulsante "Aggiungi al carrello"
		aggiungiAlCarrelloButton = new JButton("Aggiungi al carrello");
		aggiungiAlCarrelloButton.addActionListener(e -> aggiungiAlCarrello());

		// Crea la tabella che rappresenta il carrello della spesa
		tableModel = new DefaultTableModel(new Object[] { "Prodotto", "Quantità", "Prezzo" }, 0);
		cartTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(cartTable);

		// Crea il pulsante "Genera scontrino"
		generaScontrinoButton = new JButton("Genera scontrino");
		generaScontrinoButton.addActionListener(e -> generaScontrino());

		// Aggiungi i componenti sopra definiti al pannello
		JPanel topPanel = new JPanel(new FlowLayout());
		topPanel.add(new JLabel("Prodotto:"));
		topPanel.add(prodottoField);
		topPanel.add(new JLabel("Quantità:"));
		topPanel.add(qtaProdottoSpinner);
		topPanel.add(aggiungiAlCarrelloButton);

		JPanel bottomPanel = new JPanel(new FlowLayout());
		bottomPanel.add(generaScontrinoButton);

		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	private List<String> getProductSuggestions(String text) {
		List<String> suggestions = new ArrayList<>();
		for (ProdottoRecord prodotto : prodotti) {
			if (prodotto.getNome().toLowerCase().startsWith(text.toLowerCase())) {
				suggestions.add(prodotto.getNome());
			}
		}
		return suggestions;
	}

	private void aggiungiAlCarrello() {
		String nomeProdotto = prodottoField.getText();
		int qtaProdotto = (int) qtaProdottoSpinner.getValue();

		ProdottoRecord prodotto = getProductByName(nomeProdotto);
		if (prodotto != null) {
			ProdottoNelCarrello prodottoNelCarrello = new ProdottoNelCarrello(prodotto, qtaProdotto);
			prodottiNelCarrello.add(prodottoNelCarrello);
			tableModel.addRow(new Object[] { prodotto.getNome(), qtaProdotto, prodotto.getPrezzo() * qtaProdotto });
			prodottoField.setText("");
			qtaProdottoSpinner.setValue(1);
		} else {
			JOptionPane.showMessageDialog(this, "Prodotto non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
		}
	}

	private ProdottoRecord getProductByName(String nomeProdotto) {
		for (ProdottoRecord prodotto : prodotti) {
			if (prodotto.getNome().equalsIgnoreCase(nomeProdotto)) {
				return prodotto;
			}
		}
		return null;
	}

	private void generaScontrino() {
		if (prodottiNelCarrello.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Il carrello della spesa è vuoto.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Calcola il prezzo complessivo dello scontrino
		float prezzoTotale = 0;
		for (ProdottoNelCarrello prodottoNelCarrello : prodottiNelCarrello) {
			prezzoTotale += prodottoNelCarrello.getProdotto().getPrezzo() * prodottoNelCarrello.getQtaProdotto();
		}

		// Inserisci lo scontrino nella tabella Scontrino
		ScontrinoRecord scontrino = dataService.inserisciScontrino(prezzoTotale);

		// Delega l'inserimento delle voci dello scontrino nella tabella VoceScontrino ad un'istanza di InvoiceService
		invoiceService.insertInvoiceItems(scontrino.getIdscontrino(), prodottiNelCarrello);

		JOptionPane.showMessageDialog(this, "Scontrino generato con successo!", "Operazione riuscita",
				JOptionPane.INFORMATION_MESSAGE);
		resettaCarrello();
	}

	private void resettaCarrello() {
		prodottiNelCarrello.clear();
		tableModel.setRowCount(0);
	}
}
