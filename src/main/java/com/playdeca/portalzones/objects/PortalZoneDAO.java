package com.playdeca.portalzones.objects;
import com.playdeca.portalzones.PortalZones;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.*;
import java.util.ArrayList;

public class PortalZoneDAO {
    private static JavaPlugin plugin;
    private static DatabaseManager databaseManager;

    public PortalZoneDAO(PortalZones portalZones) {
        plugin = portalZones;
        databaseManager = DatabaseManager.getInstance(plugin);
    }

    public void checkIfDBExistsIfNotCreate(){
        if (!tableExists()) {
            createTable();
        }
    }

    public boolean tableExists() {
        try {
            databaseManager.connect();
            Connection connection = DatabaseManager.getInstance(plugin).getConnection();
            if (connection == null) {
                return false;
            }

            ResultSet tables = connection.getMetaData().getTables(null, null, "portal_zone", null);
            return tables.next();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Error checking if portal_zone table exists");
            Bukkit.getLogger().warning(e.getMessage());
            return false; // Return false in case of an error
        }finally {
            databaseManager.disconnect();
        }
    }

    public void createTable() {
        try {
            databaseManager.connect();
            Connection connection = DatabaseManager.getInstance(plugin).getConnection();
            Statement statement = connection.createStatement();
            // Define the table schema with an auto-incremented ID field
            String sql = "CREATE TABLE IF NOT EXISTS portal_zone (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Auto-incremented ID
                    "name TEXT, " +
                    "region1 TEXT, " +
                    "region2 TEXT, " +
                    "softCount INTEGER, " +
                    "hardCount INTEGER, " +
                    "xyz1_world TEXT, " +
                    "xyz1_x REAL, " +
                    "xyz1_y REAL, " +
                    "xyz1_z REAL, " +
                    "xyz2_world TEXT, " +
                    "xyz2_x REAL, " +
                    "xyz2_y REAL, " +
                    "xyz2_z REAL)";
            statement.execute(sql);
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Error creating portal_zone table");
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            databaseManager.disconnect();
        }
    }

    public void createPortalZone(PortalZone portalZone) {
        try {
            databaseManager.connect();
            Connection connection = DatabaseManager.getInstance(plugin).getConnection();
            String sql = "INSERT INTO portal_zone (name, region1, region2, softCount, hardCount, xyz1_world, xyz1_x, xyz1_y, xyz1_z, xyz2_world, xyz2_x, xyz2_y, xyz2_z) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, portalZone.getName());
            statement.setString(2, portalZone.getRegion1());
            statement.setString(3, portalZone.getRegion2());
            statement.setInt(4, portalZone.getSoftCount());
            statement.setInt(5, portalZone.getHardCount());
            statement.setString(6, portalZone.getWorld1().getName());
            statement.setDouble(7, portalZone.getXyz1().getX());
            statement.setDouble(8, portalZone.getXyz1().getY());
            statement.setDouble(9, portalZone.getXyz1().getZ());
            statement.setString(10, portalZone.getWorld2().getName());
            statement.setDouble(11, portalZone.getXyz2().getX());
            statement.setDouble(12, portalZone.getXyz2().getY());
            statement.setDouble(13, portalZone.getXyz2().getZ());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Error saving portal zone: " + portalZone.getName());
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            databaseManager.disconnect();
        }
    }

