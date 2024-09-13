package ui.inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;

import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class VisualizzaProdottiPanel extends JPanel {

	private JTable productTable;
	private DefaultTableModel tableModel;
	private DataService dataService;

	public VisualizzaProdottiPanel(GestisciInventarioPanel gestisciInventarioPanel) {
		this.dataService = new DataService();
		setLayout(new BorderLayout());

		// Create a table model with columns for product details
		tableModel = new DefaultTableModel(new Object[] { "ID", "Nome", "Prezzo", "Quantità", "Descrizione" }, 0);
		productTable = new JTable(tableModel);

		// Add the table to a scroll pane
		JScrollPane scrollPane = new JScrollPane(productTable);
		add(scrollPane, BorderLayout.CENTER);

		// Load products into the table
		caricaProdotti();
	}

	private void caricaProdotti() {
		// Clear existing rows in the table
		tableModel.setRowCount(0);

		// Retrieve products from the database using jOOQ
		List<ProdottoRecord> prodotti = dataService.getProdotti();

		// Add each product to the table
		for (ProdottoRecord prodotto : prodotti) {
			tableModel.addRow(new Object[] { prodotto.getIdprodotto(), prodotto.getNome(), prodotto.getPrezzo(),
					prodotto.getQtadisponibile(), prodotto.getDescrizione() });
		}
	}
}
