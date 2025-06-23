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

    public static void dropAllTables() {
        String q1 = "DROP TABLE records;";
        String q2 = "DROP TABLE budgets;";
        String q3 = "DROP TABLE categories;";
        try(Connection connected = DriverManager.getConnection(url)){
            PreparedStatement pstmt = connected.prepareStatement(q1);
            pstmt.executeUpdate();
            pstmt = connected.prepareStatement(q2);
            pstmt.executeUpdate();
            pstmt = connected.prepareStatement(q3);
            pstmt.executeUpdate();
            System.out.println("All Tables are deleted");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addRecord(String name,String type,double amount,String dt,int cId) {
        String query = "INSERT INTO records(name,type,amount,date,cId) VALUES (?,?,?,?,?);";
        String q2 = "UPDATE categories SET spent = spent + ? WHERE id = ?;";
        try(Connection connected = DriverManager.getConnection(url)){
            PreparedStatement pstmt = connected.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3,amount);
            pstmt.setString(4,dt);
            pstmt.setInt(5, cId);
            pstmt.executeUpdate();
            System.out.println("Record Inserted Successfully");

            pstmt = connected.prepareStatement(q2);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, cId);
            pstmt.executeUpdate();
            System.out.println("Spent Updated");

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
    
    public static void deleteRecord(String name, String category, String date) {
        // First get the category ID and amount
        String getInfoQuery = "SELECT r.amount, r.cId FROM records r " +
                            "JOIN categories c ON r.cId = c.id " +
                            "WHERE r.name = ? AND c.name = ? AND r.date = ?;";
        double amount = 0;
        int categoryId = -1;
        
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement stmt = connected.prepareStatement(getInfoQuery)) {
            
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setString(3, date);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                amount = rs.getDouble("amount");
                categoryId = rs.getInt("cId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Delete the record
        String deleteQuery = "DELETE FROM records WHERE name = ? AND cId = ? AND date = ?;";
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement pstmt = connected.prepareStatement(deleteQuery)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, categoryId);
            pstmt.setString(3, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Update the category's spent amount
        String updateSpentQuery = "UPDATE categories SET spent = spent - ? WHERE id = ?;";
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement pstmt = connected.prepareStatement(updateSpentQuery)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateRecord(String originalName, String originalCategory, String originalDate,
                                  String newName, String newCategory, double newAmount) {
        // First get the original record's amount and category ID
        String getInfoQuery = "SELECT r.amount, r.cId FROM records r " +
                            "JOIN categories c ON r.cId = c.id " +
                            "WHERE r.name = ? AND c.name = ? AND r.date = ?;";
        double oldAmount = 0;
        int oldCategoryId = -1;
        
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement stmt = connected.prepareStatement(getInfoQuery)) {
            
            stmt.setString(1, originalName);
            stmt.setString(2, originalCategory);
            stmt.setString(3, originalDate);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                oldAmount = rs.getDouble("amount");
                oldCategoryId = rs.getInt("cId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Get the new category ID
        String getNewCategoryIdQuery = "SELECT id FROM categories WHERE name = ?;";
        int newCategoryId = -1;
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement stmt = connected.prepareStatement(getNewCategoryIdQuery)) {
            
            stmt.setString(1, newCategory);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                newCategoryId = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Update the record
        String updateRecordQuery = "UPDATE records SET name = ?, cId = ?, amount = ? " +
                                 "WHERE name = ? AND cId = ? AND date = ?;";
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement pstmt = connected.prepareStatement(updateRecordQuery)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newCategoryId);
            pstmt.setDouble(3, newAmount);
            pstmt.setString(4, originalName);
            pstmt.setInt(5, oldCategoryId);
            pstmt.setString(6, originalDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Update the old category's spent amount
        String updateOldSpentQuery = "UPDATE categories SET spent = spent - ? WHERE id = ?;";
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement pstmt = connected.prepareStatement(updateOldSpentQuery)) {
            pstmt.setDouble(1, oldAmount);
            pstmt.setInt(2, oldCategoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Update the new category's spent amount
        String updateNewSpentQuery = "UPDATE categories SET spent = spent + ? WHERE id = ?;";
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement pstmt = connected.prepareStatement(updateNewSpentQuery)) {
            pstmt.setDouble(1, newAmount);
            pstmt.setInt(2, newCategoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateCategory(String oldName, String newName, String newType) {
        // First get the category ID
        String getCategoryIdQuery = "SELECT id FROM categories WHERE name = ?;";
        int categoryId = -1;
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement stmt = connected.prepareStatement(getCategoryIdQuery)) {
            
            stmt.setString(1, oldName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                categoryId = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (categoryId == -1) {
            System.out.println("Category not found");
            return;
        }

        // Update the category
        String updateCategoryQuery = "UPDATE categories SET name = ?, type = ? WHERE id = ?;";
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement stmt = connected.prepareStatement(updateCategoryQuery)) {
            
            stmt.setString(1, newName);
            stmt.setString(2, newType);
            stmt.setInt(3, categoryId);
            stmt.executeUpdate();
            System.out.println("Category updated successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteCategory(String name) {
        String query = "DELETE FROM categories WHERE name = ?;";
        try (Connection connected = DriverManager.getConnection(url);
             PreparedStatement stmt = connected.prepareStatement(query)) {
            
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
