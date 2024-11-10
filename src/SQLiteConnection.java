import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList; 

class SQLiteConnection {
    static String url = "jdbc:sqlite:lib/database.db";
    public static void initSQLite() {
        
        try (Connection connected = DriverManager.getConnection(url)) {
            if (connected != null) {
                Class.forName("org.sqlite.JDBC");
                System.out.println("Connected to the database.");

                String query = "CREATE TABLE IF NOT EXISTS categories(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,type TEXT NOT NULL,spent REAL NOT NULL);";
                Statement stmt = connected.createStatement();
                stmt.execute(query);
                System.out.println("Categories Table Created Successfully");
                
                query = "CREATE TABLE IF NOT EXISTS records (id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT NOT NULL,type text NOT NULL,amount REAL,date TEXT,cId INT REFERENCES categories(id));";
                stmt.execute(query);
                System.out.println("records Table Created Successfully");
                
                query = "CREATE TABLE IF NOT EXISTS budgets(id INTEGER PRIMARY KEY AUTOINCREMENT,cId INT REFERENCES categories(id),category_limit REAL NOT NULL);";
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
        String query = "INSERT INTO categories(name,type,spent) VALUES (?,?,?);";
        try(Connection connected = DriverManager.getConnection(url)){
            PreparedStatement pstmt = connected.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3,0.0 );
            pstmt.executeUpdate();
            System.out.println("Category Added Successfully");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addBudget(int id,double limit) {
        String query = "INSERT INTO budgets(cId,category_limit) VALUES (?,?);";
        try(Connection connected = DriverManager.getConnection(url)){
            PreparedStatement pstmt = connected.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setDouble(2, limit);
            pstmt.executeUpdate();
            System.out.println("Budget Added Successfully");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String[][] getRecords(){
        String query = "SELECT records.name AS rName, categories.type, records.amount, records.date, categories.name AS cName FROM records " +"JOIN categories ON records.cId = categories.id;";
        List<String[]> categoriesSet = new ArrayList<String[]>();
        try(Connection connected = DriverManager.getConnection(url)){
            Statement stmt = connected.createStatement();
            ResultSet resultSet =  stmt.executeQuery(query);
            while(resultSet.next())
            {
                categoriesSet.add(new String[]{resultSet.getString("rName"),resultSet.getString("cName"),resultSet.getString("date"),resultSet.getString("amount"),resultSet.getString("type")});
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categoriesSet.toArray(new String[0][0]);
    }

    public static String[][] getCategories(){
        String query = "SELECT name,type FROM categories;";
        List<String[]> categoriesSet = new ArrayList<String[]>();
        try(Connection connected = DriverManager.getConnection(url)){
            Statement stmt = connected.createStatement();
            ResultSet resultSet =  stmt.executeQuery(query);
            while(resultSet.next())
            {
                categoriesSet.add(new String[]{resultSet.getString("name"),resultSet.getString("type")});
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categoriesSet.toArray(new String[0][0]);
    }

    public static Object[] getCategoryIdTypeSpent(String name) {
        String query = "SELECT id,type,spent FROM categories WHERE name = ?;";

        Object[] cInfo = new Object[3];
        try (Connection connected = DriverManager.getConnection(url);
            PreparedStatement stmt = connected.prepareStatement(query)) {
            
            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();
            
            if (resultSet.next()) {
                cInfo[0] = resultSet.getInt("id");
                cInfo[1] = resultSet.getString("type");
                cInfo[2] = resultSet.getDouble("spent");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cInfo;
    }
    
    public static String[][] getBudgets(){
        String query = "SELECT categories.name, categories.type, categories.spent, budgets.category_limit FROM categories " +"JOIN budgets ON budgets.cId = categories.id;";
        List<String[]> categoriesSet = new ArrayList<String[]>();
        try(Connection connected = DriverManager.getConnection(url)){
            Statement stmt = connected.createStatement();
            ResultSet resultSet =  stmt.executeQuery(query);
            while(resultSet.next())
            {
                categoriesSet.add(new String[]{resultSet.getString("name"),resultSet.getString("type"),resultSet.getString("category_limit"),resultSet.getString("spent")});
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categoriesSet.toArray(new String[0][0]);
    }
}
