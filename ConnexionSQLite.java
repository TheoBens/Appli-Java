import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionSQLite {

    public static Connection connect() {
        Connection connexion = null;
        try {

            String url = "jdbc:sqlite:C:/Users/theob/OneDrive/Bureau/3IL/Projet_Dev_Java/BDD/BDD_local.db";

            connexion = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connexion;
    }
	
}
