package net.Dockter.LightPoles.Lamps.Blocks;

import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Fence extends LampBlock
{
  public static final int ID = 85;

  public Fence(Block block, Lamp lamp)
  {
    super(block, lamp);
  }

  public boolean isIntact() {
    return getBlock().getTypeId() == 85;
  }

  public String getString() {
    return parseString(new double[] { this.location.getX(), this.location.getY(), this.location.getZ() });
  }
}