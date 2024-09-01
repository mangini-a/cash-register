package com.mpp.dbinit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

class CreateDB {
	
	// Percorso relativo del file che contiene il database
	public static String DB_REL_FILE = "../db/inventario.db3";
	public static String DB_URL = "jdbc:sqlite:" + DB_REL_FILE;

	public static void main(String[] args) throws SQLException, IOException {
		Connection conn = DriverManager.getConnection(DB_URL);
		DatabaseMetaData meta = conn.getMetaData();
		System.out.println("The driver name is " + meta.getDriverName() + ".");
		System.out.println("A new database has been created.");
		System.out.println("Does the file actually exist? " + new File(DB_REL_FILE).exists() + ".");
	}

}
