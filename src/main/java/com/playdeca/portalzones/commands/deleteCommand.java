package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.helpers.ZonesHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class deleteCommand extends ZonesHelper {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
                if(args[0].equalsIgnoreCase("delete")){
                    if(args.length == 2){
                        handleDeleteCommand(player, args, label);
                        return true;
                    }
                }
        }
        return false;
    }

    private void handleDeleteCommand(Player player, String[] args, String label) {
        if (args.length == 2) {
            String zoneName = args[1];

            // Load the list of portal zones from the configuration
            File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            String path = "portalZones." + zoneName;

            if (config.contains(path)) {
                config.set(path, null); // Remove the portal zone from the configuration
                try {
                    config.save(configFile);
                    player.sendMessage("Portal zone '" + zoneName + "' has been deleted.");
                    portalZonesListener.loadZones();
                } catch (Exception e) {
                    Bukkit.getLogger().warning("Error deleting portal zone: " + e.getMessage());
                    player.sendMessage("Failed to delete the portal zone. Please check the console for errors.");
                }
            } else {
                player.sendMessage("Portal zone with the name '" + zoneName + "' not found.");
            }
        } else {
            player.sendMessage("Usage: /" + label + " delete <zoneName>");
        }
    }

}
