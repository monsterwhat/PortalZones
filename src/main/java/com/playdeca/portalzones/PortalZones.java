package com.playdeca.portalzones;

import com.playdeca.portalzones.commands.PortalZoneCommand;
import com.playdeca.portalzones.listeners.PortalZoneListener;
import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.services.PortalZoneService;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public final class PortalZones extends JavaPlugin {

    private final HashMap<String, PortalZone> portalZones = new HashMap<>();
    @Override
    public void onEnable() {
        try {
            // Check if WorldEdit and WorldGuard are present
            if (!checkDependencies()) {
                getLogger().severe("WorldEdit and WorldGuard are required but not found. Disabling PortalZones.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            saveDefaultConfig();
            getLogger().info("PortalZones configuration file has been loaded.");
            // Load the configuration file
            loadPortalZones();
            getLogger().info("PortalZones portal zones have been loaded.");

            getServer().getPluginManager().registerEvents(new PortalZoneListener(this), this);
            getLogger().info("PortalZones listener has been instantiated.");

            // Register the main portal zone's command with alias "/pz" and "/portalzone"
            getCommand("portalzones").setExecutor(new PortalZoneCommand());
            getCommand("pz").setExecutor(new PortalZoneCommand());;
            getLogger().info("PortalZones command has been registered.");

            getLogger().info("PortalZones has been enabled.");


        }catch (Exception e) {
            getLogger().warning("An error occurred while enabling PortalZones.");
            getLogger().warning(e.getMessage());
            getLogger().info("Disabling PortalZones.");
            getServer().getPluginManager().disablePlugin(this);
        }
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

    public void loadPortalZones() {
        try {
            // clear portalZones before repopulating
            portalZones.clear();
            // Get the portal zones section from the configuration
            ConfigurationSection portalZonesSection = getConfig().getConfigurationSection("portalZones");
            // Check if the portal zones section is null
            if (portalZonesSection != null) {
                // Save the new portal zone to the configuration
                File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                for (String name : portalZonesSection.getKeys(false)) {
                    // Load each portal zone and populate the portalZones map
                    PortalZone portalZone = PortalZoneService.loadFromConfig(config, name);
                    portalZones.put(name, portalZone);
                }
            }
            getLogger().info("PortalZones portal zones have been loaded/reloaded.");
        }catch (Exception e) {
            getLogger().severe("An error occurred while loading the portal zones list.");
            getLogger().severe(e.getMessage());
        }
    }


}

