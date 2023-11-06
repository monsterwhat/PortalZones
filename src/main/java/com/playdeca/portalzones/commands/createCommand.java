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
            player.sendMessage(displayZoneName(zoneName));
            player.sendMessage(displayRegion1(newPortal.getRegion1()));
            player.sendMessage(displayRegion2(newPortal.getRegion2()));
            player.sendMessage(displaySoftCount(newPortal.getSoftCount()));
            player.sendMessage(displayHardCount(newPortal.getHardCount()));
            player.sendMessage(displayDestination1(newPortal.getXyz1().toString()));
            player.sendMessage(displayDestination2(newPortal.getXyz2().toString()));

            //re-Load zones from DB
            ZoneManager.getInstance().loadZones();
        }else {
            player.sendMessage("Usage: /pz create <name>");
        }
    }

}

