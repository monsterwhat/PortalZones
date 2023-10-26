package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.helpers.ZonesHelper;
import com.playdeca.portalzones.objects.PortalZone;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Arrays;

public class createCommand extends ZonesHelper {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if(args[0].equalsIgnoreCase("create")){
                handleCreateCommand(player, args);
                return true;
            }
        }
        return false;
    }

    private void handleCreateCommand(Player player, String[] args){
        if(player!=null){
            if (args.length == 2) {
                try {
                    String zoneName = args[1];
                    PortalZone newPortal = new PortalZone(zoneName);
                    newPortal.saveToConfig(config);
                    config.save(configFile);
                    selectedZone = newPortal;
                    player.sendMessage("Portal Zone created and selected: " + zoneName);
                    portalZonesListener.loadZones();
                } catch (IOException e) {
                    Bukkit.getLogger().warning("Error saving portal zone: " + e.getMessage());
                    Bukkit.getLogger().warning("Error saving portal zone: " + Arrays.toString(e.getStackTrace()));
                    player.sendMessage("Failed to save the portal zone. Please check the console for errors.");
                }
            }else {
                player.sendMessage("Usage: /pz newcreate <name>");
            }
        }
    }


}

