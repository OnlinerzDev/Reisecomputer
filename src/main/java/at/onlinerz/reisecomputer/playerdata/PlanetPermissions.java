package at.onlinerz.reisecomputer.playerdata;

import at.onlinerz.reisecomputer.Reisecomputer;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlanetPermissions {

    public static void addPlanetPermission(UUID uuid, String planet) {
        try (Connection conn = Reisecomputer.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO travelcomp_permissions(uuid, planet) VALUES(?, ?)")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, planet);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            Reisecomputer.pl.getLogger().info("Planet-Permission '"+planet+"' von "+uuid+" konnte in der Datenbank nicht registriert werden.");
        }
    }

    public static Boolean hasPlanetPermission(UUID uuid, String planet) {
        try (Connection conn = Reisecomputer.dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT planet FROM travelcomp_permissions WHERE uuid = ?;"
        )) {
            stmt.setString(1, uuid.toString());
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()) {
                if(resultSet.getString("planet").equalsIgnoreCase(planet)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            return false;
        }

        return false;
    }


}
