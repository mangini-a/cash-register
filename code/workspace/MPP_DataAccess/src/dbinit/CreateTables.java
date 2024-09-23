package dbinit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTables {

	public static void main(String[] args) {
		try {
			Connection conn = DriverManager.getConnection(CreateDatabase.DB_URL);
			if (conn != null) {
				Statement stmt = conn.createStatement();
				
				// Creazione della tabella Prodotto
				String createProdottoTable = "CREATE TABLE Prodotto (" +
						"IdProdotto INTEGER PRIMARY KEY, " + // INTEGER PRIMARY KEY è l'equivalente di AUTO_INCREMENT in SQLite
						"Nome TEXT NOT NULL, " +
						"Prezzo REAL NOT NULL, " + // In SQLite, DECIMAL è rappresentato come REAL
						"QtaDisponibile INTEGER NOT NULL, " +
						"Descrizione TEXT" + 
						");";
				stmt.executeUpdate(createProdottoTable);
				
				// Creazione della tabella Scontrino
				String createScontrinoTable = "CREATE TABLE Scontrino (" +
						"IdScontrino INTEGER PRIMARY KEY, " +
						"DataOra DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
						"PrezzoTot REAL NOT NULL" +
						");";
				stmt.executeUpdate(createScontrinoTable);
				
				// Creazione della tabella VoceScontrino
				String createVoceScontrinoTable = "CREATE TABLE VoceScontrino (" +
						"IdScontrino INTEGER NOT NULL, " +
						"IdProdotto INTEGER NOT NULL, " +
						"QtaProdotto INTEGER NOT NULL, " +
						"PRIMARY KEY (IdScontrino, IdProdotto), " +
						"FOREIGN KEY (IdProdotto) REFERENCES Prodotto(IdProdotto), " +
						"FOREIGN KEY (IdScontrino) REFERENCES Scontrino(IdScontrino)" +
						");";
				stmt.executeUpdate(createVoceScontrinoTable);
				
				stmt.close();
				conn.close();
				System.out.println("Tables created succesfully.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Operations finished.");
		}
	}
}
