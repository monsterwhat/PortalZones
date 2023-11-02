package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.services.HelperService;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Arrays;

public class updateCommand extends HelperService {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
                if(args[0].equalsIgnoreCase("update")){
                    if(selectedZone==null){
                        player.sendMessage("Please select a portal zone first.");
                        return false;
                    }else{
                        handleUpdateCommand(player, args);
                        return true;
                    }
                }
        }
        return false;
    }

    private void handleUpdateCommand(Player player, String[] args){
            if (args.length < 2) {
                player.sendMessage("Usage: /pz update <all|region1Name|region2Name|softCountTime|hardCountTime|Destination1|Destination2> <value>");
                player.sendMessage("To save changes use /pz update save");
                return;
            }

            String property = args[1];

            switch (property) {
                case "save":
                    saveChanges(player);
                    break;
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
                    player.sendMessage("Destination1 & Destination2 Need to be manually set!");
                    break;
                case "region1Name":
                    if (args.length != 3) {
                        player.sendMessage("Usage: /pz update <region1> <regionName>");
                        break;
                    }
                    updateRegion1(args[2]);
                    break;
                case "region2Name":
                    if (args.length != 3) {
                        player.sendMessage("Usage: /pz update <region2> <regionName>");
                        break;
                    }
                    updateRegion2(args[2]);
                    break;
                case "softcountTime":
                    if (args.length != 3) {
                        player.sendMessage("Usage: /pz update <softcount> <Time>");
                        break;
                    }
                    updateSoftCount(Integer.parseInt(args[2]));
                    break;
                case "hardcountTime":
                    if (args.length != 3) {
                        player.sendMessage("Usage: /pz update <hardcount> <Time>");
                        break;
                    }
                    updateHardCount(Integer.parseInt(args[2]));
                    break;
                case "Destination1":
                    if (args.length != 2) {
                        player.sendMessage("Usage: /pz update <Destination1>");
                        break;
                    }
                    updateXYZ1(player);
                    break;
                case "Destination2":
                    if (args.length != 2) {
                        player.sendMessage("Usage: /pz update <Destination2>");
                        break;
                    }
                    updateXYZ2(player);
                    break;
                default:
                    player.sendMessage("Unknown property: " + property);
                    break;
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

    private void updateHardCount(int hardCount){
        selectedZone.setHardCount(hardCount);
    }

    private void updateXYZ1(Player player){
        Region selection;
        try {
            World world = new BukkitWorld(player.getWorld());
            selection = sessionManager.get(BukkitAdapter.adapt(player)).getSelection(world);
            if (selection != null) {
                BlockVector3 newSelection = selection.getMaximumPoint();
                Location newLocation = new Location(BukkitAdapter.adapt(selection.getWorld()), newSelection.getX(), newSelection.getY()+1, newSelection.getZ());
                selectedZone.setXyz1(newLocation);
            }else {
                player.sendMessage("Please select the destination (Left Click) block using the WorldEdit wand tool before using this command.");
            }
        }catch (Exception e){
            Bukkit.getLogger().warning("Error updating XYZ1: " + e.getMessage());
            Bukkit.getLogger().warning("Error updating XYZ1: " + Arrays.toString(e.getStackTrace()));
        }
    }
    private void updateXYZ2(Player player){
        Region selection;
        try {
            World world = new BukkitWorld(player.getWorld());
            selection = sessionManager.get(BukkitAdapter.adapt(player)).getSelection(world);
            if (selection != null) {
                BlockVector3 newSelection = selection.getMaximumPoint();
                Location newLocation = new Location(BukkitAdapter.adapt(selection.getWorld()), newSelection.getX(), newSelection.getY()+1, newSelection.getZ());
                selectedZone.setXyz2(newLocation);
            }else{
                player.sendMessage("Please select the destination (Left Click) block using the WorldEdit wand tool before using this command.");
            }
        }catch (Exception e){
            Bukkit.getLogger().warning("Error updating XYZ2: " + e.getMessage());
            Bukkit.getLogger().warning("Error updating XYZ2: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void saveChanges(Player player){
        selectedZone.saveToConfig(config);
        try {
            config.save(configFile);
            pzService.loadZones();
            player.sendMessage("Portal Zone updated: " + selectedZone.getName());

        } catch (IOException e) {
            Bukkit.getLogger().warning("Error saving portal zone: " + e.getMessage());
            Bukkit.getLogger().warning("Error saving portal zone: " + Arrays.toString(e.getStackTrace()));
        }
    }

}
