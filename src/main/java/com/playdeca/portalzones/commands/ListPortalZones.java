package com.playdeca.portalzones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.Objects;
import java.util.Set;

public class ListPortalZones implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {

            // Load the list of portal zones from the configuration
            File configFile = new File("plugins/PortalZones", "portalzones.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            Set<String> zoneNames = Objects.requireNonNull(config.getConfigurationSection("portalZones")).getKeys(false);

            if (zoneNames.isEmpty()) {
                player.sendMessage("No portal zones found.");
            } else {
                player.sendMessage("Portal Zones:");
                for (String name : zoneNames) {
                    String path = "portalZones." + name;
                    String region1 = config.getString(path + ".region1");
                    String region2 = config.getString(path + ".region2");
                    int softCount = config.getInt(path + ".softCount");
                    int hardCount = config.getInt(path + ".hardCount");
                    player.sendMessage(name + " - Region1: " + region1 + " - Region2: " + region2 +
                            " - SoftCount: " + softCount + " - HardCount: " + hardCount);
                }
            }
        } else {
            sender.sendMessage("You must be a player to use this command.");
        }
        return true;
    }
}
