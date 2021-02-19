package activemq;

import java.sql.*;
import org.apache.log4j.Logger;

public class DB {
    private static Logger log = Logger.getLogger(DB.class.getName());

    private String url;
    private String user;
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    private static final String deleteTable1 = "DROP TABLE IF EXISTS TABLE1";
    private static final String deleteTable2 = "DROP TABLE IF EXISTS TABLE2";
    private static final String createTable1 =
            "CREATE TABLE TABLE1 (TEXT_ID INT PRIMARY KEY, TEXT VARCHAR NOT NULL)";
    private static final String createTable2 =
            "CREATE TABLE TABLE2 (HEADER_ID INT PRIMARY KEY, " +
                    "TEXT_ID INT UNIQUE REFERENCES TABLE1, HEADER VARCHAR NOT NULL)";
    private static final String insertFields1 =
            "INSERT INTO TABLE1 (TEXT_ID, TEXT) VALUES (?, ?)";
    private static final String insertFields2 =
            "INSERT INTO TABLE2 (HEADER_ID, TEXT_ID, HEADER) VALUES (?, ?, ?)";

    public void createDB() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            //Удаляем сначала таблицу 2 и только затем таблицу1,
            // так во второй храним внешний ключ от 1-ой
            statement.executeUpdate(deleteTable2);
            statement.executeUpdate(deleteTable1);
            statement.executeUpdate(createTable1);
            statement.executeUpdate(createTable2);
            log.info("DB is ready");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            log.info("Exception: {}", e);
        }
    }
   // В таблицу1 записывем данные TEXT_ID и TEXT- тело сообщения
    public void insertFields1(int TEXT_ID, String TEXT) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertFields1);
            preparedStatement.setInt(1, TEXT_ID);
            preparedStatement.setString(2, TEXT);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            log.info("Inserting values into table1 "+String.valueOf(TEXT_ID)+", "+TEXT);
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            log.error("Exception: {}", e);
        }
    }

    //   В таблицу2 записывем данные HEADER_ID, TEXT_ID(внешний ключ) и HEADER - элемент заголовка
    public void insertFields2(int HEADER_ID, int TEXT_ID, String  HEADER) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertFields2);
            preparedStatement.setInt(1, HEADER_ID);
            preparedStatement.setInt(2, TEXT_ID);
            preparedStatement.setString(3, HEADER);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            log.info("Inserting values into table2 "+String.valueOf(HEADER_ID)+", "+
                    String.valueOf(TEXT_ID)+", "+HEADER);
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            log.error("Exception: {}", e);
        }
    }
}
