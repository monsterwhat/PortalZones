package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.services.HelperService;
import com.playdeca.portalzones.services.PortalZoneService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public class selectCommand extends HelperService {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
                if(args[0].equalsIgnoreCase("select")){
                    handleSelectCommandDB(player, args);
                    return true;
                }
        }
        return false;
    }

    private void handleSelectCommand(Player player, String[] args){
        if (args.length == 2) {
            try {
                if(selectedZone != null){
                    selectedZone = null;
                }
                String zoneName = args[1];
                //should check if the zone exists first...
                if(!config.contains("portalZones." + zoneName)){
                    player.sendMessage("Portal Zone not found: " + zoneName);
                    return;
                }
                selectedZone = PortalZoneService.loadZone(zoneName);
                if(selectedZone != null){
                    player.sendMessage("Portal Zone selected: " + zoneName);
                }else {
                    player.sendMessage("The selected zone is null. This cant be happening!");
                }
            }catch (Exception e){
                Bukkit.getLogger().warning("Error selecting portal zone: " + e.getMessage());
                Bukkit.getLogger().warning("Error selecting portal zone: " + Arrays.toString(e.getStackTrace()));
                player.sendMessage("Failed to select the portal zone. Please check the console for errors.");
            }
        }else {
            player.sendMessage("Usage: /pz select <PortalZoneName>");
        }
    }

    private void handleSelectCommandDB(Player player, String[] args){
        if (args.length == 2) {
            try {
                if(selectedZone != null){
                    selectedZone = null;
                }
                String zoneName = args[1];
                //should check if the zone exists first...
                PortalZone zone = portalZoneDAO.readPortalZone(zoneName);
                if(zone == null){
                    player.sendMessage("Portal Zone not found: " + zoneName);
                    return;
                }

                selectedZone = zone;
                player.sendMessage("Portal Zone selected: " + zoneName);

            }catch (Exception e){
                Bukkit.getLogger().warning("Error selecting portal zone: " + e.getMessage());
                Bukkit.getLogger().warning("Error selecting portal zone: " + Arrays.toString(e.getStackTrace()));
                player.sendMessage("Failed to select the portal zone. Please check the console for errors.");
            }
        }else {
            player.sendMessage("Usage: /pz select <PortalZoneName>");
        }
    }

}
