package net.Dockter.LightPoles.Lamps.Expandable;

import de.bukkit.Ginsek.StreetLamps.Collections.LampCollection;
import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Configs.PluginConfig;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Fence;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.LampBlock;
import de.bukkit.Ginsek.StreetLamps.Lamps.ExpandableLamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampProvider;
import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Globe extends Lamp
  implements ExpandableLamp
{
  private static int MAX_HEIGHT;
  private static int MIN_HEIGHT;
  private int height;
  private Base base = null;
  private ArrayList<Bulb> bulbs = new ArrayList();
  private ArrayList<Fence> fences = new ArrayList();

  public static LampProvider provider = new Provider("globe", getPattern());

  private Globe(int h, Block[] bulbs, LampWorld lampWorld)
  {
    super(lampWorld);
    this.height = h;

    if (bulbs[0] != null) {
      this.bulbs.add(new Bulb(bulbs[0], this, true));

      Fence fence = new Fence(bulbs[0].getRelative(0, -1, 0), this);
      if (!this.fences.contains(fence))
        this.fences.add(fence);
    }
    else {
      this.bulbs.add(new Bulb(null, null, false));
    }

    if (bulbs[1] != null) {
      this.bulbs.add(new Bulb(bulbs[1], this, true));

      Fence fence = new Fence(bulbs[1].getRelative(0, -1, 0), this);
      if (!this.fences.contains(fence))
        this.fences.add(fence);
    }
    else {
      this.bulbs.add(new Bulb(null, null, false));
    }

    if (bulbs[2] != null) {
      this.bulbs.add(new Bulb(bulbs[2], this, true));

      Fence fence = new Fence(bulbs[2].getRelative(0, -1, 0), this);
      if (!this.fences.contains(fence))
        this.fences.add(fence);
    }
    else {
      this.bulbs.add(new Bulb(null, null, false));
    }

    if (bulbs[3] != null) {
      this.bulbs.add(new Bulb(bulbs[3], this, true));

      Fence fence = new Fence(bulbs[3].getRelative(0, -1, 0), this);
      if (!this.fences.contains(fence))
        this.fences.add(fence);
    }
    else {
      this.bulbs.add(new Bulb(null, null, false));
    }

    Block top = getTop();
    for (int i = h - 1; i >= 0; i--) {
      Fence fence = new Fence(top.getRelative(0, -i, 0), this);
      if (!this.fences.contains(fence)) {
        this.fences.add(fence);
      }
    }

    this.base = new Base(top.getRelative(0, -h, 0), this);
  }
  private Block getTop() {
    if (((Bulb)this.bulbs.get(0)).activated)
      return ((Bulb)this.bulbs.get(0)).getRelative(-1, -1, 0);
    if (((Bulb)this.bulbs.get(1)).activated)
      return ((Bulb)this.bulbs.get(1)).getRelative(1, -1, 0);
    if (((Bulb)this.bulbs.get(2)).activated)
      return ((Bulb)this.bulbs.get(2)).getRelative(0, -1, -1);
    if (((Bulb)this.bulbs.get(3)).activated) {
      return ((Bulb)this.bulbs.get(3)).getRelative(0, -1, 1);
    }
    return null;
  }
  private static Block getCenter(Block bulb) {
    if (bulb.getRelative(0, -1, 0).getTypeId() != 85) {
      return null;
    }

    Block block = bulb.getRelative(0, -1, 0);
    if (block.getRelative(-1, 0, 0).getTypeId() == 85) {
      return block.getRelative(-1, 0, 0);
    }

    if (block.getRelative(1, 0, 0).getTypeId() == 85) {
      return block.getRelative(1, 0, 0);
    }

    if (block.getRelative(0, 0, -1).getTypeId() == 85) {
      return block.getRelative(0, 0, -1);
    }

    if (block.getRelative(0, 0, 1).getTypeId() == 85) {
      return block.getRelative(0, 0, 1);
    }

    return null;
  }

  protected int removeTorch() {
    if (!this.base.isTorch(0, -1, 0)) return 0;
    this.base.getBlock().getRelative(0, -1, 0).setTypeId(0);
    return 1;
  }

  public final Bulb[] getBulbs()
  {
    ArrayList tmp = new ArrayList();
    for (Bulb bulb : this.bulbs) {
      if (!bulb.activated) continue; tmp.add(bulb);
    }
    return (Bulb[])tmp.toArray(new Bulb[0]);
  }

  public Fence[] getFences() {
    return (Fence[])this.fences.toArray(new Fence[0]);
  }

  public Base[] getBases() {
    return new Base[] { this.base };
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
    return this.base.isPowered();
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
    String str = this.height + "," + ((Bulb)this.bulbs.get(0)).getString();
    for (int i = 1; i < 4; i++) {
      str = str + "," + ((Bulb)this.bulbs.get(i)).getString();
    }
    return str;
  }

  public boolean equals(Lamp lamp)
  {
    if ((lamp instanceof Globe)) {
      return ((Globe)lamp).base.equals(this.base);
    }
    return false;
  }

  public String getName()
  {
    return "globe";
  }

  private static String getPattern()
  {
    return parseString(new String[] { "[0-9]+", "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "[0-9]+" });
  }

  public int expand()
  {
    Block top = getTop();
    int added = 0;
    if (!((Bulb)this.bulbs.get(0)).activated)
    {
      if ((top.getRelative(1, 0, 0).getTypeId() == 85) && 
        (top.getRelative(1, 1, 0).getTypeId() == Bulb.MATERIAL_ON)) {
        this.bulbs.set(0, new Bulb(top.getRelative(1, 1, 0), this, true));
        added++;
      }
    }
    if (!((Bulb)this.bulbs.get(1)).activated)
    {
      if ((top.getRelative(-1, 0, 0).getTypeId() == 85) && 
        (top.getRelative(-1, 1, 0).getTypeId() == Bulb.MATERIAL_ON)) {
        this.bulbs.set(1, new Bulb(top.getRelative(-1, 1, 0), this, true));
        added++;
      }
    }
    if (!((Bulb)this.bulbs.get(2)).activated)
    {
      if ((top.getRelative(0, 0, 1).getTypeId() == 85) && 
        (top.getRelative(0, 1, 1).getTypeId() == Bulb.MATERIAL_ON)) {
        this.bulbs.set(2, new Bulb(top.getRelative(0, 1, 1), this, true));
        added++;
      }
    }
    if (!((Bulb)this.bulbs.get(3)).activated)
    {
      if ((top.getRelative(0, 0, -1).getTypeId() == 85) && 
        (top.getRelative(0, 1, -1).getTypeId() == Bulb.MATERIAL_ON)) {
        this.bulbs.set(3, new Bulb(top.getRelative(0, 1, -1), this, true));
        added++;
      }
    }
    return added;
  }

  protected boolean containsFence(LampBlock fence) {
    return this.fences.contains(fence);
  }

  protected boolean containsBulb(LampBlock bulb) {
    return this.bulbs.contains(bulb);
  }

  public static class Provider extends LampProvider
  {
    protected Provider(String name, String pattern)
    {
      super(pattern, null);
    }

    public Lamp createLamp(Block bulb)
    {
      Block center = Globe.access$0(bulb);
      if (center == null) return null;

      Lamp existingLamp = this.lamps.getLamp(LampBlock.getComparable(center));
      if (existingLamp != null) {
        if (((existingLamp instanceof Globe)) && 
          (((Globe)existingLamp).expand() > 0)) {
          existingLamp.update();
          this.lamps.expand(existingLamp);
          return existingLamp;
        }

        return null;
      }

      for (int h = 0; h <= Globe.MAX_HEIGHT + 1; h++)
        if (center.getRelative(0, -h, 0).getTypeId() != 85)
          break;
      if ((h < Globe.MIN_HEIGHT) || (h > Globe.MAX_HEIGHT)) {
        return null;
      }

      Block[] bulbs = new Block[4];

      if ((center.getRelative(1, 0, 0).getTypeId() == 85) && 
        (center.getRelative(1, 1, 0).getTypeId() == Bulb.MATERIAL_ON))
        bulbs[0] = center.getRelative(1, 1, 0);
      else {
        bulbs[0] = null;
      }

      if ((center.getRelative(-1, 0, 0).getTypeId() == 85) && 
        (center.getRelative(-1, 1, 0).getTypeId() == Bulb.MATERIAL_ON))
        bulbs[1] = center.getRelative(-1, 1, 0);
      else {
        bulbs[1] = null;
      }

      if ((center.getRelative(0, 0, 1).getTypeId() == 85) && 
        (center.getRelative(0, 1, 1).getTypeId() == Bulb.MATERIAL_ON))
        bulbs[2] = center.getRelative(0, 1, 1);
      else {
        bulbs[2] = null;
      }

      if ((center.getRelative(0, 0, -1).getTypeId() == 85) && 
        (center.getRelative(0, 1, -1).getTypeId() == Bulb.MATERIAL_ON))
        bulbs[3] = center.getRelative(0, 1, -1);
      else {
        bulbs[3] = null;
      }

      if (!Base.isPowered(center.getRelative(0, -h, 0))) {
        return null;
      }

      Globe globe = new Globe(h, bulbs, this.lampWorld, null);
      Globe.access$4(globe);
      return this.lamps.add(globe);
    }
    protected Lamp create(String string, World world) {
      String[] param = string.split(",");
      int pos = 0;

      int h = Integer.valueOf(param[(pos++)]).intValue();
      boolean[] b = new boolean[4];
      Block[] bulbs = new Block[4];
      for (int i = 0; i < 4; i++) {
        b[i] = param[(pos++)].equals("1");
        if (b[i] != 0) {
          bulbs[i] = world.getBlockAt(
            Integer.valueOf(param[(pos++)]).intValue(), 
            Integer.valueOf(param[(pos++)]).intValue(), 
            Integer.valueOf(param[(pos++)]).intValue());
        }
        else {
          pos += 3;
          bulbs[i] = null;
        }
      }

      Globe globe = new Globe(h, bulbs, this.lampWorld, null);
      globe.setID(Integer.valueOf(param[pos]).intValue());
      return globe;
    }

    public void setup(PluginConfig config)
    {
      int[] minmax = config.getMinMax(getName(), "height");
      Globe.MIN_HEIGHT = minmax[0];
      Globe.MAX_HEIGHT = minmax[1];
    }

    Provider(String name, String pattern, LampWorld lampWorld) {
      super(pattern, lampWorld);
    }

    public LampProvider setup(LampWorld lampWorld) {
      return new Provider("globe", Globe.access$8(), lampWorld);
    }
  }
}