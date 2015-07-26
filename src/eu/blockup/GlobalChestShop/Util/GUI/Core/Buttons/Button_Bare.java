package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_Bare extends Button {

  public Button_Bare() {
    super();
  }

  public Button_Bare(Button_Bare button) {
    super(button);
  }

  public Button_Bare(Button button) {
    super(button);
  }

  public Button_Bare(ItemStack displayItem, String title, String... description) {
    super(displayItem, title, description);
  }

  public Button_Bare(ItemStack itemStack) {
    super(itemStack);
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {}

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {}

  

	@Override
	protected Sound getClickSound(ClickType type) {
		return null;
	}
}
