package org.example;

import org.example.Controller.PessoaController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        try {
            Properties properties = new Properties();
            properties.load(PessoaController.class.getClassLoader().getResourceAsStream("bd.properties"));

            String URL = properties.getProperty("db.url");
            String USER = properties.getProperty("db.user");
            String PASSWORD = properties.getProperty("db.password");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                System.out.println("Conexão com a database foi bem sucedida");
                PessoaController dadospop = new PessoaController(conn);
            }
        } catch (SQLException ex) {
            System.out.println("Exception ::" + ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}