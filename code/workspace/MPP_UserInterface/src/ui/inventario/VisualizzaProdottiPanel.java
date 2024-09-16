package ui.inventario;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;

@SuppressWarnings("serial")
public class VisualizzaProdottiPanel extends JPanel {

	private JTable prodottiTable;
	private DefaultTableModel tableModel;
	private DataService dataService;

	public VisualizzaProdottiPanel(GestioneInventarioPanel gestisciInventarioPanel) {
		this.dataService = new DataService();
		setLayout(new BorderLayout());

		// Crea un template tabulare per visualizzare i dettagli di ciascun prodotto
		tableModel = new DefaultTableModel(new Object[] { "Nome", "Prezzo (€)", "Quantità disponibile", "Descrizione" }, 0) {
			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
		};
		prodottiTable = new JTable(tableModel);

		// Aggiungi la tabella ad un pannello che si possa scorrere verticalmente
		JScrollPane scrollPane = new JScrollPane(prodottiTable);
		add(scrollPane, BorderLayout.CENTER);

		// Carica i prodotti dal database
		caricaProdotti();

		// Avvia un task in background per aggiornare periodicamente la tabella (prodottiTable)
		avviaBackgroundTask();
	}

	private void caricaProdotti() {
		// Resetta le entry esistenti nella tabella
		tableModel.setRowCount(0);

		// Recupera i prodotti dal database per mezzo di jOOQ
		List<ProdottoRecord> prodotti = dataService.getProdotti();

		// Aggiungi ciascuno dei prodotti alla tabella
		for (ProdottoRecord prodotto : prodotti) {
			tableModel.addRow(new Object[] { prodotto.getNome(), prodotto.getPrezzo(), prodotto.getQtadisponibile(),
					prodotto.getDescrizione() });
		}
	}

	private void avviaBackgroundTask() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (true) {
					Thread.sleep(5000); // Effettua l'operazione di polling ogni 5 secondi
					caricaProdotti();
				}
			}
		};
		worker.execute();
	}
}
