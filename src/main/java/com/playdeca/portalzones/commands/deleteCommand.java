package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.objects.ZoneManager;
import com.playdeca.portalzones.services.HelperService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class deleteCommand extends HelperService {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
                if(args[0].equalsIgnoreCase("delete")){
                    if(args.length == 2){
                        handleDeleteCommandDB(player, args, label);
                        return true;
                    }
                }
        }
        return false;
    }

    private void handleDeleteCommandDB(Player player, String[] args, String label) {
        if (args.length == 2) {
            String zoneName = args[1];

            try {
             // Load the list of portal zones from the configuration
                PortalZone portalZone = portalZoneDAO.readPortalZone(zoneName);
                if (portalZone != null) {
                    portalZoneDAO.deletePortalZone(zoneName);
                    player.sendMessage("Portal zone '" + zoneName + "' has been deleted.");
                    ZoneManager.getInstance().loadZones();
                }else {
                    player.sendMessage("Portal zone with the name '" + zoneName + "' not found.");
                }

                } catch (Exception e) {
                    Bukkit.getLogger().warning("Error deleting portal zone: " + e.getMessage());
                    player.sendMessage("Failed to delete the portal zone. Please check the console for errors.");
                }
        } else {
            player.sendMessage("Usage: /" + label + " delete <zoneName>");
        }
    }

}
