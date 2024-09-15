package ui.scontrini;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dto.ProdottoNelCarrelloDTO;

@SuppressWarnings("serial")
public class DettagliScontrinoDialog extends JDialog {
	
	private JTable dettagliTable;
	private DefaultTableModel tableModel;

	public DettagliScontrinoDialog(Frame owner, List<ProdottoNelCarrelloDTO> prodottiNelCarrello, float prezzoTotale) {
		
        super(owner, "Dettagli scontrino", true);
        setLayout(new BorderLayout());

        // Crea la tabella raffigurante i dettagli di uno scontrino
        tableModel = new DefaultTableModel(new Object[]{"Prodotto", "Quantità", "Prezzo (€)"}, 0);
        dettagliTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dettagliTable);

        // Popola la tabella con i prodotti aggiunti al carrello
        for (ProdottoNelCarrelloDTO prodottoNelCarrello : prodottiNelCarrello) {
            tableModel.addRow(new Object[]{prodottoNelCarrello.getNome(), prodottoNelCarrello.getQta(), prodottoNelCarrello.getPrezzo()});
        }

        // Aggiunge un'etichetta per visualizzare il prezzo totale dello scontrino
        JLabel prezzoTotaleLabel = new JLabel("Totale complessivo: " + prezzoTotale);

        // Aggiunge i componenti alla finestra (dialog)
        add(scrollPane, BorderLayout.CENTER);
        add(prezzoTotaleLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
