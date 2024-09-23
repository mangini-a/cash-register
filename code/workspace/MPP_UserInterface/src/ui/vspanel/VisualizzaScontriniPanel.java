package ui.vspanel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import invoices.InvoiceService;
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
	private DataService dataService;
    private InvoiceService invoiceService;

	public VisualizzaScontriniPanel(MainFrame mainFrame, DataService dataService) {
		this.dataService = dataService;
    	this.invoiceService = InvoiceService.getInstance(dataService);
		
		setLayout(new BorderLayout());

		// Aggiunge questa schermata al pannello dei contenuti del frame principale
		mainFrame.getContentPane().add(this, BorderLayout.CENTER);

		// Crea un template tabulare per visualizzare i dettagli di ciascuno scontrino
		// La colonna ID è nascosta e serve per mantenere il valore
		tableModel = new DefaultTableModel(new Object[] { "Data e ora di emissione", "Totale complessivo (€)", "ID" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Impedisce di modificare le celle della tabella
			}
		};
		scontriniTable = new JTable(tableModel);
		
		// Centra il testo in tutte le colonne della tabella
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		scontriniTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		scontriniTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		scontriniTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Nasconde la terza colonna (ID)
        scontriniTable.getColumnModel().getColumn(2).setMinWidth(0);
        scontriniTable.getColumnModel().getColumn(2).setMaxWidth(0);
        scontriniTable.getColumnModel().getColumn(2).setWidth(0);

		// Aggiunge la tabella ad un pannello che si possa scorrere verticalmente
		JScrollPane scrollPane = new JScrollPane(scontriniTable);

		// Carica gli scontrini dal database
		try {
			caricaScontrini();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Aggiunge un mouse listener per gestire la selezione di una entry della tabella
		scontriniTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // Se viene rilevato un doppio click
					int selectedRow = scontriniTable.getSelectedRow();
					if (selectedRow != -1) {
						int idScontrino = (int) scontriniTable.getValueAt(selectedRow, 2);
						mostraDettagliScontrino(idScontrino);
					}
				}
			}
		});

		// Aggiunge il pannello scorrevole su cui è innestata la tabella al centro del frame
		add(scrollPane, BorderLayout.CENTER);
	}
	
	/*
	 * Recupera tutti gli scontrini dal database e popola il template tabulare con i rispettivi dati.
	 * Pubblico in quanto utilizzato anche dalla classe RegistraScontrinoPanel, che si trova in un altro package.
	 */
	public void caricaScontrini() throws SQLException {
        // Recupera tutti gli scontrini dal database
        scontrini = invoiceService.findAll();
        
        // Definisce il formato di timestamp desiderato dall'utente
        SimpleDateFormat displayDateFormat  = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		// Popola il template tabulare con gli scontrini recuperati
		tableModel.setRowCount(0);
		for (ScontrinoRecord scontrino : scontrini) {
			try {
				Timestamp dataOra = Timestamp.valueOf(scontrino.getDataora()); // Converte a timestamp
				dataOra.setTime(dataOra.getTime() + 2 * 60 * 60 * 1000); // Aggiunge 2 ore in millisecondi
				String desiredFormatData = displayDateFormat.format(dataOra); // Converte a stringa
				tableModel.addRow(new Object[] { desiredFormatData, scontrino.getPrezzotot(), scontrino.getIdscontrino() });
			} catch (Exception e) {
				e.printStackTrace(); // In caso di errore nella conversione della data
	            JOptionPane.showMessageDialog(this, "Errore nel formato della data: " + scontrino.getDataora(),
	                                          "Errore di parsing", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// Visualizza le linee di dettaglio dello scontrino selezionato in una finestra di dialogo
	private void mostraDettagliScontrino(int idScontrino) {
		// Recupera le linee di dettaglio dello scontrino dal database
		dettagliScontrino = invoiceService.findLinesByInvoiceId(idScontrino);
		
		// Controlla che ci siano linee di dettaglio da mostrare
		if (dettagliScontrino != null && !dettagliScontrino.isEmpty()) {
			Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
			new DettagliScontrinoDialog(parentFrame, dataService, dettagliScontrino);
		} else {
			JOptionPane.showMessageDialog(this,
					"Non sono state trovate linee di dettaglio per lo scontrino selezionato.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
