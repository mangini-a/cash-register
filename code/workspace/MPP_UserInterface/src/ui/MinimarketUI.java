package ui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jooq.DataService;

public class MinimarketUI extends JFrame {

	private JPanel contentPane;
	private JTextField nomeProdottoField;
	private JTextField prezzoProdottoField;
	private JTextField qtaProdottoField;
	private JTextField descrizioneProdottoField;
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MinimarketUI frame = new MinimarketUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MinimarketUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5, 2, 5, 5));

		JLabel lblNomeProdotto = new JLabel("Nome:");
		contentPane.add(lblNomeProdotto);

		nomeProdottoField = new JTextField();
		contentPane.add(nomeProdottoField);
		nomeProdottoField.setColumns(10);

		JLabel lblPrezzoProdotto = new JLabel("Prezzo:");
		contentPane.add(lblPrezzoProdotto);

		prezzoProdottoField = new JTextField();
		contentPane.add(prezzoProdottoField);
		prezzoProdottoField.setColumns(10);

		JLabel lblQtaProdotto = new JLabel("Quantità:");
		contentPane.add(lblQtaProdotto);

		qtaProdottoField = new JTextField();
		contentPane.add(qtaProdottoField);
		qtaProdottoField.setColumns(10);

		JLabel lblDescrizioneProdotto = new JLabel("Descrizione:");
		contentPane.add(lblDescrizioneProdotto);

		descrizioneProdottoField = new JTextField();
		contentPane.add(descrizioneProdottoField);
		descrizioneProdottoField.setColumns(10);

		JButton btnAggiungiProdotto = new JButton("Aggiungi Prodotto");
		contentPane.add(btnAggiungiProdotto);

		JButton btnEsci = new JButton("Esci");
		contentPane.add(btnEsci);

		// Add action listeners for buttons
		btnAggiungiProdotto.addActionListener(e -> aggiungiProdotto());
		btnEsci.addActionListener(e -> System.exit(0));
	}

	private void aggiungiProdotto() {
		String nomeProdotto = nomeProdottoField.getText();
		String prezzoProdottoText = prezzoProdottoField.getText();
		String qtaProdottoText = qtaProdottoField.getText();
		String descrizioneProdotto = descrizioneProdottoField.getText();
		
		// Replace commas with periods to handle both decimal separators
        prezzoProdottoText = prezzoProdottoText.replace(",", ".");

		// Parse product price as float
		float prezzoProdotto = 0.0f;

		try {
			prezzoProdotto = Float.parseFloat(prezzoProdottoText);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Prezzo non valido. Inserisci un numero positivo.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the price is invalid
		}

		// Parse product quantity as integer
		int qtaProdotto = 0;

		try {
			qtaProdotto = Integer.parseInt(qtaProdottoText);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Quantità non valida. Inserisci un numero intero positivo.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit the method if the quantity is invalid
		}

		// Add your business logic to handle the product addition
		DataService dataService = new DataService();
		dataService.aggiungiProdotto(nomeProdotto, prezzoProdotto, qtaProdotto, descrizioneProdotto);

		// Show a confirmation message
		JOptionPane.showMessageDialog(this, "Prodotto aggiunto con successo!", "Esito positivo",
				JOptionPane.INFORMATION_MESSAGE);

		// Clear the fields after adding the product
		nomeProdottoField.setText("");
		prezzoProdottoField.setText("");
		qtaProdottoField.setText("");
		descrizioneProdottoField.setText("");
	}
}
