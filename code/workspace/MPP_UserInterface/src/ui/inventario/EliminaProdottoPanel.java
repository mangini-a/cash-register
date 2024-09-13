package ui.inventario;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;

@SuppressWarnings("serial")
public class EliminaProdottoPanel extends JPanel {

	private JComboBox<String> prodottiComboBox;
	private DataService dataService;

	public EliminaProdottoPanel(GestisciInventarioPanel gestisciInventarioPanel) {
		this.dataService = new DataService();
		setLayout(new GridLayout(2, 2, 5, 5));

		JLabel lblProdotto = new JLabel("Seleziona prodotto:");
		add(lblProdotto);

		// Carica le descrizioni dei prodotti nella combo box
		String[] descrizioniProdotti = caricaDescrizioniProdotti();
		prodottiComboBox = new JComboBox<>(descrizioniProdotti);
		add(prodottiComboBox);

		JButton btnEliminaProdotto = new JButton("Elimina Prodotto");
		add(btnEliminaProdotto);

		btnEliminaProdotto.addActionListener(e -> eliminaProdotto());
	}

	private String[] caricaDescrizioniProdotti() {
		// Recupera le descrizioni dei prodotti dal database usando jOOQ
		List<ProdottoRecord> prodotti = dataService.getProdotti();
		String[] descrizioniProdotti = new String[prodotti.size()];
		for (int i = 0; i < prodotti.size(); i++) {
			descrizioniProdotti[i] = prodotti.get(i).getDescrizione();
		}
		return descrizioniProdotti;
	}

	private void eliminaProdotto() {
		// Recupera il prodotto selezionato dall'utente attraverso la combo box
		String descrizioneProdotto = (String) prodottiComboBox.getSelectedItem();

		// Richiede conferma all'utente prima di rimuovere effettivamente il prodotto dalla relativa tabella del DB
		int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler rimuovere " + descrizioneProdotto + "?",
				"Conferma eliminazione", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			// Gestisci la rimozione del prodotto in questione mediante la Business Logic (classe DataService)
			int rowsAffected = dataService.eliminaProdotto(descrizioneProdotto);

			if (rowsAffected > 0) {
				System.out.println(descrizioneProdotto + " rimosso con successo.");

				// Mostra a video un messaggio di conferma
				JOptionPane.showMessageDialog(this, "Prodotto rimosso con successo!", "Operazione riuscita",
						JOptionPane.INFORMATION_MESSAGE);

				// Aggiorna la combo box per rendere palese la rimozione
				prodottiComboBox.removeItem(descrizioneProdotto);
			} else {
				JOptionPane.showMessageDialog(this, "Non è stato possibile rimuovere il prodotto.", "Errore", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
