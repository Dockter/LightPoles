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

public class Pendant extends Lamp
  implements ExpandableLamp
{
  private static int MAX_HEIGHT;
  private static int MIN_HEIGHT;
  private static int MAX_DEPTH;
  private static int MIN_DEPTH;
  private static int MAX_WIDTH;
  private static int MIN_WIDTH;
  private ArrayList<Fence> fences = new ArrayList();
  private ArrayList<Bulb> bulbs = new ArrayList();
  private Base base = null;
  private int height;
  private int[] width = new int[4];
  private int[] depth = new int[4];

  public static LampProvider provider = new Provider("pendant", getPattern());

  private Pendant(int h, int[] d, int[] w, Block[] bulbs, LampWorld lampWorld)
  {
    super(lampWorld);

    this.height = h;
    this.depth = d;
    this.width = w;

    if (bulbs[0] != null) {
      this.bulbs.add(new Bulb(bulbs[0], this, true));

      for (int i = d[0]; i > 0; i--) {
        Fence fence = new Fence(bulbs[0].getRelative(0, i, 0), this);
        if (!this.fences.contains(fence)) {
          this.fences.add(fence);
        }

      }

      for (int i = w[0] - 1; i > 0; i--) {
        Fence fence = new Fence(bulbs[0].getRelative(-i, d[0], 0), this);
        if (!this.fences.contains(fence))
          this.fences.add(fence);
      }
    }
    else {
      this.bulbs.add(new Bulb(null, null, false));
    }

    if (bulbs[1] != null) {
      this.bulbs.add(new Bulb(bulbs[1], this, true));

      for (int i = d[1]; i > 0; i--) {
        Fence fence = new Fence(bulbs[1].getRelative(0, i, 0), this);
        if (!this.fences.contains(fence)) {
          this.fences.add(fence);
        }
      }

      for (int i = w[1] - 1; i > 0; i--) {
        Fence fence = new Fence(bulbs[1].getRelative(i, d[1], 0), this);
        if (!this.fences.contains(fence))
          this.fences.add(fence);
      }
    }
    else {
      this.bulbs.add(new Bulb(null, null, false));
    }

    if (bulbs[2] != null) {
      this.bulbs.add(new Bulb(bulbs[2], this, true));

      for (int i = d[2]; i > 0; i--) {
        Fence fence = new Fence(bulbs[2].getRelative(0, i, 0), this);
        if (!this.fences.contains(fence)) {
          this.fences.add(fence);
        }
      }

      for (int i = w[2] - 1; i > 0; i--) {
        Fence fence = new Fence(bulbs[2].getRelative(0, d[2], -i), this);
        if (!this.fences.contains(fence))
          this.fences.add(fence);
      }
    }
    else {
      this.bulbs.add(new Bulb(null, null, false));
    }

    if (bulbs[3] != null) {
      this.bulbs.add(new Bulb(bulbs[3], this, true));

      for (int i = d[3]; i > 0; i--) {
        Fence fence = new Fence(bulbs[3].getRelative(0, i, 0), this);
        if (!this.fences.contains(fence)) {
          this.fences.add(fence);
        }
      }

      for (int i = w[3] - 1; i > 0; i--) {
        Fence fence = new Fence(bulbs[3].getRelative(0, d[3], i), this);
        if (!this.fences.contains(fence))
          this.fences.add(fence);
      }
    }
    else {
      this.bulbs.add(new Bulb(null, null, false));
    }

    Block top = getTop();
    for (int i = h - 1; i > 0; i--) {
      Fence fence = new Fence(top.getRelative(0, -i, 0), this);
      if (!this.fences.contains(fence)) {
        this.fences.add(fence);
      }
    }

    this.base = new Base(top.getRelative(0, -h, 0), this);
  }
  private Block getTop() {
    if (((Bulb)this.bulbs.get(0)).activated)
      return ((Bulb)this.bulbs.get(0)).getRelative(-(this.width[0] - 1), this.depth[0], 0);
    if (((Bulb)this.bulbs.get(1)).activated)
      return ((Bulb)this.bulbs.get(1)).getRelative(this.width[1] - 1, this.depth[1], 0);
    if (((Bulb)this.bulbs.get(2)).activated)
      return ((Bulb)this.bulbs.get(2)).getRelative(0, this.depth[2], -(this.width[2] - 1));
    if (((Bulb)this.bulbs.get(3)).activated) {
      return ((Bulb)this.bulbs.get(3)).getRelative(0, this.depth[3], this.width[3] - 1);
    }
    return null;
  }

  private static Block getCenter(Block bulb) {
    for (int d = 0; d <= MAX_DEPTH + 1; d++)
      if (bulb.getRelative(0, d + 1, 0).getTypeId() != 85)
        break;
    if ((d < MIN_DEPTH) || (d > MAX_DEPTH)) return null;

    Block block_tmp = bulb.getRelative(0, d, 0);

    Block block = block_tmp;
    for (int w = 1; w <= MAX_WIDTH + 1; w++) {
      if (block.getRelative(-w, 0, 0).getTypeId() != 85) {
        w = -1;
        break;
      }
      if (block.getRelative(-w, -1, 0).getTypeId() == 85) break;
    }
    if ((w + 1 >= MIN_WIDTH) && (w + 1 <= MAX_WIDTH)) {
      return block.getRelative(-w, 0, 0);
    }

    for (w = 1; w <= MAX_WIDTH + 1; w++) {
      if (block.getRelative(w, 0, 0).getTypeId() != 85) {
        w = -1;
        break;
      }
      if (block.getRelative(w, -1, 0).getTypeId() == 85) break;
    }
    if ((w + 1 >= MIN_WIDTH) && (w + 1 <= MAX_WIDTH)) {
      return block.getRelative(w, 0, 0);
    }

    for (w = 1; w <= MAX_WIDTH + 1; w++) {
      if (block.getRelative(0, 0, -w).getTypeId() != 85) {
        w = -1;
        break;
      }
      if (block.getRelative(0, -1, -w).getTypeId() == 85) break;
    }
    if ((w + 1 >= MIN_WIDTH) && (w + 1 <= MAX_WIDTH)) {
      return block.getRelative(0, 0, -w);
    }

    for (w = 1; w <= MAX_WIDTH + 1; w++) {
      if (block.getRelative(0, 0, w).getTypeId() != 85) {
        w = -1;
        break;
      }
      if (block.getRelative(0, -1, w).getTypeId() == 85) break;
    }
    if ((w + 1 >= MIN_WIDTH) && (w + 1 <= MAX_WIDTH)) {
      return block.getRelative(0, 0, w);
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
    String str = this.height + "," + parseString(new String[] { ((Bulb)this.bulbs.get(0)).getString(), this.depth[0], this.width[0] });
    for (int i = 1; i < 4; i++) {
      str = str + "," + parseString(new String[] { ((Bulb)this.bulbs.get(i)).getString(), this.depth[i], this.width[i] });
    }
    return str;
  }

  protected boolean containsBulb(LampBlock comparable)
  {
    return this.bulbs.contains(comparable);
  }

  protected boolean containsFence(LampBlock comparable) {
    return this.fences.contains(comparable);
  }

  public boolean equals(Lamp lamp) {
    if ((lamp instanceof Pendant)) {
      return ((Pendant)lamp).base.equals(this.base);
    }
    return false;
  }

  public String getName()
  {
    return "pendant";
  }

  private static String getPattern()
  {
    String BULBe = "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+,[0-9]+,[0-9]+";
    return parseString(new String[] { "[0-9]+", BULBe, BULBe, BULBe, BULBe, "[0-9]+" });
  }

  public int expand()
  {
    Block top = getTop();
    int added = 0;
    if (!((Bulb)this.bulbs.get(0)).activated)
    {
      for (int w = 1; w <= MAX_WIDTH + 1; w++) {
        if (top.getRelative(w, 0, 0).getTypeId() != 85) {
          w = -1;
          break;
        }
        if ((top.getRelative(w, -1, 0).getTypeId() != 85) && 
          (top.getRelative(w, -1, 0).getTypeId() != Bulb.MATERIAL_ON)) continue;
        w++;
        break;
      }

      if ((w >= MIN_WIDTH) && (w <= MAX_WIDTH)) {
        for (int d = 0; d <= MAX_DEPTH + 1; d++) {
          if ((top.getRelative(w - 1, -d, 0).getTypeId() != 85) && 
            (top.getRelative(w - 1, -d, 0).getTypeId() != Bulb.MATERIAL_ON)) {
            d = -1;
            break;
          }
          if (top.getRelative(w - 1, -d - 1, 0).getTypeId() == Bulb.MATERIAL_ON) {
            d++;
            break;
          }
        }
        if ((d >= MIN_DEPTH) && (d <= MAX_DEPTH)) {
          this.depth[0] = d;
          this.width[0] = w;
          Bulb newBulb = new Bulb(top.getRelative(w - 1, -d, 0), this, true);
          this.bulbs.set(0, newBulb);

          for (int i = d; i > 0; i--) {
            Fence fence = new Fence(newBulb.getRelative(0, i, 0), this);
            if (!this.fences.contains(fence)) {
              this.fences.add(fence);
            }

          }

          for (int i = w - 1; i > 0; i--) {
            Fence fence = new Fence(newBulb.getRelative(-i, d, 0), this);
            if (!this.fences.contains(fence)) {
              this.fences.add(fence);
            }
          }

          added++;
        }
      }
    }
    if (!((Bulb)this.bulbs.get(1)).activated)
    {
      for (int w = 1; w <= MAX_WIDTH + 1; w++) {
        if (top.getRelative(-w, 0, 0).getTypeId() != 85) {
          w = -1;
          break;
        }
        if ((top.getRelative(-w, -1, 0).getTypeId() != 85) && 
          (top.getRelative(-w, -1, 0).getTypeId() != Bulb.MATERIAL_ON)) continue;
        w++;
        break;
      }

      if ((w >= MIN_WIDTH) && (w <= MAX_WIDTH)) {
        for (int d = 0; d <= MAX_DEPTH + 1; d++) {
          if ((top.getRelative(-(w - 1), -d, 0).getTypeId() != 85) && 
            (top.getRelative(-(w - 1), -d, 0).getTypeId() != Bulb.MATERIAL_ON)) {
            d = -1;
            break;
          }
          if (top.getRelative(-(w - 1), -d - 1, 0).getTypeId() == Bulb.MATERIAL_ON) {
            d++;
            break;
          }
        }
        if ((d >= MIN_DEPTH) && (d <= MAX_DEPTH)) {
          this.depth[1] = d;
          this.width[1] = w;
          Bulb newBulb = new Bulb(top.getRelative(-(w - 1), -d, 0), this, true);
          this.bulbs.set(1, newBulb);

          for (int i = d; i > 0; i--) {
            Fence fence = new Fence(newBulb.getRelative(0, i, 0), this);
            if (!this.fences.contains(fence)) {
              this.fences.add(fence);
            }
          }

          for (int i = w - 1; i > 0; i--) {
            Fence fence = new Fence(newBulb.getRelative(i, d, 0), this);
            if (!this.fences.contains(fence)) {
              this.fences.add(fence);
            }
          }

          added++;
        }
      }
    }
    if (!((Bulb)this.bulbs.get(2)).activated)
    {
      for (int w = 1; w <= MAX_WIDTH + 1; w++) {
        if (top.getRelative(0, 0, w).getTypeId() != 85) {
          w = -1;
          break;
        }
        if ((top.getRelative(0, -1, w).getTypeId() != 85) && 
          (top.getRelative(0, -1, w).getTypeId() != Bulb.MATERIAL_ON)) continue;
        w++;
        break;
      }

      if ((w >= MIN_WIDTH) && (w <= MAX_WIDTH)) {
        for (int d = 0; d <= MAX_DEPTH + 1; d++) {
          if ((top.getRelative(0, -d, w - 1).getTypeId() != 85) && 
            (top.getRelative(0, -d, w - 1).getTypeId() != Bulb.MATERIAL_ON)) {
            d = -1;
            break;
          }
          if (top.getRelative(0, -d - 1, w - 1).getTypeId() == Bulb.MATERIAL_ON) {
            d++;
            break;
          }
        }
        if ((d >= MIN_DEPTH) && (d <= MAX_DEPTH)) {
          this.depth[2] = d;
          this.width[2] = w;
          Bulb newBulb = new Bulb(top.getRelative(0, -d, w - 1), this, true);
          this.bulbs.set(2, newBulb);

          for (int i = d; i > 0; i--) {
            Fence fence = new Fence(newBulb.getRelative(0, i, 0), this);
            if (!this.fences.contains(fence)) {
              this.fences.add(fence);
            }
          }

          for (int i = w - 1; i > 0; i--) {
            Fence fence = new Fence(newBulb.getRelative(0, d, -i), this);
            if (!this.fences.contains(fence)) {
              this.fences.add(fence);
            }
          }

          added++;
        }
      }
    }
    if (!((Bulb)this.bulbs.get(3)).activated)
    {
      for (int w = 1; w <= MAX_WIDTH + 1; w++) {
        if (top.getRelative(0, 0, -w).getTypeId() != 85) {
          w = -1;
          break;
        }
        if ((top.getRelative(0, -1, -w).getTypeId() != 85) && 
          (top.getRelative(0, -1, -w).getTypeId() != Bulb.MATERIAL_ON)) continue;
        w++;
        break;
      }

      if ((w >= MIN_WIDTH) && (w <= MAX_WIDTH)) {
        for (int d = 0; d <= MAX_DEPTH + 1; d++) {
          if ((top.getRelative(0, -d, -(w - 1)).getTypeId() != 85) && 
            (top.getRelative(0, -d, -(w - 1)).getTypeId() != Bulb.MATERIAL_ON)) {
            d = -1;
            break;
          }
          if (top.getRelative(0, -d - 1, -(w - 1)).getTypeId() == Bulb.MATERIAL_ON) {
            d++;
            break;
          }
        }
        if ((d >= MIN_DEPTH) && (d <= MAX_DEPTH)) {
          this.depth[3] = d;
          this.width[3] = w;
          Bulb newBulb = new Bulb(top.getRelative(0, -d, -(w - 1)), this, true);
          this.bulbs.set(3, newBulb);

          for (int i = d; i > 0; i--) {
            Fence fence = new Fence(newBulb.getRelative(0, i, 0), this);
            if (!this.fences.contains(fence)) {
              this.fences.add(fence);
            }
          }

          for (int i = w - 1; i > 0; i--) {
            Fence fence = new Fence(newBulb.getRelative(0, d, i), this);
            if (!this.fences.contains(fence)) {
              this.fences.add(fence);
            }
          }

          added++;
        }
      }
    }
    return added;
  }

  public static class Provider extends LampProvider
  {
    protected Provider(String name, String pattern)
    {
      super(pattern, null);
    }

    public Lamp createLamp(Block bulb)
    {
      int[] depth = new int[4];
      int[] width = new int[4];

      Block center = Pendant.access$0(bulb);
      if (center == null) return null;

      Lamp existingLamp = this.lamps.getLamp(LampBlock.getComparable(center));
      if (existingLamp != null) {
        if (((existingLamp instanceof Pendant)) && 
          (((Pendant)existingLamp).expand() > 0)) {
          existingLamp.update();
          this.lamps.expand(existingLamp);
          return existingLamp;
        }

        return null;
      }

      for (int h = 0; h <= Pendant.MAX_HEIGHT + 1; h++)
        if (center.getRelative(0, -h, 0).getTypeId() != 85)
          break;
      if ((h < Pendant.MIN_HEIGHT) || (h > Pendant.MAX_HEIGHT)) {
        return null;
      }

      Block[] bulbs = new Block[4];

      for (int w = 1; w <= Pendant.MAX_WIDTH + 1; w++) {
        if (center.getRelative(w, 0, 0).getTypeId() != 85) {
          w = -1;
          break;
        }
        if ((center.getRelative(w, -1, 0).getTypeId() != 85) && 
          (center.getRelative(w, -1, 0).getTypeId() != Bulb.MATERIAL_ON)) continue;
        w++;
        break;
      }

      if ((w < Pendant.MIN_WIDTH) || (w > Pendant.MAX_WIDTH)) {
        bulbs[0] = null;
      } else {
        for (int d = 0; d <= Pendant.MAX_DEPTH + 1; d++) {
          if ((center.getRelative(w - 1, -d, 0).getTypeId() != 85) && 
            (center.getRelative(w - 1, -d, 0).getTypeId() != Bulb.MATERIAL_ON)) {
            d = -1;
            break;
          }
          if (center.getRelative(w - 1, -d - 1, 0).getTypeId() == Bulb.MATERIAL_ON) {
            d++;
            break;
          }
        }
        if ((d < Pendant.MIN_DEPTH) || (d > Pendant.MAX_DEPTH)) {
          bulbs[0] = null;
        } else {
          depth[0] = d;
          width[0] = w;
          bulbs[0] = center.getRelative(w - 1, -d, 0);
        }

      }

      for (w = 1; w <= Pendant.MAX_WIDTH + 1; w++) {
        if (center.getRelative(-w, 0, 0).getTypeId() != 85) {
          w = -1;
          break;
        }
        if ((center.getRelative(-w, -1, 0).getTypeId() != 85) && 
          (center.getRelative(-w, -1, 0).getTypeId() != Bulb.MATERIAL_ON)) continue;
        w++;
        break;
      }

      if ((w < Pendant.MIN_WIDTH) || (w > Pendant.MAX_WIDTH)) {
        bulbs[1] = null;
      } else {
        for (int d = 0; d <= Pendant.MAX_DEPTH + 1; d++) {
          if ((center.getRelative(-(w - 1), -d, 0).getTypeId() != 85) && 
            (center.getRelative(-(w - 1), -d, 0).getTypeId() != Bulb.MATERIAL_ON)) {
            d = -1;
            break;
          }
          if (center.getRelative(-(w - 1), -d - 1, 0).getTypeId() == Bulb.MATERIAL_ON) {
            d++;
            break;
          }
        }
        if ((d < Pendant.MIN_DEPTH) || (d > Pendant.MAX_DEPTH)) {
          bulbs[1] = null;
        }
        else {
          depth[1] = d;
          width[1] = w;
          bulbs[1] = center.getRelative(-(w - 1), -d, 0);
        }

      }

      for (w = 1; w <= Pendant.MAX_WIDTH + 1; w++) {
        if (center.getRelative(0, 0, w).getTypeId() != 85) {
          w = -1;
          break;
        }
        if ((center.getRelative(0, -1, w).getTypeId() != 85) && 
          (center.getRelative(0, -1, w).getTypeId() != Bulb.MATERIAL_ON)) continue;
        w++;
        break;
      }

      if ((w < Pendant.MIN_WIDTH) || (w > Pendant.MAX_WIDTH)) {
        bulbs[2] = null;
      } else {
        for (int d = 0; d <= Pendant.MAX_DEPTH + 1; d++) {
          if ((center.getRelative(0, -d, w - 1).getTypeId() != 85) && 
            (center.getRelative(0, -d, w - 1).getTypeId() != Bulb.MATERIAL_ON)) {
            d = -1;
            break;
          }
          if (center.getRelative(0, -d - 1, w - 1).getTypeId() == Bulb.MATERIAL_ON) {
            d++;
            break;
          }
        }
        if ((d < Pendant.MIN_DEPTH) || (d > Pendant.MAX_DEPTH)) {
          bulbs[2] = null;
        } else {
          depth[2] = d;
          width[2] = w;
          bulbs[2] = center.getRelative(0, -d, w - 1);
        }

      }

      for (w = 1; w <= Pendant.MAX_WIDTH + 1; w++) {
        if (center.getRelative(0, 0, -w).getTypeId() != 85) {
          w = -1;
          break;
        }
        if ((center.getRelative(0, -1, -w).getTypeId() != 85) && 
          (center.getRelative(0, -1, -w).getTypeId() != Bulb.MATERIAL_ON)) continue;
        w++;
        break;
      }

      if ((w < Pendant.MIN_WIDTH) || (w > Pendant.MAX_WIDTH)) {
        bulbs[3] = null;
      } else {
        for (int d = 0; d <= Pendant.MAX_DEPTH + 1; d++) {
          if ((center.getRelative(0, -d, -(w - 1)).getTypeId() != 85) && 
            (center.getRelative(0, -d, -(w - 1)).getTypeId() != Bulb.MATERIAL_ON)) {
            d = -1;
            break;
          }
          if (center.getRelative(0, -d - 1, -(w - 1)).getTypeId() == Bulb.MATERIAL_ON) {
            d++;
            break;
          }
        }
        if ((d < Pendant.MIN_DEPTH) || (d > Pendant.MAX_DEPTH)) {
          bulbs[3] = null;
        } else {
          depth[3] = d;
          width[3] = w;
          bulbs[3] = center.getRelative(0, -d, -(w - 1));
        }
      }

      if (!Base.isPowered(center.getRelative(0, -h, 0))) {
        return null;
      }

      Pendant pendant = new Pendant(h, depth, width, bulbs, this.lampWorld, null);
      Pendant.access$8(pendant);
      return this.lamps.add(pendant);
    }
    protected Lamp create(String string, World world) {
      String[] param = string.split(",");
      int pos = 0;

      int h = Integer.valueOf(param[(pos++)]).intValue();
      boolean[] b = new boolean[4];
      Block[] bulbs = new Block[4];
      int[] d = new int[4];
      int[] w = new int[4];
      for (int i = 0; i < 4; i++) {
        b[i] = param[(pos++)].equals("1");
        if (b[i] != 0) {
          bulbs[i] = world.getBlockAt(
            Integer.valueOf(param[(pos++)]).intValue(), 
            Integer.valueOf(param[(pos++)]).intValue(), 
            Integer.valueOf(param[(pos++)]).intValue());

          d[i] = Integer.valueOf(param[(pos++)]).intValue();
          w[i] = Integer.valueOf(param[(pos++)]).intValue();
        } else {
          pos += 5;
          bulbs[i] = null;
          d[i] = 0;
          w[i] = 0;
        }
      }

      Pendant pentand = new Pendant(h, d, w, bulbs, this.lampWorld, null);
      pentand.setID(Integer.valueOf(param[pos]).intValue());

      return pentand;
    }

    public void setup(PluginConfig config)
    {
      int[] minmax = config.getMinMax(getName(), "height");
      Pendant.MIN_HEIGHT = minmax[0];
      Pendant.MAX_HEIGHT = minmax[1];

      minmax = config.getMinMax(getName(), "width");
      Pendant.MIN_WIDTH = minmax[0];
      Pendant.MAX_WIDTH = minmax[1];

      minmax = config.getMinMax(getName(), "depth");
      Pendant.MIN_DEPTH = minmax[0];
      Pendant.MAX_DEPTH = minmax[1];
    }
    Provider(String name, String pattern, LampWorld lampWorld) {
      super(pattern, lampWorld);
    }

    public LampProvider setup(LampWorld lampWorld) {
      return new Provider("pendant", Pendant.access$16(), lampWorld);
    }
  }
}