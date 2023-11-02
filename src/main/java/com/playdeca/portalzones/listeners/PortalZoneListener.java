package com.playdeca.portalzones.listeners;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.services.PortalZoneService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class PortalZoneListener implements Listener {
    private final PortalZoneService pzService;
    public PortalZoneListener(PortalZones portalZones) {
        pzService = new PortalZoneService(portalZones);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            Player player = event.getPlayer();
            //checkPortalZones(player);
            pzService.checkPortalZonesFull(player);
        }catch (Exception e) {
            Bukkit.getLogger().warning("Error on onPlayerMove: " + e.getMessage());
        }
    }

}
