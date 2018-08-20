package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;


public abstract class Button_CloseGUI extends Button {

 

  public Button_CloseGUI() {
    super();
  }

  public Button_CloseGUI(Button_Bare button) {
    super(button);
  }

  public Button_CloseGUI(Button button) {
    super(button);
  }

  public Button_CloseGUI(ItemStack displayItem, String title, String... description) {
    super(displayItem, title, description);
  }

  public Button_CloseGUI(ItemStack itemStack) {
    super(itemStack);
  }

  
  private void playSoundCloseSound(Player player) {
	  player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
  }
  
  
  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    inventoryGUI.setCloseButtonAsPressed(true);
    inventoryGUI.close(player);
    this.afterInventoryClose(inventoryGUI, player, cursor, current, type, event);
    playSoundCloseSound(player);
  }

  protected abstract void afterInventoryClose(InventoryGUI closedGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
      InventoryClickEvent event);

}
