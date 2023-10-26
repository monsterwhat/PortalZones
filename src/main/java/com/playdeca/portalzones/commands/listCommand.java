package com.playdeca.portalzones.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Objects;
import java.util.Set;

public class listCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
                if(args[0].equalsIgnoreCase("list")){
                        handleListCommand(player);
                        return true;
                    }
                }
        return false;
    }

    private void handleListCommand(Player player) {

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
                Location xyz1 = Location.deserialize(Objects.requireNonNull(config.getConfigurationSection(path + ".xyz1")).getValues(true));
                Location xyz2 = Location.deserialize(Objects.requireNonNull(config.getConfigurationSection(path + ".xyz2")).getValues(true));
                player.sendMessage(name + " - Region1: " + region1 + " - Region2: " + region2 +
                        " - SoftCount: " + softCount + " - HardCount: " + hardCount + " - XYZ1: " + xyz1.getX() + " " + xyz1.getY() + " " + xyz1.getZ() + " - XYZ2: " + xyz2.getX() + " " + xyz2.getY() + " " + xyz2.getZ());
            }
        }
    }
}
