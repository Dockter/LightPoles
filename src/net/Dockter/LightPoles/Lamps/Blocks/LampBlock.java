package net.Dockter.LightPoles.Lamps.Blocks;

import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import org.bukkit.Location;
import org.bukkit.block.Block;

public abstract class LampBlock
{
  protected final Location location;
  protected final Block block;
  public final Lamp lamp;
  private static final BlockComparable comparable = new BlockComparable(null);

  LampBlock(Block block, Lamp lamp)
  {
    if (block != null)
      this.location = block.getLocation();
    else {
      this.location = null;
    }
    this.block = block;
    this.lamp = lamp;
  }

  private boolean equals(Block block) {
    return this.location.equals(block.getLocation());
  }
  private boolean equals(Lamp lamp) {
    return lamp.equals(this.lamp);
  }
  public boolean equals(Object obj) {
    if ((obj instanceof Lamp)) return equals((Lamp)obj);
    if ((obj instanceof Block)) return equals((Block)obj);
    if ((obj instanceof LampBlock)) return this.location.equals(((LampBlock)obj).location);
    return super.equals(obj);
  }

  public Block getBlock() {
    return this.location.getBlock();
  }
  public Block getRelative(int x, int y, int z) {
    if (this.location.getBlock() != null) {
      return this.location.getBlock().getRelative(x, y, z);
    }
    return null;
  }

  protected static String parseString(String[] s)
  {
    String str = s[0];
    for (int i = 1; i < s.length; i++) {
      str = str + "," + s[i];
    }
    return str;
  }
  protected static String parseString(double[] d) {
    String str = Double.valueOf(d[0]).intValue();
    for (int i = 1; i < d.length; i++) {
      str = str + "," + Double.valueOf(d[i]).intValue();
    }
    return str;
  }
  public abstract String getString();

  public static BlockComparable getComparable(Block block) { comparable.block = block;
    comparable.id = block.getTypeId();
    return comparable; }

  public static final class BlockComparable {
    public Block block;
    public int id;

    public boolean equals(Object obj) {
      if (!(obj instanceof LampBlock)) return false;
      return ((LampBlock)obj).block.equals(this.block);
    }
  }
}