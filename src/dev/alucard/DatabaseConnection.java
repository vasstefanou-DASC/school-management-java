package dev.alucard;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private final MysqlDataSource dataSource;

    public DatabaseConnection() {

        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("school");
        dataSource.setUser("devuser");
        dataSource.setPassword("j55Willbar6anek#2");

    }

    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
}
