package net.Dockter.LightPoles.Listener;

import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.StreetLamps;
import java.util.ArrayList;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;

public class SLTimeListener
{
  public static int NIGHT_END;
  public static int NIGHT_START;
  private static ArrayList<SLTimeListener> timeListener = new ArrayList();
  public static LightPoles plugin;
  public static boolean DAYTIMEMODE = false;
  private final LampWorld lampWorld;

  public SLTimeListener(LampWorld lampWorld)
  {
    this.lampWorld = lampWorld;
    timeListener.add(this);
  }

  public void start()
  {
    long time = this.lampWorld.world.getTime();
    this.lampWorld.night = ((time > NIGHT_START) && (time < NIGHT_END));
    if (DAYTIMEMODE) {
      if (this.lampWorld.night)
        this.lampWorld.turnOn_byMode();
      else if (!this.lampWorld.isRaining()) {
        this.lampWorld.turnOff_byMode();
      }
    }

    plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
    {
      public void run() {
        SLTimeListener.this.updateDay();
      }
    }
    , 60L, 60L);
  }
  public void updateDay() {
    long time = this.lampWorld.world.getTime();
    boolean night = (time > NIGHT_START) && (time < NIGHT_END);
    if (night != this.lampWorld.night) {
      this.lampWorld.night = night;

      if (!DAYTIMEMODE) return;

      if (this.lampWorld.night)
        this.lampWorld.turnOn_byMode();
      else if (!this.lampWorld.isRaining())
        this.lampWorld.turnOff_byMode();
    }
  }

  public static void update()
  {
    for (SLTimeListener listener : timeListener)
      listener.updateDay();
  }
}