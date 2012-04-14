package net.Dockter.LightPoles;

import de.bukkit.Ginsek.StreetLamps.Collections.ControllerList;
import de.bukkit.Ginsek.StreetLamps.Collections.LampCollection;
import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Collections.WorldCollection;
import de.bukkit.Ginsek.StreetLamps.Configs.PluginConfig;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.Simple.PureBulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.UpdateQueue;
import de.bukkit.Ginsek.StreetLamps.Listener.SLBlockListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLPlayerListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLTimeListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLWeatherListener;
import java.util.ArrayList;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;

public class CommandHandler extends PlayerListener
{
  private final PluginConfig config;
  public static boolean opOnly = false;

  public CommandHandler(PluginConfig config) {
    this.config = config;
  }

  boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if ((command.getName().equalsIgnoreCase("lightpoles"))) {
      if (args.length == 0) return false;

      int permissionLevel = 0;
      if (isAdmin(sender)) permissionLevel = 3;
      else if (isAdvancedUser(sender)) permissionLevel = 2;
      else if (isUser(sender)) permissionLevel = 1;
      if (args[0].equalsIgnoreCase("help")) return onHelp(sender, permissionLevel);

      switch (permissionLevel) {
      case 3:
        if (args[0].equalsIgnoreCase("save")) return onSave(args);
        if (args[0].equalsIgnoreCase("set")) return onSet(args);
        if ((args[0].equalsIgnoreCase("power")) || (args[0].equalsIgnoreCase("powermode"))) return onTogglePower(sender);
        if (args[0].equalsIgnoreCase("repeater")) return onToggleRepeater(sender);
        if (args[0].equalsIgnoreCase("removetorch")) return onToggleRemoveTorch(sender);
        if ((args[0].equalsIgnoreCase("weather")) || (args[0].equalsIgnoreCase("weathermode"))) return onToggleWeather(sender);
        if ((args[0].equalsIgnoreCase("manually")) || (args[0].equalsIgnoreCase("manuallymode"))) return onToggleManually(sender);
        if ((args[0].equalsIgnoreCase("cluster")) || (args[0].equalsIgnoreCase("clustermode"))) return onToggleCluster(sender, args);
        if ((args[0].equalsIgnoreCase("daytime")) || (args[0].equalsIgnoreCase("daytimemode")) || (args[0].equalsIgnoreCase("time"))) return onToggleDaytime(sender);
        if (!args[0].equalsIgnoreCase("bruteforce")) break; return onBruteforce(sender, args);
      case 2:
        if (args[0].equalsIgnoreCase("connect")) return onConnect(sender);
        if (args[0].equalsIgnoreCase("info")) return onInfo(sender);
        if (args[0].equalsIgnoreCase("update")) return onUpdate();
        if (args[0].equalsIgnoreCase("on")) return onForceOn();
        if (args[0].equalsIgnoreCase("off")) return onForceOff();
      case 1:
        if (args[0].equalsIgnoreCase("list")) return listLamps(sender, args);
        if (args[0].equalsIgnoreCase("materials")) return listMaterials(sender);
        if (args[0].equalsIgnoreCase("modes")) return listModes(sender);
      }
    }
    return false;
  }

  private boolean isAdmin(CommandSender sender) {
    return opOnly ? isOP(sender) : sender.hasPermission("streetlamps.admin");
  }
  private boolean isAdvancedUser(CommandSender sender) {
    return opOnly ? isOP(sender) : sender.hasPermission("streetlamps.advancedUser");
  }
  private boolean isUser(CommandSender sender) {
    return opOnly ? isOP(sender) : sender.hasPermission("streetlamps.user");
  }
  private boolean isOP(CommandSender sender) {
    return sender.isOp();
  }

  private boolean onHelp(CommandSender sender, int permissionLevel) {
    switch (permissionLevel) {
    case 3:
      sender.sendMessage("### Admin ###");
      sender.sendMessage("save - saves all loaded lamps");
      sender.sendMessage("set on|off <id> - sets the on/off material");
      sender.sendMessage("<mode> - toggles the specified mode");
      sender.sendMessage("        -> modes are: power, weather, manually, time, repeater, removetorch");
      sender.sendMessage("cluster [set <value>] - toggles cluster/sets the size");
    case 2:
      sender.sendMessage("### AdvancedUser ###");
      sender.sendMessage("connect - starts/finishes connecting lamps to a controller");
      sender.sendMessage("info - used lamps will display information");
      sender.sendMessage("on, off, update - force all lamps to turn on/off or update");
    case 1:
      sender.sendMessage("### User ###");
      sender.sendMessage("list [worldname] - lists all lamps in the specific world");
      sender.sendMessage("materials - lists all materials needed to build lamps");
      sender.sendMessage("modes - lists all modes");
    }
    return true;
  }
  private boolean onInfo(CommandSender sender) {
    if (SLPlayerListener.infoPlayer.remove((Player)sender)) {
      sender.sendMessage("deactivated info-mode");
    } else {
      SLPlayerListener.infoPlayer.add((Player)sender);
      sender.sendMessage("activated info-mode");
    }
    return true;
  }
  private boolean onConnect(CommandSender sender) {
    LampWorld lampWorld = WorldCollection.getLampWorld(((Player)sender).getWorld());
    if (lampWorld == null) return true;
    lampWorld.controllerList.onCommand((Player)sender);
    return true;
  }

  private boolean onUpdate() {
    for (LampWorld lampWorld : WorldCollection.lampWorlds) {
      lampWorld.update_onCommand();
    }
    return true;
  }
  private boolean onForceOn() {
    for (LampWorld lampWorld : WorldCollection.lampWorlds) {
      lampWorld.forceOn_byCommand();
    }
    return true;
  }
  private boolean onForceOff() {
    for (LampWorld lampWorld : WorldCollection.lampWorlds) {
      lampWorld.forceOff_byCommand();
    }
    return true;
  }

  private boolean onSave(String[] args) {
    WorldCollection.saveLamps();
    LightPoles.broadcast("saved StreetLamps");
    return true;
  }
  private boolean onSet(String[] args) {
    if (args.length > 2) {
      if (args[1].equalsIgnoreCase("on")) {
        try {
          int id = Integer.parseInt(args[2]);

          Bulb.changingMaterial = true;
          for (LampWorld lampWorld : WorldCollection.lampWorlds) {
            lampWorld.changeMaterialOn(id);
          }
          Bulb.changingMaterial = false;
          Bulb.MATERIAL_ON = id;

          this.config.setON(id);

          LightPoles.broadcast("Changed bulb (ON) to " + id + ".");
        } catch (NumberFormatException e) {
          return false;
        }
        return true;
      }if (args[1].equalsIgnoreCase("off")) {
        try {
          int id = Integer.parseInt(args[2]);

          Bulb.changingMaterial = true;
          for (LampWorld lampWorld : WorldCollection.lampWorlds) {
            lampWorld.changeMaterialOff(id);
          }
          Bulb.changingMaterial = false;
          Bulb.MATERIAL_OFF = id;

          this.config.setOFF(id);

          LightPoles.broadcast("Changed bulb (OFF) to " + id + ".");
        } catch (NumberFormatException e) {
          return false;
        }
        return true;
      }
    }
    return false;
  }

  private boolean onTogglePower(CommandSender sender) {
    Base.POWERMODE = !Base.POWERMODE;
    this.config.setPower(Base.POWERMODE);
    onUpdate();

    sender.sendMessage("You turned POWER " + (Base.POWERMODE ? "ON" : "OFF"));
    return true;
  }
  private boolean onToggleRepeater(CommandSender sender) {
    Base.REPEATER = !Base.REPEATER;
    this.config.setRepeater(Base.REPEATER);
    onUpdate();

    sender.sendMessage("You turned REPREATER " + (Base.REPEATER ? "ON" : "OFF"));
    return true;
  }
  private boolean onToggleRemoveTorch(CommandSender sender) {
    Lamp.REMOVETORCH = !Lamp.REMOVETORCH;
    this.config.setRemoveTorch(Lamp.REMOVETORCH);

    sender.sendMessage("You turned REMOVETORCH " + (Lamp.REMOVETORCH ? "ON" : "OFF"));
    return true;
  }
  private boolean onToggleWeather(CommandSender sender) {
    SLWeatherListener.WEATHERMODE = !SLWeatherListener.WEATHERMODE;
    this.config.setWeather(SLWeatherListener.WEATHERMODE);
    onUpdate();

    sender.sendMessage("You turned WEATHER " + (SLWeatherListener.WEATHERMODE ? "ON" : "OFF"));
    return true;
  }
  private boolean onToggleManually(CommandSender sender) {
    SLPlayerListener.MANUALLY = !SLPlayerListener.MANUALLY;
    this.config.setManually(SLPlayerListener.MANUALLY);

    sender.sendMessage("You turned MANUALLY " + (SLPlayerListener.MANUALLY ? "ON" : "OFF"));
    return true;
  }
  private boolean onToggleDaytime(CommandSender sender) {
    SLTimeListener.DAYTIMEMODE = !SLTimeListener.DAYTIMEMODE;
    this.config.setDaytime(SLTimeListener.DAYTIMEMODE);
    onUpdate();

    sender.sendMessage("You turned TIME " + (SLTimeListener.DAYTIMEMODE ? "ON" : "OFF"));
    return true;
  }
  private boolean onToggleCluster(CommandSender sender, String[] args) {
    if (args.length > 1) return onSetCluster(sender, args);
    LampCollection.CLUSTER = !LampCollection.CLUSTER;
    this.config.setCluster(LampCollection.CLUSTER);

    sender.sendMessage("You turned CLUSTER " + (LampCollection.CLUSTER ? "ON" : "OFF"));
    return true;
  }
  private boolean onSetCluster(CommandSender sender, String[] args) {
    try {
      int size = Integer.parseInt(args[1]);
      UpdateQueue.SIZE = size;
      this.config.setClusterSize(size);

      sender.sendMessage("You set SIZE of CLUSTER to " + size);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
  private boolean onBruteforce(CommandSender sender, String[] args) {
    if (!PureBulb.enabled) {
      sender.sendMessage("PureBulbs is disabled");
    }
    if (PureBulb.running) {
      if ((args.length > 1) && (args[1].equals("silent")))
        PureBulb.silent = !PureBulb.silent;
      else {
        sender.sendMessage("still processing " + PureBulb.currentworld + " (" + PureBulb.current + "/" + PureBulb.max + " chunks)");
      }
      return true;
    }

    if (args.length < 2) return false;

    boolean silent = false;
    int start = 0;
    int stop = 127;
    boolean first = true;
    for (int i = 2; i < args.length; i++) {
      if (args[i].equals("silent"))
        silent = true;
      else if (args[i].matches("[0-9]+")) {
        if (first) {
          try {
            start = Integer.parseInt(args[i]);
            if (start > 127) start = 127; 
          }
          catch (NumberFormatException nfe) {
            return false;
          }
          first = false;
        } else {
          try {
            stop = Integer.parseInt(args[i]);
            if (stop <= 127) break; stop = 127;
          } catch (NumberFormatException nfe) {
            return false;
          }
        }
      }
    }

    if (!PureBulb.create(args[1], silent, start, stop))
      listWorlds(sender);
    else {
      sender.getServer().broadcastMessage("[StreetLamps] creating PureBulbs, expect server lag");
    }

    return true;
  }

  private boolean listLamps(CommandSender sender, String[] args) {
    if (args.length > 1) {
      LampWorld lampWorld = WorldCollection.getLampWorld(args[1]);
      if (lampWorld == null) listWorlds(sender);

      String report = "### " + lampWorld.world.getName() + " ###/n" + lampWorld.getReport();
      for (String line : report.split("/n")) {
        sender.sendMessage(line);
      }
      return true;
    }
    return listWorlds(sender);
  }
  private boolean listWorlds(CommandSender sender) {
    if (WorldCollection.lampWorlds.isEmpty()) return true;
    String worldList = "";

    for (int i = 0; i < WorldCollection.lampWorlds.size() - 1; i++) {
      worldList = worldList + ((LampWorld)WorldCollection.lampWorlds.get(i)).world.getName() + ", ";
    }
    worldList = worldList + ((LampWorld)WorldCollection.lampWorlds.get(WorldCollection.lampWorlds.size() - 1)).world.getName();

    if (!worldList.equals("")) {
      sender.sendMessage("loaded Worlds: " + worldList);
    }
    return true;
  }
  private boolean listMaterials(CommandSender sender) {
    String report = "### Materials ###/n";
    report = report + "tool: " + SLBlockListener.MATERIAL_buildTOOL + "/n";
    report = report + "on: " + Bulb.MATERIAL_ON + "/n";
    report = report + "off: " + Bulb.MATERIAL_OFF;

    for (String line : report.split("/n")) {
      sender.sendMessage(line);
    }
    return true;
  }
  private boolean listModes(CommandSender sender) {
    String report = "### Modes ###/n";
    report = report + "manually: " + SLPlayerListener.MANUALLY + "/n";
    report = report + "weather: " + SLWeatherListener.WEATHERMODE + "/n";
    report = report + "daytime: " + SLTimeListener.DAYTIMEMODE + "/n";
    report = report + "power: " + Base.POWERMODE + (Base.REPEATER ? " (detects repeater)" : "") + (Lamp.REMOVETORCH ? " (remove torch)" : "") + "/n";
    report = report + "cluster: " + LampCollection.CLUSTER + " (size: " + UpdateQueue.SIZE + ")";

    for (String line : report.split("/n")) {
      sender.sendMessage(line);
    }
    return true;
  }
}