package com.playdeca.portalzones.commands;

import org.bukkit.Bukkit;
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

        try {
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
                    String destination1 = "World: "+ xyz1.getWorld().getName() + ",X:" + xyz1.getBlockX() + ", Y:" + xyz1.getBlockY() + ", Z:" + xyz1.getBlockZ();
                    String destination2 = "World: "+ xyz2.getWorld().getName() + ",X:" + xyz2.getBlockX() + ", Y:" + xyz2.getBlockY() + ", Z:" + xyz2.getBlockZ();
                    player.sendMessage("Zone name: " + name);
                    player.sendMessage("Region 1: " + region1);
                    player.sendMessage("Region 2: " + region2);
                    player.sendMessage("Soft Count: " + softCount);
                    player.sendMessage("Hard Count: " + hardCount);
                    player.sendMessage("Destination 1: " + destination1);
                    player.sendMessage("Destination 2: " + destination2);
                    player.sendMessage(" ");
                }
            }
        }catch (Exception e){
            player.sendMessage("Error: " + e);
            Bukkit.getLogger().warning("Error: " + e);
            Bukkit.getLogger().warning("Error: " + e.getStackTrace());
        }
    }
}
