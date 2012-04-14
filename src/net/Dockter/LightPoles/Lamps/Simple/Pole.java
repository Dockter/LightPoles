package net.Dockter.LightPoles.Lamps.Simple;

import de.bukkit.Ginsek.StreetLamps.Collections.LampCollection;
import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Configs.PluginConfig;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Fence;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.LampBlock;
import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampProvider;
import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Pole extends Lamp
{
  private static int MAX_HEIGHT;
  private static int MIN_HEIGHT;
  private final int h;
  private ArrayList<Fence> fences = new ArrayList();
  protected Bulb bulb = null;
  protected Base base = null;

  public static LampProvider provider = new Provider("pole", getPattern());

  private Pole(int h, Block bulb, LampWorld lampWorld)
  {
    super(lampWorld);

    this.h = h;

    this.bulb = new Bulb(bulb, this, true);

    for (int i = h; i > 0; i--) {
      Fence fence = new Fence(bulb.getRelative(0, -i, 0), this);
      if (!this.fences.contains(fence)) {
        this.fences.add(fence);
      }

    }

    this.base = new Base(bulb.getRelative(0, -(h + 1), 0), this);
  }

  protected int removeTorch() {
    if (!this.base.isTorch(0, -1, 0)) return 0;
    this.base.getBlock().getRelative(0, -1, 0).setTypeId(0);
    return 1;
  }

  public Bulb[] getBulbs()
  {
    return new Bulb[] { this.bulb };
  }

  public Fence[] getFences() {
    return (Fence[])this.fences.toArray(new Fence[0]);
  }

  public Base[] getBases() {
    return new Base[] { this.base };
  }

  protected void on()
  {
    this.bulb.forceOn();
  }

  protected void off() {
    this.bulb.forceOff();
  }

  protected void toggle() {
    this.bulb.toggle();
  }

  public boolean isPowered()
  {
    if (!Base.POWERMODE) return true;
    return this.base.isPowered();
  }

  public boolean isIntact()
  {
    if (!this.bulb.isIntact()) {
      return false;
    }

    for (Fence fence : this.fences) {
      if (!fence.isIntact()) return false;
    }
    return true;
  }

  public String createString()
  {
    return parseString(new String[] { this.h, this.bulb.getString() });
  }

  protected boolean containsBulb(LampBlock comparable)
  {
    return this.bulb.equals(comparable);
  }

  protected boolean containsFence(LampBlock comparable) {
    return this.fences.contains(comparable);
  }

  public boolean equals(Lamp lamp) {
    if ((lamp instanceof Pole)) {
      return ((Pole)lamp).bulb.equals(this.bulb);
    }
    return false;
  }

  public String getName()
  {
    return "pole";
  }

  private static String getPattern()
  {
    return parseString(new String[] { "[0-9]+", "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "[0-9]+" });
  }

  public static class Provider extends LampProvider {
    Provider(String name, String pattern) {
      super(pattern, null);
    }

    public Lamp createLamp(Block bulb)
    {
      if (bulb == null) return null;

      int h = 0;
      for (h = 0; h <= Pole.MAX_HEIGHT + 1; h++)
        if (bulb.getRelative(0, -(h + 1), 0).getTypeId() != 85)
          break;
      if ((h < Pole.MIN_HEIGHT) || (h > Pole.MAX_HEIGHT)) {
        return null;
      }

      if (!Base.isPowered(bulb.getRelative(0, -(h + 1), 0))) {
        return null;
      }

      Pole pole = new Pole(h, bulb, this.lampWorld, null);
      Pole.access$3(pole);
      return this.lamps.add(pole);
    }

    protected Lamp create(String string, World world) {
      String[] param = string.split(",");
      Block block = world.getBlockAt(Integer.valueOf(param[2]).intValue(), Integer.valueOf(param[3]).intValue(), Integer.valueOf(param[4]).intValue());

      Pole pole = new Pole(Integer.valueOf(param[0]).intValue(), block, this.lampWorld, null);
      pole.setID(Integer.valueOf(param[5]).intValue());
      return pole;
    }

    public void setup(PluginConfig config)
    {
      int[] minmax = config.getMinMax(getName(), "height");
      Pole.MIN_HEIGHT = minmax[0];
      Pole.MAX_HEIGHT = minmax[1];
    }

    Provider(String name, String pattern, LampWorld lampWorld) {
      super(pattern, lampWorld);
    }

    public LampProvider setup(LampWorld lampWorld) {
      return new Provider("pole", Pole.access$7(), lampWorld);
    }
  }
}