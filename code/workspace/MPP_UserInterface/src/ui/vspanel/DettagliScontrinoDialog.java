package ui.vspanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
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

	public DettagliScontrinoDialog(Frame owner, DataService dataService, List<VocescontrinoRecord> dettagliScontrino) {
		super(owner, "Dettagli scontrino", true);
		
        setLayout(new BorderLayout());

        // Crea una tabella per rappresentare le linee di dettaglio di uno scontrino
        tableModel = new DefaultTableModel(new Object[]{ "Prodotto", "Quantità", "Prezzo (€)" }, 0) {
        	@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impedisce di modificare le celle della tabella
            }
        };
        dettagliTable = new JTable(tableModel);
        
        // Mostra le righe della tabella colorate in modo alternato
        dettagliTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        
        // Innesta la tabella su un pannello scorrevole
        JScrollPane scrollPane = new JScrollPane(dettagliTable);
        
        // Popola la tabella con le linee di dettaglio dello scontrino
        float totale = 0;
        ProdottoRecord prodotto;
        for (VocescontrinoRecord dettaglio : dettagliScontrino) {
        	try {
        		prodotto = dataService.getProdottoById(dettaglio.getIdprodotto());
        		if (prodotto != null) {
        			tableModel.addRow(new Object[]{ prodotto.getNome(), dettaglio.getQtaprodotto(), prodotto.getPrezzo() });
        			totale += prodotto.getPrezzo() * dettaglio.getQtaprodotto();
        		}
        	} catch (Exception e) {
        		e.printStackTrace();
            }
        }
        
        // Aggiunge in coda una riga che mostra il totale complessivo dello scontrino
        tableModel.addRow(new Object[]{ "TOTALE", "", totale });

        // Posiziona la tabella (innestata su un pannello scorrevole) al centro della finestra di dialogo
        add(scrollPane, BorderLayout.CENTER);
        
        // Assicura che tutti i componenti vengano disposti in modo appropriato
        pack();
        
        // Fa coincidere il centro della finestra con quello della schermata "Visualizza gli scontrini emessi"
        setLocationRelativeTo(owner);
        
        // Mostra questo Dialog 
        setVisible(true);
	}
	
	// Controlla l'indice di ogni riga della tabella ed impostane il colore a seconda dello stesso
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                cell.setBackground(Color.decode("#F1F1F1")); // Righe pari
            } else {
                cell.setBackground(Color.decode("#FFFFFF")); // Righe dispari
            }
            setHorizontalAlignment(JLabel.CENTER);
            return cell;
        }
    }
}
