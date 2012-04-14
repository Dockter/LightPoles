package net.Dockter.LightPoles.Lamps;

import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LampController
{
  public static final int BUTTON = 77;
  public static final int LEVER = 69;
  public static ArrayList<Block> usedBlocks = new ArrayList();
  protected boolean state = false;
  public final Block block;
  public ArrayList<Lamp> lamps;
  private static ArrayList<LampController> controller = new ArrayList();

  public LampController(Block block)
  {
    this.block = block;
  }
  public LampController(String controle, World world) {
    this.lamps = new ArrayList();
    String[] args = controle.split(",");
    this.block = world.getBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    this.state = args[3].equals("1");
    for (int i = 4; i < args.length; i++) {
      Lamp lamp = Lamp.getWithID(Integer.parseInt(args[i]));
      if ((lamp != null) && (lamp.connect(this))) {
        this.lamps.add(lamp);
      }
    }
    update();
  }

  public final void connect(ArrayList<Lamp> lamps) {
    this.lamps = lamps;
    for (Lamp lamp : lamps) {
      if (!lamp.connect(this)) {
        lamps.remove(lamp);
      }
    }
    update();
  }
  public final void disconnect(Lamp lamp) {
    this.lamps.remove(lamp);
    if (this.lamps.isEmpty()) destroy(); 
  }

  public final void destroy() {
    for (Lamp lamp : this.lamps) {
      lamp.disconnect();
    }
    usedBlocks.remove(this.block);
    controller.remove(this);
  }

  private final void update() {
    if (this.state) {
      for (Lamp lamp : this.lamps)
        lamp.forceOn_byCircuit();
    }
    else
      for (Lamp lamp : this.lamps)
        lamp.forceOff_byCircuit();
  }

  public boolean onUse()
  {
    this.state = (!this.state);
    update();
    return this.state;
  }

  public final String getString() {
    String string = this.block.getX() + "," + this.block.getY() + "," + this.block.getZ() + "," + (this.state ? 1 : 0);
    for (Lamp lamp : this.lamps) {
      string = string + "," + lamp.id;
    }
    return string;
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof LampController)) return false;
    return this.block.equals(((LampController)obj).block);
  }

  public static LampController getComparable(Block block)
  {
    return new LampController(block);
  }

  public void info(Player player) {
    player.sendMessage("State: " + (this.state ? "on" : "off"));
    player.sendMessage("Connected Lamps:");
    String ids = "";
    for (Lamp lamp : this.lamps) {
      ids = ids + lamp.id + " ";
    }
    player.sendMessage(ids);
  }

  public static class ConnectionList
  {
    private final Player player;
    private final ArrayList<Lamp> lamps = new ArrayList();
    private LampController controller = null;

    ConnectionList(Player player) {
      this.player = player;
    }

    public void addLamp(Lamp lamp) {
      if (!this.lamps.contains(lamp))
        this.lamps.add(lamp);
    }

    public void setController(Block clickedBlock) {
      if (LampController.usedBlocks.contains(clickedBlock)) return;
      LampController.usedBlocks.add(clickedBlock);
      this.controller = new LampController(clickedBlock);
    }
    public LampController finish() {
      if (this.controller == null) return null;
      this.controller.connect(this.lamps);
      return this.controller;
    }

    public boolean equals(Object obj) {
      if (!(obj instanceof ConnectionList)) return false;
      return this.player.equals(((ConnectionList)obj).player);
    }
    public static ConnectionList getComparable(Player player) {
      return new ConnectionList(player);
    }
  }
}