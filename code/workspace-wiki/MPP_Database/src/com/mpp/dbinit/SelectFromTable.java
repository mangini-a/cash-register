package com.mpp.dbinit;

import java.io.IOException;
import java.sql.*;

public class SelectFromTable {

	public static void main(String[] args) throws IOException, SQLException {
		try {
			Connection conn = DriverManager.getConnection(CreateDB.DB_URL);
			if (conn != null) {
				Statement stmt = conn.createStatement();
		
				// Stampa dei prodotti
				System.out.println("Prodotti disponibili:");
				System.out.println("============================================================");
				System.out.printf("%-10s %-20s %-10s %-15s %-30s\n", "IdProdotto", "Nome", "Prezzo", "QtaDisponibile", "Descrizione");
				System.out.println("------------------------------------------------------------");
				String queryProdotti = "SELECT * FROM Prodotto;";
				ResultSet rsProdotti = stmt.executeQuery(queryProdotti);
				while (rsProdotti.next()) {
					int id = rsProdotti.getInt("IdProdotto");
					String nome = rsProdotti.getString("Nome");
					double prezzo = rsProdotti.getDouble("Prezzo");
					int qta = rsProdotti.getInt("QtaDisponibile");
					String descrizione = rsProdotti.getString("Descrizione");
					System.out.printf("%-10d %-20s %-10.2f %-15d %-30s\n", id, nome, prezzo, qta, descrizione);
				}
				rsProdotti.close();

				// Stampa delle voci scontrino
				System.out.println("\nVoci scontrino:");
				System.out.println("====================================================================================");
				System.out.printf("%-10s %-10s %-15s %-20s\n", "IdScontrino", "IdProdotto", "QtaProdotto", "DataOra");
				System.out.println("------------------------------------------------------------------------------------");
				String queryVociScontrino = "SELECT * FROM VoceScontrino;";
				ResultSet rsVociScontrino = stmt.executeQuery(queryVociScontrino);
				while (rsVociScontrino.next()) {
					int idScontrino = rsVociScontrino.getInt("IdScontrino");
					int idProdotto = rsVociScontrino.getInt("IdProdotto");
					int qtaProdotto = rsVociScontrino.getInt("QtaProdotto");
					String dataOra = rsVociScontrino.getString("DataOra");
					System.out.printf("%-10d %-10d %-15d %-20s\n", idScontrino, idProdotto, qtaProdotto, dataOra);
				}
				rsVociScontrino.close();
		
				// Stampa dei registratori di cassa
				System.out.println("\nRegistratori di cassa:");
				System.out.println("===========================================");
				System.out.printf("%-15s %-15s\n", "IdRegistratore", "IdScontrino");
				System.out.println("-------------------------------------------");
				String queryRegistratori = "SELECT * FROM Registratore;";
				ResultSet rsRegistratori = stmt.executeQuery(queryRegistratori);
				while (rsRegistratori.next()) {
				    int idRegistratore = rsRegistratori.getInt("IdRegistratore");
				    int idScontrino = rsRegistratori.getInt("IdScontrino");
				    System.out.printf("%-15d %-15d\n", idRegistratore, idScontrino);
				}
				rsRegistratori.close();

				
				stmt.close();
				conn.close();
				System.out.println("\nQuery executed successfully.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
