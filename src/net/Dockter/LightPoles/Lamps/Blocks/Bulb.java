package net.Dockter.LightPoles.Lamps.Blocks;

import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Bulb extends LampBlock
{
  public static int MATERIAL_ON;
  public static int MATERIAL_OFF;
  public final boolean activated;
  public static boolean changingMaterial = false;

  public Bulb(Block block, Lamp lamp, boolean activated)
  {
    super(block, lamp);
    this.activated = activated;
  }

  public boolean isIntact() {
    if (!this.activated) return true;
    Block block = getBlock();
    if (block == null) return true;

    return (block.getTypeId() == MATERIAL_ON) || (block.getTypeId() == MATERIAL_OFF);
  }

  public void forceOn()
  {
    if (this.activated) {
      Block block = getBlock();
      if (block != null)
        block.setTypeId(MATERIAL_ON);
    }
  }

  public void forceOff() {
    if (this.activated) {
      Block block = getBlock();
      if (block != null)
        block.setTypeId(MATERIAL_OFF);
    }
  }

  public void toggle() {
    if (changingMaterial) return;
    if (this.activated) {
      Block block = getBlock();
      if (block.getTypeId() == MATERIAL_ON)
        block.setTypeId(MATERIAL_OFF);
      else if (block.getTypeId() == MATERIAL_OFF)
        block.setTypeId(MATERIAL_ON);
    }
  }

  public String getString() {
    if (!this.activated) return parseString(new double[] { 0.0D, 0.0D, 0.0D, 0.0D });
    return parseString(new double[] { 1.0D, this.location.getX(), this.location.getY(), this.location.getZ() });
  }

  public void changeMaterialOn(int id)
  {
    if (!this.activated) return;

    Block block = getBlock();
    if (block.getTypeId() == MATERIAL_ON)
      block.setTypeId(id);
  }

  public void changeMaterialOff(int id) {
    if (!this.activated) return;

    Block block = getBlock();
    if (block.getTypeId() == MATERIAL_OFF)
      block.setTypeId(id);
  }

  public boolean isOff()
  {
    return getBlock().getTypeId() == MATERIAL_OFF;
  }
}