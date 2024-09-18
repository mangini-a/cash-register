package ui.vspanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import jooq.DataService;
import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;
import ui.MainFrame;

@SuppressWarnings("serial")
public class VisualizzaScontriniPanel extends JPanel {

	private JTable scontriniTable;
	private DefaultTableModel tableModel;
	private List<ScontrinoRecord> scontrini;
	private List<VocescontrinoRecord> dettagliScontrino;

	public VisualizzaScontriniPanel(MainFrame mainFrame) {
		setLayout(new BorderLayout());

		// Aggiunge questa schermata al pannello dei contenuti del frame principale
		mainFrame.getContentPane().add(this, BorderLayout.CENTER);

		// Add your components to the panel
		// ...
		// Crea un template tabulare per visualizzare i dettagli di ciascuno scontrino
		tableModel = new DefaultTableModel(new Object[] { "Data e ora di emissione", "Totale complessivo (€)" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make all cells non-editable
			}
		};
		scontriniTable = new JTable(tableModel);
		
		// Mostra le righe della tabella colorate in modo alternato
        scontriniTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

		// Aggiunge la tabella ad un pannello che si possa scorrere verticalmente
		JScrollPane scrollPane = new JScrollPane(scontriniTable);

		// Carica gli scontrini dal database
		caricaScontrini();

		// Aggiunge un mouse listener per gestire la selezione di una entry della
		// tabella
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

		add(scrollPane, BorderLayout.CENTER);
	}

	
	public void aggiornaScontrini() {
	    caricaScontrini(); // Questo ricarica la tabella con i dati aggiornati
	}

	
	/*
	 * Recupera tutti gli scontrini dal database e popola il template tabulare con i
	 * rispettivi dati.
	 */
	private void caricaScontrini() {
		// Recupera gli scontrini dal database, chiudendo automaticamente la connessione al termine
        try (DataService dataService = new DataService()) {
        	scontrini = dataService.getScontrini();
        } // La connessione viene chiusa qui
		
		// Popola il template tabulare con gli scontrini
		tableModel.setRowCount(0);
		for (ScontrinoRecord scontrino : scontrini) {
			tableModel.addRow(new Object[] { scontrino.getDataora(), scontrino.getPrezzotot() });
		}
	}

	/*
	 * Visualizza le linee di dettaglio dello scontrino selezionato in una finestra di dialogo.
	 */
	private void mostraDettagliScontrino(int idScontrino) {
		// Recupera le linee di dettaglio dello scontrino dal database, chiudendo automaticamente la connessione al termine
		try (DataService dataService = new DataService()) {
			dettagliScontrino = dataService.getDettagliScontrino(idScontrino);
		} // La connessione viene chiusa qui
		
		// Controlla che ci siano linee di dettaglio da mostrare
		if (dettagliScontrino != null && !dettagliScontrino.isEmpty()) {
			Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
			new DettagliScontrinoDialog(parentFrame, dettagliScontrino);
		} else {
			JOptionPane.showMessageDialog(this,
					"Non sono state trovate linee di dettaglio per lo scontrino selezionato.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/*
     *  Controlla l'indice di ogni riga della tabella ed impostane il colore a seconda dello stesso.
     */
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                cell.setBackground(Color.decode("#F1F1F1")); // Righe pari
            } else {
                cell.setBackground(Color.decode("#FFFFFF")); // Righe dispari
            }
            return cell;
        }
    }
}
