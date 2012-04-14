package net.Dockter.LightPoles.Listener;

import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Collections.WorldCollection;
import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

public class SLWorldListener extends WorldListener
{
  public void onWorldLoad(WorldLoadEvent wle)
  {
    loadLamps(wle.getWorld());
  }
  public void onWorldInit(WorldInitEvent wie) {
    loadLamps(wie.getWorld());
  }
  public void loadLamps(World world) {
    if (WorldCollection.lampWorlds.contains(LampWorld.getComparable(world))) return;
    WorldCollection.registerWorld(world);
  }
}