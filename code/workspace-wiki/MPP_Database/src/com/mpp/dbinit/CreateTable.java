package com.mpp.dbinit;

import java.io.IOException;
import java.sql.*;

public class CreateTable {

	public static void main(String[] args) throws IOException {
		try {
			Connection conn = DriverManager.getConnection(CreateDB.DB_URL);
			if (conn != null) {
				Statement stmt = conn.createStatement();
				
				// Creazione della tabella Prodotto
				String createProdottoTable = "CREATE TABLE Prodotto (" +
	                    "IdProdotto INTEGER PRIMARY KEY, " +  // INTEGER PRIMARY KEY è l'equivalente di AUTO_INCREMENT in SQLite
	                    "Nome TEXT NOT NULL, " +
	                    "Prezzo REAL NOT NULL, " +  // In SQLite, DECIMAL è rappresentato come REAL
	                    "QtaDisponibile INTEGER NOT NULL, " +
	                    "Descrizione TEXT" +
	                    ");";
				stmt.executeUpdate(createProdottoTable);
				
				// Creazione della tabella VoceScontrino
				String createVoceScontrinoTable = "CREATE TABLE VoceScontrino (" +
	                    "IdScontrino INTEGER NOT NULL, " +
	                    "IdProdotto INTEGER NOT NULL, " +
	                    "QtaProdotto INTEGER NOT NULL, " +
	                    "DataOra TEXT NOT NULL DEFAULT (datetime('now')), " + // SQLite usa TEXT per TIMESTAMP, e si usa datetime('now') per il timestamp corrente
	                    "PRIMARY KEY (IdScontrino, IdProdotto), " +
	                    "FOREIGN KEY (IdProdotto) REFERENCES Prodotto(IdProdotto)" +
	                    ");";
	            stmt.executeUpdate(createVoceScontrinoTable);
	            
				stmt.close();
				conn.close();
				System.out.println("Tables created successfully");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Operations finished");
		}
	}

}
