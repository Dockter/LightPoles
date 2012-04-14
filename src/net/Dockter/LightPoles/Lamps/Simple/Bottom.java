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
import org.bukkit.World;
import org.bukkit.block.Block;

public class Bottom extends Lamp
{
  private Bulb bulb = null;
  private Base base = null;

  public static LampProvider provider = new Provider("bottom", getPattern());

  private Bottom(Block bulb, LampWorld lampWorld)
  {
    super(lampWorld);

    this.bulb = new Bulb(bulb, this, true);

    this.base = new Base(bulb.getRelative(0, -1, 0), this);
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
    return new Fence[0];
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
    return this.bulb.isIntact();
  }

  public String createString()
  {
    return this.bulb.getString();
  }

  protected boolean containsBulb(LampBlock comparable)
  {
    return this.bulb.equals(comparable);
  }

  protected boolean containsFence(LampBlock comparable) {
    return false;
  }

  public boolean equals(Lamp lamp) {
    if ((lamp instanceof Bottom)) {
      return ((Bottom)lamp).bulb.equals(this.bulb);
    }
    return false;
  }

  public String getName()
  {
    return "bottom";
  }

  private static String getPattern()
  {
    return parseString(new String[] { "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "[0-9]+" });
  }

  public static class Provider extends LampProvider {
    Provider(String name, String pattern) {
      super(pattern, null);
    }

    Provider(String name, String pattern, LampWorld lampWorld) {
      super(pattern, lampWorld);
    }

    public Lamp createLamp(Block bulb)
    {
      if (bulb == null) return null;

      Block base = bulb.getRelative(0, -1, 0);
      if (base == null) return null;

      int id = base.getTypeId();
      if ((id == 0) || (id == 20) || (id == 85) || (!Base.isPowered(base))) {
        return null;
      }

      Bottom bottom = new Bottom(bulb, this.lampWorld, null);
      Bottom.access$1(bottom);
      return this.lamps.add(bottom);
    }

    protected Lamp create(String string, World world) {
      String[] param = string.split(",");
      Block bulbBlock = world.getBlockAt(Integer.valueOf(param[1]).intValue(), Integer.valueOf(param[2]).intValue(), Integer.valueOf(param[3]).intValue());

      Bottom bottom = new Bottom(bulbBlock, this.lampWorld, null);
      bottom.setID(Integer.valueOf(param[4]).intValue());
      return bottom;
    }

    public void setup(PluginConfig config)
    {
    }

    public LampProvider setup(LampWorld lampWorld)
    {
      return new Provider("bottom", Bottom.access$3(), lampWorld);
    }
  }
}