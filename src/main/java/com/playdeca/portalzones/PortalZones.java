package com.playdeca.portalzones;

import com.playdeca.portalzones.commands.CreatePortalZone;
import com.playdeca.portalzones.commands.ListPortalZones;
import com.playdeca.portalzones.listeners.PortalZoneListener;
import com.playdeca.portalzones.objects.PortalZone;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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

            // Load the configuration file
            saveDefaultConfig();
            getLogger().info("PortalZones configuration file has been loaded.");
            // Load portal zones from the configuration
            loadPortalZones();
            getLogger().info("PortalZones portal zones have been loaded.");

            getServer().getPluginManager().registerEvents(new PortalZoneListener(this), this);
            getLogger().info("PortalZones listener has been instantiated.");

            // Initialize your plugin, set up event listeners, and other necessary initialization.
            getLogger().info("PortalZones has been enabled.");

            // Register the command to create Portal Zones.
            getCommand("pzcreate").setExecutor(new CreatePortalZone());
            // Register the command to list Portal Zones.
            getCommand("pzlist").setExecutor(new ListPortalZones());


        }catch (Exception e) {
            getLogger().warning("An error occurred while enabling PortalZones.");
            getLogger().warning(e.getMessage());
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

    private void loadPortalZones() {
        try {
            ConfigurationSection portalZonesSection = getConfig().getConfigurationSection("portalZones");
            if (portalZonesSection != null) {
                // Save the new portal zone to the configuration
                File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                for (String name : portalZonesSection.getKeys(false)) {
                    // Load each portal zone and populate the portalZones map
                    PortalZone portalZone = PortalZone.loadFromConfig(config, name);
                    portalZones.put(name, portalZone);
                }
            }
        }catch (Exception e) {
            getLogger().severe("An error occurred while loading the portal zones list.");
            getLogger().severe(e.getMessage());
        }

    }


}

