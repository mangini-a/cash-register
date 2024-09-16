package ui.scontrini;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import jooq.DataService;
import jooq.InvoiceService;
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;
import jooq.impl.InvoiceServiceImpl;

@SuppressWarnings("serial")
public class RegistraScontrinoPanel extends JPanel {

	private JTextField prodottoField;
	private JSpinner qtaProdottoSpinner;
	private JTable carrelloTable;
	private DefaultTableModel tableModel;
	private JButton aggiungiAlCarrelloButton;
	private JButton generaScontrinoButton;
	private List<ProdottoRecord> prodotti;
	private List<VocescontrinoRecord> vociScontrino;
	private DataService dataService;
	private InvoiceService invoiceService;

	public RegistraScontrinoPanel() {
		this.dataService = new DataService();
		this.invoiceService = new InvoiceServiceImpl(dataService);
		setLayout(new BorderLayout());

		// Carica i prodotti dal database
		prodotti = dataService.getProdotti();
		vociScontrino = new ArrayList<>();

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

		// Crea lo spinner per selezionare la quantità di prodotto desiderata dal
		// cliente
		qtaProdottoSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

		// Crea il pulsante "Aggiungi al carrello"
		aggiungiAlCarrelloButton = new JButton("Aggiungi al carrello", new ImageIcon("../img/cart-icon.png"));
		aggiungiAlCarrelloButton.addActionListener(e -> aggiungiAlCarrello());

		// Crea la tabella che rappresenta il carrello della spesa
		tableModel = new DefaultTableModel(new Object[] { "Prodotto", "Quantità", "Prezzo (€)" }, 0);
		carrelloTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(carrelloTable);

		// Crea il pulsante "Genera scontrino"
		generaScontrinoButton = new JButton("Genera scontrino", new ImageIcon("../img/invoicegen-icon.png"));
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

	/*
	 * Restituisce una lista di nomi di prodotti che matchano con il testo in input
	 * per la funzione di auto-completamento.
	 */
	private List<String> getProductSuggestions(String text) {
		List<String> suggestions = new ArrayList<>();
		for (ProdottoRecord prodotto : prodotti) {
			if (prodotto.getNome().toLowerCase().startsWith(text.toLowerCase())) {
				suggestions.add(prodotto.getNome());
			}
		}
		return suggestions;
	}

	/*
	 * Aggiunge un prodotto al carrello qualora la quantità a disposizione dello
	 * stock sia sufficiente: in tal caso, ne aggiorna la quantità disponibile ad inventario.
	 */
	private void aggiungiAlCarrello() {
		// Estrae il nome del prodotto aggiunto al carrello, così come la quantità in
		// cui è stato richiesto
		String nomeProdotto = prodottoField.getText();
		int qtaProdotto = (int) qtaProdottoSpinner.getValue();

		ProdottoRecord prodotto = getProductByName(nomeProdotto);
		if (prodotto != null) {
			// Se la quantità richiesta non eccede quella disponibile a stock
			if (qtaProdotto <= prodotto.getQtadisponibile()) {
				VocescontrinoRecord voceScontrino = createVoceScontrinoRecord(prodotto, qtaProdotto);
				vociScontrino.add(voceScontrino);
				tableModel.addRow(new Object[] { prodotto.getNome(), qtaProdotto, prodotto.getPrezzo() * qtaProdotto });
				// Aggiorna la quantità disponibile ad inventario successivamente all'aggiunta al carrello
				dataService.aggiornaQtaProdotto(prodotto.getIdprodotto(), prodotto.getQtadisponibile() - qtaProdotto);
				// Resetta i campi di selezione
				prodottoField.setText("");
				qtaProdottoSpinner.setValue(1);
			} else {
				JOptionPane.showMessageDialog(this, "La quantità disponibile non è sufficiente: ne restano" + prodotto.getQtadisponibile() + ".", "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Prodotto non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * Recupera una entry della tabella Prodotto sulla base del nome del prodotto in
	 * questione.
	 */
	private ProdottoRecord getProductByName(String nomeProdotto) {
		for (ProdottoRecord prodotto : prodotti) {
			if (prodotto.getNome().equalsIgnoreCase(nomeProdotto)) {
				return prodotto;
			}
		}
		return null;
	}
	
	/*
	 * Crea un'istanza di VocescontrinoRecord, senza inserirla nel database (non si conosce ancora IdScontrino).
	 */
	private VocescontrinoRecord createVoceScontrinoRecord(ProdottoRecord prodotto, int qtaProdotto) {
		VocescontrinoRecord voceScontrino = new VocescontrinoRecord();
        voceScontrino.setIdprodotto(prodotto.getIdprodotto());
        voceScontrino.setQtaprodotto(qtaProdotto);
        return voceScontrino;
	}

	/*
	 * Genera uno scontrino, delegando il compito ad un'istanza della classe che
	 * implementa InvoiceService.
	 */
	private void generaScontrino() {
		if (vociScontrino.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Il carrello della spesa è vuoto.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Delega la generazione dello scontrino all'istanza della classe che implementa
		// InvoiceService.
		float prezzoTotale = calcolaPrezzoTotale();
		invoiceService.generaScontrino(vociScontrino, prezzoTotale);

		JOptionPane.showMessageDialog(this, "Scontrino generato con successo!", "Operazione riuscita",
				JOptionPane.INFORMATION_MESSAGE);
		resettaCarrello();
	}

	/*
	 * Calcola il prezzo complessivo di uno scontrino, recuperando quantità
	 * acquistata e prezzo dei prodotti che ne fanno parte.
	 */
	private float calcolaPrezzoTotale() {
		float prezzoTotale = 0;
		for (VocescontrinoRecord voceScontrino : vociScontrino) {
			ProdottoRecord prodotto = getProductById(voceScontrino.getIdprodotto());
			prezzoTotale += prodotto.getPrezzo() * voceScontrino.getQtaprodotto();
		}
		return prezzoTotale;
	}

	/*
	 * Recupera un record della tabella Prodotto sulla base del proprio ID.
	 */
	private ProdottoRecord getProductById(int idProdotto) {
		for (ProdottoRecord prodotto : prodotti) {
			if (prodotto.getIdprodotto() == idProdotto) {
				return prodotto;
			}
		}
		return null;
	}

	/*
	 * Resetta sia il carrello che la relativa rappresentazione tabulare.
	 */
	private void resettaCarrello() {
		vociScontrino.clear();
		tableModel.setRowCount(0);
	}
}
