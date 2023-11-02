package com.playdeca.portalzones.services;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.listeners.PortalZoneListener;
import com.playdeca.portalzones.objects.PortalZone;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.session.SessionManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public abstract class HelperService implements CommandExecutor {

    protected FileConfiguration config;
    protected PortalZone selectedZone;
    protected PortalZones portalZonesPlugin;
    protected PortalZoneListener portalZonesListener;
    protected File configFile;
    protected SessionManager sessionManager;
    protected PortalZoneService pzService;

    public HelperService() {
        selectedZone = null;
        portalZonesPlugin = PortalZones.getPlugin(PortalZones.class);
        configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        portalZonesListener = new PortalZoneListener(portalZonesPlugin);
        sessionManager = WorldEdit.getInstance().getSessionManager();
        pzService = new PortalZoneService(portalZonesPlugin);
    }

}
