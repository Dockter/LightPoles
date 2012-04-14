package net.Dockter.LightPoles.Lamps;

import de.bukkit.Ginsek.StreetLamps.Collections.LampCollection;
import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Configs.LampLoader;
import de.bukkit.Ginsek.StreetLamps.Configs.PluginConfig;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.LampBlock.BlockComparable;
import de.bukkit.Ginsek.StreetLamps.StreetLamps;
import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;

public abstract class LampProvider
{
  protected final String name;
  protected final String pattern;
  protected final LampWorld lampWorld;
  protected final World world;
  public final LampCollection lamps;

  protected LampProvider(String name, String pattern, LampWorld lampWorld)
  {
    this.name = name;
    this.pattern = pattern;
    this.lampWorld = lampWorld;
    if (lampWorld != null) {
      this.world = lampWorld.world;
      this.lamps = new LampCollection(this.world, name);
    } else {
      this.world = null;
      this.lamps = null;
    }
  }
  protected abstract Lamp create(String paramString, World paramWorld);

  public abstract Lamp createLamp(Block paramBlock);

  public String getName() { return this.name; }

  public String getString() {
    return this.lamps.getString();
  }
  public boolean provides(String str) {
    return getName().equals(str);
  }
  public void onDisable() {
    this.lamps.onDisable();
  }
  public Lamp getLamp(LampBlock.BlockComparable comparable) {
    return this.lamps.getLamp(comparable);
  }
  public Lamp unregister(LampBlock.BlockComparable comparable) {
    return this.lamps.remove(comparable);
  }
  public void forceOn_byCommand() {
    this.lamps.forceOn_byCommand();
  }
  public void forceOff_byMode() {
    this.lamps.forceOff_byMode();
  }
  public void forceOff_byCommand() {
    this.lamps.forceOff_byCommand();
  }
  public void allOn_byMode() {
    this.lamps.allOn_byMode();
  }
  public void allOff_byMode() {
    this.lamps.allOff_byMode();
  }

  public void update_onCommand()
  {
    this.lamps.update_onCommand();
  }
  public boolean matches(String string) {
    return string.matches(this.pattern);
  }
  public void load(LampLoader lampLoader, LampWorld lampWorld) {
    String storedLamps = lampLoader.getLamps(this.world.getName(), this.name);

    if ((storedLamps == null) || (storedLamps.isEmpty())) {
      return;
    }

    int count = storedLamps.split(";").length;
    for (String lampString : storedLamps.split(";")) {
      if ((lampString != null) && (!lampString.isEmpty())) {
        if (lampString.matches(this.pattern)) {
          Lamp lamp = create(lampString, this.world);
          if ((lamp == null) || 
            (!lamp.isIntact())) continue;
          this.lamps.add(lamp);
        }
        else
        {
          LightPoles.log("pattern mismatch: " + lampString, new Object[0]);
        }
      }
    }
    LightPoles.log("loaded " + this.lamps.lampList.size() + "/" + count + " " + getName() + " for " + this.world.getName(), new Object[0]);
  }

  public boolean equals(Object obj) {
    if ((obj instanceof LampProvider)) {
      return ((LampProvider)obj).getName().equals(getName());
    }
    return false; } 
  public abstract void setup(PluginConfig paramPluginConfig);

  public abstract LampProvider setup(LampWorld paramLampWorld);

  public boolean containsBulb(LampBlock.BlockComparable bulb) { return this.lamps.containsBulb(bulb); }

  public Base getBase(LampBlock.BlockComparable base) {
    return this.lamps.getBase(base);
  }

  public void changeMaterialOn(int id) {
    this.lamps.changeMaterialOn(id);
  }
  public void changeMaterialOff(int id) {
    this.lamps.changeMaterialOff(id);
  }

  public String getReport() {
    return getName() + ": " + this.lamps.lampList.size();
  }

  public boolean belongsToLamp(LampBlock.BlockComparable comparable) {
    return this.lamps.belongsToLamp(comparable);
  }
}