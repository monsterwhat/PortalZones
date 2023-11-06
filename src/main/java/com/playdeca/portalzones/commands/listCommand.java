package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.objects.ZoneManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;

public class listCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
                if(args[0].equalsIgnoreCase("list")){
                        handleListCommandDB(player);
                        return true;
                    }
                }
        return false;
    }

    private void handleListCommandDB(Player player) {

        try {
            ArrayList<PortalZone> zones = ZoneManager.getInstance().getZones();
            if(zones.isEmpty()){
                player.sendMessage("No portal zones found.");
            }
            player.sendMessage("Portal Zones: ");
            for (PortalZone zone : zones) {
                player.sendMessage("--------------------");
                player.sendMessage("Zone name: " + zone.getName());
                player.sendMessage("Region 1: " + zone.getRegion1());
                player.sendMessage("Region 2: " + zone.getRegion2());
                player.sendMessage("Soft Count: " + zone.getSoftCount());
                player.sendMessage("Hard Count: " + zone.getHardCount());
                player.sendMessage("Destination 1: World: " + zone.getWorld1() + ", " + zone.getXyz1().getX() + ", " + zone.getXyz1().getY() + ", " + zone.getXyz1().getZ());
                player.sendMessage("Destination 2: World: " + zone.getWorld2() + ", " + zone.getXyz2().getX() + ", " + zone.getXyz2().getY() + ", " + zone.getXyz2().getZ());
                player.sendMessage("--------------------");
            }

        }catch (Exception e){
            player.sendMessage("Error: " + e);
            Bukkit.getLogger().warning("Error: " + e);
            Bukkit.getLogger().warning("Error: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
