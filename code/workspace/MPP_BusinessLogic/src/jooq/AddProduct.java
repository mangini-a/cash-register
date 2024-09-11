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

public class AddProduct {

	public static void main(String[] args) {
		try {
			Connection conn = DriverManager.getConnection(CreateDatabase.DB_URL);
			if (conn != null) {
				DSLContext context = DSL.using(conn, SQLDialect.SQLITE);

				ProdottoRecord prodotto = context.newRecord(Prodotto.PRODOTTO);
				prodotto.setIdprodotto(49);
				prodotto.setNome("Mango");
				prodotto.setPrezzo(0.60f);
				prodotto.setQtadisponibile(20);
				prodotto.setDescrizione("Mango fresco 1pz");
				prodotto.store();

				conn.close();
				System.out.println("Record added successfully.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
