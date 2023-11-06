package com.playdeca.portalzones.objects;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private Connection connection;
    private static final String DATABASE_NAME = "portal_zone.db";
    private final JavaPlugin plugin;
    private final File dataFolder;
    private final File databaseFile;
    private static DatabaseManager instance;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
        dataFolder = plugin.getDataFolder();
        databaseFile = new File(dataFolder, DATABASE_NAME);
    }

    public static DatabaseManager getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new DatabaseManager(plugin);
        }
        return instance;
    }

    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load the SQLite JDBC driver
                Class.forName("org.sqlite.JDBC");

                if (!databaseFileExists()) {
                    // If it doesn't exist, create it
                    createDatabaseFile();
                }

                // Connect to the database
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
            }
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().warning("Error connecting to database.");
            plugin.getLogger().warning(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Error closing database connection.");
            plugin.getLogger().warning(e.getMessage());
        }
    }

    private boolean databaseFileExists() {
        File databaseFile = new File(DATABASE_NAME);
        return databaseFile.exists();
    }

    private void createDatabaseFile() {
        try {
            if (!dataFolder.exists()) {
                if(dataFolder.mkdirs()){
                    plugin.getLogger().info("Data folder created: " + dataFolder.getAbsolutePath());
                }else{
                    plugin.getLogger().warning("Data folder could not be created: " + dataFolder.getAbsolutePath());
                }
            }

            if (databaseFile.createNewFile()) {
                plugin.getLogger().info("Database file created: " + databaseFile.getAbsolutePath());
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Error creating database file.");
            plugin.getLogger().warning(e.getMessage());
        }
    }

}