package com.acme.eshop.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static java.lang.System.exit;


public class SqlRepository {
    private static final Logger logger = LoggerFactory.getLogger(SqlRepository.class);
    private static final Properties sqlCommands = new Properties();

    // parse sql queries once for the whole run
    static {
        try (InputStream inputStream = SqlRepository.class.getClassLoader().getResourceAsStream(
                "sql.properties")) {
            if (inputStream == null) {
                logger.info("Sorry, unable to find sql.properties, exiting application.");
                // Abnormal exit
                exit(-1);
            }

            //load a properties file from class path, inside static method
            sqlCommands.load(inputStream);
        } catch (IOException ex) {
            logger.info("Sorry, unable to parse sql.properties, exiting application.", ex);
            // Abnormal exit
            exit(-1);
        }
    }

    public SqlRepository() {
    }

    public static String get(String command) {
        return sqlCommands.getProperty(command);
    }

    private static void runSqlTableCommands(String command, boolean dropped) {
        try (Connection connection = DataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
            logger.info("{} table command was successful.", dropped ? "Drop" : "Create");
        } catch (SQLException ex) {
            if (dropped = true) {
                logger.warn("Tables do not exist anyway");
            } else {
                logger.error("Error while creating table.", ex);
                exit(-1);
            }
        }
    }

    private static void createTables() {

        List.of(
                        SqlRepository.get("create.table.customer"),
                        SqlRepository.get("create.table.product"),
                        SqlRepository.get("create.table.order"),
                        SqlRepository.get("create.table.order_item"))
                .forEach(c -> runSqlTableCommands(c, false));
    }

    private static void dropTables() {

        List.of(SqlRepository.get("drop.table.customer"),
                        SqlRepository.get("drop.table.product"),
                        SqlRepository.get("drop.table.order"),
                        SqlRepository.get("drop.table.order_item"))
                .forEach(c -> runSqlTableCommands(c, true));

    }

    public void dropAndCreateTables() {
        dropTables();
        createTables();
        logger.info("Dropped and created database tables");
    }
}

