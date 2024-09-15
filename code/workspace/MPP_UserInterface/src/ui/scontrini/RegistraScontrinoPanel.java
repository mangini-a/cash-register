package ui.scontrini;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import dto.ProdottoNelCarrelloDTO;
import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;

@SuppressWarnings("serial")
public class RegistraScontrinoPanel extends JPanel {

	private JTextField prodottoField;
	private JSpinner qtaProdottoSpinner;
	private JTable carrelloTable;
	private DefaultTableModel tableModel;
	private JButton aggiungiAlCarrelloButton;
	private JButton generaScontrinoButton;
	private List<ProdottoRecord> prodotti;
	private List<ProdottoNelCarrelloDTO> prodottiNelCarrello;
	private DataService dataService;

	public RegistraScontrinoPanel() {
		this.dataService = new DataService();
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
		carrelloTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(carrelloTable);

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
		// Estrae il nome del prodotto aggiunto al carrello, così come la quantità in cui è stato richiesto
		String nomeProdotto = prodottoField.getText();
		int qtaProdotto = (int) qtaProdottoSpinner.getValue();

		ProdottoRecord prodotto = getProductByName(nomeProdotto);
		if (prodotto != null) {
			// Se la quantità richiesta non eccede quella disponibile a stock
			if (qtaProdotto <= prodotto.getQtadisponibile()) {
				// Crea un'istanza di ProdottoNelCarrelloDTO, aggiungendola alla relativa lista (che rappresenta il carrello)
				ProdottoNelCarrelloDTO prodottoNelCarrello = new ProdottoNelCarrelloDTO(prodotto.getIdprodotto(), nomeProdotto, prodotto.getPrezzo(), qtaProdotto);
				prodottiNelCarrello.add(prodottoNelCarrello);
				tableModel.addRow(new Object[] { prodotto.getNome(), qtaProdotto, prodotto.getPrezzo() * qtaProdotto });
				// Resetta i campi di selezione
				prodottoField.setText("");
				qtaProdottoSpinner.setValue(1);
			} else {
				JOptionPane.showMessageDialog(this, "La quantità disponibile non è sufficiente.", "Errore", JOptionPane.ERROR_MESSAGE);
			}
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
		
		// Delega la generazione dello scontrino all'istanza di DataService
        float prezzoTotale = calcolaPrezzoTotale();
        dataService.inserisciScontrino(prezzoTotale, prodottiNelCarrello);

		JOptionPane.showMessageDialog(this, "Scontrino generato con successo!", "Operazione riuscita",
				JOptionPane.INFORMATION_MESSAGE);
		resettaCarrello();
	}

	/*
	 * Calcola il prezzo complessivo di uno scontrino, recuperando quantità acquistata e prezzo dei prodotti che ne fanno parte.
	 */
	private float calcolaPrezzoTotale() {
		float prezzoTotale = 0;
        for (ProdottoNelCarrelloDTO prodottoNelCarrello : prodottiNelCarrello) {
            prezzoTotale += prodottoNelCarrello.getPrezzo() * prodottoNelCarrello.getQta();
        }
        return prezzoTotale;
	}

	private void resettaCarrello() {
		prodottiNelCarrello.clear();
		tableModel.setRowCount(0);
	}
}
