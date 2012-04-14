package net.Dockter.LightPoles.Configs;

import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.StreetLamps;
import java.io.File;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

public class LampLoader
{
  private Configuration config;
  private static final int VERSION = 7;
  public static final String[] lamps = { "pole", "ceiling", "pendant", "sconce", "globe", "bottom", "tube" };

  public int getVERSION() {
    return this.config.getInt("version", -1);
  }
  public void setVERSION(int version) {
    this.config.setProperty("version", Integer.valueOf(version));
  }

  public LampLoader(Plugin plugin) {
    this.config = new Configuration(new File(plugin.getDataFolder(), "lamps.yml"));
    this.config.load();

    int v = getVERSION();
    if (v < 7)
      switch (v) {
      case -1:
        updateTo1(this.config, plugin.getServer().getWorlds());
      case 1:
        break;
      case 0:
      default:
        this.config = plugin.getConfiguration();
        this.config.load();
      }
  }

  private void updateTo1(Configuration config, List<World> worlds)
  {
    String[] lamps = { "pole", "ceiling", "pendant", "sconce", "globe", "bottom", "tube" };
    int lastID = 0;
    for (World world : worlds) {
      for (String lamptype : lamps) {
        String lampstring = config.getString(world.getName() + "." + lamptype, "");
        if ((!lampstring.isEmpty()) && (lampstring != ";")) {
          String lampString_new = "";
          for (String string : lampstring.split(";")) {
            if (!string.isEmpty()) {
              lampString_new = lampString_new + string + "," + lastID++ + ";";
            }
          }
          config.setProperty(world.getName() + "." + lamptype, lampString_new);
        }
      }
    }
    Lamp.lastID = lastID;
    config.setProperty("lastID", Integer.valueOf(lastID));

    config.save();
    setVERSION(1);
    LightPoles.log("# updated lamps.yml to VERSION 1", new Object[0]);
    LightPoles.log("# added IDs to existing lamps", new Object[0]);
  }

  public int getLastID() {
    return this.config.getInt("lastID", 0);
  }
  public String getLamps(String name) {
    return this.config.getString(name, "");
  }
  public String getLamps(String world, String name) {
    return this.config.getString(world + "." + name, "");
  }

  public void saveLamp(String world, String lampType, String string) {
    this.config.setProperty(world + "." + lampType, string);
  }
  public void save() {
    this.config.setProperty("lastID", Integer.valueOf(Lamp.lastID));
    this.config.save();
  }
}