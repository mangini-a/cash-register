package inventory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;

/*
 * Incapsula la logica atta a recuperare, aggiungere, modificare ed eliminare prodotti dal database.
 * Utilizza DataService come una dipendenza per attuare operazioni di CRUD sulla tabella Prodotto. 
 * Ciò permette di offrire un'interfaccia di più alto livello per la gestione dei prodotti.
 */
public class ProductService {
	
	private DataService dataService;
	private static final Logger logger = Logger.getLogger(ProductService.class);

	// Costruttore privato per prevenire la creazione di istanze del servizio dall'esterno
	private ProductService(DataService dataService) {
		this.dataService = dataService;
	}
	
	// Istanza privata e statica del servizio
    private static ProductService instance;
    
    // Metodo pubblico e statico per accedere all'istanza dall'esterno
    public static ProductService getInstance(DataService dataService) {
        if (instance == null) {
            instance = new ProductService(dataService);
        }
        return instance;
    }

	// Controlla se il database contenga già un prodotto con lo stesso nome, chiudendo la connessione al termine
	public boolean exists(String nome) throws SQLException {
		try {
			return dataService.isProductAlreadyExisting(nome);
		} catch (Exception e) {
            // Handle general exception
            logError("Error checking if product exists: " + e.getMessage());
            return false;
        }
	}
	
	// Inserisce un nuovo prodotto nella rispettiva tabella del database, chiudendo la connessione al termine
	public void create(String nome, float prezzo, int qtaDisponibile, String descrizione) throws SQLException {
		try {
			dataService.aggiungiProdotto(nome, prezzo, qtaDisponibile, descrizione);
		} catch (Exception e) {
            // Handle general exception
            logError("Error creating product: " + e.getMessage());
        }
	}
	
	// Modifica un determinato prodotto sulla base del proprio nome attuale, chiudendo la connessione al termine
	public boolean update(String nomeAttuale, String nuovoNome, float nuovoPrezzo, int nuovaQta, String nuovaDescrizione) throws SQLException {
		try {
			return dataService.modificaProdotto(nomeAttuale, nuovoNome, nuovoPrezzo, nuovaQta, nuovaDescrizione) > 0;
		} catch (Exception e) {
            // Handle general exception
            logError("Error updating product: " + e.getMessage());
            return false;
        }
	}
	
	// Recupera tutti i prodotti dal database, chiudendo automaticamente la connessione al termine
	public List<ProdottoRecord> findAll() throws SQLException {
		try {
			return dataService.getProdotti();
		} catch (Exception e) {
            // Handle general exception
            logError("Error retrieving products: " + e.getMessage());
            return Collections.emptyList();
        }
	}
	
	// Rimuove un determinato prodotto dal database sulla base del proprio nome, chiudendo la connessione al termine
	public boolean delete(String nome) throws SQLException {
		try {
			return dataService.eliminaProdotto(nome) > 0;
		} catch (Exception e) {
            // Handle general exception
            logError("Error deleting product: " + e.getMessage());
            return false;
        }
	}
	
	private void logError(String message) {
        logger.error(message);
    }
}
