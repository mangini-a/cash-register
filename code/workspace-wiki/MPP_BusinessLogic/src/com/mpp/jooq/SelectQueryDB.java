package com.mpp.jooq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.mpp.dbinit.CreateDB;
import com.mpp.jooq.generated.tables.Prodotto;
import com.mpp.jooq.generated.tables.records.ProdottoRecord;

public class SelectQueryDB {

	public static void main(String[] args) throws SQLException {
		
		Connection conn = DriverManager.getConnection(CreateDB.DB_URL);
		DSLContext create = DSL.using(conn, SQLDialect.SQLITE);
		
		Result<ProdottoRecord> result = create.selectFrom(Prodotto.PRODOTTO)
                							  .fetch();

		// Visualizzazione dei risultati
		if (!result.isEmpty()) {
			for (ProdottoRecord prodotto : result) {
				System.out.println("ID Prodotto: " + prodotto.getIdprodotto());
				System.out.println("Nome: " + prodotto.getNome());
				System.out.println("Prezzo: " + prodotto.getPrezzo());
				System.out.println("Quantità Disponibile: " + prodotto.getQtadisponibile());
				System.out.println("Descrizione: " + prodotto.getDescrizione());
				System.out.println("-----------------------------------");
			}
		} else {
			System.out.println("No products found.");
		}

	}

}
