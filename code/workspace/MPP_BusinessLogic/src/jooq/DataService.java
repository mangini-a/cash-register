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
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.ScontrinoRecord;

/*
 * Fornisce servizi per interfacciarsi con il database usando il linguaggio Java, evitando di scrivere codice SQL.
 * Per usare questa classe bisogna creare un oggetto con new DataService(), per poi chiamare i metodi di interesse.
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
	 * Inserisce nel database (minimarket.db3) un nuovo prodotto, servendosi dei
	 * dati passati dall'utente tramite UI.
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
	 * Recupera dal database tutti i prodotti contenuti nella tabella Prodotto.
	 */
	public List<ProdottoRecord> getProdotti() {
		return context.selectFrom(Prodotto.PRODOTTO).fetch();
	}

	/*
	 * Recupera dal database uno specifico prodotto selezionato dall'utente.
	 */
	public ProdottoRecord getProdotto(String descrizione) {
		return context.selectFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.DESCRIZIONE.eq(descrizione)).fetchOne();
	}

	/*
	 * Rimuove dal database un determinato prodotto, selezionato dall'utente indicandone la descrizione.
	 */
	public int eliminaProdotto(String descrizione) {
		return context.deleteFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.DESCRIZIONE.eq(descrizione)).execute();
	}

	/*
	 * Modifica un certo prodotto già contenuto nel database, selezionato dall'utente indicandone la descrizione.
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
	 * Inserisce uno scontrino nella rispettiva tabella del database "Scontrino".
	 */
	public ScontrinoRecord inserisciScontrino(float prezzoTotale) {
		ScontrinoRecord scontrino = context.newRecord(Scontrino.SCONTRINO);
		scontrino.setPrezzotot(prezzoTotale);
		scontrino.store();
		return scontrino;
	}

	/*
	 * Recupera dal database tutti gli scontrini contenuti nella tabella Scontrino.
	 */
	public List<ScontrinoRecord> getScontrini() {
		return context.selectFrom(Scontrino.SCONTRINO).fetch();
	}
}
