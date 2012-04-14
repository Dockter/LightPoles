package net.Dockter.LightPoles.Lamps;

import java.util.ArrayList;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class UpdateQueue
  implements Runnable
{
  public static Plugin PLUGIN = null;
  public static int SIZE = 20;

  private Thread thread = null;

  private final Object lock = new Object();
  private ArrayList<Lamp> lamps = new ArrayList();

  private boolean turnOn = true;

  public UpdateQueue()
  {
    this.thread = new Thread(this);
    this.thread.start();
  }

  public void turnOn(ArrayList<Lamp> lamps)
  {
    synchronized (this.lock) {
      this.turnOn = true;
      this.lamps = new ArrayList();
      this.lamps.addAll(lamps);
      synchronized (this) {
        notify();
      }
    }
  }

  public void turnOff(ArrayList<Lamp> lamps) {
    synchronized (this.lock) {
      this.turnOn = false;
      this.lamps = new ArrayList();
      this.lamps.addAll(lamps);
      synchronized (this) {
        notify();
      }
    }
  }

  public void run()
  {
    boolean done = false;
    while (true)
    {
      done = false;

      synchronized (this.lock) {
        ArrayList toUpdate = new ArrayList();
        for (int i = 0; (i < SIZE) && (!this.lamps.isEmpty()); i++) {
          toUpdate.add((Lamp)this.lamps.remove(0));
        }
        if (!toUpdate.isEmpty()) {
          PLUGIN.getServer().getScheduler().scheduleSyncDelayedTask(PLUGIN, new Runnable(toUpdate)
          {
            public void run() {
              for (Lamp lamp : this.val$toUpdate) {
                if (UpdateQueue.this.turnOn)
                  lamp.turnOn();
                else
                  lamp.turnOff();
              }
            }
          });
        }
        done = this.lamps.isEmpty();
      }

      try
      {
        if (!done) {
          synchronized (this) {
            Thread.sleep(1000L);
          }
        }
        synchronized (this) {
          wait();
        }
      }
      catch (InterruptedException localInterruptedException)
      {
      }
    }
  }
}