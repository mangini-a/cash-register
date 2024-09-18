package ui.vspanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
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
        
        // Mostra le righe della tabella colorate in modo alternato
        dettagliTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        
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
