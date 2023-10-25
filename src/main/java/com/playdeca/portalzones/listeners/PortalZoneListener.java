package com.playdeca.portalzones.listeners;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.services.PortalZoneService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class PortalZoneListener implements Listener {
    private final PortalZones plugin;
    private final HashMap<String, Location> portalZones;
    BukkitTask SoftTimer, HardTimer, countDown;
    boolean insidePortalZone = false;
    BossBar timeBar = Bukkit.createBossBar("", org.bukkit.boss.BarColor.YELLOW, org.bukkit.boss.BarStyle.SOLID);

    public PortalZoneListener(PortalZones plugin) {
        this.plugin = plugin;
        this.portalZones = PortalZoneService.getAllPortalZones();
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            Player player = event.getPlayer();
            checkPortalZones(player);
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error on onPlayerMove: " + e.getMessage());
        }
    }

    private void checkPortalZones(Player player) {
        try {
            boolean playerInPortalZone = false;  // Initialize a flag to track if the player is within any portal zone

            for (Map.Entry<String, Location> entry : portalZones.entrySet()) {
                String regionName = entry.getKey();
                Location portalLocation = entry.getValue();

                if (PortalZoneService.isWithinRegion(player, regionName)) {
                    if (!insidePortalZone) {
                        Bukkit.getLogger().info(player.getName() + " entered a portal zone");

                        // Get the softCount and hardCount from the PortalZone
                        int softCount = PortalZoneService.getSoftCountForPortalZone(regionName);
                        int hardCount = PortalZoneService.getHardCountForPortalZone(regionName);

                        // Start the timer
                        Bukkit.getLogger().info("Starting timers for " + player.getName() + " in region " + regionName);
                        startTimers(player, portalLocation, softCount, hardCount);
                        insidePortalZone = true; // Set the flag to true
                    }
                    playerInPortalZone = true;  // Set the flag to true as the player is within a portal zone
                }
            }
            // Check if the player left all portal zones
            if (!playerInPortalZone && insidePortalZone) {
                // Player left the portal zone
                player.sendMessage("You have left a teleporting zone...");
                Bukkit.getLogger().info(player.getName() + " left the portalZone");
                CancelTimers(player);
                insidePortalZone = false; // Set the flag to false
            }

        } catch (Exception e) {
            Bukkit.getLogger().info("Error on checkPortalZones: " + e.getMessage());
        }
    }

    void startTimers(Player player, Location portalLocation, int softCount, int hardCount){
        startSoftTimer(player, portalLocation, softCount, hardCount);
    }

    void startSoftTimer(Player player, Location portalLocation, int softCount, int hardCount){
        if(softCount == 0){
            softCount = 20;
        }
        player.sendMessage("Entered a teleporting zone...");

        SoftTimer = new BukkitRunnable() {
            @Override
            public void run() {
            startHardTimer(player, hardCount, portalLocation);
            }
        }.runTaskLaterAsynchronously(this.plugin, 20L*softCount);
    }

    private void startHardTimer(Player player, int hardCount, Location portalLocation){
        if(hardCount == 0){
            hardCount = 20;
        }
        Bukkit.getLogger().info("Starting hard timer for " + player.getName() + " in region " + portalLocation);
        startCountdown(player, hardCount);
        HardTimer = new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(portalLocation);
            }
        }.runTaskLater(this.plugin, 20L*hardCount);
    }

    public void startCountdown(Player player, int countDownTime){
        countDown = new BukkitRunnable() {
            int countDownLimit = countDownTime;
            @Override
            public void run() {
                try {
                    if(!timeBar.getPlayers().contains(player)){
                        timeBar.addPlayer(player);
                    }

                    if (countDownLimit > 0){
                        var timeLeft = countDownLimit--;
                        //player.sendMessage("Teleporting in " + timeLeft + "s ...");
                        timeBar.setTitle("Teleporting in " + timeLeft + "s ...");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        timeBar.setProgress((double) timeLeft / countDownTime);
                    }else{
                        player.sendMessage("Teleporting...");
                        player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
                        countDown.cancel();
                    }
                }catch (Exception e){
                    Bukkit.getLogger().info("Error on startCountdown: " + e.getMessage());
                }
            }
        }.runTaskTimerAsynchronously(plugin,0L,20L);
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

    public void stopCountDown(){
        if (countDown != null) {
            countDown.cancel();
        }
    }

    public void CancelTimers(Player player){
        stopSoftTimer();
        stopHardTimer();
        stopCountDown();
        timeBar.setProgress(0);
        timeBar.removePlayer(player);
    }


}
