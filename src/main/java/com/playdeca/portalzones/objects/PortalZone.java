package com.playdeca.portalzones.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class PortalZone {
    private int id;
    private String name, region1, region2;
    private int softCount, hardCount;
    private Location xyz1, xyz2;
    private World world1, world2;

    public PortalZone(int id, String name, String region1, String region2,int softCount, int hardCount, Location xyz1,World world1, Location xyz2, World world2) {
        this.id = id;
        this.name = name;
        this.region1 = region1;
        this.region2 = region2;
        this.softCount = softCount;
        this.hardCount = hardCount;
        this.xyz1 = xyz1;
        this.world1 = world1;
        this.xyz2 = xyz2;
        this.world2 = world2;
    }

    public PortalZone(String name, World world1){
        this.name = name;
        this.region1 = "";
        this.region2 = "";
        this.softCount = 0;
        this.hardCount = 0;
        Location defaultLocation = new Location(world1, 0, 0, 0);
        this.xyz1 = defaultLocation;
        this.world1 = world1;
        this.xyz2 = defaultLocation;
        this.world2 = world1;
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

    public int getId(){
        return id;
    }

    public World getWorld1() {
        return world1;
    }

    public World getWorld2() {
        return world2;
    }

    public void setWorld1(World world1) {
        this.world1 = world1;
    }

    public void setWorld2(World world2) {
        this.world2 = world2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
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
