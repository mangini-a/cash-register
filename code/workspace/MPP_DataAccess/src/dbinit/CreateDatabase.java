package dbinit;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDatabase {

	// Definizione del percorso relativo del file contenente il database
	public static String DB_REL_FILE = "../db/minimarket.db3";
	public static String DB_URL = "jdbc:sqlite:" + DB_REL_FILE;

	public static void main(String[] args) {
		try {
			Connection conn = DriverManager.getConnection(DB_URL);
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName() + ".");
				System.out.println("A new database has been created.");
				conn.close();
			}
			System.out.println("Does the file actually exist? " + new File(DB_REL_FILE).exists() + ".");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
