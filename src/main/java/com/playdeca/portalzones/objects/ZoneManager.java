package com.playdeca.portalzones.objects;

import java.util.ArrayList;

public class ZoneManager {
    private PortalZone selectedZone;
    private ArrayList<PortalZone> zones;

    private ZoneManager() {
        // Initialize the selectedZone if needed
        selectedZone = null;
        loadZones();
    }

    public void loadZones(){
        zones = PortalZoneDAO.getAllPortalZones();
    }

    private static final class InstanceHolder {
        private static final ZoneManager instance = new ZoneManager();
    }

    public static ZoneManager getInstance() {
        return InstanceHolder.instance;
    }

    public PortalZone getSelectedZone() {
        return selectedZone;
    }

    public void setSelectedZone(PortalZone zone) {
        selectedZone = zone;
    }

    public ArrayList<PortalZone> getZones() {
        return zones;
    }

    public void setZones(ArrayList<PortalZone> zones) {
        this.zones = zones;
    }

}
