package com.mpp.jooq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.mpp.dbinit.CreateDB;
import com.mpp.jooq.generated.tables.Prodotto;
import com.mpp.jooq.generated.tables.records.ProdottoRecord;

public class InsertInDB {

	public static void main(String[] args) throws SQLException {
		
		Connection conn = DriverManager.getConnection(CreateDB.DB_URL);
		DSLContext create = DSL.using(conn, SQLDialect.SQLITE);
		
		// Dettagli del prodotto "Mango"
        Integer idProdotto = 49;
        String nome = "Mango";
        Float prezzo = 0.60f; // Prezzo del Mango
        Integer qtaDisponibile = 200; // Quantità disponibile
        String descrizione = "Mango fresco 1pz"; // Descrizione del Mango

        // Creazione di un nuovo record di prodotto
        ProdottoRecord prodottoRecord = new ProdottoRecord(idProdotto, nome, prezzo, qtaDisponibile, descrizione);

        // Inserimento del prodotto nel database
        create.insertInto(Prodotto.PRODOTTO)
              .set(prodottoRecord)
              .execute();

        System.out.println("Product 'Mango' successfully inserted!");
	}

}
