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

public class Sconce extends Lamp
{
  private static int MAX_WIDTH;
  private static int MIN_WIDTH;
  private int width;
  private int direction;
  private Bulb bulb = null;
  private Base[] base = null;
  private ArrayList<Fence> fences = new ArrayList();

  public static LampProvider provider = new Provider("sconce", getPattern());

  public Sconce(Block bulb, int width, int direction, LampWorld lampWorld)
  {
    super(lampWorld);

    this.width = width;
    this.direction = direction;

    this.bulb = new Bulb(bulb, this, true);

    this.base = new Base[2];

    switch (direction)
    {
    case 0:
      for (int i = width; i > 0; i--) {
        this.fences.add(new Fence(bulb.getRelative(i, 0, 0), this));
      }

      this.base[0] = new Base(bulb.getRelative(width + 1, 0, 0), this);
      this.base[1] = new Base(bulb.getRelative(width + 2, 0, 0), this);
      break;
    case 1:
      for (int i = width; i > 0; i--) {
        this.fences.add(new Fence(bulb.getRelative(-i, 0, 0), this));
      }
      this.base[0] = new Base(bulb.getRelative(-(width + 1), 0, 0), this);
      this.base[1] = new Base(bulb.getRelative(-(width + 2), 0, 0), this);
      break;
    case 2:
      for (int i = width; i > 0; i--) {
        this.fences.add(new Fence(bulb.getRelative(0, 0, i), this));
      }
      this.base[0] = new Base(bulb.getRelative(0, 0, width + 1), this);
      this.base[1] = new Base(bulb.getRelative(0, 0, width + 2), this);
      break;
    case 3:
      for (int i = width; i > 0; i--) {
        this.fences.add(new Fence(bulb.getRelative(0, 0, -i), this));
      }
      this.base[0] = new Base(bulb.getRelative(0, 0, -(width + 1)), this);
      this.base[1] = new Base(bulb.getRelative(0, 0, -(width + 2)), this);
    }
  }

  protected int removeTorch()
  {
    if (!this.base[0].isTorch()) return 0;
    this.base[0].getBlock().setTypeId(0);
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
    return this.base;
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

  public boolean isPowered() {
    if (!Base.POWERMODE) return true;

    if (this.base[0].isPowered()) return true;

    return this.base[1].getBlock().getTypeId() == 76;
  }

  public String createString()
  {
    return parseString(new String[] { this.bulb.getString(), this.width, this.direction });
  }

  protected boolean containsBulb(LampBlock comparable)
  {
    return this.bulb.equals(comparable);
  }

  protected boolean containsFence(LampBlock comparable) {
    return this.fences.contains(comparable);
  }

  public boolean equals(Lamp lamp) {
    if ((lamp instanceof Sconce)) {
      return ((Sconce)lamp).bulb.equals(this.bulb);
    }
    return false;
  }

  public String getName()
  {
    return "sconce";
  }

  private static String getPattern()
  {
    return parseString(new String[] { "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "[0-9]+", "[0-3]", "[0-9]+" });
  }

  public static class Provider extends LampProvider {
    Provider(String name, String pattern) {
      super(pattern, null);
    }

    public Lamp createLamp(Block bulb)
    {
      if (bulb == null) return null;

      int[] w = getWidth(bulb);

      if (w[0] < 0) {
        return null;
      }

      Sconce sconce = new Sconce(bulb, w[0], w[1], this.lampWorld);
      Sconce.access$0(sconce);
      return this.lamps.add(sconce);
    }

    private int[] getWidth(Block bulb)
    {
      for (int w = 0; w < Sconce.MAX_WIDTH + 1; w++) {
        if (bulb.getRelative(w + 1, 0, 0).getTypeId() != 85) {
          break;
        }
      }
      if (((w >= Sconce.MIN_WIDTH) || (w <= Sconce.MAX_WIDTH)) && (
        (Base.isPowered(bulb.getRelative(w + 1, 0, 0))) || 
        (bulb.getRelative(w + 2, 0, 0).getTypeId() == 76))) {
        return new int[] { w };
      }

      for (w = 0; w < Sconce.MAX_WIDTH + 1; w++) {
        if (bulb.getRelative(-(w + 1), 0, 0).getTypeId() != 85) {
          break;
        }
      }
      if (((w >= Sconce.MIN_WIDTH) || (w <= Sconce.MAX_WIDTH)) && (
        (Base.isPowered(bulb.getRelative(-(w + 1), 0, 0))) || 
        (bulb.getRelative(-(w + 2), 0, 0).getTypeId() == 76))) {
        return new int[] { w, 1 };
      }

      for (w = 0; w < Sconce.MAX_WIDTH + 1; w++) {
        if (bulb.getRelative(0, 0, w + 1).getTypeId() != 85) {
          break;
        }
      }
      if (((w >= Sconce.MIN_WIDTH) || (w <= Sconce.MAX_WIDTH)) && (
        (Base.isPowered(bulb.getRelative(0, 0, w + 1))) || 
        (bulb.getRelative(0, 0, w + 2).getTypeId() == 76))) {
        return new int[] { w, 2 };
      }

      for (w = 0; w < Sconce.MAX_WIDTH + 1; w++) {
        if (bulb.getRelative(0, 0, -(w + 1)).getTypeId() != 85) {
          break;
        }
      }
      if (((w >= Sconce.MIN_WIDTH) || (w <= Sconce.MAX_WIDTH)) && (
        (Base.isPowered(bulb.getRelative(0, 0, -(w + 1)))) || 
        (bulb.getRelative(0, 0, -(w + 2)).getTypeId() == 76))) {
        return new int[] { w, 3 };
      }
      return new int[] { -1 };
    }

    protected Lamp create(String string, World world)
    {
      String[] param = string.split(",");
      Block bulbBlock = world.getBlockAt(Integer.valueOf(param[1]).intValue(), Integer.valueOf(param[2]).intValue(), Integer.valueOf(param[3]).intValue());

      Sconce sconce = new Sconce(bulbBlock, Integer.valueOf(param[4]).intValue(), Integer.valueOf(param[5]).intValue(), this.lampWorld);
      sconce.setID(Integer.valueOf(param[6]).intValue());
      return sconce;
    }

    public void setup(PluginConfig config)
    {
      int[] minmax = config.getMinMax(getName(), "width");
      Sconce.MIN_WIDTH = minmax[0];
      Sconce.MAX_WIDTH = minmax[1];
    }

    Provider(String name, String pattern, LampWorld lampWorld) {
      super(pattern, lampWorld);
    }

    public LampProvider setup(LampWorld lampWorld) {
      return new Provider("sconce", Sconce.access$6(), lampWorld);
    }
  }
}