package net.Dockter.LightPoles.Lamps.Blocks;

import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Base extends LampBlock
{
  public static boolean POWERMODE = false;
  public static boolean REPEATER = false;

  public Base(Block block, Lamp lamp) {
    super(block, lamp);
  }

  public boolean isPowered() {
    Block block = getBlock();
    if (REPEATER) {
      return (block.isBlockPowered()) || (block.isBlockIndirectlyPowered());
    }
    return block.isBlockPowered();
  }

  public static boolean isPowered(Block block) {
    if (REPEATER) {
      return (block.isBlockPowered()) || (block.isBlockIndirectlyPowered());
    }
    return block.isBlockPowered();
  }

  public String getString() {
    return parseString(new double[] { this.location.getX(), this.location.getY(), this.location.getZ() });
  }

  public boolean isTorch() {
    return this.block.getTypeId() == 76;
  }
  public boolean isTorch(int x, int y, int z) {
    return this.block.getRelative(x, y, z).getTypeId() == 76;
  }
}