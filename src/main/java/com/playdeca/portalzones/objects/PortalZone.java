package com.playdeca.portalzones.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class PortalZone {
    private String name, region1, region2;
    private int softCount, hardCount;
    private Location xyz1, xyz2;

    public PortalZone(){
        this.name = "";
        this.region1 = "";
        this.region2 = "";
        this.softCount = 0;
        this.hardCount = 0;
        Location defaultLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        this.xyz1 = defaultLocation;
        this.xyz2 = defaultLocation;
    }

    public PortalZone(String name, String region1, String region2, int softCount, int hardCount, Location xyz1, Location xyz2) {
        this.name = name;
        this.region1 = region1;
        this.region2 = region2;
        this.softCount = softCount;
        this.hardCount = hardCount;
        this.xyz1 = xyz1;
        this.xyz2 = xyz2;
    }

    public PortalZone(String name){
        this.name = name;
        this.region1 = "";
        this.region2 = "";
        this.softCount = 0;
        this.hardCount = 0;
        Location defaultLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        this.xyz1 = defaultLocation;
        this.xyz2 = defaultLocation;
    }

    public void saveToConfig(FileConfiguration config) {
        try {
            String path = "portalZones." + name;
            config.set(path + ".region1", region1);
            config.set(path + ".region2", region2);
            config.set(path + ".softCount", softCount);
            config.set(path + ".hardCount", hardCount);
            config.set(path + ".xyz1", xyz1.serialize()); // Serialize the Location
            config.set(path + ".xyz2", xyz2.serialize());
        }catch (Exception e){
            Bukkit.getLogger().warning("Error saving portal zone: " + name);
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public String getRegion1() {
        return region1;
    }

    public String getRegion2() {
        return region2;
    }

    public int getSoftCount() {
        return softCount;
    }

    public int getHardCount() {
        return hardCount;
    }

    public Location getXyz1() {
        return xyz1;
    }

    public Location getXyz2() {
        return xyz2;
    }

    public void setRegion1(String region1) {
        this.region1 = region1;
    }

    public void setRegion2(String region2) {
        this.region2 = region2;
    }

    public void setSoftCount(int softCount) {
        this.softCount = softCount;
    }

    public void setHardCount(int hardCount) {
        this.hardCount = hardCount;
    }

    public void setXyz1(Location xyz1) {
        this.xyz1 = xyz1;
    }

    public void setXyz2(Location xyz2) {
        this.xyz2 = xyz2;
    }


}
