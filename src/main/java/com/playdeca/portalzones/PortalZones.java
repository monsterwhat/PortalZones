package com.playdeca.portalzones;

import com.playdeca.portalzones.commands.*;
import com.playdeca.portalzones.listeners.PortalZoneListener;
import com.playdeca.portalzones.objects.PortalZoneDAO;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;

public final class PortalZones extends JavaPlugin {
    private PortalZoneCommand PortalZoneCommand;

    @Override
    public void onEnable() {
        try {
            // Check if WorldEdit and WorldGuard are present
            if (!checkDependencies()) {
                getLogger().severe("WorldEdit and WorldGuard are required but not found. Disabling PortalZones.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            // Initialize PortalZoneDAO
            PortalZoneDAO portalZoneDAO = new PortalZoneDAO(this);
            // Check if the database exists, if not create it
            portalZoneDAO.checkIfDBExistsIfNotCreate();

            PortalZoneCommand = new PortalZoneCommand();

            getServer().getPluginManager().registerEvents(new PortalZoneListener(this), this);
            getLogger().info("PortalZones listener has been instantiated.");

            registerCommands();

            getLogger().info("PortalZones has been enabled.");

        }catch (Exception e) {
            getLogger().warning("An error occurred while enabling PortalZones.");
            getLogger().warning(e.getMessage());
            getLogger().warning("Please report this error to the developer.");
            getLogger().warning(Arrays.toString(e.getStackTrace()));
            getLogger().info("Disabling PortalZones.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public void registerCommands(){
        this.getCommand("pz").setExecutor(PortalZoneCommand);
        this.getCommand("portalzone").setExecutor(PortalZoneCommand);
        getLogger().info("PortalZones commands have been registered.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("PortalZones is being disabled.");
    }

    private boolean checkDependencies() {
        try {
            // Check if WorldEdit is available
            if (getServer().getPluginManager().getPlugin("WorldEdit") == null) {
                getLogger().severe("WorldEdit is required but not found.");
                return false;
            }

            // Check if WorldGuard is available
            if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
                getLogger().severe("WorldGuard is required but not found.");
                return false;
            }

            return true; // Both dependencies are present
        }catch (Exception e) {
            getLogger().severe("An error occurred while checking dependencies.");
            getLogger().severe(e.getMessage());
            return false;
        }
    }



}

