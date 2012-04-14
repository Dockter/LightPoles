package net.Dockter.LightPoles.Collections;

import de.bukkit.Ginsek.StreetLamps.Configs.LampLoader;
import de.bukkit.Ginsek.StreetLamps.Configs.PluginConfig;
import de.bukkit.Ginsek.StreetLamps.Lamps.Expandable.Globe;
import de.bukkit.Ginsek.StreetLamps.Lamps.Expandable.Pendant;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampProvider;
import de.bukkit.Ginsek.StreetLamps.Lamps.Simple.Bottom;
import de.bukkit.Ginsek.StreetLamps.Lamps.Simple.Ceiling;
import de.bukkit.Ginsek.StreetLamps.Lamps.Simple.Pole;
import de.bukkit.Ginsek.StreetLamps.Lamps.Simple.PureBulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Simple.Sconce;
import de.bukkit.Ginsek.StreetLamps.Lamps.Simple.Tube;
import java.util.ArrayList;
import org.bukkit.World;

public class WorldCollection
{
  public static final ArrayList<LampWorld> lampWorlds = new ArrayList();
  private static ProviderList providerList;
  private static LampLoader lampLoader;

  public static void setup(LampLoader lampConfig, PluginConfig pluginConfig)
  {
    lampLoader = lampConfig;
    providerList = new ProviderList(pluginConfig);

    if (pluginConfig.getPureBulb()) {
      PureBulb.enabled = true;
      providerList.register(PureBulb.provider);
    } else {
      String lampList = pluginConfig.getLampList();
      if ((lampList == null) || (lampList.isEmpty()) || (!lampList.matches("([a-z]+;)*"))) {
        return;
      }

      for (String lampType : lampList.split(";"))
        if (Bottom.provider.provides(lampType)) {
          providerList.register(Bottom.provider);
        }
        else if (Pole.provider.provides(lampType)) {
          providerList.register(Pole.provider);
        }
        else if (Tube.provider.provides(lampType)) {
          providerList.register(Tube.provider);
        }
        else if (Ceiling.provider.provides(lampType)) {
          providerList.register(Ceiling.provider);
        }
        else if (Sconce.provider.provides(lampType)) {
          providerList.register(Sconce.provider);
        }
        else if (Globe.provider.provides(lampType)) {
          providerList.register(Globe.provider);
        }
        else if (Pendant.provider.provides(lampType))
          providerList.register(Pendant.provider);
    }
  }

  public void loadLamps(World world)
  {
    if (lampWorlds.contains(LampWorld.getComparable(world))) return;
    LampWorld lampWorld = new LampWorld(world, providerList, lampLoader);

    lampWorlds.add(lampWorld);
  }

  public static LampWorld getLampWorld(World world) {
    int index = lampWorlds.indexOf(LampWorld.getComparable(world));
    return index == -1 ? null : (LampWorld)lampWorlds.get(index);
  }
  public static LampWorld getLampWorld(String world) {
    for (LampWorld lampWorld : lampWorlds) {
      if (lampWorld.world.getName().equals(world)) return lampWorld;
    }
    return null;
  }

  public static void saveLamps() {
    for (LampWorld lampWorld : lampWorlds) {
      lampWorld.saveLamps(lampLoader);
    }
    lampLoader.save();
  }

  public static void registerWorld(World world) {
    LampWorld lampWorld = new LampWorld(world, providerList, lampLoader);

    lampWorlds.add(lampWorld);
  }
}