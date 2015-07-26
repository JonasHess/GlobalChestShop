package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_Return extends Button {

  public Button_Return(InventoryGUI currentGUI, String title, String... description) {
    super(currentGUI.getParentGUI().getDisplayIcon(), title, description);
  }

  public Button_Return(InventoryGUI currentGUI) {
    super();
  }
  
  public Button_Return() {
    super();
  }

  public Button_Return(Button_Bare button) {
    super(button);
  }

  public Button_Return(Button button) {
    super(button);
  }


  public Button_Return(ItemStack displayItem, String title, String... description) {
    super(displayItem, title, description);
  }

  public Button_Return(ItemStack itemStack) {
    super(itemStack);
  }



  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    inventoryGUI.returnToParentGUI(player);
    this.afterInventoryJump(inventoryGUI, inventoryGUI.getParentGUI(), player, cursor, current, type, event);
  }


  public void afterInventoryJump(InventoryGUI desertedGui, InventoryGUI accededGUI, Player player, ItemStack cursor, ItemStack current,
      ClickType type, InventoryClickEvent event) {}

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
    // TODO Auto-generated method stub

  }
  

	@Override
	protected Sound getClickSound(ClickType type) {
		return Sound.DOOR_OPEN;
	}

}
