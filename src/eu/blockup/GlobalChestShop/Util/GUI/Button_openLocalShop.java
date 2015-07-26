package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class Button_openLocalShop extends Button{
  
  
  private UUID uuid;

  public Button_openLocalShop(UUID uuid) {
    super(GlobalChestShop.plugin.getPlayerHead(GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(uuid), false));
    this.uuid = uuid;
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    
    Shop s = GlobalChestShop.plugin.getShopVerwaltung().openLocalShopOfPlayer(uuid, player, inventoryGUI);
    if (s != null) {
      if (type == ClickType.RIGHT && GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
        player.sendMessage(ChatColor.AQUA + "Admin-Teleport to LocalChestShop of " + s.getSecondSignLine(true)) ;
        try {
          player.teleport(s.getSignLocation());
        } catch (Exception e) {
          player.sendMessage("This shop is too far away to teleport");
        }
      }
    }
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
    
  }

}
