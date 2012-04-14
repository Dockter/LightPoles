package net.Dockter.LightPoles.Listener;

import de.bukkit.Ginsek.StreetLamps.Collections.ControllerList;
import de.bukkit.Ginsek.StreetLamps.Collections.LampWorld;
import de.bukkit.Ginsek.StreetLamps.Collections.WorldCollection;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Base;
import de.bukkit.Ginsek.StreetLamps.Lamps.Blocks.Bulb;
import de.bukkit.Ginsek.StreetLamps.Lamps.Lamp;
import de.bukkit.Ginsek.StreetLamps.Lamps.LampController;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.inventory.ItemStack;

public class SLBlockListener extends BlockListener
{
  public static int MATERIAL_buildTOOL;
  public static boolean lock = false;

  public void onBlockDamage(BlockDamageEvent bde) { if (bde.isCancelled()) return;
    if (bde.getItemInHand().getTypeId() != MATERIAL_buildTOOL) return;
    if (bde.getBlock().getTypeId() != Bulb.MATERIAL_ON) return;

    LampWorld lampWorld = WorldCollection.getLampWorld(bde.getBlock().getWorld());
    if (lampWorld == null) return;
    if (lampWorld.belongsToLamp(bde.getBlock())) return;

    Lamp lamp = lampWorld.createLamp(bde.getBlock());
    if (lamp == null) return;
    lamp.update();
    lamp.onCreate(bde.getPlayer());

    bde.getPlayer().sendMessage("You build a " + lamp.getName()); }

  public void onBlockBreak(BlockBreakEvent bbe) {
    if (bbe.isCancelled()) return;
    int id = bbe.getBlock().getTypeId();
    if ((id == Bulb.MATERIAL_ON) || (id == Bulb.MATERIAL_OFF) || (id == 85)) {
      LampWorld lampWorld = WorldCollection.getLampWorld(bbe.getBlock().getWorld());
      if (lampWorld == null) return;
      Lamp lamp = lampWorld.removeLamp(bbe.getBlock());
      if (lamp == null) return;

      bbe.getPlayer().sendMessage("You broke a " + lamp.getName());
    } else if ((id == 77) || (id == 69)) {
      LampWorld lampWorld = WorldCollection.getLampWorld(bbe.getBlock().getWorld());
      if (lampWorld == null) return;
      LampController controller = lampWorld.controllerList.getController(bbe.getBlock());
      if (controller == null) return;
      controller.destroy();

      bbe.getPlayer().sendMessage("You destroyed a lamp controller.");
    }
  }

  public void onBlockBurn(BlockBurnEvent bbe) {
    if (bbe.isCancelled()) return;

    int id = bbe.getBlock().getTypeId();
    if ((id == Bulb.MATERIAL_ON) || (id == Bulb.MATERIAL_OFF) || (id == 85)) {
      LampWorld lampWorld = WorldCollection.getLampWorld(bbe.getBlock().getWorld());
      if (lampWorld == null) return;
      lampWorld.removeLamp(bbe.getBlock());
    } else if ((id == 77) || (id == 69)) {
      LampWorld lampWorld = WorldCollection.getLampWorld(bbe.getBlock().getWorld());
      if (lampWorld == null) return;
      LampController controller = lampWorld.controllerList.getController(bbe.getBlock());
      if (controller == null) return;
      controller.destroy();
    }
  }

  public void onBlockPhysics(BlockPhysicsEvent bpe) {
    if (bpe.isCancelled()) return;

    if ((!Base.POWERMODE) || (lock)) return;
    LampWorld lampWorld = WorldCollection.getLampWorld(bpe.getBlock().getWorld());
    if (lampWorld == null) return;
    Base base = lampWorld.getBase(bpe.getBlock());
    if (base == null) return;

    lock = true;
    base.lamp.update();
    lock = false;
  }
}