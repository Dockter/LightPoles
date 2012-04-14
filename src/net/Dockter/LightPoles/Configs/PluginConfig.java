package net.Dockter.LightPoles.Configs;

import de.bukkit.Ginsek.StreetLamps.StreetLamps;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.util.config.Configuration;

public class PluginConfig
{
  private Configuration config;
  private static final int VERSION = 8;
  private static final boolean TESTBUILD = false;
  private static final int BUILD = 11;

  public PluginConfig(LightPoles plugin)
  {
    this.config = plugin.getConfiguration();
    this.config.load();

    int v = getVERSION();
    if (v < 8)
      switch (v) {
      case 3:
        updateTo4(this.config);
      case 4:
        updateTo5(this.config);
      case 5:
        updateTo6(this.config);
      case 6:
        updateTo7(this.config, plugin);
      case 7:
        updateTo8(this.config);
        break;
      default:
        extractFile(plugin, "config.yml");
        this.config = plugin.getConfiguration();
        this.config.load();
      }
  }

  private void updateTo4(Configuration config)
  {
    config.setProperty("Config.modes.cluster.enabled", Boolean.valueOf(true));
    config.setProperty("Config.modes.cluster.size", Integer.valueOf(20));

    setVersion(4);
    config.save();
    LightPoles.log("# updated config.yml to VERSION 4", new Object[0]);
    LightPoles.log("# added new mode: CLUSTER", new Object[0]);
  }
  private void updateTo5(Configuration config) {
    String lampList = config.getString("Config.lamplist", "") + "globe;";
    config.setProperty("Config.lamplist", lampList);
    config.setProperty("Lamps.globe.height.min", Integer.valueOf(1));
    config.setProperty("Lamps.globe.height.max", Integer.valueOf(6));
    config.setProperty("Config.disableOnError", Boolean.valueOf(false));

    setVersion(5);
    config.save();
    LightPoles.log("# updated config.yml to VERSION 5", new Object[0]);
    LightPoles.log("# added new lamp: globe", new Object[0]);
    LightPoles.log("# added: disable on error", new Object[0]);
  }
  private void updateTo6(Configuration config) {
    String lampList = config.getString("Config.lamplist", "") + "tube;";
    config.setProperty("Config.lamplist", lampList);
    config.setProperty("Lamps.tube.height.min", Integer.valueOf(1));
    config.setProperty("Lamps.tube.height.max", Integer.valueOf(6));
    config.setProperty("Lamps.tube.width.min", Integer.valueOf(2));
    config.setProperty("Lamps.tube.width.max", Integer.valueOf(6));

    setVersion(6);
    config.save();
    LightPoles.log("# updated config.yml to VERSION 6", new Object[0]);
    LightPoles.log("# added new lamp: tube", new Object[0]);
  }
  private void updateTo7(Configuration config, LightPoles plugin) {
    LightPoles.log("# updated config.yml to VERSION 7", new Object[0]);

    LightPoles.log("# removed lamplist", new Object[0]);
    String lamplist = config.getString("Config.lamplist", "");
    config.removeProperty("Config.lamplist");

    LightPoles.log("# added separate switches for each lamp", new Object[0]);
    for (String lamp : LampLoader.lamps) {
      config.setProperty("Lamps." + lamp + ".enabled", Boolean.valueOf(false));
    }
    for (String lamp : lamplist.split(";")) {
      if ((lamp != null) && (!lamp.equals(""))) {
        config.setProperty("Lamps." + lamp + ".enabled", Boolean.valueOf(true));
      }
    }

    LightPoles.log("# added detectRepeater", new Object[0]);
    boolean power = config.getBoolean("Config.modes.power", false);
    config.removeProperty("Config.modes.power");
    config.setProperty("Config.modes.power.enabled", Boolean.valueOf(power));
    config.setProperty("Config.modes.power.detectRepeater", Boolean.valueOf(false));

    LightPoles.log("# moved start/end of day", new Object[0]);
    boolean daytime = config.getBoolean("Config.modes.daytime", false);
    config.removeProperty("Config.modes.daytime");
    int start = config.getInt("Config.daytime.night_start", 12500);
    int end = config.getInt("Config.daytime.night_end", 12500);
    config.removeProperty("Config.daytime.night_start");
    config.removeProperty("Config.daytime.night_end");
    config.removeProperty("Config.daytime");
    config.setProperty("Config.modes.daytime.enabled", Boolean.valueOf(daytime));
    config.setProperty("Config.modes.daytime.night_start", Integer.valueOf(start));
    config.setProperty("Config.modes.daytime.night_end", Integer.valueOf(end));

    LightPoles.log("# added pureBulb", new Object[0]);
    config.setProperty("Config.modes.pureBulb.enabled", Boolean.valueOf(false));

    LightPoles.log("# removed disableOnError", new Object[0]);
    config.removeProperty("Config.disableOnError");

    LightPoles.log("# added controller", new Object[0]);
    config.setProperty("Config.modes.controller.enabled", Boolean.valueOf(true));

    setVersion(7);
    config.save();
  }
  private void updateTo8(Configuration config) {
    LightPoles.log("# updated config.yml to VERSION 8", new Object[0]);

    LightPoles.log("# added removeTorchAfterConstruction: false", new Object[0]);
    config.setProperty("Config.modes.power.removeTorchAfterConstruction", Boolean.valueOf(true));

    setVersion(8);
    config.save();
  }

  private void setVersion(int version) {
    this.config.setProperty("Config.version", Integer.valueOf(version));
  }

