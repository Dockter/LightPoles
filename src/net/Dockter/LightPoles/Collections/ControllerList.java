package net.Dockter.LightPoles.Collections;

import de.bukkit.Ginsek.StreetLamps.Lamps.LampController;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampController.ConnectionList;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ControllerList
{
  public static boolean CONTROLLER = false;
  private final ArrayList<LampController.ConnectionList> connectionLists = new ArrayList();
  public final ArrayList<LampController> controllerList = new ArrayList();

  public boolean onCommand(Player player) {
    LampController.ConnectionList comparable = LampController.ConnectionList.getComparable(player);
    if (!this.connectionLists.contains(comparable)) {
      this.connectionLists.add(comparable);
      player.sendMessage("Mark lamps and set controller.");
      return true;
    }

    LampController.ConnectionList connectionList = (LampController.ConnectionList)this.connectionLists.get(this.connectionLists.indexOf(comparable));
    this.connectionLists.remove(connectionList);

    LampController controller = connectionList.finish();
    if (controller == null) {
      player.sendMessage("FAILED: You forgot to set a controller.");
      return true;
    }
    if (controller.lamps.size() == 0) {
      player.sendMessage("FAILED: You didn't mark any lamps.");
      return true;
    }

    this.controllerList.add(controller);
    player.sendMessage("You connected " + controller.lamps.size() + " lamps to a controller.");
    return true;
  }
  public LampController.ConnectionList getConnectionList(Player player) {
    LampController.ConnectionList comparable = LampController.ConnectionList.getComparable(player);
    int index = this.connectionLists.indexOf(comparable);
    if (index == -1) return null;
    return (LampController.ConnectionList)this.connectionLists.get(index);
  }
  public LampController getController(Block block) {
    LampController comparable = LampController.getComparable(block);
    int index = this.controllerList.indexOf(comparable);
    if (index == -1) return null;
    return (LampController)this.controllerList.get(index);
  }
  public void destroy(LampController control) {
    this.controllerList.remove(control);
    LampController.usedBlocks.remove(control.block);
    control.destroy();
  }
  public boolean isInUse(Block clickedBlock) {
    return LampController.usedBlocks.contains(clickedBlock);
  }
}