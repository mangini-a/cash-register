package ui.inventario;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;
/*
@SuppressWarnings("serial")
public class VisualizzaProdottiPanel extends JPanel {

	public VisualizzaProdottiPanel(GestioneInventarioPanel gestisciInventarioPanel) {
		// Crea un pannello per il pulsante di refresh
		JPanel refreshPanel = new JPanel();
		JButton refreshButton = new JButton(new ImageIcon("../img/refresh-icon.png"));
		refreshButton.addActionListener(e -> caricaProdotti());
		refreshPanel.add(refreshButton);
		add(refreshPanel, BorderLayout.NORTH);
	}

	private void aggiungiAzioniAiPulsanti() {
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			final int index = i;
			// Implementa il pulsante per la modifica del prodotto
			JButton updateButton = new JButton(new ImageIcon("../img/update-icon.png"));
			updateButton.addActionListener(e -> aggiornaProdotto(index));
			tableModel.setValueAt(updateButton, i, 4);
			// Implementa il pulsante per l'eliminazione del prodotto
			JButton deleteButton = new JButton(new ImageIcon("../img/delete-icon.png"));
			deleteButton.addActionListener(e -> eliminaProdotto(index));
			tableModel.setValueAt(deleteButton, i, 5);
		}
	}

	private void aggiornaProdotto(int row) {
		// Recupera i dettagli del prodotto selezionato dall'utente
		String nome = (String) tableModel.getValueAt(row, 0);
		float prezzo = Float.parseFloat((String) tableModel.getValueAt(row, 1));
		int qta = Integer.parseInt((String) tableModel.getValueAt(row, 2));
		String descrizione = (String) tableModel.getValueAt(row, 3);

		// Crea un pannello per l'aggiornamento del prodotto
		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new GridLayout(4, 2));

		JLabel nomeLabel = new JLabel("Nome:");
		JTextField nomeField = new JTextField(nome);
		updatePanel.add(nomeLabel);
		updatePanel.add(nomeField);

		JLabel prezzoLabel = new JLabel("Prezzo (€):");
		JTextField prezzoField = new JTextField(String.valueOf(prezzo));
		updatePanel.add(prezzoLabel);
		updatePanel.add(prezzoField);

		JLabel qtaLabel = new JLabel("Quantità disponibile:");
		JTextField qtaField = new JTextField(String.valueOf(qta));
		updatePanel.add(qtaLabel);
		updatePanel.add(qtaField);

		JLabel descrizioneLabel = new JLabel("Descrizione:");
		JTextField descrizioneField = new JTextField(descrizione);
		updatePanel.add(descrizioneLabel);
		updatePanel.add(descrizioneField);

		// Crea un pannello per i pulsanti di conferma e annullamento
		JPanel buttonPanel = new JPanel();

		JButton confermaButton = new JButton("Conferma");
		confermaButton.addActionListener(e -> {
			// Acquisisce i nuovi parametri inseriti dall'utente
			String nuovoNome = nomeField.getText();
			String nuovoPrezzoText = prezzoField.getText();
			String nuovaQtaText = qtaField.getText();
			String nuovaDescrizione = descrizioneField.getText();

			// Replace commas with periods to handle both decimal separators
			nuovoPrezzoText = nuovoPrezzoText.replace(",", ".");

			// Parse product price as float
			float nuovoPrezzo = 0.0f;

			try {
				nuovoPrezzo = Float.parseFloat(nuovoPrezzoText);
				if (nuovoPrezzo <= 0) {
					JOptionPane.showMessageDialog(this, "Prezzo non valido. Inserisci un numero positivo.", "Errore",
							JOptionPane.ERROR_MESSAGE);
					return; // Exit the method if the price is not positive
				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this, "Prezzo non valido. Inserisci un numero reale.", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the price is invalid
			}

			// Parse product quantity as integer
			int nuovaQta = 0;

			try {
				nuovaQta = Integer.parseInt(nuovaQtaText);
				if (nuovaQta <= 0) {
					JOptionPane.showMessageDialog(this, "Quantità non valida. Inserisci un numero positivo.", "Errore",
							JOptionPane.ERROR_MESSAGE);
					return; // Exit the method if the quantity is not positive
				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this, "Quantità non valida. Inserisci un numero intero.", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the quantity is invalid
			}

			// Handle the product modification using the underlying business logic
			int rowsAffected = dataService.modificaProdotto(nome, nuovoNome, nuovoPrezzo, nuovaQta, nuovaDescrizione);
			if (rowsAffected > 0) {
				// Mostra a video un messaggio di conferma
				JOptionPane.showMessageDialog(this, "Prodotto modificato con successo!", "Operazione riuscita",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Non è stato possibile modificare il prodotto.", "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
			caricaProdotti();
		});

		JButton annullaButton = new JButton("Annulla");
		annullaButton.addActionListener(e -> {
			// Chiudi la finestra di dialogo
			((JDialog) SwingUtilities.getWindowAncestor((JComponent) e.getSource())).dispose();
		});

		buttonPanel.add(confermaButton);
		buttonPanel.add(annullaButton);

		// Crea una finestra di dialogo per l'aggiornamento del prodotto
		JDialog updateDialog = new JDialog();
		updateDialog.setTitle("Modifica prodotto");
		updateDialog.setModal(true);
		updateDialog.getContentPane().add(updatePanel, BorderLayout.CENTER);
		updateDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		updateDialog.pack();
		updateDialog.setLocationRelativeTo(this);
		updateDialog.setVisible(true);
	}
}
*/