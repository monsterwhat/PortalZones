package com.playdeca.portalzones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class helpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            handleHelpCommand(player);
            return true;
        }
        return false;
    }

    public void handleHelpCommand(Player player){
        player.sendMessage("Portal Zones Help:");
        player.sendMessage("/pz create <name> - Creates a new portal zone with the given name.");
        player.sendMessage("/pz select <name> - Selects the portal zone with the given name.");
        player.sendMessage("/pz update <all|region1Name|region2Name|softCountTime|hardCountTime|xyz1|xyz2> <value(if applicable)> - Updates the given property of the selected portal zone.");
        player.sendMessage("/pz list - Lists all portal zones.");
        player.sendMessage("/pz delete <name> - Deletes the portal zone with the given name.");
        player.sendMessage("/pz help - Displays this help message.");
        player.sendMessage("");
        player.sendMessage("When creating a new portal zone you must set all the data manually.");
        player.sendMessage("When updating a portal zone you can use the 'all' property to update all the data at once.");
        player.sendMessage("When updating a portal zone you can use the 'xyz1' and 'xyz2' properties to set the destination blocks.");
        player.sendMessage("When updating a portal zone you can use the 'region1Name' and 'region2Name' properties to set the region names.");
        player.sendMessage("When updating a portal zone you can use the 'softCountTime' and 'hardCountTime' properties to set the count times.");
        player.sendMessage("The SoftCountTime is the time in seconds that a player must be in the portal zone before the hardCountTime starts.");
        player.sendMessage("The HardCountTime is the time in seconds that a player must be in the portal zone before they are teleported.");
        player.sendMessage("The HardCountTime will be displayed and have a sound played when the player is in the portal zone.");
    }

}
