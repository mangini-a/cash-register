package ui.gipanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import inventory.ProductService;

import java.util.List;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class GestisciInventarioPanel extends JPanel {

	private JTable prodottiTable;
	private DefaultTableModel tableModel;
	private List<ProdottoRecord> prodotti;
	private DataService dataService;
	private ProductService productService;

	public GestisciInventarioPanel(JFrame mainFrame, DataService dataService) {
		this.dataService = dataService;
		this.productService = ProductService.getInstance(dataService);
		
		setLayout(new BorderLayout());

		// Modello della tabella con le colonne specificate ("Modifica" ed "Elimina" hanno colonne vuote per il titolo)
		tableModel = new DefaultTableModel(
				new Object[] { "Nome", "Descrizione", "Quantità disponibile", "Prezzo (€)", "", "" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 4 || column == 5; // Solo le colonne delle icone sono modificabili
			}
		};

		// Crea la JTable con il modello
		prodottiTable = new JTable(tableModel);
		
		/* Centra il contenuto di ogni riga della tabella
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < prodottiTable.getColumnCount(); i++) {
		    prodottiTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
		} */

		// Aggiunge un renderer e un editor per la colonna "Modifica"
		TableColumn modificaColonna = prodottiTable.getColumnModel().getColumn(4);
		modificaColonna.setCellRenderer(new IconRenderer("../img/edit.png"));
		modificaColonna.setCellEditor(new IconEditor(new JButton(new ImageIcon("../img/edit.png"))));
		modificaColonna.setMaxWidth(40); // Imposta una larghezza fissa per la colonna dell'icona
		modificaColonna.setMinWidth(40);

		// Aggiunge un renderer e un editor per la colonna "Elimina"
		TableColumn eliminaColonna = prodottiTable.getColumnModel().getColumn(5);
		eliminaColonna.setCellRenderer(new IconRenderer("../img/bin.png"));
		eliminaColonna.setCellEditor(new IconEditor(new JButton(new ImageIcon("../img/bin.png"))));
		eliminaColonna.setMaxWidth(40); // Imposta una larghezza fissa per la colonna dell'icona
		eliminaColonna.setMinWidth(40);

		// Aggiunge la tabella ad un pannello scorrevole
		JScrollPane scrollPane = new JScrollPane(prodottiTable);
		add(scrollPane, BorderLayout.CENTER);

		// Aggiunge il pulsante verde arrotondato nella parte superiore
		JPanel topPanel = new JPanel(new GridBagLayout()); // Usa GridBagLayout per centrare il pulsante
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0); // Margini per separare il pulsante dai bordi
		gbc.anchor = GridBagConstraints.CENTER; // Centra il pulsante

		// Crea il pulsante verde arrotondato
		JButton greenButton = new JButton("Aggiungi un nuovo prodotto");
		greenButton.setIcon(new ImageIcon("../img/add.png")); // Imposta l'icona del "+"

		// Imposta il colore di sfondo verde
		greenButton.setBackground(new Color(76, 175, 80));

		// Imposta il colore del testo bianco
		greenButton.setForeground(Color.WHITE);

		// Rende il pulsante arrotondato utilizzando un UI personalizzato
		greenButton.setUI(new RoundedButtonUI()); // Applica l'interfaccia personalizzata per rendere il pulsante
													// arrotondato
		greenButton.setFocusPainted(false);

		// Imposta l'icona a sinistra e il testo a destra
		greenButton.setHorizontalAlignment(SwingConstants.LEFT);
		greenButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona
		
		// Aggiunge un ActionListener per gestire i click sul pulsante "Aggiungi un nuovo prodotto"
		greenButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        aggiungiProdotto();
				caricaProdotti();
		    }
		});

		// Aggiunge il pulsante al pannello superiore
		topPanel.add(greenButton, gbc);

		// Aggiunge il pannello superiore al layout
		add(topPanel, BorderLayout.NORTH);

		// Carica i prodotti dal database
		caricaProdotti();
	}

	/*
	 * Recupera i prodotti dal database e li aggiunge alla tabella. 
	 * Per ciascuna riga di quest'ultima prevede anche un pulsante per la modifica ed uno per la rimozione.
	 * Pubblico in quanto utilizzato anche dalla classe RegistraScontrinoPanel, che si trova in un altro package.
	 */
	public void caricaProdotti() {
		try {
			// Resetta le righe della tabella
			tableModel.setRowCount(0);

			// Recupera tutti i prodotti dal database
			prodotti = productService.findAll();
			
			// Aggiungi ciascuno dei prodotti alla tabella, corredato da due pulsanti per la modifica e la rimozione
			for (ProdottoRecord prodotto : prodotti) {
				tableModel.addRow(new Object[] { prodotto.getNome(), prodotto.getDescrizione(),
						prodotto.getQtadisponibile(), prodotto.getPrezzo(),
						new JButton(new ImageIcon("../img/edit.png")), new JButton(new ImageIcon("../img/bin.png")) });
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Caricamento dei prodotti non riuscito.", "Errore",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * Inner class: renderer personalizzato per visualizzare le icone nella quinta e nella sesta colonna.
	 */
	private class IconRenderer extends JButton implements TableCellRenderer {

		public IconRenderer(String iconPath) {
			setIcon(new ImageIcon(iconPath));
			setBorderPainted(false);
			setContentAreaFilled(false);
			setFocusPainted(false);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return this;
		}
	}

	/*
	 * Inner class: editor personalizzato per gestire i click sulle icone.
	 */
	private class IconEditor extends DefaultCellEditor {
		private JButton button;

		public IconEditor(JButton button) {
			super(new JCheckBox());
			this.button = button;
			this.button.setOpaque(true);
			this.button.setBorderPainted(false);
			this.button.setContentAreaFilled(false);
			this.button.setFocusPainted(false);
			this.button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (column == 4) { // Al click della colonna "Modifica"
				// Recupera gli attuali dettagli del prodotto selezionato dall'utente
				String nomeAttuale = (String) tableModel.getValueAt(row, 0);
				String descrizioneAttuale = (String) tableModel.getValueAt(row, 1);
				int qtaAttuale = (Integer) tableModel.getValueAt(row, 2);
				float prezzoAttuale = (Float) tableModel.getValueAt(row, 3);
				
				// Passa i dati attuali del prodotto al metodo modificaProdotto
		        modificaProdotto(nomeAttuale, descrizioneAttuale, qtaAttuale, prezzoAttuale);
				caricaProdotti();
			} else if (column == 5) { // Al click della colonna "Elimina"
				// Procede alla rimozione del prodotto dal database previa conferma dell'utente
				eliminaProdotto(row);
				caricaProdotti();
			}
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return button;
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}

	/*
	 * Mostra la finestra di dialogo preposta all'inserimento di un nuovo prodotto nel database.
	 */
	private void aggiungiProdotto() {
		JFrame parentFrame = (JFrame) SwingUtilities.windowForComponent(this);
		AggiungiProdottoDialog dialog = new AggiungiProdottoDialog(parentFrame, dataService);
		dialog.setVisible(true);
	}

	/*
	 * Mostra la finestra di dialogo preposta alla modifica di un prodotto già contenuto nel database.
	 */
	private void modificaProdotto(String nomeAttuale, String descrizioneAttuale, int qtaAttuale,
			float prezzoAttuale) {
		JFrame parentFrame = (JFrame) SwingUtilities.windowForComponent(this);
		ModificaProdottoDialog dialog = new ModificaProdottoDialog(parentFrame, dataService, nomeAttuale, descrizioneAttuale,
				qtaAttuale, prezzoAttuale);
		dialog.setVisible(true);
	}

	/*
	 * Elimina dalla relativa tabella del database il prodotto selezionato dall'utente.
	 */
	private void eliminaProdotto(int row) {
		// Recupera il nome del prodotto selezionato dall'utente
		String nomeProdotto = (String) tableModel.getValueAt(row, 0);

		// Richiede conferma all'utente prima di procedere all'eliminazione
		int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler rimuovere: " + nomeProdotto + "?",
				"Conferma eliminazione", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				// Rimuove il prodotto dal database
				boolean esito = productService.delete(nomeProdotto);
				
				if (esito) {
					JOptionPane.showMessageDialog(this, "Prodotto rimosso con successo!", "Operazione riuscita",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Non è stato possibile rimuovere il prodotto.", "Errore",
							JOptionPane.ERROR_MESSAGE);
				}

				// Aggiorna la tabella dei prodotti recuperandoli dal database
				caricaProdotti();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Non è stato possibile rimuovere il prodotto.", "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/*
	 * Inner class: interfaccia utente personalizzata per rendere il pulsante "Aggiungi un nuovo prodotto" arrotondato.
	 */
	private class RoundedButtonUI extends BasicButtonUI {
		@Override
		public void installUI(JComponent c) {
			super.installUI(c);
			JButton button = (JButton) c;
			button.setOpaque(false);
			button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Aggiusta i margini interni
		}

		@Override
		public void paint(Graphics g, JComponent c) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			JButton button = (JButton) c;

			// Imposta il colore di sfondo
			g2.setColor(button.getBackground());
			g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 30, 30); // Disegna un rettangolo arrotondato

			// Disegna il testo e l'icona normalmente
			super.paint(g, c);
		}
	}
}
