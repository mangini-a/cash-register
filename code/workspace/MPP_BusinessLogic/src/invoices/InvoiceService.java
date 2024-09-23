package invoices;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import inventory.ProductService;
import jooq.DataService;
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;

public class InvoiceService {
	
	private DataService dataService;
	private static final Logger logger = Logger.getLogger(ProductService.class);

	// Costruttore privato per prevenire la creazione di istanze del servizio dall'esterno
	private InvoiceService(DataService dataService) {
		this.dataService = dataService;
	}
	
	// Istanza privata e statica del servizio
    private static InvoiceService instance;
    
    // Metodo pubblico e statico per accedere all'istanza dall'esterno
    public static InvoiceService getInstance(DataService dataService) {
        if (instance == null) {
            instance = new InvoiceService(dataService);
        }
        return instance;
    }

	// Crea un'istanza di VocescontrinoRecord, senza inserirla nel database (non si conosce ancora IdScontrino)
	public VocescontrinoRecord createLine(ProdottoRecord prodotto, int qtaProdotto) {
		VocescontrinoRecord voceScontrino = new VocescontrinoRecord();
		voceScontrino.setIdprodotto(prodotto.getIdprodotto());
		voceScontrino.setQtaprodotto(qtaProdotto);
		return voceScontrino;
	}

	// Recupera tutti gli scontrini dal database, chiudendo automaticamente la connessione al termine
	public List<ScontrinoRecord> findAll() throws SQLException {
		try {
			return dataService.getScontrini();
		} catch (Exception e) {
			// Handle general exception
			logError("Error retrieving invoices: " + e.getMessage());
			return Collections.emptyList();
		}
	}
	
	// Recupera le linee di dettaglio dello scontrino dal database, chiudendo automaticamente la connessione al termine
	public List<VocescontrinoRecord> findLinesByInvoiceId(int idScontrino) {
		try {
			return dataService.getDettagliScontrino(idScontrino);
		} catch (Exception e) {
			// Handle general exception
			logError("Error retrieving invoice details: " + e.getMessage());
			return Collections.emptyList();
		}
	}
	
	private void logError(String message) {
        logger.error(message);
    }
}
