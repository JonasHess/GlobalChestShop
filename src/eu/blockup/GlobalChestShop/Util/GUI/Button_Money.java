package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_Money extends Button{

  public UUID uuid;
  
  public Button_Money(UUID uuid) {
    super(new ItemStack(Material.GOLD_INGOT));
    this.uuid = uuid;
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
    this.setTitle(GlobalChestShop.text.get(GlobalChestShop.text.Button_Money));
    try{
      this.setDescription(GlobalChestShop.plugin.formatPrice(GlobalChestShop.plugin.getEconomy().getBalance(player, player.getLocation().getWorld().getName()), false));
      
    } catch(Exception e) {
      this.setDescription(GlobalChestShop.plugin.formatPrice(-1, true));
    }
    
  }

}
