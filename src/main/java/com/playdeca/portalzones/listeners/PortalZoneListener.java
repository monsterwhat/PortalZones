package com.playdeca.portalzones.listeners;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.objects.PortalZone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class PortalZoneListener implements Listener {
    private final PortalZones plugin;
    private final HashMap<String, Location> portalZones;
    private Map<Player, BukkitTask> playerTimers = new HashMap<>();

    public PortalZoneListener(PortalZones plugin) {
        this.plugin = plugin;
        this.portalZones = PortalZone.getAllPortalZones();
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            Player player = event.getPlayer();
            Location from = event.getFrom();
            Location to = event.getTo();

            if (from != null && to != null && !from.getBlock().equals(to.getBlock())) {
                // Player moved, check if they entered a portal zone
                checkPortalZones(player);
            }
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error on onPlayerMove: " + e.getMessage());
        }

    }

    private void checkPortalZones(Player player) {
        try {
            for (Map.Entry<String, Location> entry : portalZones.entrySet()) {
                String regionName = entry.getKey();
                Location portalLocation = entry.getValue();

                if (PortalZone.isWithinRegion(player, regionName)) {
                    Bukkit.getLogger().info(player.getName() + " entered a portal zone");

                    // Get the softCount and hardCount from the PortalZone (You need a way to identify which PortalZone this is)
                    int softCount = PortalZone.getSoftCountForPortalZone(regionName);
                    int hardCount = PortalZone.getHardCountForPortalZone(regionName);

                    // Start the timer
                    startTimers(player, portalLocation, regionName, softCount, hardCount);
                    break;
                } else {
                    // Cancel timers if the player leaves the region
                    cancelTimers(player);
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Error on checkPortalZones: " + e.getMessage());
        }
    }

    private void startTimers(Player player, Location portalLocation, String regionName, int softCount, int hardCount) {
        try {
            // If there is an existing timer for the player, cancel it
            if (playerTimers.containsKey(player)) {
                cancelTimers(player);
            }

            // Start the softCount timer (not displayed to the player)
            BukkitTask softCountTask = new BukkitRunnable() {
                @Override
                public void run() {
                    // Start the hardCount timer and display it to the player
                    startHardCountTimer(player, portalLocation, regionName, hardCount);
                    this.cancel();
                }
            }.runTaskLater(plugin, softCount * 20L); // Convert seconds to ticks

            // Store the timers for the player
            playerTimers.put(player, softCountTask);
        }catch (Exception e) {
            Bukkit.getLogger().info("Error on startTimers: " + e.getMessage());
        }

    }

    private void startHardCountTimer(Player player, Location portalLocation, String regionName, int hardCount) {
        try {
            BukkitTask hardCountTask = new BukkitRunnable() {
                int timeLeft = hardCount;

                @Override
                public void run() {
                    if (timeLeft <= 0) {
                        // Teleport the player when hardCount reaches 0
                        player.teleport(portalLocation);
                        this.cancel();
                    } else {
                        player.sendMessage("Teleporting in " + timeLeft + " seconds...");
                        timeLeft--;

                        // Check if the player is still in the region; if not, cancel the timers
                        if (!PortalZone.isWithinRegion(player, regionName)) {
                            cancelTimers(player);
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);

            // Store the hardCount timer for the player
            playerTimers.put(player, hardCountTask);
        }catch (Exception e) {
            Bukkit.getLogger().info("Error on startHardCountTimer: " + e.getMessage());
        }
    }

    private void cancelTimers(Player player) {
        try {
            // Cancel any existing timers for the player
            if (playerTimers.containsKey(player)) {
                playerTimers.get(player).cancel();
                playerTimers.remove(player);
            }
        }catch (Exception e) {
            Bukkit.getLogger().info("Error on cancelTimers: " + e.getMessage());
        }

    }


}
