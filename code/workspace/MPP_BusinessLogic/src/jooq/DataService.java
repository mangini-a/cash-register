package jooq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import dbinit.CreateDatabase;
import jooq.generated.tables.Prodotto;
import jooq.generated.tables.records.ProdottoRecord;

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
}
