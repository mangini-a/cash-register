package dbinit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertProducts {

	public static void main(String[] args) {
		try {
			Connection conn = DriverManager.getConnection(CreateDatabase.DB_URL);
			if (conn != null) {
				Statement stmt = conn.createStatement();
				
				// Inserimento di 48 prodotti
				String[] prodotti = {
						"('Latte', 1.50, 100, 'Latte fresco intero 1L')",
		                "('Pane', 0.80, 200, 'Pane bianco da 500g')",
		                "('Pasta', 1.20, 150, 'Pasta di semola 500g')",
		                "('Olio d''oliva', 6.50, 75, 'Olio extravergine di oliva 1L')",
		                "('Zucchero', 1.00, 180, 'Zucchero bianco 1kg')",
		                "('Caffè', 3.50, 90, 'Caffè macinato 250g')",
		                "('Biscotti', 2.00, 120, 'Biscotti al cioccolato 300g')",
		                "('Riso', 1.70, 130, 'Riso carnaroli 1kg')",
		                "('Uova', 2.50, 60, 'Confezione da 12 uova')",
		                "('Burro', 1.80, 85, 'Burro tradizionale 250g')",
		                "('Formaggio', 4.00, 50, 'Formaggio grana padano 200g')",
		                "('Yogurt', 0.90, 140, 'Yogurt alla frutta 125g')",
		                "('Mela', 0.30, 300, 'Mela golden 1pz')",
		                "('Arancia', 0.40, 250, 'Arancia tarocco 1pz')",
		                "('Banana', 0.35, 220, 'Banana 1pz')",
		                "('Patate', 1.10, 170, 'Patate gialle 1kg')",
		                "('Carote', 0.80, 200, 'Carote fresche 1kg')",
		                "('Pomodori', 2.00, 150, 'Pomodori ramati 1kg')",
		                "('Insalata', 1.00, 110, 'Insalata mista 1 confezione')",
		                "('Peperoni', 2.50, 90, 'Peperoni misti 1kg')",
		                "('Acqua', 0.50, 500, 'Acqua naturale 1.5L')",
		                "('Vino', 5.00, 60, 'Vino rosso 750ml')",
		                "('Birra', 1.20, 100, 'Birra bionda 500ml')",
		                "('Succo di frutta', 1.80, 80, 'Succo di arancia 1L')",
		                "('Tonno', 3.00, 75, 'Tonno in scatola 3x80g')",
		                "('Cioccolato', 2.20, 90, 'Cioccolato fondente 100g')",
		                "('Gelato', 4.00, 50, 'Gelato alla vaniglia 500ml')",
		                "('Detersivo', 6.00, 70, 'Detersivo per lavatrice 2L')",
		                "('Shampoo', 3.50, 80, 'Shampoo nutriente 250ml')",
		                "('Sapone', 1.00, 200, 'Sapone liquido 500ml')",
		                "('Dentifricio', 2.50, 110, 'Dentifricio sbiancante 75ml')",
		                "('Carta igienica', 4.50, 90, 'Carta igienica 10 rotoli')",
		                "('Fazzoletti', 1.20, 130, 'Fazzoletti di carta 10 pacchetti')",
		                "('Detergente', 2.80, 100, 'Detergente multiuso 1L')",
		                "('Spugna', 0.70, 150, 'Spugna abrasiva per piatti')",
		                "('Cereali', 3.00, 70, 'Cereali integrali 375g')",
		                "('Marmellata', 2.50, 65, 'Marmellata di fragole 350g')",
		                "('Miele', 4.50, 55, 'Miele millefiori 500g')",
		                "('Farina', 1.20, 150, 'Farina 00 1kg')",
		                "('Lievito', 0.50, 180, 'Lievito in polvere 4x16g')",
		                "('Panna', 1.80, 95, 'Panna da cucina 200ml')",
		                "('Prosciutto', 3.50, 70, 'Prosciutto crudo 100g')",
		                "('Salame', 4.00, 60, 'Salame nostrano 200g')",
		                "('Pollo', 5.50, 40, 'Pollo intero 1kg')",
		                "('Bistecca', 7.00, 30, 'Bistecca di manzo 500g')",
		                "('Pesce', 8.00, 35, 'Filetti di pesce 500g')",
		                "('Pesto', 2.50, 80, 'Pesto alla genovese 190g')",
		                "('Salsa di pomodoro', 1.50, 150, 'Salsa di pomodoro 700g')"			
				};
				
				for (String prodotto : prodotti) {
					String insertProdotto = "INSERT INTO Prodotto (Nome, Prezzo, QtaDisponibile, Descrizione) VALUES " + prodotto + ";";
					stmt.executeUpdate(insertProdotto);
				}
				
				stmt.close();
				conn.close();
				System.out.println("Records added successfully.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Done.");
		}
	}
}
