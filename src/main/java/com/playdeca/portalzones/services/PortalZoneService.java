package com.playdeca.portalzones.services;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.objects.ZoneManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PortalZoneService {
    boolean insidePortalZone = false;
    private final TimerService timer;

    public PortalZoneService(PortalZones plugin) {
        this.timer = new TimerService(plugin);
    }

    public void checkPortalZonesFull(Player player){
        try {
            boolean playerInPortalZone = false;
            Location portalLocation;

            // Loop through all the portal zones
            for (PortalZone portalZone : ZoneManager.getInstance().getZones()) {
                String regionName1 = portalZone.getRegion1();
                String regionName2 = portalZone.getRegion2();

                if (PortalZoneService.isPlayerWithinRegion(player, regionName1) || PortalZoneService.isPlayerWithinRegion(player, regionName2)) {
                    if (!insidePortalZone) {
                        Bukkit.getLogger().info(player.getName() + " entered a portal zone");

                        if (PortalZoneService.isPlayerWithinRegion(player, regionName1)) {
                            portalLocation = portalZone.getXyz1();
                        } else {
                            //if not in region1, then in region2
                            portalLocation = portalZone.getXyz2();
                        }
                        int softCount = portalZone.getSoftCount();
                        int hardCount = portalZone.getHardCount();

                        // Start the timer
                        Bukkit.getLogger().info("Starting timers for " + player.getName());
                        startTimers(player, portalLocation, softCount, hardCount);
                        insidePortalZone = true;
                    }
                    playerInPortalZone = true;  // Set the flag to true as the player is within a portal zone
                }
            }

            // Check if the player left the portal zone
            if (!playerInPortalZone && insidePortalZone) {
                player.sendMessage("You have left a teleporting zone...");
                Bukkit.getLogger().info(player.getName() + " left the portalZone");
                timer.CancelTimers(player);
                insidePortalZone = false;
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

}
