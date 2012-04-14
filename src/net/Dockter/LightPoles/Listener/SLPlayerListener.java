package net.Dockter.LightPoles.Listener;

import de.bukkit.Ginsek.StreetLamps.Collections.ControllerList;
import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Collections.WorldCollection;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampController;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampController.ConnectionList;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class SLPlayerListener extends PlayerListener
{
  public static boolean MANUALLY = false;
  public static boolean INFO = true;
  public static final ArrayList<Player> infoPlayer = new ArrayList();

  public void onPlayerInteract(PlayerInteractEvent pie) {
    if (pie.isCancelled()) return;
    if (pie.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    if (pie.getClickedBlock() == null) return;

    if ((INFO) && (infoPlayer.contains(pie.getPlayer()))) {
      int id = pie.getClickedBlock().getTypeId();
      if ((id == Bulb.MATERIAL_ON) || (id == Bulb.MATERIAL_OFF) || (id == 85)) {
        LampWorld lampWorld = WorldCollection.getLampWorld(pie.getPlayer().getWorld());
        if (lampWorld == null) return;
        Lamp lamp = lampWorld.getLamp(pie.getClickedBlock());
        if (lamp == null) return;

        lamp.info(pie.getPlayer());
      } else if ((id == 77) || (id == 69)) {
        LampWorld lampWorld = WorldCollection.getLampWorld(pie.getPlayer().getWorld());
        if (lampWorld == null) return;
        LampController controller = lampWorld.controllerList.getController(pie.getClickedBlock());
        if (controller == null) return;

        controller.info(pie.getPlayer());
      }
      return;
    }

    if (ControllerList.CONTROLLER) {
      LampWorld lampWorld = WorldCollection.getLampWorld(pie.getPlayer().getWorld());
      if (lampWorld != null) {
        LampController.ConnectionList conList = lampWorld.controllerList.getConnectionList(pie.getPlayer());
        if (conList != null) {
          if ((pie.getClickedBlock().getTypeId() == 77) || (pie.getClickedBlock().getTypeId() == 69)) {
            if ((lampWorld != null) && (lampWorld.controllerList.isInUse(pie.getClickedBlock()))) {
              pie.getPlayer().sendMessage("This controler already is in use.");
              return;
            }
            conList.setController(pie.getClickedBlock());
            pie.getPlayer().sendMessage("You set the controller.");
          }

          if (lampWorld != null) {
            Lamp lamp = lampWorld.getLamp(pie.getClickedBlock());
            if (lamp != null) {
              if (lamp.isConnected()) {
                pie.getPlayer().sendMessage("This lamp is already connected to a circuit.");
              } else {
                if (lamp.markedBy != null) {
                  pie.getPlayer().sendMessage("This lamp is marked by player " + lamp.markedBy.getName() + ".");
                  return;
                }

                lamp.markedBy = pie.getPlayer();
                conList.addLamp(lamp);
                pie.getPlayer().sendMessage("You marked that lamp.");
              }
            }
            return;
          }
        }

        if ((pie.getClickedBlock().getTypeId() == 77) || (pie.getClickedBlock().getTypeId() == 69)) {
          LampController lampController = lampWorld.controllerList.getController(pie.getClickedBlock());
          if (lampController == null) return;
          lampController.onUse();
          return;
        }
      }
    }

    if (MANUALLY) {
      int id = pie.getClickedBlock().getTypeId();
      if ((id != 85) && (id != Bulb.MATERIAL_ON) && (id != Bulb.MATERIAL_OFF)) return;

      LampWorld lampWorld = WorldCollection.getLampWorld(pie.getClickedBlock().getWorld());
      if (lampWorld == null) return;
      Lamp lamp = lampWorld.getLamp(pie.getClickedBlock());
      if (lamp == null) return;
      lamp.toggleState();
    }
  }
}