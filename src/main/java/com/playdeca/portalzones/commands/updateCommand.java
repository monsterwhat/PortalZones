package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.objects.ZoneManager;
import com.playdeca.portalzones.services.HelperService;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public class updateCommand extends HelperService {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if(args[0].equalsIgnoreCase("update")){
                if(ZoneManager.getInstance().getSelectedZone() != null){
                    handleUpdateCommand(player, args);
                    return true;
                }else{
                    player.sendMessage("Please select a portal zone first.");
                    return false;
                }
            }
        }
        return false;
    }

    private void handleUpdateCommand(Player player, String[] args){
            if (args.length < 2) {
                player.sendMessage("Usage: /pz update <all|region1Name|region2Name|softCountTime|hardCountTime|Destination1|Destination2> <value>");
                return;
            }

            String property = args[1];

            switch (property) {
                case "all":
                    if (args.length != 6) {
                        player.sendMessage("Usage: /pz update <all> <region1Name> <region2Name> <softcountTime> <hardcountTime>");
                        break;
                    }
                    updateRegion1DB(args[2], player);
                    updateRegion2DB(args[3], player);
                    updateSoftCountDB(Integer.parseInt(args[4]), player);
                    updateHardCountDB(Integer.parseInt(args[5]), player);
                    player.sendMessage("Portal Zone updated: " + ZoneManager.getInstance().getSelectedZone().getName());
                    player.sendMessage("Destination1 & Destination2 Need to be manually set!");
                    break;
                case "region1Name":
                    if (args.length != 3) {
                        player.sendMessage("Usage: /pz update <region1> <regionName>");
                        break;
                    }
                    updateRegion1DB(args[2], player);
                    break;
                case "region2Name":
                    if (args.length != 3) {
                        player.sendMessage("Usage: /pz update <region2> <regionName>");
                        break;
                    }
                    updateRegion2DB(args[2], player);
                    break;
                case "softCountTime":
                    if (args.length != 3) {
                        player.sendMessage("Usage: /pz update <softcount> <Time>");
                        break;
                    }
                    updateSoftCountDB(Integer.parseInt(args[2]), player);
                    break;
                case "hardCountTime":
                    if (args.length != 3) {
                        player.sendMessage("Usage: /pz update <hardcount> <Time>");
                        break;
                    }
                    updateHardCountDB(Integer.parseInt(args[2]), player);
                    break;
                case "destination1":
                    if (args.length != 2) {
                        player.sendMessage("Usage: /pz update <Destination1>");
                        break;
                    }
                    updateXYZ1DB(player);
                    break;
                case "destination2":
                    if (args.length != 2) {
                        player.sendMessage("Usage: /pz update <Destination2>");
                        break;
                    }
                    updateXYZ2DB(player);
                    break;
                default:
                    player.sendMessage("Unknown property: " + property);
                    break;
            }
            ZoneManager.getInstance().loadZones();
    }

    private void updateRegion1DB(String regionName, Player player){
        if(regionName.isEmpty() || regionName.isBlank()){
            Bukkit.getLogger().warning("Region1 is empty");
            return;
        }
        ZoneManager.getInstance().getSelectedZone().setRegion1(regionName);
        portalZoneDAO.updatePortalZone(ZoneManager.getInstance().getSelectedZone());
        player.sendMessage("Region1 ->" + ZoneManager.getInstance().getSelectedZone().getRegion1());
    }

    private void updateRegion2DB(String regionName, Player player){
        if(regionName.isEmpty() || regionName.isBlank()){
            Bukkit.getLogger().warning("Region2 is empty");
            return;
        }
        ZoneManager.getInstance().getSelectedZone().setRegion2(regionName);
        portalZoneDAO.updatePortalZone(ZoneManager.getInstance().getSelectedZone());
        player.sendMessage("Region2 ->" + ZoneManager.getInstance().getSelectedZone().getRegion2());
    }

    private void updateSoftCountDB(int softCount, Player player){
        ZoneManager.getInstance().getSelectedZone().setSoftCount(softCount);
        portalZoneDAO.updatePortalZone(ZoneManager.getInstance().getSelectedZone());
        player.sendMessage("SoftCountTime ->" + ZoneManager.getInstance().getSelectedZone().getSoftCount());
    }

    private void updateHardCountDB(int hardCount, Player player){
        ZoneManager.getInstance().getSelectedZone().setHardCount(hardCount);
        portalZoneDAO.updatePortalZone(ZoneManager.getInstance().getSelectedZone());
        player.sendMessage("HardCountTime ->" + ZoneManager.getInstance().getSelectedZone().getHardCount());
    }

    private void updateXYZ1DB(Player player){
        Region selection;
        try {
            World world = new BukkitWorld(player.getWorld());
            selection = sessionManager.get(BukkitAdapter.adapt(player)).getSelection(world);
            if (selection != null) {
                BlockVector3 newSelection = selection.getMaximumPoint();
                Location newLocation = new Location(BukkitAdapter.adapt(selection.getWorld()), newSelection.getX(), newSelection.getY()+1, newSelection.getZ());
                ZoneManager.getInstance().getSelectedZone().setXyz1(newLocation);
                portalZoneDAO.updatePortalZone(ZoneManager.getInstance().getSelectedZone());
                player.sendMessage("Destination1 ->" + ZoneManager.getInstance().getSelectedZone().getRegion1());
            }else {
                player.sendMessage("Please select the destination (Left Click) block using the WorldEdit wand tool before using this command.");
            }
        }catch (Exception e){
            Bukkit.getLogger().warning("Error updating XYZ1: " + e.getMessage());
            Bukkit.getLogger().warning("Error updating XYZ1: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void updateXYZ2DB(Player player) {
        Region selection;
        try {
            World world = new BukkitWorld(player.getWorld());
            selection = sessionManager.get(BukkitAdapter.adapt(player)).getSelection(world);
            if (selection != null) {
                BlockVector3 newSelection = selection.getMaximumPoint();
                Location newLocation = new Location(BukkitAdapter.adapt(selection.getWorld()), newSelection.getX(), newSelection.getY() + 1, newSelection.getZ());
                ZoneManager.getInstance().getSelectedZone().setXyz2(newLocation);
                portalZoneDAO.updatePortalZone(ZoneManager.getInstance().getSelectedZone());
                player.sendMessage("Destination2 ->" + ZoneManager.getInstance().getSelectedZone().getRegion2() + " updated.");
            } else {
                player.sendMessage("Please select the destination (Left Click) block using the WorldEdit wand tool before using this command.");
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error updating XYZ2: " + e.getMessage());
            Bukkit.getLogger().warning("Error updating XYZ2: " + Arrays.toString(e.getStackTrace()));
        }
    }

}
