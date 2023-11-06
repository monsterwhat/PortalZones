package com.playdeca.portalzones.services;

import com.playdeca.portalzones.PortalZones;
import com.playdeca.portalzones.listeners.PortalZoneListener;
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

    TextColor yellow = TextColor.color(255, 255, 0);
    TextColor red = TextColor.color(255, 0, 0);
    TextColor blue = TextColor.color(0, 34, 255);
    TextColor green = TextColor.color(0, 255, 0);
    TextColor purple = TextColor.color(255, 0, 255);
    TextColor white = TextColor.color(255, 255, 255);
    TextColor black = TextColor.color(0, 0, 0);


    public Component displayZoneName(String ZoneName){
        TextComponent title = Component.text("---------- ").append(Component.text(ZoneName).color(TextColor.color(white))).append(Component.text(" ----------"));
        TextComponent message = Component.text("Zone name: ").color(TextColor.color(yellow)).append(Component.text(ZoneName).color(TextColor.color(green)));
        TextComponent hoverMessage = Component.text("Click me to select the zone!").color(purple);
        title.append(message);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz select " + ZoneName);
        return title.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayRegion1(String Region1){
        TextComponent message = Component.text("Region 1: ").color(TextColor.color(yellow)).append(Component.text(Region1).color(TextColor.color(green)));
        TextComponent hoverMessage = Component.text("Click me to update the region 1!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        message.append(Component.text("Remember to select the region first!").color(TextColor.color(yellow)));
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update region1Name " + Region1);
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayRegion2(String Region2){
        TextComponent message = Component.text("Region 2: ").color(TextColor.color(yellow)).append(Component.text(Region2).color(TextColor.color(green)));
        TextComponent hoverMessage = Component.text("Click me to update the region 2!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        message.append(Component.text("Remember to select the region first!").color(TextColor.color(yellow)));
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update region2Name " + Region2);
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displaySoftCount(int SoftCount){
        TextComponent message = Component.text("Soft Count: ").color(TextColor.color(yellow)).append(Component.text(SoftCount).color(TextColor.color(green)));
        TextComponent hoverMessage = Component.text("Click me to update the soft count!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        message.append(Component.text("Remember to select the region first!").color(TextColor.color(yellow)));
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update softCount " + SoftCount);
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayHardCount(int HardCount){
        TextComponent message = Component.text("Hard Count: ").color(TextColor.color(yellow)).append(Component.text(HardCount).color(TextColor.color(green)));
        TextComponent hoverMessage = Component.text("Click me to update the hard count!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        message.append(Component.text("Remember to select the region first!").color(TextColor.color(yellow)));
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update hardCount " + HardCount);
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayDestination1(String Destination1){
        TextComponent message = Component.text("Destination 1: ").color(TextColor.color(yellow)).append(Component.text(Destination1).color(TextColor.color(green)));
        TextComponent hoverMessage = Component.text("Click me to update the destination 1!").color(purple);
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        message.append(Component.text("Remember to select the region first!").color(TextColor.color(yellow)));
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update destination1");
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

    public Component displayDestination2(String Destination2){
        TextComponent message = Component.text("Destination 2: ").color(TextColor.color(yellow)).append(Component.text(Destination2).color(TextColor.color(green)));
        TextComponent hoverMessage = Component.text("Click me to update the destination 2!");
        HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverMessage);
        message.append(Component.text("Remember to select the region first!").color(TextColor.color(yellow)));
        ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pz update destination2");
        return message.hoverEvent(hoverEvent).clickEvent(clickEvent);
    }

}
