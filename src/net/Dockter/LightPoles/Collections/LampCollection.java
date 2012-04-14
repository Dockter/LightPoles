package net.Dockter.LightPoles.Collections;

import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Fence;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.LampBlock.BlockComparable;
import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.UpdateQueue;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.World;

public class LampCollection
{
  public final World world;
  private final String lampType;
  public static boolean CLUSTER = false;
  private UpdateQueue queue = new UpdateQueue();

  public ArrayList<Lamp> lampList = new ArrayList();
  private ArrayList<Base> baseBlocks = new ArrayList();
  private ArrayList<Bulb> bulbs = new ArrayList();
  private ArrayList<Fence> fences = new ArrayList();

  public LampCollection(World world, String lampType)
  {
    this.world = world;
    this.lampType = lampType;
  }
  public boolean matches(String lampType) {
    return this.lampType.equals(lampType);
  }

  public Lamp add(Lamp lamp)
  {
    if (lamp == null) return null;

    this.fences.addAll(Arrays.asList(lamp.getFences()));
    this.bulbs.addAll(Arrays.asList(lamp.getBulbs()));

    this.baseBlocks.addAll(Arrays.asList(lamp.getBases()));

    this.lampList.add(lamp);

    lamp.update();
    return lamp;
  }
  public void addAll(ArrayList<Lamp> lamps) {
    for (Lamp lamp : lamps) {
      if (lamp == null) continue; add(lamp);
    }
  }

  public Lamp remove(LampBlock.BlockComparable comparable) {
    return remove(getLamp(comparable));
  }
  public Lamp remove(Lamp lamp) {
    if (lamp == null) return null;

    if (this.lampList.remove(lamp))
    {
      this.fences.removeAll(Arrays.asList(lamp.getFences()));
      this.bulbs.removeAll(Arrays.asList(lamp.getBulbs()));
      this.baseBlocks.removeAll(Arrays.asList(lamp.getBases()));

      lamp.forceOn();
      return lamp;
    }

    return null;
  }
  public void expand(Lamp existingLamp) {
    if (existingLamp == null) return;

    for (Fence fence : existingLamp.getFences()) {
      if (this.fences.contains(fence)) continue; this.fences.add(fence);
    }
    for (Bulb bulb : existingLamp.getBulbs()) {
      if (this.bulbs.contains(bulb)) continue; this.bulbs.add(bulb);
    }
  }

  public void allOn_byMode()
  {
    if (!CLUSTER) {
      for (Lamp lamp : this.lampList)
        lamp.turnOn();
    }
    else
      this.queue.turnOn(this.lampList);
  }

  public void allOff_byMode() {
    if (!CLUSTER) {
      for (Lamp lamp : this.lampList)
        lamp.turnOff();
    }
    else
      this.queue.turnOff(this.lampList);
  }

  public void update_onCommand() {
    for (Lamp lamp : this.lampList)
      lamp.update();
  }

  public void forceOff_byMode() {
    for (Lamp lamp : this.lampList)
      lamp.forceOff_byMode();
  }

  public void forceOff_byCommand() {
    for (Lamp lamp : this.lampList)
      lamp.forceOff_byCommand();
  }

  public void forceOn_byCommand() {
    for (Lamp lamp : this.lampList)
      lamp.forceOn_byMode();
  }

  public String getString()
  {
    String ret = "";
    for (Lamp lamp : this.lampList) {
      ret = ret + lamp.getString() + ";";
    }
    return ret;
  }

  public Base getBase(LampBlock.BlockComparable base)
  {
    int index = this.baseBlocks.indexOf(base);
    if (index == -1) return null;
    return (Base)this.baseBlocks.get(index);
  }
  public Lamp getLamp(LampBlock.BlockComparable comparable) {
    if (comparable.id == 85) {
      int index = this.fences.indexOf(comparable);
      if (index != -1)
        return ((Fence)this.fences.get(index)).lamp;
    }
    else if ((comparable.id == Bulb.MATERIAL_OFF) || (comparable.id == Bulb.MATERIAL_ON)) {
      int index = this.bulbs.indexOf(comparable);
      if (index != -1) {
        return ((Bulb)this.bulbs.get(index)).lamp;
      }
    }

    return null;
  }
  public boolean containsBulb(LampBlock.BlockComparable bulb) {
    return this.bulbs.contains(bulb);
  }

  public void onDisable() {
    forceOn_byCommand();
  }

  public void changeMaterialOn(int id) {
    for (Bulb bulb : this.bulbs)
      bulb.changeMaterialOn(id);
  }

  public void changeMaterialOff(int id) {
    for (Bulb bulb : this.bulbs)
      bulb.changeMaterialOff(id);
  }

  public boolean equals(Object obj)
  {
    if ((obj instanceof World)) return ((World)obj).equals(this.world);
    if ((obj instanceof LampCollection)) return ((LampCollection)obj).world.equals(this.world);
    return false;
  }
  public boolean belongsToLamp(LampBlock.BlockComparable comparable) {
    return (this.bulbs.contains(comparable)) || (this.fences.contains(comparable));
  }
}