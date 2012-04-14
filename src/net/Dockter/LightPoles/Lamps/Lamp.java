package net.Dockter.LightPoles.Lamps;

import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Fence;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.LampBlock;
import de.bukkit.Ginsek.StreetLamps.Listener.SLTimeListener;
import de.bukkit.Ginsek.StreetLamps.Listener.SLWeatherListener;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public abstract class Lamp
{
  private static ArrayList<Lamp> lamps = new ArrayList();

  public static int lastID = 0;
  public static boolean REMOVETORCH = false;
  private final LampWorld lampWorld;
  public int id = -1;
  protected LampController controler = null;
  public Player markedBy = null;

  private static boolean lock = false;
  protected static final String DIRECTION = "[0-3]";
  protected static final String INT = "(-)?[0-9]+";
  protected static final String posINT = "[0-9]+";
  protected static final String ID = "[0-9]+";
  protected static final String TRUE = "1";
  protected static final String FALSE = "0";
  protected static final String BOOL = "(1|0)";
  protected static final String LOC = "(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+";
  protected static final String BULB = "(1|0),(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+";

  public static Lamp getWithID(int id)
  {
    for (Lamp lamp : lamps) {
      if (lamp.id == id) {
        return lamp;
      }
    }
    return null;
  }
  protected static void setID(Lamp lamp) {
    lamp.id = (++lastID);
  }

  public boolean connect(LampController lampControler)
  {
    if (this.controler != null) return false;
    this.controler = lampControler;
    this.markedBy = null;
    return true;
  }
  public void disconnect() {
    this.controler = null;
    update();
  }
  public boolean isConnected() {
    return this.controler != null;
  }

  protected Lamp(LampWorld lampWorld)
  {
    this.lampWorld = lampWorld;
    lamps.add(this);
  }
  public int destroy() {
    lamps.remove(this);
    if (this.controler != null) {
      this.controler.disconnect(this);
    }

    if (!lamps.isEmpty()) {
      Lamp last = (Lamp)lamps.get(lamps.size() - 1);
      if (last.id < this.id) {
        last.id = this.id;
      }
    }
    lastID -= 1;

    return this.id;
  }
  public void onCreate(Player player) {
    if (!REMOVETORCH) return;
    int removed = removeTorch();
    if (removed > 0)
      player.getInventory().addItem(new ItemStack[] { new ItemStack(76, removed) }); 
  }
  protected abstract int removeTorch();

  public abstract Fence[] getFences();

  public abstract Bulb[] getBulbs();

  public abstract Base[] getBases();

  protected void setID(int id) { this.id = id; }

  public final void turnOn() {
    if (isPowered())
      forceOn_byMode();
    else
      forceOff_byMode();
  }

  public final void turnOff() {
    if ((!this.lampWorld.isNight()) && (!this.lampWorld.isRaining()))
      forceOff_byMode(); 
  }
  protected abstract void on();

  protected abstract void off();

  protected abstract void toggle();

  public final void forceOn_byMode() { if ((lock) || (Bulb.changingMaterial) || (this.controler != null)) return;
    lock = true;
    on();
    lock = false; }

  public final void forceOn() {
    if ((lock) || (Bulb.changingMaterial) || (this.controler != null)) return;
    lock = true;
    on();
    lock = false;
  }
  public final void forceOff_byMode() {
    if ((lock) || (Bulb.changingMaterial) || (this.controler != null)) return;
    lock = true;
    off();
    lock = false;
  }
  public final void forceOff_byCommand() {
    if ((lock) || (Bulb.changingMaterial) || (this.controler != null)) return;
    lock = true;
    off();
    lock = false;
  }
  public final void forceOn_byCircuit() {
    if ((lock) || (Bulb.changingMaterial)) return;
    lock = true;
    on();
    lock = false;
  }
  public final void forceOff_byCircuit() {
    if ((lock) || (Bulb.changingMaterial)) return;
    lock = true;
    off();
    lock = false;
  }
  public final void toggleState() {
    if ((lock) || (Bulb.changingMaterial) || (this.controler != null)) return;
    lock = true;
    toggle();
    lock = false;
  }

  private final boolean checkOn() {
    if ((SLWeatherListener.WEATHERMODE) && (SLTimeListener.DAYTIMEMODE)) {
      return (this.lampWorld.isRaining()) || (this.lampWorld.isNight());
    }
    if (SLWeatherListener.WEATHERMODE)
    {
      return this.lampWorld.isRaining();
    }

    if (SLTimeListener.DAYTIMEMODE)
    {
      return this.lampWorld.isNight();
    }

    return true;
  }
  public final void update() {
    if ((isPowered()) && (checkOn()))
      forceOn_byMode();
    else
      forceOff_byMode();
  }

  public abstract boolean isIntact();

  public abstract boolean isPowered();

  protected static final String parseString(String[] s) {
    String str = s[0];
    for (int i = 1; i < s.length; i++) {
      str = str + "," + s[i];
    }
    return str;
  }
  protected static final String parseString(int[] s) {
    String str = s[0];
    for (int i = 1; i < s.length; i++) {
      str = str + "," + s[i];
    }
    return str; } 
  protected abstract String createString();

  public String getString() { return createString() + "," + this.id;
  }

  protected abstract boolean containsFence(LampBlock paramLampBlock);

  protected abstract boolean containsBulb(LampBlock paramLampBlock);

  public abstract boolean equals(Lamp paramLamp);

  public final boolean equals(Object obj)
  {
    if ((obj instanceof Lamp)) return equals((Lamp)obj);

    if ((obj instanceof Integer)) return this.id == ((Integer)obj).intValue();
    return false;
  }

  public abstract String getName();

  public void info(Player player)
  {
    player.sendMessage("Pattern: " + getName());
    player.sendMessage("ID: " + this.id);
    player.sendMessage("connected: " + (this.controler == null ? "false" : "true"));
  }
}