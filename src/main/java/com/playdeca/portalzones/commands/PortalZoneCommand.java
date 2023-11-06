package com.playdeca.portalzones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PortalZoneCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1) return handleEmptyArgs(sender);
        return switch (args[0].toLowerCase()) {
            case "create" -> new createCommand().onCommand(sender, command, label, args);
            case "update" -> new updateCommand().onCommand(sender, command, label, args);
            case "select" -> new selectCommand().onCommand(sender, command, label, args);
            case "list" -> new listCommand().onCommand(sender, command, label, args);
            case "delete" -> new deleteCommand().onCommand(sender, command, label, args);
            case "help" -> new helpCommand().onCommand(sender, command, label, args);
            default -> false;
        };
    }

    private boolean handleEmptyArgs(CommandSender sender){
        if (sender instanceof Player player) {
            player.sendMessage("Portal Zones Help:");
            player.sendMessage("/pz create <name> - Creates a new portal zone with the given name.");
            player.sendMessage("/pz select <name> - Selects the portal zone with the given name.");
            player.sendMessage("/pz update <all|region1Name|region2Name|softCountTime|hardCountTime|xyz1|xyz2> <value(if applicable)> - Updates the given property of the selected portal zone.");
            player.sendMessage("/pz list - Lists all portal zones.");
            player.sendMessage("/pz delete <name> - Deletes the portal zone with the given name.");
            player.sendMessage("/pz help - Displays the help message.");
        }
        return true;
    }


}
