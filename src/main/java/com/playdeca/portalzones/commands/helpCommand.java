package com.playdeca.portalzones.commands;

import com.playdeca.portalzones.services.HelperService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class helpCommand extends HelperService {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            handleHelpCommand(player);
            return true;
        }
        return false;
    }

    public void handleHelpCommand(Player player){
        createHelpMessage(player);
    }


}
