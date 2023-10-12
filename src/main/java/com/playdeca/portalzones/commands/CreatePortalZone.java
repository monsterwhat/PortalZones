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

public class CreatePortalZone implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        try {
            if (sender instanceof Player) {
                if (args.length == 5) {
                    String zoneName = args[0];
                    String region1 = args[1];
                    String region2 = args[2];
                    int softCount = Integer.parseInt(args[3]);
                    int hardCount = Integer.parseInt(args[4]);

                    Player player = (Player) sender;

                    World world = new BukkitWorld(player.getWorld());

                    SessionManager sessionManager = WorldEdit.getInstance().getSessionManager();
                    Region selection = null;
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
                            sender.sendMessage("Failed to save the portal zone. Please check the console for errors.");
                            return true;
                        }

                        sender.sendMessage("Portal Zone created: " + zoneName);
                    } else {
                        sender.sendMessage("Please select a WorldEdit region using the WorldEdit wand tool before using this command.");
                    }
                } else {
                    sender.sendMessage("Usage: /pzcreate <name> <region1> <region2> <softcount> <hardcount>");
                }
            } else {
                sender.sendMessage("You must be a player to use this command.");
            }
            return true;
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error creating portal zone: " + e.getMessage());
            sender.sendMessage("Failed to create the portal zone. Please check the console for errors.");
            return true;
        }


    }

}
