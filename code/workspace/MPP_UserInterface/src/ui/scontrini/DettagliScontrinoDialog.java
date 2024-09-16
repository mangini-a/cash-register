package ui.scontrini;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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
        tableModel = new DefaultTableModel(new Object[]{"Prodotto", "Quantità", "Prezzo (€)"}, 0);
        dettagliTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dettagliTable);

        // Popola la tabella con le linee di dettaglio dello scontrino
        for (VocescontrinoRecord dettaglio : dettagliScontrino) {
            ProdottoRecord prodotto = dataService.getProdottoById(dettaglio.getIdprodotto());
            if (prodotto != null) {
            	tableModel.addRow(new Object[]{prodotto.getNome(), dettaglio.getQtaprodotto(), prodotto.getPrezzo()});
            }
        }

        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
	}
}
