package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.helpers.ZonesHelper;
import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.services.PortalZoneService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public class selectCommand extends ZonesHelper {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
                if(args[0].equalsIgnoreCase("select")){
                    handleSelectCommand(player, args);
                    return true;
                }
        }
        return false;
    }

    private void handleSelectCommand(Player player, String[] args){
        if (args.length == 2) {
            try {
                String zoneName = args[1];
                //should check if the zone exists first...
                if(!config.contains("portalZones." + zoneName)){
                    player.sendMessage("Portal Zone not found: " + zoneName);
                    return;
                }
                selectedZone = PortalZoneService.loadZone(zoneName);
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
