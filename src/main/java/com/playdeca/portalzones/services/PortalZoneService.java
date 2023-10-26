package com.playdeca.portalzones.services;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.objects.PortalZone;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class PortalZoneService {

    public static boolean isPlayerWithinRegion(Player player, String regionName) {
        try {
            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

            return regions.getRegions().stream().anyMatch(region -> region.getId().equalsIgnoreCase(regionName));
        }catch (Exception e){
            Bukkit.getLogger().warning("Error checking if player is within region: " + regionName);
            Bukkit.getLogger().warning(e.getMessage());
            return false;
        }
    }

    public static PortalZone loadFromConfig(FileConfiguration config, String name) {
        try {
            String path = "portalZones." + name;
            String region1 = config.getString(path + ".region1");
            String region2 = config.getString(path + ".region2");
            int softCount = config.getInt(path + ".softCount");
            int hardCount = config.getInt(path + ".hardCount");
            Location xyz1 = Location.deserialize(Objects.requireNonNull(config.getConfigurationSection(path + ".xyz1")).getValues(true));
            Location xyz2 = Location.deserialize(Objects.requireNonNull(config.getConfigurationSection(path + ".xyz2")).getValues(true));

            return new PortalZone(name, region1, region2, softCount, hardCount, xyz1, xyz2);
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error loading portal zone: " + name);
            Bukkit.getLogger().warning(e.getMessage());
            return null;
        }
    }

    public static HashMap<String, Location> getAllPortalZones() {
        try {
            // Save the new portal zone to the configuration
            File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            HashMap<String, Location> portalZonesMap = new HashMap<>();

            for (String name : Objects.requireNonNull(config.getConfigurationSection("portalZones")).getKeys(false)) {
                PortalZone portalZone = loadFromConfig(config, name);
                assert portalZone != null;
                portalZonesMap.put(portalZone.getRegion1(), portalZone.getXyz1());
                portalZonesMap.put(portalZone.getRegion2(), portalZone.getXyz2());
            }

            return portalZonesMap;
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error loading all portal zones");
            Bukkit.getLogger().warning(e.getMessage());
            return null;
        }

    }

    public static int getSoftCountForPortalZone(String regionName) {
        try {
            FileConfiguration config = getPortalZonesConfig();
            String path = "portalZones." + regionName + ".softCount";
            if (config.contains(path)) {
                Bukkit.getLogger().info("Found softCount for " + regionName);
                return config.getInt(path);
            }
            Bukkit.getLogger().info("Did not find softCount for " + regionName);
            return 0; // Default value if not found.
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error getting soft count for portal zone: " + regionName);
            return 0;
        }
    }

    public static int getHardCountForPortalZone(String regionName) {
        try {
            FileConfiguration config = getPortalZonesConfig();
            String path = "portalZones." + regionName + ".hardCount";
            if (config.contains(path)) {
                Bukkit.getLogger().info("Found hardCount for " + regionName);
                return config.getInt(path);
            }
            Bukkit.getLogger().info("Did not find hardCount for " + regionName);
            return 0; // Default value if not found.
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error getting hard count for portal zone: " + regionName);
            return 0;
        }
    }

    public static FileConfiguration getPortalZonesConfig() {
        // Load the portal zones configuration
        File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
        return YamlConfiguration.loadConfiguration(configFile);
    }

}
