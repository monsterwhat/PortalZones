package com.playdeca.portalzones.helpers;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.listeners.PortalZoneListener;
import com.playdeca.portalzones.objects.PortalZone;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.session.SessionManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public abstract class ZonesHelper implements CommandExecutor {

    protected FileConfiguration config;
    protected PortalZone selectedZone;
    protected PortalZones portalZonesPlugin;
    protected PortalZoneListener portalZonesListener;
    protected File configFile;
    protected SessionManager sessionManager;

    public ZonesHelper() {
        portalZonesPlugin = PortalZones.getPlugin(PortalZones.class);
        configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        portalZonesListener = new PortalZoneListener(portalZonesPlugin);
        sessionManager = WorldEdit.getInstance().getSessionManager();
    }

}