  public int getVERSION()
  {
    return this.config.getInt("Config.version", -1);
  }

  public boolean getDaytime()
  {
    return this.config.getBoolean("Config.modes.daytime.enabled", false);
  }
  public int getNightStart() {
    return this.config.getInt("Config.modes.daytime.night_start", 12500);
  }
  public int getNightEnd() {
    return this.config.getInt("Config.modes.daytime.night_end", 22500);
  }
  public boolean getManually() {
    return this.config.getBoolean("Config.modes.manually", false);
  }
  public boolean getWeather() {
    return this.config.getBoolean("Config.modes.weather", false);
  }
  public boolean getPower() {
    return this.config.getBoolean("Config.modes.power.enabled", false);
  }
  public boolean getDetectRepeater() {
    return this.config.getBoolean("Config.modes.power.detectRepeater", false);
  }
  public boolean getRemoveTorch() {
    return this.config.getBoolean("Config.modes.power.removeTorchAfterConstruction", false);
  }
  public boolean getCluster() {
    return this.config.getBoolean("Config.modes.cluster.enabled", false);
  }
  public int getClusterSize() {
    return this.config.getInt("Config.modes.cluster.size", 20);
  }

  public String getLampList()
  {
    String lamplist = "";
    for (String lamp : LampLoader.lamps) {
      if (getLampEnabled(lamp)) {
        lamplist = lamplist + lamp + ";";
      }
    }
    return lamplist;
  }

  public int getON()
  {
    return this.config.getInt("Config.material.bulb_on", 89);
  }
  public int getOFF() {
    return this.config.getInt("Config.material.bulb_off", 20);
  }
  public int getBUILDTOOL() {
    return this.config.getInt("Config.material.tool", 76);
  }

  public int[] getMinMax(String name, String property)
  {
    int min = this.config.getInt("Lamps." + name + "." + property + ".min", 0);
    int max = this.config.getInt("Lamps." + name + "." + property + ".max", 0);
    return new int[] { min, max };
  }

  private boolean extractFile(LightPoles plugin, String name) {
    InputStream is = plugin.getClass().getClassLoader().getResourceAsStream(name);
    try {
      File configFile = new File(plugin.getDataFolder(), name);
      if (!configFile.getParentFile().exists()) {
        configFile.getParentFile().mkdirs();
      }

      extractFile(is, configFile);
    } catch (IOException e) {
      LightPoles.log("# could not extract file: " + name, new Object[0]);
      return false;
    }
    LightPoles.log("# extracted file: " + name, new Object[0]);
    return true;
  }

  private boolean extractFileFromFolder(LightPoles plugin, String name, String path) {
    InputStream is = plugin.getClass().getClassLoader().getResourceAsStream(path + File.separator + name);
    try {
      File folder = new File(plugin.getDataFolder().getPath(), path);
      folder.mkdirs();

      File languageFile = new File(folder, name);

      extractFile(is, languageFile);
    } catch (IOException e) {
      LightPoles.log("# could not extract file: " + name, new Object[0]);
      return false;
    }
    LightPoles.log("# extracted file: " + name, new Object[0]);
    return true;
  }
  private void extractFile(InputStream is, File file) throws IOException {
    byte[] buffer = new byte[is.available()];
    is.read(buffer);
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(buffer);
    fos.flush();
    is.close();
    fos.close();
  }

  public int getInt(String path) {
    return this.config.getInt(path, 0);
  }

  public boolean getCommandEnabled(String command) {
    return this.config.getBoolean("Comamnd." + command, false);
  }
  private boolean getLampEnabled(String name) {
    return this.config.getBoolean("Lamps." + name + ".enabled", false);
  }
  public boolean getPureBulb() {
    return this.config.getBoolean("Config.modes.pureBulb.enabled", false);
  }
  public boolean getControler() {
    return this.config.getBoolean("Config.modes.controller.enabled", false);
  }

  public void setON(int id)
  {
    this.config.setProperty("Config.material.bulb_on", Integer.valueOf(id));
    this.config.save();
  }
  public void setOFF(int id) {
    this.config.setProperty("Config.material.bulb_off", Integer.valueOf(id));
    this.config.save();
  }
  public void setClusterSize(int size) {
    this.config.setProperty("Config.modes.cluster.size", Integer.valueOf(size));
    this.config.save();
  }
  public void setPower(boolean b) {
    this.config.setProperty("Config.modes.power.enabled", Boolean.valueOf(b));
    this.config.save();
  }
  public void setRepeater(boolean b) {
    this.config.setProperty("Config.modes.power.detectRepeater", Boolean.valueOf(b));
    this.config.save();
  }
  public void setRemoveTorch(boolean b) {
    this.config.setProperty("Config.modes.power.removeTorchAfterConstruction", Boolean.valueOf(b));
    this.config.save();
  }
  public void setWeather(boolean b) {
    this.config.setProperty("Config.modes.weather", Boolean.valueOf(b));
    this.config.save();
  }
  public void setManually(boolean b) {
    this.config.setProperty("Config.modes.manually", Boolean.valueOf(b));
    this.config.save();
  }
  public void setDaytime(boolean b) {
    this.config.setProperty("Config.modes.daytime.enabled", Boolean.valueOf(b));
    this.config.save();
  }
  public void setCluster(boolean b) {
    this.config.setProperty("Config.modes.cluster.enabled", Boolean.valueOf(b));
    this.config.save();
  }
}