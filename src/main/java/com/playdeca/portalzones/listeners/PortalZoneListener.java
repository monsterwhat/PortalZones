package com.playdeca.portalzones.listeners;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.objects.PortalZone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PortalZoneListener implements Listener {
    private final PortalZones plugin;
    private final HashMap<String, Location> portalZones;
    private Map<Player, BukkitTask> playerTimers = new HashMap<>();
    BukkitTask SoftTimer, HardTimer;


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
            boolean insidePortalZone = false; // Initialize the flag as false

            for (Map.Entry<String, Location> entry : portalZones.entrySet()) {
                String regionName = entry.getKey();
                Location portalLocation = entry.getValue();

                if (PortalZone.isWithinRegion(player, regionName)) {
                    if (!playerTimers.containsKey(player)) {
                        Bukkit.getLogger().info(player.getName() + " entered a portal zone");

                        // Get the softCount and hardCount from the PortalZone (You need a way to identify which PortalZone this is)
                        int softCount = PortalZone.getSoftCountForPortalZone(regionName);
                        int hardCount = PortalZone.getHardCountForPortalZone(regionName);

                        // Start the timer
                        Bukkit.getLogger().info("Starting timers for " + player.getName() + " in region " + regionName);
                        startTimers(player, portalLocation, softCount, hardCount);
                    }

                    insidePortalZone = true; // Set the flag to true
                    break;
                }
            }

            // Check if the player left all portal zones and cancel timers if necessary
            if (!insidePortalZone && playerTimers.containsKey(player)) {
                Bukkit.getLogger().info(player.getName() + " left a portal zone");
                cancelTimers(player);
            }

        } catch (Exception e) {
            Bukkit.getLogger().info("Error on checkPortalZones: " + e.getMessage());
        }
    }

    private void startTimers(Player player, Location portalLocation, int softCount, int hardCount) {
        // Check if there is no existing timer for the player
        if (!playerTimers.containsKey(player)) {

            AtomicInteger softCountTime = new AtomicInteger(softCount);
            AtomicInteger hardCountTime = new AtomicInteger(hardCount);

            BukkitTask softCountTask = null;
            AtomicReference<BukkitTask> hardCountTask = new AtomicReference<>(null);

            // Start the softCount timer
            if (softCountTime.get() > 0) {
                BukkitTask finalSoftCountTask = softCountTask;
                softCountTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    // Decrement the softCount time and check if it's zero or negative
                    if (softCountTime.decrementAndGet() <= 0) {
                        // SoftCount is done; start the hardCount timer
                        if (hardCountTime.get() > 0) {
                            // Notify the player that they entered a teleporting zone
                            player.sendMessage("Entered a teleporting zone...");
                            // Start the hardCount timer
                            hardCountTask.set(Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                                // Decrement the hardCount time and check if it's zero or negative
                                if (hardCountTime.decrementAndGet() <= 0) {
                                    // Teleport the player to the specified portal location
                                    player.teleport(portalLocation);
                                    if (hardCountTask.get() != null) {
                                        // Cancel the hardCount timer if it exists
                                        hardCountTask.get().cancel();
                                    }
                                } else {
                                    // Notify the player about the remaining teleport time
                                    player.sendMessage("Teleporting in " + hardCountTime.get() + " seconds...");
                                }
                            }, 0L, 20L));
                        } else {
                            // Teleport the player to the specified portal location immediately
                            player.teleport(portalLocation);
                        }
                        // Cancel the softCount timer
                        if (finalSoftCountTask != null) {
                            finalSoftCountTask.cancel();
                        }
                    }
                }, 0L, 20L);
            }

            // Store the timers for the player in the playerTimers map
            playerTimers.put(player, softCountTask);
        }
    }

    void startTimers2(){

    }

    void startSoftTimer(Player player, Location portalLocation, int softCount, int hardCount){
        SoftTimer = new BukkitRunnable() {
            int counter = softCount;

            @Override
            public void run() {
                if (counter <= 0) {
                    this.cancel();
                    // SoftCount is done; start the hardCount timer
                    if (hardCount > 0) {
                        player.sendMessage("Entered a teleporting zone...");
                        startHardTimer(player, portalLocation, hardCount);
                    } else {
                        player.teleport(portalLocation);
                    }
                } else {
                    player.sendMessage("Teleporting in " + counter + " seconds...");
                    counter--;
                }
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }

    private void startHardTimer(Player player, Location portalLocation, int hardCount){
        HardTimer = new BukkitRunnable() {
            int counter = hardCount;

            @Override
            public void run() {
                if (counter <= 0) {
                    this.cancel();
                    player.teleport(portalLocation);
                } else {
                    player.sendMessage("Teleporting in " + counter + " seconds...");
                    counter--;
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 0L, 20L);
    }

    public void stopSoftTimer() {
        if (SoftTimer != null) {
            SoftTimer.cancel();
        }
    }

    public void stopHardTimer(){
        if (HardTimer != null) {
            HardTimer.cancel();
        }
    }


    private void cancelTimers(Player player) {
        try {
            // Cancel any existing timers for the player
            if (playerTimers.containsKey(player)) {
                BukkitTask task = playerTimers.get(player);
                if (task != null) {
                    task.cancel();
                }
                playerTimers.remove(player);
            }
        }catch (Exception e) {
            Bukkit.getLogger().info("Error on cancelTimers: " + e.getMessage());
        }

    }


}
