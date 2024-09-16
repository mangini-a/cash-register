package ui.scontrini;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import jooq.DataService;
import jooq.InvoiceService;
import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;
import jooq.impl.InvoiceServiceImpl;

@SuppressWarnings("serial")
public class VisualizzaScontriniPanel extends JPanel {

	private JTable scontriniTable;
	private DefaultTableModel tableModel;
	private DataService dataService;
	private InvoiceService invoiceService;

	public VisualizzaScontriniPanel() {
		this.dataService = new DataService();
		this.invoiceService = new InvoiceServiceImpl(dataService);
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

	/*
	 * Recupera tutti gli scontrini dal database e popola il template tabulare con i rispettivi dati.
	 */
	private void caricaScontrini() {
		
		// Recupera gli scontrini dal database per mezzo di jOOQ
		List<ScontrinoRecord> scontrini = dataService.getScontrini();
		
		// Popola il template tabulare con gli scontrini
		tableModel.setRowCount(0);
		for (ScontrinoRecord scontrino : scontrini) {
			tableModel.addRow(new Object[] { scontrino.getDataora(), scontrino.getPrezzotot() });
		}
	}

	/*
	 * Visualizza i dettagli (ossia, le voci) dello scontrino selezionato in una finestra di dialogo.
	 */
	private void mostraDettagliScontrino(int idScontrino) {
		List<VocescontrinoRecord> dettagliScontrino = invoiceService.getDettagliScontrino(idScontrino);
        if (dettagliScontrino != null && !dettagliScontrino.isEmpty()) {
        	Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            new DettagliScontrinoDialog(parentFrame, dettagliScontrino);
        } else {
            JOptionPane.showMessageDialog(this, "Non sono state trovate linee di dettaglio per lo scontrino selezionato.", "Errore", JOptionPane.ERROR_MESSAGE);
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
