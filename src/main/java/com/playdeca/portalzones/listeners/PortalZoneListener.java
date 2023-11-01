package com.playdeca.portalzones.listeners;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.services.PortalZoneService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.ArrayList;

import com.playdeca.portalzones.timers.ZoneTimers;

public class PortalZoneListener implements Listener {
    private ArrayList<PortalZone> zones;
    private final ZoneTimers timer;
    boolean insidePortalZone = false;

    public PortalZoneListener(PortalZones plugin) {
        //this.portalZones = new HashMap<>();
        this.zones = new ArrayList<>();
        this.timer = new ZoneTimers(plugin);
        loadZones();
    }

    public void loadZones(){
        this.zones = PortalZoneService.getAllPortalZonesComplete();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            Player player = event.getPlayer();
            //checkPortalZones(player);
            checkPortalZonesFull(player);
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error on onPlayerMove: " + e.getMessage());
        }
    }

    private void checkPortalZonesFull(Player player){
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

}
