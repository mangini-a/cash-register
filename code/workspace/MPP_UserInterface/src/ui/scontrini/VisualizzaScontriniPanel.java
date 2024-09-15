package ui.scontrini;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import dto.ProdottoNelCarrelloDTO;
import jooq.DataService;
import jooq.generated.tables.records.ScontrinoRecord;

@SuppressWarnings("serial")
public class VisualizzaScontriniPanel extends JPanel {

	private JTable scontriniTable;
	private DefaultTableModel tableModel;
	private DataService dataService;

	public VisualizzaScontriniPanel() {
		this.dataService = new DataService();
		setLayout(new BorderLayout());

		// Crea un template tabulare per visualizzare i dettagli di ciascuno scontrino
		tableModel = new DefaultTableModel(new Object[] { "Data e ora di emissione", "Totale complessivo (€)" }, 0);
		scontriniTable = new JTable(tableModel);

		// Aggiunge la tabella ad un pannello che si possa scorrere verticalmente
		JScrollPane scrollPane = new JScrollPane(scontriniTable);

		// Carica gli scontrini dal database
		caricaScontrini();
		
		// Aggiunge un mouse listener per gestire la selezione di una entry della tabella
        scontriniTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedRow = scontriniTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int idScontrino = (int) scontriniTable.getValueAt(selectedRow, 0);
                        mostraDettagliScontrino(idScontrino);
                    }
                }
            }
        });

		// Avvia un task in background per aggiornare periodicamente la tabella (scontriniTable)
		avviaBackgroundTask();
		
		add(scrollPane, BorderLayout.CENTER);
	}

	private void caricaScontrini() {
		// Resetta le entry esistenti nella tabella
		tableModel.setRowCount(0);

		// Recupera gli scontrini dal database per mezzo di jOOQ
		List<ScontrinoRecord> scontrini = dataService.getScontrini();

		// Aggiungi ciascuno degli scontrini alla tabella
		for (ScontrinoRecord scontrino : scontrini) {
			tableModel.addRow(new Object[] { scontrino.getDataora(), scontrino.getPrezzotot() });
		}
		
		List<InvoiceDTO> invoices = invoiceService.getInvoices();

        // Populate the table model with invoices
        tableModel.setRowCount(0); // Clear the table
        for (InvoiceDTO invoice : invoices) {
            tableModel.addRow(new Object[]{invoice.getId(), invoice.getDate(), invoice.getTotalPrice()});
        }
	}

	private void mostraDettagliScontrino(int idScontrino) {
		// TODO Auto-generated method stub
		
	}
	
	private void avviaBackgroundTask() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (true) {
					Thread.sleep(5000); // Effettua l'operazione di polling ogni 5 secondi
					caricaScontrini();
				}
			}
		};
		worker.execute();
	}
}
