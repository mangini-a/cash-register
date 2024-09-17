package jooq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import dbinit.CreateDatabase;
import jooq.generated.tables.Prodotto;
import jooq.generated.tables.Scontrino;
import jooq.generated.tables.Vocescontrino;
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;

/*
 * Fornisce servizi per interfacciarsi con il database usando il linguaggio Java, evitando di scrivere codice SQL.
 * Per usare questa classe bisogna crearne un'istanza, così da poterne poi invocare i metodi di interesse.
 * 
 * aggiungiProdotto: Inserisce un nuovo prodotto nella tabella Prodotto.
 * eliminaProdotto: Elimina un determinato prodotto sulla base del proprio nome. (V)
 * modificaProdotto: Modifica un determinato prodotto sulla base del proprio nome. (V)
 * getProdottoByDescrizione: Recupera dal database un determinato prodotto sulla base della propria descrizione.
 * getProdottoById: Recupera dal database un determinato prodotto sulla base del proprio ID.
 * getProdotti: Recupera tutti i prodotti contenuti nella tabella Prodotto.
 * 
 * aggiornaQtaProdotto: Aggiorna la quantità disponibile di un prodotto in seguito al suo inserimento nel carrello.
 * inserisciScontrino: Inserisce un nuovo scontrino nella tabella Scontrino.
 * inserisciVoceScontrino: Inserisce una nuova linea di scontrino nella tabella VoceScontrino.
 * getScontrini: Recupera tutti gli scontrini contenuti nella tabella Scontrino.
 * getDettagliScontrino: Recupera i dettagli di un determinato scontrino sulla base del suo ID.
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
	 * Inserisce un nuovo prodotto nella tabella Prodotto.
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
	 * Elimina un determinato prodotto sulla base del proprio nome.
	 */
	public int eliminaProdotto(String nome) {
		return context.deleteFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.NOME.eq(nome)).execute();
	}

	/*
	 * Recupera dal database un determinato prodotto sulla base della propria
	 * descrizione.
	 */
	public ProdottoRecord getProdottoByDescrizione(String descrizione) {
		return context.selectFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.DESCRIZIONE.eq(descrizione)).fetchOne();
	}

	/*
	 * Recupera dal database un determinato prodotto sulla base del proprio ID.
	 */
	public ProdottoRecord getProdottoById(int idProdotto) {
		return context.selectFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.IDPRODOTTO.eq(idProdotto)).fetchOne();
	}

	/*
	 * Recupera tutti i prodotti contenuti nella tabella Prodotto.
	 */
	public List<ProdottoRecord> getProdotti() {
		return context.selectFrom(Prodotto.PRODOTTO).fetch();
	}

	/*
	 * Aggiorna la quantità disponibile di un prodotto in seguito al suo inserimento nel carrello.
	 */
	public void aggiornaQtaProdotto(Integer idProdotto, int nuovaQta) {
		context.update(Prodotto.PRODOTTO).set(Prodotto.PRODOTTO.QTADISPONIBILE, nuovaQta)
				.where(Prodotto.PRODOTTO.IDPRODOTTO.eq(idProdotto)).execute();
	}

	/*
	 * Inserisce un nuovo scontrino nella tabella Scontrino.
	 */
	public ScontrinoRecord inserisciScontrino(float prezzoTotale) {
		ScontrinoRecord scontrino = context.newRecord(Scontrino.SCONTRINO);
		scontrino.setPrezzotot(prezzoTotale);
		scontrino.store();
		return scontrino;
	}

	/*
	 * Inserisce una nuova linea di scontrino nella tabella VoceScontrino.
	 */
	public void inserisciVoceScontrino(VocescontrinoRecord voceScontrino) {
		voceScontrino.store();
	}

	/*
	 * Recupera tutti gli scontrini contenuti nella tabella Scontrino.
	 */
	public List<ScontrinoRecord> getScontrini() {
		return context.selectFrom(Scontrino.SCONTRINO).fetch();
	}

	/*
	 * Recupera i dettagli di un determinato scontrino sulla base del suo ID.
	 */
	public List<VocescontrinoRecord> getDettagliScontrino(int idScontrino) {
		return context.selectFrom(Vocescontrino.VOCESCONTRINO)
				.where(Vocescontrino.VOCESCONTRINO.IDSCONTRINO.eq(idScontrino)).fetch();
	}

	/*
	 * Modifica un determinato prodotto sulla base del proprio nome.
	 */
	public int modificaProdotto(String nome, String nuovoNome, float nuovoPrezzo, int nuovaQta, String nuovaDescrizione) {
		return context.update(Prodotto.PRODOTTO).set(Prodotto.PRODOTTO.NOME, nuovoNome)
		.set(Prodotto.PRODOTTO.PREZZO, nuovoPrezzo)
		.set(Prodotto.PRODOTTO.QTADISPONIBILE, nuovaQta)
		.set(Prodotto.PRODOTTO.DESCRIZIONE, nuovaDescrizione)
		.where(Prodotto.PRODOTTO.NOME.eq(nome)).execute();
	}
}
