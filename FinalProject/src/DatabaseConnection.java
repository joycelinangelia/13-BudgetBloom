import java.sql.*;
import java.util.*;
import java.util.Date;

public class DatabaseConnection {
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/logrecord";
    private String username = "root";
    private String password = "";

    public void initConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Connection established successfully.");
                initBaseCurrency();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    private void initBaseCurrency() {
        String checkQuery = "SELECT COUNT(*) FROM currencylist";
        String insertQuery = "INSERT INTO currencylist (ID, currency) VALUES (1, ?)";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(checkQuery)) {

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, "TWD");
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void insertQuery(String type, Date date, double amount, String currency, String category, String note) {
        String query = "INSERT INTO log (Type, Date, Amount, Currency, Category, Note) VALUES (?, ?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);
            preparedStatement.setDate(2, new java.sql.Date(date.getTime()));
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, currency);
            preparedStatement.setString(5, category);
            preparedStatement.setString(6, note);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insertQuery(String name) {
        String query = "INSERT INTO categories (Name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void editQuery(int id, String type, Date date, double amount, String currency, String category, String note) {
        String query = "UPDATE log SET Type=?, Date=?, Amount=?, Currency=?, Category=?, Note=? WHERE ID=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);
            preparedStatement.setDate(2, new java.sql.Date(date.getTime()));
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, currency);
            preparedStatement.setString(5, category);
            preparedStatement.setString(6, note);
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteLog(int logId) {
        String query = "DELETE FROM log WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, logId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCategory(int logId,String cat) {
        String query = "DELETE FROM categories WHERE id = ?";
        try{
            String deleteLogsQuery = "DELETE FROM log WHERE category = ?";
            try (PreparedStatement deleteLogsStatement = connection.prepareStatement(deleteLogsQuery)) {
                deleteLogsStatement.setString(1, cat);
                deleteLogsStatement.executeUpdate();
            }
            String deleteCategoryQuery = "DELETE FROM categories WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, logId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Log> individualQuery(int d,int m,int y){
        ArrayList<Log> log = new ArrayList<>();
        String sql = "SELECT Type, Date, Amount,Currency, Category, Note FROM log WHERE DAY(Date) = ? AND MONTH(Date) = ? AND YEAR(Date) = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, d);
            statement.setInt(2, m);
            statement.setInt(3, y);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String ty = resultSet.getString("Type");
                    Date date = resultSet.getDate("Date");
                    String cur = resultSet.getString("Currency");
                    String cat = resultSet.getString("Category");
                    String note = resultSet.getString("Note");
                    double am = resultSet.getDouble("Amount");
                    Log tmp = new Log(ty,date,am,cur,cat,note);
                    log.add(tmp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return log;
    }
    public int getID(String type, Date date, double amount, String currency, String category, String note) {
        String query = "SELECT ID FROM log WHERE Type = ? AND Date = ? AND Amount = ? AND Currency = ? AND Category = ? AND Note = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            statement.setDate(2, new java.sql.Date(date.getTime()));
            statement.setDouble(3, amount);
            statement.setString(4, currency);
            statement.setString(5, category);
            statement.setString(6, note);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int getID(String category) {
        String query = "SELECT ID FROM categories WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Log[] selectInQuery(int m,int y){ //income of the month
        ArrayList<String> category = getCategory();
        Log[] list = new Log[category.size()-1];
        for (int i = 1; i < category.size(); ++i) {
            list[i-1] = new Log(0, category.get(i));
        }
        String sql = "SELECT amount, currency, category FROM log WHERE type = 'Income' AND MONTH(Date) = ? AND YEAR(Date) = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, m);
            statement.setInt(2, y);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String cat = resultSet.getString("category");
                    String cur = resultSet.getString("currency");
                    double am = resultSet.getDouble("amount");
                    int index = category.indexOf(cat) - 1;
                    CurrencyConverter cc = new CurrencyConverter();
                    list[index].addAmount(cc.convert(am,cur,getBaseCurrency()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Log[] selectTopQuery(int m,int y){ //top expenses of the month
        ArrayList<String> category = getCategory();
        Log[] list = new Log[category.size()];
        for (int i = 0; i < category.size(); ++i) {
            list[i] = new Log(0, category.get(i));
        }
        String sql = "SELECT amount, currency, category FROM log WHERE type = 'Expense' AND MONTH(Date) = ? AND YEAR(Date) = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, m);
            statement.setInt(2, y);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String cur = resultSet.getString("currency");
                    String cat = resultSet.getString("category");
                    double am = resultSet.getDouble("amount");
                    int index = category.indexOf(cat);
                    CurrencyConverter cc = new CurrencyConverter();
                    list[index].addAmount(cc.convert(am,cur,getBaseCurrency()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Arrays.sort(list, Comparator.comparingDouble(Log::getAmount).reversed());
        return list;
    }
    public ArrayList<String> getCategory() {
        ArrayList<String> category = new ArrayList<>();
        category.add("");
        String query = "SELECT * FROM categories";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                category.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
    public String getBaseCurrency(){
        String sql = "SELECT currency FROM currencylist WHERE ID = 1";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getString("currency");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void updateBaseCurrency(String newCur){
        String sql = "UPDATE currencylist SET currency = ? WHERE ID = 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newCur);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
