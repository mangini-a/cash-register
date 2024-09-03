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
		
		ProdottoRecord mango = new ProdottoRecord(49, "Mango", 2.20, 30, "Mango 1pz");
	}

}
