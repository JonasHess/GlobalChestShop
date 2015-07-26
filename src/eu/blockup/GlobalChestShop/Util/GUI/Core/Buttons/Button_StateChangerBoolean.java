package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_StateChangerBoolean extends Button{
  private StateKeeper<Boolean> valueIbject;
  

  public Button_StateChangerBoolean(StateKeeper<Boolean> valueIbject, Player player) {
    super(valueIbject.formatToDisplayButton(player, 1));
    this.valueIbject = valueIbject;
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    valueIbject.setState(!valueIbject.getCurrentState(player), player);
    this.onRefresh(inventoryGUI, player);
    inventoryGUI.refresh(player);
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
    this.setAppearance(valueIbject.formatToDisplayButton(player, 1));
    
  }


}
