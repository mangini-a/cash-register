package ui.vspanel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;

@SuppressWarnings("serial")
public class DettagliScontrinoDialog extends JDialog {
	
	private JTable dettagliTable;
    private DefaultTableModel tableModel;
    private DataService dataService;

	public DettagliScontrinoDialog(Frame owner, List<VocescontrinoRecord> dettagliScontrino) {
		super(owner, "Dettagli scontrino", true);
		this.dataService = new DataService();
        setLayout(new BorderLayout());

        // Crea la tabella per rappresentare le linee di dettaglio di uno scontrino
        tableModel = new DefaultTableModel(new Object[]{"Prodotto", "Quantità", "Prezzo (€)"}, 0) {
        	@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        dettagliTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dettagliTable);

        // Popola la tabella con le linee di dettaglio dello scontrino, connettendosi al database
        for (VocescontrinoRecord dettaglio : dettagliScontrino) {
            ProdottoRecord prodotto = dataService.getProdottoById(dettaglio.getIdprodotto());
            if (prodotto != null) {
            	tableModel.addRow(new Object[]{prodotto.getNome(), dettaglio.getQtaprodotto(), prodotto.getPrezzo()});
            }
        }
        
        // Chiude la connessione al database
        dataService.close();

        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
	}
}
