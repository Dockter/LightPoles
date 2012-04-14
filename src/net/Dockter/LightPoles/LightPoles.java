package net.Dockter.LightPoles;

import de.bukkit.Ginsek.StreetLamps.Collections.LampCollection;
import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Collections.WorldCollection;
import de.bukkit.Ginsek.StreetLamps.Configs.LampLoader;
import de.bukkit.Ginsek.StreetLamps.Configs.PluginConfig;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Listener.SLBlockListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLPlayerListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLTimeListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLWeatherListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLWorldListener;
import java.io.PrintStream;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LightPoles extends JavaPlugin
{
  private static PluginDescriptionFile package_description = null;
  public static LightPoles plugin;
  private CommandHandler commandListener = null;
  private final SLWorldListener worldListener = new SLWorldListener();
  private final SLBlockListener blockListener = new SLBlockListener();
  private final SLWeatherListener weatherListener = new SLWeatherListener();
  private final SLPlayerListener playerListener = new SLPlayerListener();

  private PluginConfig config = null;
  private LampLoader lampLoader = null;

  public static String getVersion() {
    return package_description.getVersion();
  }
  public static String getName() {
    return package_description.getName();
  }

  public void enablePlugin() {
    getServer().getPluginManager().enablePlugin(this);
  }
  public void disablePlugin() {
    getServer().getPluginManager().disablePlugin(this);
  }

  public void onEnable() {
    if (package_description == null) {
      package_description = getDescription();
    }
    plugin = this;

    this.config = new PluginConfig(this);

    de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb.LightPoles = this.config.getON();
    de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb.LightPoles = this.config.getOFF();

    SLBlockListener.MATERIAL_buildTOOL = this.config.getBUILDTOOL();

    this.lampLoader = new LampLoader(this);
    de.bukkit.Ginsek.StreetLamps.Lamps.Lamp.LightPoles = this.lampLoader.getLastID();
    de.bukkit.Ginsek.StreetLamps.Lamps.Lamp.LightPoles = this.config.getRemoveTorch();

    de.bukkit.Ginsek.StreetLamps.Collections.ControllerList.LightPoles = this.config.getControler();

    WorldCollection.setup(this.lampLoader, this.config);
    getServer().getPluginManager().registerEvent(Event.Type.WORLD_LOAD, this.worldListener, Event.Priority.Monitor, this);
    getServer().getPluginManager().registerEvent(Event.Type.WORLD_INIT, this.worldListener, Event.Priority.Monitor, this);

    getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGE, this.blockListener, Event.Priority.Monitor, this);
    getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.Monitor, this);
    getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BURN, this.blockListener, Event.Priority.Monitor, this);
    getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PHYSICS, this.blockListener, Event.Priority.Monitor, this);

    getServer().getPluginManager().registerEvent(Event.Type.WEATHER_CHANGE, this.weatherListener, Event.Priority.Monitor, this);

    getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Monitor, this);

    this.commandListener = new CommandHandler(this.config);

    SLPlayerListener.MANUALLY = this.config.getManually();
    if (SLPlayerListener.MANUALLY) log("MANUALLY enabled", new Object[0]);

    Base.POWERMODE = this.config.getPower();
    if (Base.POWERMODE) log("POWER enabled", new Object[0]);

    SLTimeListener.plugin = this;
    SLTimeListener.NIGHT_START = this.config.getNightStart();
    SLTimeListener.NIGHT_END = this.config.getNightEnd();
    SLTimeListener.DAYTIMEMODE = this.config.getDaytime();
    if (SLTimeListener.DAYTIMEMODE) log("DAYTIME enabled", new Object[0]);

    SLWeatherListener.WEATHERMODE = this.config.getWeather();
    if (SLWeatherListener.WEATHERMODE) log("WEATHER enabled", new Object[0]);

    de.bukkit.Ginsek.StreetLamps.Lamps.UpdateQueue.LightPoles = this;
    de.bukkit.Ginsek.StreetLamps.Lamps.UpdateQueue.LightPoles = this.config.getClusterSize();
    LampCollection.CLUSTER = this.config.getCluster();
    if (LampCollection.CLUSTER) log("CLUSTER enabled", new Object[0]);

    for (World world : getServer().getWorlds()) {
      this.worldListener.loadLamps(world);
    }

    for (LampWorld lampWorld : WorldCollection.lampWorlds) {
      lampWorld.update_onCommand();
    }

    rawLog("%s %s is enabled", new Object[] { getName(), getVersion() });
  }
  public void onDisable() {
    if (this.worldListener != null) {
      WorldCollection.saveLamps();

      for (LampWorld lampWorld : WorldCollection.lampWorlds) {
        lampWorld.onDisable();
      }
    }

    rawLog("%s %s is disabled", new Object[] { getName(), getVersion() });
  }

  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
  {
    return this.commandListener.onCommand(sender, command, commandLabel, args);
  }

  private static String formatLogMessage(String message, Object[] params)
  {
    return String.format("[%s] %s", new Object[] { getName(), String.format(message, params) });
  }
  public static void rawLog(String message, Object[] params) {
    System.out.println(String.format(message, params));
  }
  public static void log(String message, Object[] params) {
    System.out.println(formatLogMessage(message, params));
  }
  public static void debug(String message) {
    System.out.println(message);
  }
  public static void broadcast(String string) {
    plugin.getServer().broadcastMessage(string);
  }
}