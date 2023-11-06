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

                player.sendMessage(displayZoneName(zone.getName()));
                player.sendMessage(displayRegion1(zone.getRegion1()));
                player.sendMessage(displayRegion2(zone.getRegion2()));
                player.sendMessage(displaySoftCount(zone.getSoftCount()));
                player.sendMessage(displayHardCount(zone.getHardCount()));
                player.sendMessage(displayDestination1(zone.getXyz1().toString()));
                player.sendMessage(displayDestination2(zone.getXyz2().toString()));

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
