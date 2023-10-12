package com.playdeca.portalzones.objects;

import com.playdeca.portalzones.PortalZones;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class PortalZone {
    private String name, region1, region2;
    private int softCount, hardCount;
    private Location xyz1, xyz2;

    public PortalZone(String name, String region1, String region2, int softCount, int hardCount, Location xyz1, Location xyz2) {
        this.name = name;
        this.region1 = region1;
        this.region2 = region2;
        this.softCount = softCount;
        this.hardCount = hardCount;
        this.xyz1 = xyz1;
        this.xyz2 = xyz2;
    }

    public static boolean isWithinRegion(Player player, String regionName) {
        try {
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        return regions.getRegions().stream().anyMatch(region -> region.getId().equalsIgnoreCase(regionName));
        }catch (Exception e){
            Bukkit.getLogger().warning("Error checking if player is within region: " + regionName);
            return false;
        }
    }

    public void saveToConfig(FileConfiguration config) {
        try {
            String path = "portalZones." + name;
            config.set(path + ".region1", region1);
            config.set(path + ".region2", region2);
            config.set(path + ".softCount", softCount);
            config.set(path + ".hardCount", hardCount);
            config.set(path + ".xyz1", xyz1.serialize()); // Serialize the Location
            config.set(path + ".xyz2", xyz2.serialize());
        }catch (Exception e){
            Bukkit.getLogger().warning("Error saving portal zone: " + name);
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
            return null;
        }
    }

    public static HashMap<String, Location> getAllPortalZones() {
        try {
            // Save the new portal zone to the configuration
            File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            HashMap<String, Location> portalZonesMap = new HashMap<>();

            for (String name : config.getConfigurationSection("portalZones").getKeys(false)) {
                PortalZone portalZone = loadFromConfig(config, name);
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
                return config.getInt(path);
            }
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
                return config.getInt(path);
            }
            return 0; // Default value if not found.
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error getting hard count for portal zone: " + regionName);
            return 0;
        }
    }

    private static FileConfiguration getPortalZonesConfig() {
        // Load the portal zones configuration
        File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public String getName() {
        return name;
    }

    public String getRegion1() {
        return region1;
    }

    public String getRegion2() {
        return region2;
    }

    public int getSoftCount() {
        return softCount;
    }

    public int getHardCount() {
        return hardCount;
    }

    public Location getXyz1() {
        return xyz1;
    }

    public Location getXyz2() {
        return xyz2;
    }
}
