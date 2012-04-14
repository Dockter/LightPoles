package net.Dockter.LightPoles.Collections;

import de.bukkit.Ginsek.StreetLamps.Configs.PluginConfig;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampProvider;
import java.util.ArrayList;

public class ProviderList
{
  private final ArrayList<LampProvider> lampProvider = new ArrayList();
  private final PluginConfig pluginConfig;

  public ProviderList(PluginConfig pluginConfig)
  {
    this.pluginConfig = pluginConfig;
  }

  public void register(LampProvider provider) {
    this.lampProvider.add(provider);
    provider.setup(this.pluginConfig);
  }

  public ArrayList<LampProvider> getProvider(LampWorld lampWorld) {
    ArrayList provider = new ArrayList();

    for (LampProvider p : this.lampProvider) {
      provider.add(p.setup(lampWorld));
    }

    return provider;
  }
}