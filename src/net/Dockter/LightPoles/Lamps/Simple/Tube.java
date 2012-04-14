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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Tube extends Lamp
{
  private static int MAX_HEIGHT;
  private static int MIN_HEIGHT;
  private static int MIN_WIDTH;
  private static int MAX_WIDTH;
  private ArrayList<Bulb> bulbs = new ArrayList();
  private Base[] base = null;
  private int h;
  private Fence top;
  private Fence top2;
  private ArrayList<Fence> fences = new ArrayList();

  public static LampProvider provider = new Provider("tube", getPattern());

  private Tube(int h, Block top, Block top2, LampWorld lampWorld)
  {
    super(lampWorld);

    this.top = new Fence(top, this);
    this.top2 = new Fence(top2, this);

    this.h = h;

    int x1 = top.getLocation().getBlockX(); int x2 = top2.getLocation().getBlockX();
    int z1 = top.getLocation().getBlockZ(); int z2 = top2.getLocation().getBlockZ();
    if (x1 != x2) {
      int x = 0;
      if (x1 < x2) {
        for (x = x2 - x1 - 1; x > 0; x--)
          this.bulbs.add(new Bulb(top.getRelative(x, 0, 0), this, true));
      }
      else
        for (x = x1 - x2 - 1; x > 0; x--)
          this.bulbs.add(new Bulb(top2.getRelative(x, 0, 0), this, true));
    }
    else
    {
      int z = 0;
      if (z1 < z2) {
        for (z = z2 - z1 - 1; z > 0; z--)
          this.bulbs.add(new Bulb(top.getRelative(0, 0, z), this, true));
      }
      else {
        for (z = z1 - z2 - 1; z > 0; z--) {
          this.bulbs.add(new Bulb(top2.getRelative(0, 0, z), this, true));
        }
      }

    }

    for (int i = h - 1; i >= 0; i--) {
      this.fences.add(new Fence(top.getRelative(0, -i, 0), this));
    }
    for (int i = h - 1; i >= 0; i--) {
      this.fences.add(new Fence(top2.getRelative(0, -i, 0), this));
    }

    this.base = new Base[2];
    this.base[0] = new Base(top.getRelative(0, -h, 0), this);
    this.base[1] = new Base(top2.getRelative(0, -h, 0), this);
  }

  protected int removeTorch() {
    int count = 0;
    if (this.base[0].isTorch(0, -1, 0)) {
      this.base[0].getBlock().getRelative(0, -1, 0).setTypeId(0);
      count++;
    }
    if (this.base[1].isTorch(0, -1, 0)) {
      this.base[1].getBlock().getRelative(0, -1, 0).setTypeId(0);
      count++;
    }
    return count;
  }

  public Fence[] getFences()
  {
    return (Fence[])this.fences.toArray(new Fence[0]);
  }

  public final Bulb[] getBulbs() {
    ArrayList tmp = new ArrayList();
    for (Bulb bulb : this.bulbs) {
      if (!bulb.activated) continue; tmp.add(bulb);
    }
    return (Bulb[])tmp.toArray(new Bulb[0]);
  }
  public Base[] getBases() {
    return this.base;
  }

  protected void on()
  {
    for (Bulb bulb : getBulbs())
      bulb.forceOn();
  }

  protected void off()
  {
    for (Bulb bulb : getBulbs())
      bulb.forceOff();
  }

  protected void toggle()
  {
    for (Bulb bulb : getBulbs())
      bulb.toggle();
  }

  public boolean isPowered()
  {
    if (!Base.POWERMODE) return true;
    return (this.base[0].isPowered()) && (this.base[1].isPowered());
  }

  public boolean isIntact()
  {
    for (Bulb bulb : this.bulbs) {
      if (!bulb.isIntact()) return false;

    }

    for (Fence fence : this.fences) {
      if (!fence.isIntact()) return false;
    }
    return true;
  }

  public String createString()
  {
    return parseString(new String[] { this.h, this.top.getString(), this.top2.getString() });
  }

  protected boolean containsFence(LampBlock comparable)
  {
    return this.fences.contains(comparable);
  }

  protected boolean containsBulb(LampBlock comparable) {
    return this.bulbs.contains(comparable);
  }

  public boolean equals(Lamp lamp) {
    if ((lamp instanceof Tube)) {
      Tube t = (Tube)lamp;
      if ((!t.top.equals(this.top)) && (!t.top.equals(this.top2))) {
        return false;
      }

      return (t.top2.equals(this.top)) || (t.top2.equals(this.top2));
    }

    return false;
  }

  public String getName()
  {
    return "tube";
  }

  private static String getPattern()
  {
    return parseString(new String[] { "[0-9]+", "(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "[0-9]+" });
  }

  public static class Provider extends LampProvider {
    Provider(String name, String pattern) {
      super(pattern, null);
    }

    private int getHeight(Block top) {
      int h = 0;
      for (; h <= Tube.MAX_HEIGHT + 1; h++) {
        if (top.getRelative(0, -h, 0).getTypeId() != 85) {
          break;
        }
      }
      if ((h > Tube.MAX_HEIGHT) || (h < Tube.MIN_HEIGHT)) {
        return -1;
      }
      return h;
    }

    public Lamp createLamp(Block bulb)
    {
      int h = 0;
      int width = 0;
      Block top = null;
      Block top2 = null;

      if ((bulb.getRelative(1, 0, 0).getTypeId() == 85) && 
        (bulb.getRelative(-1, 0, 0).getTypeId() == Bulb.MATERIAL_ON)) {
        h = getHeight(bulb.getRelative(1, 0, 0));
        if (h == -1) return null;

        do
        {
          if (bulb.getRelative(-width, 0, 0).getTypeId() != Bulb.MATERIAL_ON) {
            if (bulb.getRelative(-width, 0, 0).getTypeId() == 85) break;
            return null;
          }
          width++; } while (width <= Tube.MAX_WIDTH + 1);

        if ((width < Tube.MIN_WIDTH) || (width > Tube.MAX_WIDTH)) {
          return null;
        }

        top = bulb.getRelative(1, 0, 0);
        top2 = bulb.getRelative(-width, 0, 0);
      }
      else if ((bulb.getRelative(-1, 0, 0).getTypeId() == 85) && 
        (bulb.getRelative(1, 0, 0).getTypeId() == Bulb.MATERIAL_ON)) {
        h = getHeight(bulb.getRelative(-1, 0, 0));
        if (h == -1) {
          return null;
        }

        do
        {
          if (bulb.getRelative(width, 0, 0).getTypeId() != Bulb.MATERIAL_ON) {
            if (bulb.getRelative(width, 0, 0).getTypeId() == 85) break;
            return null;
          }
          width++; } while (width <= Tube.MAX_WIDTH + 1);

        if ((width < Tube.MIN_WIDTH) || (width > Tube.MAX_WIDTH)) {
          return null;
        }

        top = bulb.getRelative(-1, 0, 0);
        top2 = bulb.getRelative(width, 0, 0);
      }
      else if ((bulb.getRelative(0, 0, 1).getTypeId() == 85) && 
        (bulb.getRelative(0, 0, -1).getTypeId() == Bulb.MATERIAL_ON)) {
        h = getHeight(bulb.getRelative(0, 0, 1));
        if (h == -1) {
          return null;
        }

        do
        {
          if (bulb.getRelative(0, 0, -width).getTypeId() != Bulb.MATERIAL_ON) {
            if (bulb.getRelative(0, 0, -width).getTypeId() == 85) break;
            return null;
          }
          width++; } while (width <= Tube.MAX_WIDTH + 1);

        if ((width < Tube.MIN_WIDTH) || (width > Tube.MAX_WIDTH)) {
          return null;
        }

        top = bulb.getRelative(0, 0, 1);
        top2 = bulb.getRelative(0, 0, -width);
      }
      else if ((bulb.getRelative(0, 0, -1).getTypeId() == 85) && 
        (bulb.getRelative(0, 0, 1).getTypeId() == Bulb.MATERIAL_ON)) {
        h = getHeight(bulb.getRelative(0, 0, -1));
        if (h == -1) {
          return null;
        }

        do
        {
          if (bulb.getRelative(0, 0, width).getTypeId() != Bulb.MATERIAL_ON) {
            if (bulb.getRelative(0, 0, width).getTypeId() == 85) break;
            return null;
          }
          width++; } while (width <= Tube.MAX_WIDTH + 1);

        if ((width < Tube.MIN_WIDTH) || (width > Tube.MAX_WIDTH)) {
          return null;
        }

        top = bulb.getRelative(0, 0, -1);
        top2 = bulb.getRelative(0, 0, width);
      }
      if ((top == null) || (top2 == null)) return null;

      if ((!Base.isPowered(top.getRelative(0, -h, 0))) || (!Base.isPowered(top2.getRelative(0, -h, 0)))) {
        return null;
      }

      Tube tube = new Tube(h, top, top2, this.lampWorld, null);
      Tube.access$5(tube);
      return this.lamps.add(tube);
    }

    protected Lamp create(String string, World world) {
      String[] param = string.split(",");
      Block block = world.getBlockAt(Integer.valueOf(param[1]).intValue(), Integer.valueOf(param[2]).intValue(), Integer.valueOf(param[3]).intValue());
      Block block2 = world.getBlockAt(Integer.valueOf(param[4]).intValue(), Integer.valueOf(param[5]).intValue(), Integer.valueOf(param[6]).intValue());

      Tube tube = new Tube(Integer.valueOf(param[0]).intValue(), block, block2, this.lampWorld, null);
      tube.setID(Integer.valueOf(param[7]).intValue());
      return tube;
    }

    public void setup(PluginConfig config)
    {
      int[] minmax = config.getMinMax(getName(), "height");
      Tube.MIN_HEIGHT = minmax[0];
      Tube.MAX_HEIGHT = minmax[1];

      minmax = config.getMinMax(getName(), "width");
      Tube.MIN_WIDTH = minmax[0];
      Tube.MAX_WIDTH = minmax[1];
    }

    Provider(String name, String pattern, LampWorld lampWorld) {
      super(pattern, lampWorld);
    }

    public LampProvider setup(LampWorld lampWorld) {
      return new Provider("tube", Tube.access$11(), lampWorld);
    }
  }
}