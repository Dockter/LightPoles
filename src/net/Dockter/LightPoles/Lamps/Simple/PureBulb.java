package net.Dockter.LightPoles.Lamps.Simple;

import de.bukkit.Ginsek.StreetLamps.Collections.LampCollection;
import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Collections.WorldCollection;
import de.bukkit.Ginsek.StreetLamps.Configs.PluginConfig;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Fence;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.LampBlock;
import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampProvider;
import java.util.ArrayList;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class PureBulb extends Lamp
{
  private Bulb bulb = null;

  public static LampProvider provider = new Provider("purebulb", getPattern());

  private static Plugin plugin = null;
  public static boolean running = false;
  public static boolean enabled = false;
  public static int current;
  public static int max;
  public static String currentworld;
  public static boolean silent = false;
  private static BukkitScheduler scheduler = null;

  private PureBulb(Block bulb, LampWorld lampWorld)
  {
    super(lampWorld);

    this.bulb = new Bulb(bulb, this, true);
  }

  protected int removeTorch() {
    return 0;
  }

  public Bulb[] getBulbs()
  {
    return new Bulb[] { this.bulb };
  }

  public Fence[] getFences() {
    return new Fence[0];
  }

  public Base[] getBases() {
    return new Base[0];
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

  public boolean isIntact()
  {
    return this.bulb.isIntact();
  }

  public boolean isPowered() {
    return true;
  }

  public String createString()
  {
    Block block = this.bulb.getBlock();
    return parseString(new int[] { block.getX(), block.getY(), block.getZ() });
  }

  protected boolean containsBulb(LampBlock comparable)
  {
    return this.bulb.equals(comparable);
  }

  protected boolean containsFence(LampBlock comparable) {
    return false;
  }

  public boolean equals(Lamp lamp) {
    if ((lamp instanceof PureBulb)) {
      return ((PureBulb)lamp).bulb.equals(this.bulb);
    }
    return false;
  }

  public String getName()
  {
    return "purebulb";
  }

  private static String getPattern()
  {
    return parseString(new String[] { "(-)?[0-9]+,(-)?[0-9]+,(-)?[0-9]+", "[0-9]+" });
  }

  public static void enable(Plugin plugin)
  {
    enabled = true;
    plugin = plugin;
  }

  public static boolean create(String worldname, boolean silent, int start, int stop) {
    silent = silent;

    LampWorld tmp = null;
    for (LampWorld w : WorldCollection.lampWorlds) {
      if (w.world.getName().equals(worldname)) {
        tmp = w;
        currentworld = tmp.world.getName();
        break;
      }
    }
    if (tmp == null) return false;
    LampWorld world = tmp;
    if (scheduler == null) {
      scheduler = plugin.getServer().getScheduler();
    }

    scheduler.scheduleAsyncDelayedTask(plugin, new Runnable(world, start, stop)
    {
      public void run()
      {
        LampProvider provider = null;
        for (LampProvider prov : PureBulb.this.provider) {
          if (prov.provides("purebulb")) {
            provider = prov;
            break;
          }
        }
        if (provider == null) return;

        PureBulb.running = true;

        Chunk[] loadedChunks = PureBulb.this.world.getLoadedChunks();
        PureBulb.current = 0;
        PureBulb.max = loadedChunks.length;
        PureBulb.plugin.getServer().broadcastMessage("[StreetLamps] bruteforcing " + loadedChunks.length + " chunks in " + PureBulb.this.world.getName() + "at ~0.5chunks/sec, might lag (or crash) if it takes longer to bruteforce whole a chunk");
        Chunk[] arrayOfChunk1;
        int j = (arrayOfChunk1 = loadedChunks).length; for (int i = 0; i < j; ) { Chunk chunk = arrayOfChunk1[i];
          PureBulb.letItRun(provider, chunk, this.val$start, this.val$stop);
          PureBulb.current += 1;
          if (!PureBulb.silent)
            PureBulb.plugin.getServer().broadcastMessage(PureBulb.current + "/" + PureBulb.max + " chunks done");
          try
          {
            synchronized (this) {
              Thread.sleep(2000L);
            }
          }
          catch (InterruptedException localInterruptedException)
          {
            i++;
          }

        }

        PureBulb.plugin.getServer().broadcastMessage("[StreetLamps] donw with " + PureBulb.currentworld);
        PureBulb.running = false;
      }
    }
    , 0L);
    return true;
  }
  public static void letItRun(LampProvider provider, Chunk chunk, int start, int stop) {
    if (!chunk.isLoaded()) {
      return;
    }
    scheduler.scheduleSyncDelayedTask(plugin, new Runnable(start, stop, chunk, provider)
    {
      public void run() {
        ArrayList bulbs = new ArrayList();
        int x;
        for (int y = this.val$start; y >= this.val$stop; y--) {
          for (x = 0; x <= 16; x++) {
            for (int z = 0; z <= 16; z++) {
              int id = this.val$chunk.getBlock(x, y, z).getTypeId();
              if ((id == Bulb.MATERIAL_ON) || (id == Bulb.MATERIAL_OFF)) {
                Block bulb = this.val$chunk.getBlock(x, y, z);
                if (!this.val$provider.containsBulb(LampBlock.getComparable(bulb))) {
                  bulbs.add(bulb);
                }
              }
            }
          }
        }

        for (Block bulb : bulbs) {
          if ((bulb.getRelative(1, 0, 0).getTypeId() != 85) && 
            (bulb.getRelative(-1, 0, 0).getTypeId() != 85) && 
            (bulb.getRelative(0, 1, 0).getTypeId() != 85) && 
            (bulb.getRelative(0, -1, 0).getTypeId() != 85) && 
            (bulb.getRelative(0, 0, 1).getTypeId() != 85) && 
            (bulb.getRelative(0, 0, -1).getTypeId() != 85)) continue;
          this.val$provider.createLamp(bulb);
        }
      }
    }
    , 0L);
  }

  public static class Provider extends LampProvider
  {
    Provider(String name, String pattern)
    {
      super(pattern, null);
    }

    Provider(String name, String pattern, LampWorld lampWorld) {
      super(pattern, lampWorld);
    }

    public Lamp createLamp(Block bulb)
    {
      if (bulb == null) return null;

      PureBulb pureBule = new PureBulb(bulb, this.lampWorld, null);
      PureBulb.access$1(pureBule);
      return this.lamps.add(pureBule);
    }

    protected Lamp create(String string, World world) {
      String[] param = string.split(",");
      Block bulbBlock = world.getBlockAt(Integer.valueOf(param[0]).intValue(), Integer.valueOf(param[1]).intValue(), Integer.valueOf(param[2]).intValue());

      PureBulb purebulb = new PureBulb(bulbBlock, this.lampWorld, null);
      purebulb.setID(Integer.valueOf(param[3]).intValue());
      return purebulb;
    }

    public void setup(PluginConfig config)
    {
    }

    public LampProvider setup(LampWorld lampWorld)
    {
      return new Provider("purebulb", PureBulb.access$3(), lampWorld);
    }
  }
}