    public PortalZone readPortalZone(String name) {
        try {
            databaseManager.connect();
            Connection connection = DatabaseManager.getInstance(plugin).getConnection();
            String sql = "SELECT * FROM portal_zone WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String region1 = resultSet.getString("region1");
                String region2 = resultSet.getString("region2");
                int softCount = resultSet.getInt("softCount");
                int hardCount = resultSet.getInt("hardCount");
                World xyz1World = Bukkit.getWorld(resultSet.getString("xyz1_world"));
                double xyz1X = resultSet.getDouble("xyz1_x");
                double xyz1Y = resultSet.getDouble("xyz1_y");
                double xyz1Z = resultSet.getDouble("xyz1_z");
                World xyz2World = Bukkit.getWorld(resultSet.getString("xyz2_world"));
                double xyz2X = resultSet.getDouble("xyz2_x");
                double xyz2Y = resultSet.getDouble("xyz2_y");
                double xyz2Z = resultSet.getDouble("xyz2_z");
                Location xyz1 = new Location(xyz1World, xyz1X, xyz1Y, xyz1Z);
                Location xyz2 = new Location(xyz2World, xyz2X, xyz2Y, xyz2Z);
                return new PortalZone(id, name, region1, region2, softCount, hardCount, xyz1, xyz1World, xyz2, xyz2World);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Error reading portal zone: " + name);
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            databaseManager.disconnect();
        }
        return null;
    }


    public void updatePortalZone(PortalZone portalZone) {
        try {
            databaseManager.connect();
            Connection connection = DatabaseManager.getInstance(plugin).getConnection();
            String sql = "UPDATE portal_zone SET region1 = ?, region2 = ?, softCount = ?, hardCount = ?, " +
                    "xyz1_world = ?, xyz1_x = ?, xyz1_y = ?, xyz1_z = ?, " +
                    "xyz2_world = ?, xyz2_x = ?, xyz2_y = ?, xyz2_z = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, portalZone.getRegion1());
            statement.setString(2, portalZone.getRegion2());
            statement.setInt(3, portalZone.getSoftCount());
            statement.setInt(4, portalZone.getHardCount());
            statement.setString(5, portalZone.getXyz1().getWorld().getName());  // Extract world name
            statement.setDouble(6, portalZone.getXyz1().getX());
            statement.setDouble(7, portalZone.getXyz1().getY());
            statement.setDouble(8, portalZone.getXyz1().getZ());
            statement.setString(9, portalZone.getXyz2().getWorld().getName());  // Extract world name
            statement.setDouble(10, portalZone.getXyz2().getX());
            statement.setDouble(11, portalZone.getXyz2().getY());
            statement.setDouble(12, portalZone.getXyz2().getZ());
            statement.setString(13, portalZone.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Error updating portal zone: " + portalZone.getName());
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            databaseManager.disconnect();
        }
    }


    public void deletePortalZone(String name) {
        try {
            databaseManager.connect();
            Connection connection = DatabaseManager.getInstance(plugin).getConnection();
            String sql = "DELETE FROM portal_zone WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Error deleting portal zone: " + name);
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            databaseManager.disconnect();
        }
    }

    public static ArrayList<PortalZone> getAllPortalZones() {
        ArrayList<PortalZone> portalZones = new ArrayList<>();
        try {
            databaseManager.connect();
            Connection connection = DatabaseManager.getInstance(plugin).getConnection();
            String sql = "SELECT * FROM portal_zone";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String region1 = resultSet.getString("region1");
                String region2 = resultSet.getString("region2");
                int softCount = resultSet.getInt("softCount");
                int hardCount = resultSet.getInt("hardCount");
                World xyz1World = Bukkit.getWorld(resultSet.getString("xyz1_world"));
                double xyz1X = resultSet.getDouble("xyz1_x");
                double xyz1Y = resultSet.getDouble("xyz1_y");
                double xyz1Z = resultSet.getDouble("xyz1_z");
                World xyz2World = Bukkit.getWorld(resultSet.getString("xyz2_world"));
                double xyz2X = resultSet.getDouble("xyz2_x");
                double xyz2Y = resultSet.getDouble("xyz2_y");
                double xyz2Z = resultSet.getDouble("xyz2_z");
                Location xyz1 = new Location(xyz1World, xyz1X, xyz1Y, xyz1Z);
                Location xyz2 = new Location(xyz2World, xyz2X, xyz2Y, xyz2Z);
                portalZones.add(new PortalZone(id, name, region1, region2, softCount, hardCount, xyz1, xyz1World, xyz2, xyz2World));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Error retrieving portal zones");
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            databaseManager.disconnect();
        }
        return portalZones;
    }

}
