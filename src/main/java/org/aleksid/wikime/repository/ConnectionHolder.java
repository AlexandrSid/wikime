package org.aleksid.wikime.repository;

//изначально это было вложенным классом DBArticleRepository
//работает из консоли идеи, не работает на томкэте..починено
// по идее эта штука должна держать коннекшн открытым и закрывать его при заершении программы либо по таймауту.
// но это не написано

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionHolder {

    private static final String URL = "jdbc:postgresql://localhost:5432/wikime_test";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";

    private static ConnectionHolder instance;
    private final Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    private final Statement statement = connection.createStatement();

    public static ConnectionHolder getInstance() {
        if (instance == null) {
            try {
                //без этой строчки не работало с томкэтом,
                // был NPE с комментом No suitable driver found for jdbc:postgresql:...,
                // добавление руками .jar вытянутого мавеном в lib томката не помогло
                Class.forName("org.postgresql.Driver");

                instance = new ConnectionHolder();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public Statement getStatement() {
        return statement;
    }

    private ConnectionHolder() throws SQLException {
    }

}