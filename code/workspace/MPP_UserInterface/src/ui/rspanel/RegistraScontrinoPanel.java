package ui.rspanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import inventory.ProductService;
import invoices.InvoiceService;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;
import ui.MainFrame;
import ui.gipanel.GestisciInventarioPanel;
import ui.vspanel.VisualizzaScontriniPanel;

@SuppressWarnings("serial")
public class RegistraScontrinoPanel extends JPanel {
	 
	private MainFrame mainFrame;

    private static final Logger logger = Logger.getLogger(RegistraScontrinoPanel.class.getName());

    private JTextField prodottoField;
    private JSpinner qtaProdottoSpinner;
    private JTable carrelloTable;
    private DefaultTableModel tableModel;
    private JButton aggiungiAlCarrelloButton;
    private JButton generaScontrinoButton;
    private List<ProdottoRecord> prodotti;
    private List<VocescontrinoRecord> vociScontrino;
    private JPopupMenu suggestionPopup;
    private DataService dataService;
    private InvoiceService invoiceService;
	private ProductService productService;

    public RegistraScontrinoPanel(MainFrame mainFrame, DataService dataService) throws SQLException {
    	this.mainFrame = mainFrame;
    	
    	this.dataService = dataService;
    	this.invoiceService = InvoiceService.getInstance(dataService);
		this.productService = ProductService.getInstance(dataService);
    	
        setLayout(new BorderLayout());

        // Aggiunge questa schermata al pannello dei contenuti del frame principale
        mainFrame.getContentPane().add(this, BorderLayout.CENTER);

        // Recupera tutti i prodotti dal database
     	prodotti = productService.findAll();
        vociScontrino = new ArrayList<>();

        // Crea il campo di testo per la selezione dei prodotti (con auto-completamento)
        prodottoField = new JTextField(20);

        // Inizializza il JPopupMenu per i suggerimenti
        suggestionPopup = new JPopupMenu();

        // Ascoltatore per gestire l'input e mostrare il menu a discesa
        prodottoField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = prodottoField.getText();
                if (!text.isEmpty()) {
                    List<String> suggestions = getProductSuggestions(text);
                    if (!suggestions.isEmpty()) {
                        showSuggestionPopup(suggestions); // Mostra il pop-up con i suggerimenti
                    } else {
                        suggestionPopup.setVisible(false); // Nascosto se non ci sono suggerimenti
                    }
                } else {
                    suggestionPopup.setVisible(false); // Nascosto se il campo è vuoto
                }
            }
        });

        // Crea lo spinner per selezionare la quantità di prodotto desiderata dal cliente
        qtaProdottoSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

        // Crea il pulsante "Aggiungi al carrello"
        aggiungiAlCarrelloButton = new JButton("Aggiungi al carrello", new ImageIcon("../img/buy-icon.png"));
        aggiungiAlCarrelloButton.addActionListener(e -> aggiungiAlCarrello());

        // Crea la tabella che rappresenta il carrello della spesa
        tableModel = new DefaultTableModel(new Object[] { "Prodotto", "Quantità", "Prezzo (€)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impedisce di modificare le celle della tabella
            }
        };
        carrelloTable = new JTable(tableModel);
        
        // Mostra le righe della tabella colorate in modo alternato
        carrelloTable.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        // Posiziona la tabella che rappresenta i prodotti nel carrello della spesa su un pannello scorrevole
        JScrollPane scrollPane = new JScrollPane(carrelloTable);

        // Crea il pulsante "Genera scontrino"
        generaScontrinoButton = new JButton("Genera scontrino", new ImageIcon("../img/ok-icon.png"));
        generaScontrinoButton.addActionListener(e -> generaScontrino());

        // Aggiunge alla sezione superiore il campo di testo per selezionare il prodotto (con etichetta),
        // lo spinner per indicarne la quantità (con etichetta) ed il pulsante "Aggiungi al carrello".
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Prodotto:"));
        topPanel.add(prodottoField);
        topPanel.add(new JLabel("Quantità:"));
        topPanel.add(qtaProdottoSpinner);
        topPanel.add(aggiungiAlCarrelloButton);

        // Aggiunge alla sezione inferiore il pulsante "Genera scontrino"
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(generaScontrinoButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /*
     * Restituisce una lista di nomi di prodotti che matchano con il testo in input
     * per la funzione di auto-completamento.
     */
    private List<String> getProductSuggestions(String text) {
        List<String> suggestions = new ArrayList<>();
        for (ProdottoRecord prodotto : prodotti) {
            if (prodotto.getNome().toLowerCase().startsWith(text.toLowerCase())) {
                suggestions.add(prodotto.getNome());
            }
        }
        return suggestions;
    }

    private void showSuggestionPopup(List<String> suggestions) {
        // Rimuovi tutti i vecchi suggerimenti prima di aggiornare il popup
        suggestionPopup.setVisible(false);
        suggestionPopup.removeAll();

        // Aggiungi i suggerimenti filtrati
        boolean hasSuggestions = false;
        String inputText = prodottoField.getText().toLowerCase(); // Ottieni il testo inserito

        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(inputText)) { // Filtra i suggerimenti
                JMenuItem item = new JMenuItem(suggestion);
                item.addActionListener(e -> {
                    prodottoField.setText(suggestion);
                    suggestionPopup.setVisible(false); // Nascondi il popup dopo la selezione
                });
                suggestionPopup.add(item); // Aggiorna il popup
                hasSuggestions = true;
            }
        }

        // Mostra il popup solo se ci sono suggerimenti e il testo inserito non è vuoto
        if (hasSuggestions && !inputText.isEmpty()) {
            suggestionPopup.show(prodottoField, 0, prodottoField.getHeight());
            prodottoField.requestFocusInWindow(); // Mantieni il focus nel campo di testo
        } else {
            suggestionPopup.setVisible(false); // Nascondi se non ci sono suggerimenti
        }
    }

    // Aggiunge un prodotto al carrello qualora la sua disponibilità a magazzino sia sufficiente
    private void aggiungiAlCarrello() {
        // Estrae il nome del prodotto aggiunto al carrello
        String nomeProdotto = prodottoField.getText();

        // Estrae la quantità in cui lo stesso è stato richiesto
        int qtaProdotto = (int) qtaProdottoSpinner.getValue();

        // Recupera il record del prodotto selezionato a partire dal suo nome
        ProdottoRecord prodotto = productService.findByName(nomeProdotto, prodotti);
        
        if (prodotto != null) {
            // Se la quantità richiesta non eccede quella disponibile a stock
            if (qtaProdotto <= prodotto.getQtadisponibile()) {
                VocescontrinoRecord voceScontrino = invoiceService.createLine(prodotto, qtaProdotto);
                vociScontrino.add(voceScontrino);
                tableModel.addRow(new Object[] { prodotto.getNome(), qtaProdotto, prodotto.getPrezzo() * qtaProdotto });

                // Aggiorna la quantità disponibile del prodotto nel database, chiudendo automaticamente la connessione al termine
                productService.updateQuantity(prodotto.getIdprodotto(), prodotto.getQtadisponibile() - qtaProdotto);
                
                // Aggiorna la tabella dei prodotti visualizzata nella seconda schermata
                GestisciInventarioPanel gestisciInventarioPanel = mainFrame.getGestisciInventarioPanel();
                gestisciInventarioPanel.caricaProdotti();
                
                // Resetta i campi di selezione
                prodottoField.setText("");
                qtaProdottoSpinner.setValue(1);
            } else {
                JOptionPane.showMessageDialog(this,
                        "La quantità disponibile non è sufficiente: ne restano " + prodotto.getQtadisponibile() + ".",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Prodotto non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Genera uno scontrino
    private void generaScontrino() {
        // Controlla se siano stati aggiunti prodotti al carrello
        if (vociScontrino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il carrello della spesa è vuoto.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
        	float prezzoTotale = calcolaPrezzoTotale();
        	try {
        		dataService.generaScontrino(vociScontrino, prezzoTotale);
        	} catch (Exception e) {
        		JOptionPane.showMessageDialog(this, "Errore durante la generazione dello scontrino: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
            
            // Mostra un messaggio di conferma
            JOptionPane.showMessageDialog(this, "Scontrino generato con successo!", "Operazione riuscita",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Aggiorna la tabella degli scontrini visualizzata nella terza schermata
            VisualizzaScontriniPanel visualizzaScontriniPanel = mainFrame.getVisualizzaScontriniPanel();
            visualizzaScontriniPanel.caricaScontrini();  
            
            // Resetta sia il carrello (rimuovendo le voci scontrino dalla lista) che la relativa rappresentazione tabulare
            resettaCarrello();
        } catch (Exception e) {
            // Gestisce qualunque eccezione si sollevi durante la generazione dello scontrino
            logger.log(Level.SEVERE, "Errore durante la generazione dello scontrino", e);
            JOptionPane.showMessageDialog(this, "Errore durante la generazione dello scontrino: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Calcola il totale complessivo di uno scontrino, recuperando quantità acquistata e prezzo dei prodotti che ne fanno parte
    private float calcolaPrezzoTotale() {
        float prezzoTotale = 0;
        for (VocescontrinoRecord voceScontrino : vociScontrino) {
            ProdottoRecord prodotto = productService.findById(voceScontrino.getIdprodotto(), prodotti);
            if (prodotto != null) {
                prezzoTotale += prodotto.getPrezzo() * voceScontrino.getQtaprodotto();
            } else {
                logger.warning("Prodotto con ID " + voceScontrino.getIdprodotto() + " non trovato.");
            }
        }
        return prezzoTotale;
    }

    // Resetta sia il carrello che la relativa rappresentazione tabulare
    private void resettaCarrello() {
        vociScontrino.clear();
        tableModel.setRowCount(0);
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
