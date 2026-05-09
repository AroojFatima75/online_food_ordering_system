import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DBConnection {

    // We make this method 'static' so we can call it without creating an object of this class.
    public static Connection connect() {
        try {
            // 1. Load the Oracle Driver (The Translator)
            Class.forName("oracle.jdbc.OracleDriver");

            // 2. Establish Connection (The Bridge)
            // FORMAT: jdbc:oracle:thin:@HOSTNAME:PORT:SID, USERNAME, PASSWORD
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "food_new",
                    "food123"
            );
            return con;

        } catch (Exception e) {
            // If connection fails, show a popup explanation
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            return null;
        }
    }
}