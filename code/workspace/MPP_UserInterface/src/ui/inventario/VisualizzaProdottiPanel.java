package ui.inventario;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;

@SuppressWarnings("serial")
public class VisualizzaProdottiPanel extends JPanel {

	private JTable productTable;
	private DefaultTableModel tableModel;
	private DataService dataService;

	public VisualizzaProdottiPanel(GestisciInventarioPanel gestisciInventarioPanel) {
		this.dataService = new DataService();
		setLayout(new BorderLayout());

		// Crea un template tabulare dotato di colonne, atto a visualizzare i dettagli
		// di ciascun prodotto
		tableModel = new DefaultTableModel(new Object[] { "Nome", "Prezzo", "Quantità", "Descrizione" }, 0);
		productTable = new JTable(tableModel);

		// Aggiungi la tabella ad un pannello che si possa scorrere verticalmente
		JScrollPane scrollPane = new JScrollPane(productTable);
		add(scrollPane, BorderLayout.CENTER);

		// Carica i prodotti dal database
		caricaProdotti();
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
}
