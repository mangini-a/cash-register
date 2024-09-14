package ui.scontrini;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/*
 * Gestisce l'inserimento di linee scontrino nella tabella VoceScontrino usando JDBC puro (una query SQL). 
 * Ciò consente di disaccoppiarla da jOOQ e di utilizzarla in maniera indipendente.
 */
public class InvoiceService {

	private static final String DB_URL = "jdbc:sqlite:../db/minimarket.db3";

	public void insertInvoiceItems(int idScontrino, List<ProdottoNelCarrello> prodottiNelCarrello) {
		try (Connection conn = DriverManager.getConnection(DB_URL)) {
			String sql = "INSERT INTO VoceScontrino (IdScontrino, IdProdotto, QtaProdotto) VALUES (?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				for (ProdottoNelCarrello prodottoNelCarrello : prodottiNelCarrello) {
					pstmt.setInt(1, idScontrino);
					pstmt.setInt(2, prodottoNelCarrello.getProdotto().getIdprodotto());
					pstmt.setInt(3, prodottoNelCarrello.getQtaProdotto());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
