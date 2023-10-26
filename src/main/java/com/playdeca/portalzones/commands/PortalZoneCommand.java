package com.playdeca.portalzones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PortalZoneCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return switch (args[0].toLowerCase()) {
            case "create" -> new createCommand().onCommand(sender, command, label, args);
            case "delete" -> new deleteCommand().onCommand(sender, command, label, args);
            case "help" -> new helpCommand().onCommand(sender, command, label, args);
            case "list" -> new listCommand().onCommand(sender, command, label, args);
            case "select" -> new selectCommand().onCommand(sender, command, label, args);
            case "update" -> new updateCommand().onCommand(sender, command, label, args);
            default -> false;
        };
    }


}
