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

public class SelectOneQueryDB {

	public static void main(String[] args) throws SQLException {
		Connection conn = DriverManager.getConnection(CreateDB.DB_URL);
		DSLContext create = DSL.using(conn, SQLDialect.SQLITE);

		Integer idProdottoMango = 49;

        // Esecuzione della query per ottenere il prodotto Mango con ID 49
        ProdottoRecord mango = create.selectFrom(Prodotto.PRODOTTO)
                                     .where(Prodotto.PRODOTTO.IDPRODOTTO.eq(idProdottoMango))
                                     .fetchOne();

        // Visualizzazione del risultato
        if (mango != null) {
            System.out.println("ID Prodotto: " + mango.getIdprodotto());
            System.out.println("Nome: " + mango.getNome());
            System.out.println("Prezzo: " + mango.getPrezzo());
            System.out.println("Quantità Disponibile: " + mango.getQtadisponibile());
            System.out.println("Descrizione: " + mango.getDescrizione());
        } else {
            System.out.println("The product with ID"  + idProdottoMango +  "was not found.");
        }
	}

}
