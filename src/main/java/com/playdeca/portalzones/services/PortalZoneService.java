package com.playdeca.portalzones.services;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.objects.PortalZoneDAO;
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
import java.util.ArrayList;
import java.util.Objects;

public class PortalZoneService {
    boolean insidePortalZone = false;
    private ArrayList<PortalZone> zones;
    private final TimerService timer;

    public PortalZoneService(PortalZones plugin) {
        this.timer = new TimerService(plugin);
        this.zones = new ArrayList<>();
        loadZonesDB();
    }

    public void loadZones(){
        this.zones = PortalZoneService.getAllPortalZonesComplete();
    }

    public void loadZonesDB(){
        this.zones = PortalZoneDAO.getAllPortalZones();
    }

    public void checkPortalZonesFull(Player player){
        try {
            boolean playerInPortalZone = false;

            // Loop through all the portal zones
            for (PortalZone portalZone : zones) {
                String regionName = portalZone.getRegion1();
                String regionName2 = portalZone.getRegion2();
                Location portalLocation = portalZone.getXyz1();
                Location portalLocation2 = portalZone.getXyz2();

                if (PortalZoneService.isPlayerWithinRegion(player, regionName)) {
                    if (!insidePortalZone) {
                        Bukkit.getLogger().info(player.getName() + " entered a portal zone");

                        // Get the softCount and hardCount from the PortalZone
                        int softCount = portalZone.getSoftCount();
                        int hardCount = portalZone.getHardCount();

                        // Start the timer
                        Bukkit.getLogger().info("Starting timers for " + player.getName() + " in region " + regionName);
                        startTimers(player, portalLocation, softCount, hardCount);
                        insidePortalZone = true; // Set the flag to true
                    }
                    playerInPortalZone = true;  // Set the flag to true as the player is within a portal zone
                }
                if (PortalZoneService.isPlayerWithinRegion(player, regionName2)){
                    if (!insidePortalZone) {
                        Bukkit.getLogger().info(player.getName() + " entered a portal zone");

                        // Get the softCount and hardCount from the PortalZone
                        int softCount = portalZone.getSoftCount();
                        int hardCount = portalZone.getHardCount();

                        // Start the timer
                        Bukkit.getLogger().info("Starting timers for " + player.getName() + " in region " + regionName2);
                        startTimers(player, portalLocation2, softCount, hardCount);
                        insidePortalZone = true; // Set the flag to true
                    }
                    playerInPortalZone = true;  // Set the flag to true as the player is within a portal zone
                }
            }

            // Check if the player left the portal zone
            if (!playerInPortalZone && insidePortalZone) {
                player.sendMessage("You have left a teleporting zone...");
                Bukkit.getLogger().info(player.getName() + " left the portalZone");
                timer.CancelTimers(player);
                insidePortalZone = false; // Set the flag to false
            }

        }catch (Exception e) {
            Bukkit.getLogger().warning("Error on checkPortalZonesFull: " + e.getMessage());
        }
    }

    void startTimers(Player player, Location portalLocation, int softCount, int hardCount){
        timer.startSoftTimer(player, portalLocation, softCount, hardCount);
    }

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

    public static ArrayList<PortalZone> getAllPortalZonesComplete(){
        try {

            File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            ArrayList<PortalZone> portalZonesList = new ArrayList<>();

            for (String name : Objects.requireNonNull(config.getConfigurationSection("portalZones")).getKeys(false)) {
                PortalZone portalZone = loadFromConfig(config, name);
                assert portalZone != null;
                portalZonesList.add(portalZone);
            }

            return portalZonesList;

        }catch (Exception e) {
            Bukkit.getLogger().warning("Error loading all portal zones");
            Bukkit.getLogger().warning(e.getMessage());
            return null;
        }
    }

    public static PortalZone loadZone(String name){
        try {
            File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            PortalZone portalZone = loadFromConfig(config, name);
            assert portalZone != null;

            return portalZone;
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error loading portal zone: " + name);
            Bukkit.getLogger().warning(e.getMessage());
            return null;
        }
    }

}
