package com.playdeca.portalzones.services;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.listeners.PortalZoneListener;
import com.playdeca.portalzones.objects.PortalZone;
import com.playdeca.portalzones.objects.PortalZoneDAO;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.session.SessionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class HelperService implements CommandExecutor {
    protected FileConfiguration config;
    protected PortalZones portalZonesPlugin;
    protected PortalZoneListener portalZonesListener;
    protected File configFile;
    protected SessionManager sessionManager;
    protected PortalZoneService pzService;
    protected PortalZoneDAO portalZoneDAO;

    public HelperService() {
        portalZonesPlugin = PortalZones.getPlugin(PortalZones.class);
        configFile = new File(PortalZones.getPlugin(PortalZones.class).getDataFolder(), "portalzones.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        portalZonesListener = new PortalZoneListener(portalZonesPlugin);
        sessionManager = WorldEdit.getInstance().getSessionManager();
        pzService = new PortalZoneService(portalZonesPlugin);
        portalZoneDAO = new PortalZoneDAO(portalZonesPlugin);
    }

    public TextColor yellow = TextColor.color(255, 255, 85);
    public TextColor red = TextColor.color(255, 85, 85);
    public TextColor blue = TextColor.color(85, 85, 255);
    public TextColor green = TextColor.color(85, 255, 85);
    public TextColor purple = TextColor.color(255, 85, 255);
    public TextColor grey = TextColor.color(170, 170, 170);
    public TextColor black = TextColor.color(0, 0, 0);
    public TextColor white = TextColor.color(255, 255, 255);


    public Component displayZoneName(PortalZone zone){
        String ZoneName = zone.getName();
        var pretitle = Component.text("[").color(grey).append(Component.text("Portal Zones").color(red)).append(Component.text("] ").color(grey));
        var title = Component.text("---------- ").color(yellow).append(Component.text(zone.getName())).append(Component.text(" ----------").color(yellow)).appendNewline();
        var msg = Component.text("Zone Name: ").color(TextColor.color(blue)).append(Component.text(ZoneName).color(TextColor.color(yellow)));
        TextComponent hoverMessage = Component.text("Click me to select the zone!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz select " + ZoneName);
        return pretitle.append(title).append(msg).hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayRegion1(PortalZone zone){
        String Region1 = zone.getRegion1();
        TextComponent message = Component.text("Region 1: ").color(TextColor.color(blue)).append(Component.text(Region1).color(TextColor.color(yellow)));
        TextComponent hoverMessage = Component.text("Click me to update the region 1!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update region1Name " + Region1);
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayRegion2(PortalZone zone){
        String Region2 = zone.getRegion2();
        TextComponent message = Component.text("Region 2: ").color(TextColor.color(blue)).append(Component.text(Region2).color(TextColor.color(yellow)));
        TextComponent hoverMessage = Component.text("Click me to update the region 2!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update region2Name " + Region2);
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displaySoftCount(PortalZone zone){
        int SoftCount = zone.getSoftCount();
        TextComponent message = Component.text("Soft Count: ").color(TextColor.color(blue)).append(Component.text(SoftCount).color(TextColor.color(yellow)));
        TextComponent hoverMessage = Component.text("Click me to update the soft count!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update softCountTime " + SoftCount);
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayHardCount(PortalZone zone){
        int HardCount = zone.getHardCount();
        TextComponent message = Component.text("Hard Count: ").color(TextColor.color(blue)).append(Component.text(HardCount).color(TextColor.color(yellow)));
        TextComponent hoverMessage = Component.text("Click me to update the hard count!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update hardCountTime " + HardCount);
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayDestination1(PortalZone zone){
        String destination1xyz = zone.getWorld1() + ", " + zone.getXyz1().getX() + ", " + zone.getXyz1().getY() + ", " + zone.getXyz1().getZ();
        TextComponent message = Component.text("Destination 1: ").color(TextColor.color(blue)).append(Component.text(destination1xyz).color(TextColor.color(yellow)));
        TextComponent hoverMessage = Component.text("Click me to update the destination 1!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update destination1");
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayDestination2(PortalZone zone){
        String destination2xyz = zone.getWorld2() + ", " + zone.getXyz2().getX() + ", " + zone.getXyz2().getY() + ", " + zone.getXyz2().getZ();
        var message = Component.text("Destination 2: ").color(TextColor.color(blue)).append(Component.text(destination2xyz).color(TextColor.color(yellow)));
        TextComponent hoverMessage = Component.text("Click me to update the destination 2!");
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update destination2");
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component simpleRegionInfo(PortalZone zone){
        var pretitle = Component.text("[").color(grey).append(Component.text("Portal Zones").color(red)).append(Component.text("] ").color(grey));
        var title = Component.text("---------- ").color(yellow).append(Component.text(zone.getName())).append(Component.text(" ----------").color(yellow)).appendNewline();
        var msg1 = Component.text("Region 1: ").color(TextColor.color(blue)).append(Component.text(zone.getRegion1()).color(TextColor.color(yellow))).appendNewline();
        var msg2 = Component.text("Region 2: ").color(TextColor.color(blue)).append(Component.text(zone.getRegion2()).color(TextColor.color(yellow))).appendNewline();
        var msg3 = Component.text("Soft Count: ").color(TextColor.color(blue)).append(Component.text(zone.getSoftCount()).color(TextColor.color(yellow))).appendNewline();
        var msg4 = Component.text("Hard Count: ").color(TextColor.color(blue)).append(Component.text(zone.getHardCount()).color(TextColor.color(yellow))).appendNewline();
        String destination1xyz = zone.getWorld1() + ", " + zone.getXyz1().getX() + ", " + zone.getXyz1().getY() + ", " + zone.getXyz1().getZ();
        String destination2xyz = zone.getWorld2() + ", " + zone.getXyz2().getX() + ", " + zone.getXyz2().getY() + ", " + zone.getXyz2().getZ();
        var msg5 = Component.text("Destination 1: ").color(TextColor.color(blue)).append(Component.text(destination1xyz).color(TextColor.color(yellow))).appendNewline();
        var msg6 = Component.text("Destination 2: ").color(TextColor.color(blue)).append(Component.text(destination2xyz).color(TextColor.color(yellow))).appendNewline();
        TextComponent hoverMessage = Component.text("Click me to select the zone!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz select " + zone.getName());

        return pretitle.append(title).hoverEvent(hoverEvent).clickEvent(clickEvent).append(msg1).append(msg2).append(msg3).append(msg4).append(msg5).append(msg6);
    }


    public void createHelpMessage(Player player){
        Component pretitle = Component.text("[").color(grey).append(Component.text("Portal Zones").color(red)).append(Component.text("] ").color(grey));
        Component title = Component.text("---------- ").color(yellow).append(Component.text("Portal Zones Help").color(red).append(Component.text(" ----------").color(yellow))).appendNewline();
        Component createmsg = sendHelpCreateMessage();
        Component selectmsg = sendHelpSelectMessage();
        Component updatemsg = sendHelpUpdateMessage();
        Component listmsg = sendHelpListMessage();
        Component deletemsg = sendHelpDeleteMessage();

        player.sendMessage(pretitle.append(title).append(createmsg).append(selectmsg).append(updatemsg).append(listmsg).append(deletemsg));
    }

    public Component sendHelpCreateMessage(){
        Component msg = Component.text("/pz create <name> - Creates a new portal zone with the given name.").appendNewline();
        Component hovermsg = Component.text("Click me to create a new portal zone!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hovermsg);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz create ");

        return  msg.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component sendHelpSelectMessage(){
        Component msg = Component.text("/pz select <name> - Selects the portal zone with the given name.").appendNewline();
        Component hovermsg = Component.text("Click me to select a portal zone!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hovermsg);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz select ");

        return  msg.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component sendHelpUpdateMessage(){
        Component msg = Component.text("/pz update <all|region1Name|region2Name|softCountTime|hardCountTime|xyz1|xyz2> <value(if applicable)> - Updates the given property of the selected portal zone.").appendNewline();
        Component hovermsg = Component.text("Click me to update a portal zone!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hovermsg);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update ");

        return  msg.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component sendHelpListMessage(){
        Component msg = Component.text("/pz list - Lists all portal zones.").appendNewline();
        Component hovermsg = Component.text("Click me to list all portal zones!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hovermsg);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz list");

        return  msg.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component sendHelpDeleteMessage(){
        Component msg = Component.text("/pz delete <name> - Deletes the portal zone with the given name.");
        Component hovermsg = Component.text("Click me to delete a portal zone!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hovermsg);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz delete ");

        return  msg.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }


}
