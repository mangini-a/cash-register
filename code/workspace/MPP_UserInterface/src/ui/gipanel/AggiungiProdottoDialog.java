package ui.gipanel;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.*;

import inventory.ProductService;
import jooq.DataService;

@SuppressWarnings("serial")
public class AggiungiProdottoDialog extends JDialog {

	private JTextField nomeField;
	private JTextField descrizioneField;
	private JTextField qtaField;
	private JTextField prezzoField;
	private JButton confermaButton;
	private JButton annullaButton;
	private ProductService productService;

	public AggiungiProdottoDialog(JFrame parentFrame, DataService dataService) {
		super(parentFrame, "Aggiungi un nuovo prodotto", true);
		this.productService = ProductService.getInstance(dataService);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// Crea il campo per l'inserimento del nome del prodotto
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		add(new JLabel("Nome:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		nomeField = new JTextField(20);
		add(nomeField, gbc);

		// Crea il campo per l'inserimento della descrizione del prodotto
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Descrizione:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		descrizioneField = new JTextField(20);
		add(descrizioneField, gbc);

		// Crea il campo per l'inserimento della quantità del prodotto
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Quantità:"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		qtaField = new JTextField(20);
		add(qtaField, gbc);
		
		// Crea il campo per l'inserimento del prezzo del prodotto
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Prezzo (€):"), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		prezzoField = new JTextField(20);
		add(prezzoField, gbc);
		
		// Crea un pannello separato per i pulsanti "Conferma" ed "Annulla"
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// Crea il pulsante "Conferma" e lo aggiunge al pannello inferiore
		confermaButton = new JButton("Conferma", new ImageIcon("../img/yes.png"));
		confermaButton.addActionListener(e -> {
			try {
				aggiungiProdotto();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		buttonPanel.add(confermaButton);

		// Crea il pulsante "Annulla" e lo aggiunge al pannello inferiore
		annullaButton = new JButton("Annulla", new ImageIcon("../img/no.png"));
		annullaButton.addActionListener(e -> dispose());
		buttonPanel.add(annullaButton);

		// Aggiunge il pannello inferiore a quello principale
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2; // Occupa ambo le colonne a disposizione
		gbc.anchor = GridBagConstraints.CENTER;
		add(buttonPanel, gbc);

		// Imposta una dimensione fissa per la finestra di dialogo
		setPreferredSize(new Dimension(400, 250));
		setResizable(false);

		// Assicura che tutti i componenti vengano disposti in modo appropriato
		pack();

		// Acquisisce la posizione del frame padre
		Point parentLocation = parentFrame.getLocationOnScreen();

		// Centra la posizione della finestra di dialogo rispetto al frame padre
		int x = parentLocation.x + (parentFrame.getWidth() - getWidth()) / 2;
		int y = parentLocation.y + (parentFrame.getHeight() - getHeight()) / 2;
		setLocation(x, y);
	}

	private void aggiungiProdotto() throws HeadlessException, SQLException {
		// Acquisisce il nome del nuovo prodotto inserito dall'utente nel form
		String nome = nomeField.getText();
		
		// Controlla se il database contenga già un prodotto con lo stesso nome
		if (productService.exists(nome)) {
			JOptionPane.showMessageDialog(this, "Un prodotto con il nome '" + nome + "' è già presente a magazzino.", 
					"Errore", JOptionPane.ERROR_MESSAGE);
			return; // Esce dal metodo se il nome inserito è già presente ad inventario
		}
		
		// Acquisisce le altre informazioni inserite dall'utente nel form
		String prezzoText = prezzoField.getText();
		String qtaText = qtaField.getText();
		String descrizione = descrizioneField.getText();

		// Sostituisce eventuali virgole con dei punti per gestire entrambi i separatori decimali
		prezzoText = prezzoText.replace(",", ".");

		// Ottiene il prezzo del prodotto sotto forma di numero reale
		float prezzo = 0.0f;

		try {
			prezzo = Float.parseFloat(prezzoText);
			if (prezzo <= 0) {
				JOptionPane.showMessageDialog(this, "Prezzo non valido. Inserisci un numero positivo.", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return; // Esce dal metodo se il prezzo risulta negativo o nullo
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Prezzo non valido. Inserisci un numero reale.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Esce dal metodo se il prezzo è costituito da un valore non valido
		}

		// Ottiene la quantità del prodotto sotto forma di numero intero
		int qta = 0;

		try {
			qta = Integer.parseInt(qtaText);
			if (qta <= 0) {
				JOptionPane.showMessageDialog(this, "Quantità non valida. Inserisci un numero positivo.", "Errore",
						JOptionPane.ERROR_MESSAGE);
				return; // Esce dal metodo se la quantità non risulta positiva
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Quantità non valida. Inserisci un numero intero.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Esce dal metodo se la quantità è costituita da un valore non valido
		}

		try {
			// Inserisce un nuovo prodotto nella rispettiva tabella del database
			productService.create(nome, prezzo, qta, descrizione);

			JOptionPane.showMessageDialog(this, "Prodotto aggiunto con successo!", "Operazione riuscita",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Non è stato possibile aggiungere il prodotto.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}

		// Chiude la finestra di dialogo deputata all'inserimento di un nuovo prodotto
		dispose();
	}
}
