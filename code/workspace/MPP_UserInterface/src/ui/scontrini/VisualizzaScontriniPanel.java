package ui.scontrini;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

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
		tableModel = new DefaultTableModel(new Object[] { "Data e ora", "Prezzo totale" }, 0);
		scontriniTable = new JTable(tableModel);

		// Aggiungi la tabella ad un pannello che si possa scorrere verticalmente
		JScrollPane scrollPane = new JScrollPane(scontriniTable);
		add(scrollPane, BorderLayout.CENTER);

		// Carica gli scontrini dal database
		caricaScontrini();

		// Avvia un task in background per aggiornare periodicamente la tabella (scontriniTable)
		avviaBackgroundTask();
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
