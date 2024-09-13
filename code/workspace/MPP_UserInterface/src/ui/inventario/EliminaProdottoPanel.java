package ui.inventario;

import javax.swing.*;

import jooq.DataService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EliminaProdottoPanel extends JPanel {

	private JComboBox<String> prodottoComboBox;

	public EliminaProdottoPanel(GestisciInventarioPanel gestisciInventarioPanel) {
		setLayout(new GridLayout(2, 2, 5, 5));

		JLabel lblProdotto = new JLabel("Seleziona prodotto:");
		add(lblProdotto);

		prodottoComboBox = new JComboBox<>(new String[] { "Product 1", "Product 2", "Product 3" }); // actual product //
																									// names
		add(prodottoComboBox);

		JButton btnEliminaProdotto = new JButton("Delete Product");
		add(btnEliminaProdotto);

		btnEliminaProdotto.addActionListener(e -> eliminaProdotto());
	}

	private void eliminaProdotto() {
		String prodotto = (String) prodottoComboBox.getSelectedItem();

		// Confirm the deletion with the user
		int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare " + prodotto + "?",
				"Conferma eliminazione", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			// Handle the product deletion using the underlying business logic
			DataService dataService = new DataService();
			dataService.eliminaProdotto(nomeProdotto, prezzoProdotto, qtaProdotto, descrizioneProdotto);

			// Show a confirmation message
			JOptionPane.showMessageDialog(this, "Prodotto eliminato con successo!", "Operazione riuscita",
					JOptionPane.INFORMATION_MESSAGE);

			// Optionally, update the combo box to reflect the deletion
			prodottoComboBox.removeItem(prodotto);
		}
	}
}
