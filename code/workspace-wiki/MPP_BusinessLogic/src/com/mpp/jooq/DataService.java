package com.mpp.jooq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.mpp.dbinit.CreateDB;
import com.mpp.jooq.generated.tables.Prodotto;
import com.mpp.jooq.generated.tables.records.ProdottoRecord;

/*
 * Utile per avere tutti i prodotti inseriti ad inventario.
 */
public class DataService {
	
	public static List<ProdottoRecord> getProdottoRecord() {
		Connection conn;
		try {
			conn = DriverManager.getConnection(CreateDB.DB_URL);
			DSLContext create = DSL.using(conn, SQLDialect.SQLITE);
			return create.selectFrom(Prodotto.PRODOTTO).fetchInto(ProdottoRecord.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public static void main(String[] args) {
		System.out.println(getProdottoRecord());
	}

}
