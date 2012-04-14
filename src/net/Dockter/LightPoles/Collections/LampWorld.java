package net.Dockter.LightPoles.Collections;

import de.bukkit.Ginsek.StreetLamps.Configs.LampLoader;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.LampBlock;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.LampBlock.BlockComparable;
import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampController;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampProvider;
import de.bukkit.Ginsek.StreetLamps.Listener.SLTimeListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLWeatherListener;
import de.bukkit.Ginsek.StreetLamps.StreetLamps;
import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LampWorld
{
  public final World world;
  public final ArrayList<LampProvider> provider;
  private final SLTimeListener timeListener;
  public final ControllerList controllerList = new ControllerList();

  public boolean night = false;
  public boolean raining = false;

  private static final WorldComparable comparable = new WorldComparable(null);

  public LampWorld(World world, ProviderList providerList, LampLoader lampLoader)
  {
    this.world = world;

    this.provider = providerList.getProvider(this);

    for (LampProvider lampProvider : this.provider) {
      lampProvider.load(lampLoader, this);
    }

    for (String controle : lampLoader.getLamps(world.getName(), "LampController").split(";")) {
      if (!controle.isEmpty()) {
        LampController cont = new LampController(controle, world);
        if ((!LampController.usedBlocks.contains(cont.block)) && (cont.lamps.size() > 0)) {
          LampController.usedBlocks.add(cont.block);
          this.controllerList.controllerList.add(cont);
        }
      }
    }
    LightPoles.log("loaded " + this.controllerList.controllerList.size() + " LampController for " + world.getName(), new Object[0]);

    this.timeListener = new SLTimeListener(this);
    this.timeListener.start();
  }
  public LampWorld(World world) {
    this.world = world;
    this.provider = null;
    this.timeListener = null;
  }

  public Lamp createLamp(Block block)
  {
    for (LampProvider p : this.provider) {
      Lamp lamp = p.createLamp(block);
      if (lamp != null) return lamp;
    }
    return null;
  }
  public Lamp getLamp(Block block) {
    LampBlock.BlockComparable comparable = LampBlock.getComparable(block);

    for (LampProvider p : this.provider) {
      Lamp lamp = p.getLamp(comparable);
      if (lamp != null) return lamp;
    }
    return null;
  }
  public Lamp removeLamp(Block block) {
    LampBlock.BlockComparable comparable = LampBlock.getComparable(block);
    for (LampProvider p : this.provider) {
      Lamp lamp = p.unregister(comparable);
      if (lamp != null) return lamp;
    }
    return null;
  }

  public Base getBase(Block block) {
    LampBlock.BlockComparable comparable = LampBlock.getComparable(block);
    for (LampProvider p : this.provider) {
      Base base = p.getBase(comparable);
      if (base != null) return base;
    }

    return null;
  }

  public void turnOn_byMode() {
    for (LampProvider p : this.provider)
      p.allOn_byMode();
  }

  public void turnOff_byMode() {
    for (LampProvider p : this.provider)
      p.allOff_byMode();
  }

  public void forceOn_byCommand()
  {
    for (LampProvider p : this.provider)
      p.forceOn_byCommand();
  }

  public void forceOff_byMode()
  {
    for (LampProvider p : this.provider)
      p.forceOff_byMode();
  }

  public void forceOff_byCommand() {
    for (LampProvider p : this.provider)
      p.forceOff_byCommand();
  }

  public void update_onCommand()
  {
    for (LampProvider p : this.provider)
      p.update_onCommand();
  }

  public void onDisable()
  {
    for (LampProvider p : this.provider)
      p.onDisable();
  }

  public void saveLamps(LampLoader lampLoader)
  {
    String worldName = this.world.getName();
    for (LampProvider lampProvider : this.provider) {
      lampLoader.saveLamp(worldName, lampProvider.getName(), lampProvider.getString());
    }

    String controllerString = "";
    for (LampController controle : this.controllerList.controllerList) {
      controllerString = controllerString + controle.getString() + ";";
    }
    lampLoader.saveLamp(worldName, "LampController", controllerString);
  }

  public boolean isNight() {
    if (!SLTimeListener.DAYTIMEMODE) return false;
    return this.night;
  }
  public boolean isRaining() {
    if (!SLWeatherListener.WEATHERMODE) return false;
    return this.raining;
  }

  public void changeMaterialOn(int id) {
    for (LampProvider p : this.provider)
      p.changeMaterialOn(id);
  }

  public void changeMaterialOff(int id) {
    for (LampProvider p : this.provider)
      p.changeMaterialOff(id);
  }

  public String getReport()
  {
    String report = "";
    for (LampProvider p : this.provider) {
      report = report + p.getReport() + "/n";
    }
    report = report + "LampController: " + this.controllerList.controllerList.size();
    return report;
  }

  public boolean equals(Object obj) {
    if ((obj instanceof LampWorld)) return ((LampWorld)obj).world.equals(this.world);
    if ((obj instanceof World)) return ((World)obj).equals(this.world);
    return false;
  }

  public boolean belongsToLamp(Block block) {
    LampBlock.BlockComparable comparable = LampBlock.getComparable(block);
    for (LampProvider p : this.provider) {
      if (p.belongsToLamp(comparable)) return true;
    }
    return false;
  }

  public static WorldComparable getComparable(World world) {
    comparable.world = world;
    return comparable;
  }

  public static final class WorldComparable {
    public World world;

    public boolean equals(Object obj) {
      if (!(obj instanceof LampWorld)) return false;
      return ((LampWorld)obj).world.equals(this.world);
    }
  }
}