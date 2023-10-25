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

    File configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
    SessionManager sessionManager = WorldEdit.getInstance().getSessionManager();
    PortalZone selectedZone;
    PortalZones portalZonesPlugin = PortalZones.getPlugin(PortalZones.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            switch (label.toLowerCase()) {
                case "portalzone":
                case "pz":
                    if (args.length == 0) {
                        player.sendMessage("Usage: /" + label + " <create|select|list|modify|delete|newcreate>");
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
                            handleSelectCommand(player, args);
                            break;
                        case "update":
                            // Handle update
                            handleUpdateCommand(player, args);
                            break;
                        case "list":
                            // Handle list
                            handleListCommand(player);
                            break;
                        case "delete":
                            // Handle delete
                            handleDeleteCommand(player, args, label);
                            break;
                        default:
                            player.sendMessage("Unknown subcommand. Usage: /" + label + " < create | select | update | list | delete > ");
                            break;
                    }
                    break;
            }
        } else {
            sender.sendMessage("You must be a player to use this command.");
        }
        return true;
    }

    private void saveChanges(Player player){
        selectedZone.saveToConfig(config);
        try {
            config.save(configFile);
            portalZonesPlugin.loadPortalZones();
            player.sendMessage("Portal Zone updated: " + selectedZone.getName());

        } catch (IOException e) {
            Bukkit.getLogger().warning("Error saving portal zone: " + e.getMessage());
            Bukkit.getLogger().warning("Error saving portal zone: " + e.getStackTrace());
        }
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
                portalZonesPlugin.loadPortalZones();
                } catch (IOException e) {
                    Bukkit.getLogger().warning("Error saving portal zone: " + e.getMessage());
                    Bukkit.getLogger().warning("Error saving portal zone: " + e.getStackTrace());
                    player.sendMessage("Failed to save the portal zone. Please check the console for errors.");
                }
            }else {
                player.sendMessage("Usage: /pz newcreate <name>");
            }
        }
    }

    private void handleSelectCommand(Player player, String[] args){
        if(player!=null){
            if (args.length == 2) {
                try {
                    String zoneName = args[1];
                    //should check if the zone exists first...
                    if(!config.contains("portalZones." + zoneName)){
                        player.sendMessage("Portal Zone not found: " + zoneName);
                        return;
                    }
                    selectedZone = new PortalZone(zoneName);
                    player.sendMessage("Portal Zone selected: " + zoneName);
                }catch (Exception e){
                    Bukkit.getLogger().warning("Error selecting portal zone: " + e.getMessage());
                    Bukkit.getLogger().warning("Error selecting portal zone: " + e.getStackTrace());
                    player.sendMessage("Failed to select the portal zone. Please check the console for errors.");
                }
            }else {
                player.sendMessage("Usage: /pz select <PortalZoneName>");
            }
        }
    }

    private void handleUpdateCommand(Player player, String[] args){
        if(player!=null){
                if (args.length < 2) {
                    player.sendMessage("Usage: /pz update <all|region1Name|region2Name|softCountTime|hardCountTime|xyz1|xyz2> <value>");
                    return;
                }

                String property = args[1];

                switch (property) {
                    case "all":
                        if (args.length != 6) {
                            player.sendMessage("Usage: /pz update <all> <region1Name> <region2Name> <softcountTime> <hardcountTime>");
                            break;
                        }
                        updateRegion1(args[2]);
                        updateRegion2(args[3]);
                        updateSoftCount(Integer.parseInt(args[4]));
                        updateHardCount(Integer.parseInt(args[5]));
                        player.sendMessage("Portal Zone updated: " + selectedZone.getName());
                        player.sendMessage("XYZ1 & 2 Need to be manually set!");
                        saveChanges(player);
                        break;
                    case "region1Name":
                        if (args.length != 3) {
                            player.sendMessage("Usage: /pz update <region1> <regionName>");
                            break;
                        }
                        updateRegion1(args[2]);
                        saveChanges(player);
                        break;
                    case "region2Name":
                        if (args.length != 3) {
                            player.sendMessage("Usage: /pz update <region2> <regionName>");
                            break;
                        }
                        updateRegion2(args[2]);
                        saveChanges(player);
                        break;
                    case "softcountTime":
                        if (args.length != 3) {
                            player.sendMessage("Usage: /pz update <softcount> <Time>");
                            break;
                        }
                        updateSoftCount(Integer.parseInt(args[2]));
                        saveChanges(player);
                        break;
                    case "hardcountTime":
                        if (args.length != 3) {
                            player.sendMessage("Usage: /pz update <hardcount> <Time>");
                            break;
                        }
                        updateHardCount(Integer.parseInt(args[2]));
                        saveChanges(player);
                        break;
                    case "xyz1":
                        if (args.length != 2) {
                            player.sendMessage("Usage: /pz update <xyz1>");
                            break;
                        }
                        updateXYZ1(player);
                        saveChanges(player);
                        break;
                    case "xyz2":
                        if (args.length != 2) {
                            player.sendMessage("Usage: /pz update <xyz2>");
                            break;
                        }
                        updateXYZ2(player);
                        saveChanges(player);
                        break;
                    default:
                        player.sendMessage("Unknown property: " + property);
                        break;
                }
            }
    }

    private void updateRegion1(String regionName){
        if(regionName.isEmpty() || regionName.isBlank()){
            Bukkit.getLogger().warning("Region1 is empty");
            return;
        }
        selectedZone.setRegion1(regionName);
    }

    private void updateRegion2(String regionName){
        if(regionName.isEmpty() || regionName.isBlank()){
            Bukkit.getLogger().warning("Region2 is empty");
            return;
        }
        selectedZone.setRegion2(regionName);
    }

    private void updateSoftCount(int softCount){
        selectedZone.setSoftCount(softCount);
    }

    private void updateHardCount(int hardCount){ selectedZone.setHardCount(hardCount); }

    private void updateXYZ1(Player player){
        Region selection;
        try {
            World world = new BukkitWorld(player.getWorld());
            selection = sessionManager.get(BukkitAdapter.adapt(player)).getSelection(world);
            if (selection != null) {
                BlockVector3 newSelection = selection.getMaximumPoint();
                Location newLocation = new Location(BukkitAdapter.adapt(selection.getWorld()), newSelection.getX(), newSelection.getY(), newSelection.getZ());
                selectedZone.setXyz1(newLocation);
            }else {
                player.sendMessage("Please select the destination (Left Click) block using the WorldEdit wand tool before using this command.");
            }
        }catch (Exception e){
            Bukkit.getLogger().warning("Error updating XYZ1: " + e.getMessage());
            Bukkit.getLogger().warning("Error updating XYZ1: " + e.getStackTrace());
        }
    }
    private void updateXYZ2(Player player){
        Region selection;
        try {
            World world = new BukkitWorld(player.getWorld());
            selection = sessionManager.get(BukkitAdapter.adapt(player)).getSelection(world);
            if (selection != null) {
                BlockVector3 newSelection = selection.getMaximumPoint();
                Location newLocation = new Location(BukkitAdapter.adapt(selection.getWorld()), newSelection.getX(), newSelection.getY(), newSelection.getZ());
                selectedZone.setXyz2(newLocation);
            }else{
                player.sendMessage("Please select the destination (Left Click) block using the WorldEdit wand tool before using this command.");
            }
        }catch (Exception e){
            Bukkit.getLogger().warning("Error updating XYZ2: " + e.getMessage());
            Bukkit.getLogger().warning("Error updating XYZ2: " + e.getStackTrace());
        }
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
                    portalZonesPlugin.loadPortalZones();
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
}
