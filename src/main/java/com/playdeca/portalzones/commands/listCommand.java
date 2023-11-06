package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.objects.ZoneManager;
import com.playdeca.portalzones.services.HelperService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;

public class listCommand extends HelperService {

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
                player.sendMessage(displayZoneName(zone.getName()));
                player.sendMessage(displayRegion1(zone.getRegion1()));
                player.sendMessage(displayRegion2(zone.getRegion2()));
                player.sendMessage(displaySoftCount(zone.getSoftCount()));
                player.sendMessage(displayHardCount(zone.getHardCount()));
                player.sendMessage(displayDestination1(zone.getWorld1() + ", " + zone.getXyz1().getX() + ", " + zone.getXyz1().getY() + ", " + zone.getXyz1().getZ()));
                player.sendMessage(displayDestination2(zone.getWorld2() + ", " + zone.getXyz2().getX() + ", " + zone.getXyz2().getY() + ", " + zone.getXyz2().getZ()));
            }

        }catch (Exception e){
            player.sendMessage("Error: " + e);
            Bukkit.getLogger().warning("Error: " + e);
            Bukkit.getLogger().warning("Error: " + Arrays.toString(e.getStackTrace()));
        }
    }



}
