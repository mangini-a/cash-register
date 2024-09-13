package ui.scontrini;

import javax.swing.*;

import ui.MainFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistraScontrinoPanel extends JPanel {

	private JComboBox<String> prodottoComboBox;
	private JTextField qtaProdottoField;
	private JTextArea carrelloTextArea;

	public RegistraScontrinoPanel(MainFrame mainFrame) {
		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2, 2, 5, 5));

		JLabel lblProdotto = new JLabel("Seleziona prodotto:");
		topPanel.add(lblProdotto);

		prodottoComboBox = new JComboBox<>(new String[] { "Product 1", "Product 2", "Product 3" }); // Replace with
		topPanel.add(prodottoComboBox);

		JLabel lblQtaProdotto = new JLabel("Quantità:");
		topPanel.add(lblQtaProdotto);

		qtaProdottoField = new JTextField();
		topPanel.add(qtaProdottoField);
		qtaProdottoField.setColumns(10);

		JButton btnAggiungiAlCarrello = new JButton("Aggiungi al carrello");
		topPanel.add(btnAggiungiAlCarrello);

		btnAggiungiAlCarrello.addActionListener(e -> aggiungiAlCarrello());

		add(topPanel, BorderLayout.NORTH);

		carrelloTextArea = new JTextArea();
		carrelloTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(carrelloTextArea);
		add(scrollPane, BorderLayout.CENTER);

		JButton btnGeneraScontrino = new JButton("Genera Scontrino");
		add(btnGeneraScontrino, BorderLayout.SOUTH);

		btnGeneraScontrino.addActionListener(e -> generaScontrino());
	}

	private void aggiungiAlCarrello() {
		String prodotto = (String) prodottoComboBox.getSelectedItem();
		String qtaProdottoText = qtaProdottoField.getText();

		// Parse product quantity as integer
		int qtaProdotto = 0;

		try {
			qtaProdotto = Integer.parseInt(qtaProdottoText);
			if (qtaProdotto <= 0) {
				JOptionPane.showMessageDialog(this, "Quantità del prodotto non valida. Inserisci un numero positivo.",
						"Errore", JOptionPane.ERROR_MESSAGE);
				return; // Exit the method if the quantity is not positive
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Quantità del prodotto non valida. Inserisci un numero intero.",
					"Errore", JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the quantity is invalid
		}

		// Add the product to the cart text area
		carrelloTextArea.append(prodotto + " - Quantità: " + qtaProdotto + "\n");

		// Clear the quantity field after adding to the cart
		qtaProdottoField.setText("");
	}

	private void generaScontrino() {
		// Add your business logic to generate the invoice
		System.out.println("Generating invoice...");
		System.out.println(carrelloTextArea.getText());

		// Show a confirmation message
		JOptionPane.showMessageDialog(this, "Scontrino generato con successo!", "Operazione riuscita",
				JOptionPane.INFORMATION_MESSAGE);

		// Clear the cart text area after generating the invoice
		carrelloTextArea.setText("");
	}
}
