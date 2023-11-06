package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.objects.ZoneManager;
import com.playdeca.portalzones.services.HelperService;
import com.playdeca.portalzones.objects.PortalZone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class createCommand extends HelperService {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if(args[0].equalsIgnoreCase("create")){
                handleCreateCommandDB(player, args);
                return true;
            }
        }
        return false;
    }

    private void handleCreateCommandDB(Player player, String[] args){
        if (args.length == 2) {
            String zoneName = args[1];
            PortalZone newPortal = new PortalZone(zoneName, player.getWorld());
            portalZoneDAO.createPortalZone(newPortal);
            ZoneManager.getInstance().setSelectedZone(newPortal);
            player.sendMessage("Portal Zone created and selected: " + zoneName);
            //re-Load zones from DB
            ZoneManager.getInstance().loadZones();
        }else {
            player.sendMessage("Usage: /pz create <name>");
        }
    }

}

