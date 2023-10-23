package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.objects.PortalZone;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.world.World;
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
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class PortalZoneCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            switch (label.toLowerCase()) {
                case "portalzone":
                case "pz":
                    if (args.length == 0) {
                        player.sendMessage("Usage: /" + label + " <create|list>");
                        return true;
                    }
                    String subCommand = args[0].toLowerCase();
                    switch (subCommand) {
                        case "create":
                            // Handle create
                            handleCreateCommand(player, args);
                            break;
                        case "select":
                            // Handle select
                            break;
                        case "list":
                            // Handle list
                            handleListCommand(player);
                            break;
                        case "modify":
                            // Handle modify
                            handleModifyCommand(player, args, label);
                            break;
                        case "delete":
                            // Handle delete
                            handleDeleteCommand(player, args, label);
                            break;
                        default:
                            player.sendMessage("Unknown subcommand. Usage: /" + label + " <create|list>");
                            break;
                    }
                    break;
            }
        } else {
            sender.sendMessage("You must be a player to use this command.");
        }
        return true;
    }

    private void handleCreateCommand(Player player, String[] args) {
        try {
            if (player != null) {
                if (args.length == 5) {
                    String zoneName = args[0];
                    String region1 = args[1];
                    String region2 = args[2];
                    int softCount = Integer.parseInt(args[3]);
                    int hardCount = Integer.parseInt(args[4]);

                    World world = new BukkitWorld(player.getWorld());

                    SessionManager sessionManager = WorldEdit.getInstance().getSessionManager();
                    Region selection;
                    try {
                        selection = sessionManager.get(BukkitAdapter.adapt(player)).getSelection(world);
                    } catch (IncompleteRegionException e) {
                        throw new RuntimeException(e);
                    }

                    if (selection != null) {

                        BlockVector3 min = selection.getMinimumPoint();
                        BlockVector3 max = selection.getMaximumPoint();

                        // Convert WorldEdit's Location to Bukkit Location for XYZ1 and XYZ2
                        Location xyz1 = new Location(BukkitAdapter.adapt(selection.getWorld()), min.getX(), min.getY(), min.getZ());
                        Location xyz2 = new Location(BukkitAdapter.adapt(selection.getWorld()), max.getX(), max.getY(), max.getZ());

                        // Create the PortalZone with XYZ1 and XYZ2 locations
                        PortalZone portalZone = new PortalZone(zoneName, region1, region2, softCount, hardCount, xyz1, xyz2);

                        // Save the new portal zone to the configuration
                        File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
                        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

                        portalZone.saveToConfig(config);

                        try {
                            config.save(configFile);
                        } catch (IOException e) {
                            Bukkit.getLogger().warning("Error saving portal zone: " + e.getMessage());
                            player.sendMessage("Failed to save the portal zone. Please check the console for errors.");
                        }

                        player.sendMessage("Portal Zone created: " + zoneName);
                    } else {
                        player.sendMessage("Please select a WorldEdit region using the WorldEdit wand tool before using this command.");
                    }
                } else {
                    player.sendMessage("Usage: /pzcreate <name> <region1> <region2> <softcount> <hardcount>");
                }
            } else {
                Bukkit.getLogger().warning("Player is null.");
            }
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error creating portal zone: " + e.getMessage());
            assert player != null;
            player.sendMessage("Failed to create the portal zone. Please check the console for errors, or report this bug to the staff.");
        }
        assert player != null;
        player.sendMessage("Create command was executed.");
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
                    player.sendMessage(name + " - Region1: " + region1 + " - Region2: " + region2 +
                            " - SoftCount: " + softCount + " - HardCount: " + hardCount);
            }
        }
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
                } catch (IOException e) {
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

    private void handleModifyCommand(Player player, String[] args, String label) {
        if (args.length >= 3) {
            String zoneName = args[1];

            // Load the list of portal zones from the configuration
            File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            String path = "portalZones." + zoneName;

            if (config.contains(path)) {
                // Modify the properties as needed
                for (int i = 2; i < args.length; i++) {
                    String[] parts = args[i].split("=", 2);
                    if (parts.length == 2) {
                        String property = parts[0].toLowerCase();
                        String value = parts[1];

                        // Update the corresponding property
                        switch (property) {
                            case "region1":
                                config.set(path + ".region1", value);
                                player.sendMessage("Region1 for portal zone '" + zoneName + "' has been modified to " + value);
                                break;
                            case "region2":
                                config.set(path + ".region2", value);
                                player.sendMessage("Region2 for portal zone '" + zoneName + "' has been modified to " + value);
                                break;
                            case "softcount":
                                int softCount = Integer.parseInt(value);
                                config.set(path + ".softCount", softCount);
                                player.sendMessage("SoftCount for portal zone '" + zoneName + "' has been modified to " + softCount);
                                break;
                            case "hardcount":
                                int hardCount = Integer.parseInt(value);
                                config.set(path + ".hardCount", hardCount);
                                player.sendMessage("HardCount for portal zone '" + zoneName + "' has been modified to " + hardCount);
                                break;
                            case "xyz1":
                                // Get XYZ1 from the WorldEdit selection
                                SessionManager sessionManager = WorldEdit.getInstance().getSessionManager();
                                Region selection = null;
                                try {
                                    selection = sessionManager.get(BukkitAdapter.adapt(player)).getSelection(BukkitAdapter.adapt(player.getWorld()));
                                } catch (IncompleteRegionException e) {
                                    player.sendMessage("Please select a WorldEdit region using the WorldEdit wand tool before using this command.");
                                    return;
                                }

                                if (selection != null) {
                                    BlockVector3 min = selection.getMinimumPoint();
                                    Location newLocation = new Location(BukkitAdapter.adapt(BukkitAdapter.adapt(player.getWorld())), min.getX(), min.getY(), min.getZ());
                                    config.set(path + ".xyz1", newLocation.serialize());
                                    player.sendMessage("XYZ1 for portal zone '" + zoneName + "' has been modified to " + newLocation.toString());
                                } else {
                                    player.sendMessage("Please select a WorldEdit region using the WorldEdit wand tool before using this command.");
                                }
                                break;
                            case "xyz2":
                                // Get XYZ2 from the WorldEdit selection
                                SessionManager sessionManager2 = WorldEdit.getInstance().getSessionManager();
                                Region selection2 = null;
                                try {
                                    selection2 = sessionManager2.get(BukkitAdapter.adapt(player)).getSelection(BukkitAdapter.adapt(player.getWorld()));
                                } catch (IncompleteRegionException e) {
                                    player.sendMessage("Please select a WorldEdit region using the WorldEdit wand tool before using this command.");
                                    return;
                                }

                                if (selection2 != null) {
                                    BlockVector3 max = selection2.getMaximumPoint();
                                    Location newLocation = new Location(BukkitAdapter.adapt(BukkitAdapter.adapt(player.getWorld())), max.getX(), max.getY(), max.getZ());
                                    config.set(path + ".xyz2", newLocation.serialize());
                                    player.sendMessage("XYZ2 for portal zone '" + zoneName + "' has been modified to " + newLocation.toString());
                                } else {
                                    player.sendMessage("Please select a WorldEdit region using the WorldEdit wand tool before using this command.");
                                }
                                break;
                            default:
                                player.sendMessage("Unknown property: " + property);
                                break;
                        }
                    } else {
                        player.sendMessage("Invalid property format. Available properties: region1, region2, softCount, hardCount, xyz1, xyz2");
                    }
                }

                // Save the modified configuration
                try {
                    config.save(configFile);
                } catch (IOException e) {
                    Bukkit.getLogger().warning("Error modifying portal zone: " + e.getMessage());
                    player.sendMessage("Failed to modify the portal zone. Please check the console for errors.");
                }
            } else {
                player.sendMessage("Portal zone with the name '" + zoneName + "' not found.");
            }
        } else {
            player.sendMessage("Usage: /" + label + " modify <zoneName> <property=value> [property=value...]");
        }
    }




}
