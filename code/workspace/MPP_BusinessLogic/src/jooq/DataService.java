package jooq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import dbinit.CreateDatabase;
import dto.ProdottoNelCarrelloDTO;
import jooq.generated.tables.Prodotto;
import jooq.generated.tables.Scontrino;
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.ScontrinoRecord;

/*
 * Fornisce servizi per interfacciarsi con il database usando il linguaggio Java, evitando di scrivere codice SQL.
 * Per usare questa classe bisogna creare un oggetto con new DataService(), per poi invocare i metodi di interesse.
 */
public class DataService {

	private Connection conn;
	private DSLContext context;

	public DataService() {
		try {
			conn = DriverManager.getConnection(CreateDatabase.DB_URL);
			context = DSL.using(conn, SQLDialect.SQLITE);
		} catch (SQLException e) {
			System.out.println("ERROR: Failed to connect!");
			System.out.println("ERROR: " + e.getErrorCode());
			e.printStackTrace();
		}
	}

	/*
	 * Inserisce un prodotto nella rispettiva tabella del database.
	 */
	public void aggiungiProdotto(String nome, float prezzo, int qtaDisponibile, String descrizione) {
		ProdottoRecord prodottoRecord = context.newRecord(Prodotto.PRODOTTO);
		prodottoRecord.setNome(nome);
		prodottoRecord.setPrezzo(prezzo);
		prodottoRecord.setQtadisponibile(qtaDisponibile);
		prodottoRecord.setDescrizione(descrizione);

		// Inserisce il record, omettendo la colonna IdProdotto
		prodottoRecord.insert();

		// Recupera l'ID generato automaticamente dal database
		int idGenerato = prodottoRecord.getIdprodotto();
		System.out.println("IdProdotto generato: " + idGenerato);
	}

	/*
	 * Recupera dal database tutti i prodotti contenuti nella rispettiva tabella.
	 */
	public List<ProdottoRecord> getProdotti() {
		return context.selectFrom(Prodotto.PRODOTTO).fetch();
	}

	/*
	 * Recupera dal database un determinato prodotto sulla base della propria descrizione.
	 */
	public ProdottoRecord getProdotto(String descrizione) {
		return context.selectFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.DESCRIZIONE.eq(descrizione)).fetchOne();
	}

	/*
	 * Rimuove dal database un determinato prodotto sulla base della propria descrizione.
	 */
	public int eliminaProdotto(String descrizione) {
		return context.deleteFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.DESCRIZIONE.eq(descrizione)).execute();
	}

	/*
	 * Modifica un certo prodotto già contenuto nel database sulla base della propria descrizione.
	 */
	public int modificaProdotto(String nuovoNomeProdotto, float nuovoPrezzoProdotto, int nuovaQtaProdotto,
			String nuovaDescrizioneProdotto, String prodottoSelezionato) {
		return context.update(Prodotto.PRODOTTO)
                .set(Prodotto.PRODOTTO.NOME, nuovoNomeProdotto)
                .set(Prodotto.PRODOTTO.PREZZO, nuovoPrezzoProdotto)
                .set(Prodotto.PRODOTTO.QTADISPONIBILE, nuovaQtaProdotto)
                .set(Prodotto.PRODOTTO.DESCRIZIONE, nuovaDescrizioneProdotto)
                .where(Prodotto.PRODOTTO.DESCRIZIONE.eq(prodottoSelezionato))
                .execute();
	}
	
	/*
	 * Inserisce uno scontrino nella rispettiva tabella del database.
	 */
	public void inserisciScontrino(float prezzoTotale, List<ProdottoNelCarrelloDTO> prodottiNelCarrello) {
		ScontrinoRecord scontrino = context.newRecord(Scontrino.SCONTRINO);
		scontrino.setPrezzotot(prezzoTotale);
		
		// Serializza i prodotti nel carrello come una stringa JSON o un oggetto serializzato
        String prodottiNelCarrelloJson = serializzaProdottiNelCarrello(prodottiNelCarrello);
        scontrino.setDettagli(prodottiNelCarrelloJson);
		
		scontrino.store();
	}

	private String serializzaProdottiNelCarrello(List<ProdottoNelCarrelloDTO> prodottiNelCarrello) {
		// Implement the serialization logic here
        // For example, you can use a JSON library like Jackson or Gson to serialize the cart items
        return "serialized_cart_items";
	}

	/*
	 * Recupera dal database tutti gli scontrini contenuti nella rispettiva tabella.
	 */
	public List<ScontrinoRecord> getScontrini() {
		return context.selectFrom(Scontrino.SCONTRINO).fetch();
	}
}
