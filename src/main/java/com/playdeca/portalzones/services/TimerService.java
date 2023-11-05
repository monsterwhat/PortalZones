package com.playdeca.portalzones.services;

import com.playdeca.portalzones.PortalZones;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TimerService {
    BukkitTask SoftTimer, HardTimer, countDown;
    public BossBar timeBar = Bukkit.createBossBar("", org.bukkit.boss.BarColor.YELLOW, org.bukkit.boss.BarStyle.SOLID);
    private final PortalZones plugin;
    public TimerService(PortalZones plugin) {
        this.plugin = plugin;
    }

    public void startSoftTimer(Player player, Location portalLocation, int softCount, int hardCount){
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

        SoftTimer = new BukkitRunnable() {
            @Override
            public void run() {
                startHardTimer(player, hardCount, portalLocation);
            }
        }.runTaskLaterAsynchronously(plugin, 20L*softCount);
    }

    public void startHardTimer(Player player, int hardCount, Location portalLocation){
        startCountdown(player, hardCount);

        HardTimer = new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(portalLocation);
                player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
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
                        CancelTimers(player);
                    }
                }catch (Exception e){
                    Bukkit.getLogger().info("Error on startCountdown: " + e.getMessage());
                    Bukkit.getLogger().warning("Error on startCountdown: " + e.getLocalizedMessage());
                }
            }
        }.runTaskTimerAsynchronously(plugin,0L,20L);
    }

    public void CancelTimers(Player player){
        cancelSoftTimer();
        cancelHardTimer();
        cancelCountDown();
        timeBar.setProgress(0);
        timeBar.removePlayer(player);
    }

    public void cancelSoftTimer(){
        if (SoftTimer != null) {
            SoftTimer.cancel();
        }
    }

    public void cancelHardTimer(){
        if (HardTimer != null) {
            HardTimer.cancel();
        }
    }

    public void cancelCountDown(){
        if (countDown != null) {
            countDown.cancel();
        }
    }

}
