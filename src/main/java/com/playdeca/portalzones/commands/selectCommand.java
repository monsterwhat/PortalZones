package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.objects.ZoneManager;
import com.playdeca.portalzones.services.HelperService;
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

    private void handleSelectCommandDB(Player player, String[] args){
        if (args.length == 2) {
            try {

                if(ZoneManager.getInstance().getSelectedZone() != null){
                    ZoneManager.getInstance().setSelectedZone(null);
                }
                String zoneName = args[1];
                PortalZone zone = portalZoneDAO.readPortalZone(zoneName);
                if(zone == null){
                    player.sendMessage("Portal Zone not found: " + zoneName);
                    return;
                }
                ZoneManager.getInstance().setSelectedZone(zone);

                player.sendMessage(displayZoneName(zone));
                player.sendMessage(displayRegion1(zone));
                player.sendMessage(displayRegion2(zone));
                player.sendMessage(displaySoftCount(zone));
                player.sendMessage(displayHardCount(zone));
                player.sendMessage(displayDestination1(zone));
                player.sendMessage(displayDestination2(zone));

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
