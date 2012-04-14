package net.Dockter.LightPoles.Listener;

import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Collections.WorldCollection;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

public class SLWeatherListener extends WeatherListener
{
  public static boolean WEATHERMODE = false;

  public void onWeatherChange(WeatherChangeEvent wce) {
    if (wce.isCancelled()) return;

    LampWorld lampWorld = WorldCollection.getLampWorld(wce.getWorld());
    if (lampWorld == null) return;

    lampWorld.raining = wce.toWeatherState();
    if (lampWorld.raining) {
      if (WEATHERMODE) lampWorld.turnOn_byMode();

    }
    else if ((WEATHERMODE) && (!lampWorld.isNight()))
      lampWorld.forceOff_byMode();
  }
}