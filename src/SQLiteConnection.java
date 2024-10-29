import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Date;

class SQLiteConnection {
    static String url = "jdbc:sqlite:lib/database.db";
    public static void initSQLite() {
        
        try (Connection connected = DriverManager.getConnection(url)) {
            if (connected != null) {
                Class.forName("org.sqlite.JDBC");
                System.out.println("Connected to the database.");

                String query = "CREATE TABLE IF NOT EXISTS categories(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,type TEXT NOT NULL)";
                Statement stmt = connected.createStatement();
                stmt.execute(query);
                System.out.println("Categories Table Created Successfully");
                
                query = "CREATE TABLE IF NOT EXISTS records (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL,type text NOT NULL,amount REAL,date TEXT,cId INT REFERENCES categories(id));";
                stmt.execute(query);
                System.out.println("records Table Created Successfully");

                query = "CREATE TABLE IF NOT EXISTS budgets(id INTEGER PRIMARY KEY AUTOINCREMENT,cId INT REFERENCES catagories(id),amount REAL NOT NULL);";
                stmt.execute(query);
                System.out.println("budgets Table Created Successfully");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addRecord(String name,String type,double amount,String dt,int cId) {
        String query = "INSERT INTO records(name,type,amount,date,cId) VALUES (?,?,?,?,?);";
        try(Connection connected = DriverManager.getConnection(url)){
            PreparedStatement pstmt = connected.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3,amount);
            pstmt.setString(4,dt);
            pstmt.setInt(5, cId);
            pstmt.executeUpdate();
            System.out.println("Record Inserted Successfully");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addCategory(String name,String type) {
        String query = "INSERT INTO categories(name,type) VALUES (?,?);";
        try(Connection connected = DriverManager.getConnection(url)){
            PreparedStatement pstmt = connected.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.executeUpdate();
            System.out.println("Category Added Successfully");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addBudget(int id,double amount) {
        String query = "INSERT INTO categories(cId,amount) VALUES (?,?);";
        try(Connection connected = DriverManager.getConnection(url)){
            PreparedStatement pstmt = connected.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setDouble(2, amount);
            pstmt.executeUpdate();
            System.out.println("Budget Added Successfully");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static resultSet getRecord() {
        String query = "SELECT * FROM results;";
        try(Connection connected = DriverManager.getConnection(url)){
            Statement stmt = connected.createStatement();
            ResultSet resultSet =  stmt.executeQuery(query);
            return resultSet;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }  
}
