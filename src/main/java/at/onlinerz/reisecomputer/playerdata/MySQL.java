package at.onlinerz.reisecomputer.playerdata;

import at.onlinerz.reisecomputer.Reisecomputer;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQL {

    public static void testDataSource(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1000)) {
                throw new SQLException("Es konnte keine MySQL-Verbindung hergestellt werden.");
            } else {
                Reisecomputer.pl.getLogger().info("MySQL-Verbindung wurde erfolgreich hergestellt!");
            }
        }
    }

    public static void setupDatabase() throws SQLException, IOException {
        // first lets read our setup file.
        // This file contains statements to create our inital tables.
        // it is located in the resources.
        String setup;
        try (InputStream in = Reisecomputer.pl.getClass().getClassLoader().getResourceAsStream("dbsetup.sql")) {
            setup = new String(in.readAllBytes());
        } catch (IOException e) {
            Reisecomputer.pl.getLogger().log(Level.SEVERE, "Datenbank-Datei konnte nicht gelesen werden.", e);
            throw e;
        }
        String[] queries = setup.split(";");
        // execute each query to the database.
        for (String query : queries) {
            // If you use the legacy way you have to check for empty queries here.
            if (query.isBlank()) continue;
            try (Connection conn = Reisecomputer.dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.execute();
            }
        }
        Reisecomputer.pl.getLogger().info("Datenbank-Setup abgeschlossen.");
    }
}
