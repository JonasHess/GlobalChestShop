package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.CustomCategory;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class Button_CustomCategory extends Button{

	
	private CustomCategory customCategory;
	private int worldGroup;
	public Button_CustomCategory(CustomCategory customCategory, int worldGroup) {
		super(customCategory.getIconItemStack(), customCategory.getName());
		this.customCategory = customCategory;
		this.worldGroup = worldGroup;
	}

	@Override
	public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
		if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
			if (type == ClickType.SHIFT_LEFT) {
				int newPosition = Math.max(2, customCategory.isShownInCreativeMenu()) -1;
				customCategory.setShownInCreativeMenu(newPosition);
				inventoryGUI.redrawAllButtons(player);
				inventoryGUI.refresh(player);
				return;
			} else if (type == ClickType.SHIFT_RIGHT) {
				int newPosition = Math.min(4, customCategory.isShownInCreativeMenu()) +1;
				customCategory.setShownInCreativeMenu(newPosition);
				inventoryGUI.redrawAllButtons(player);
				inventoryGUI.refresh(player);
				return;
			}
		}
		new GUI_CustomCategoryPage(customCategory, inventoryGUI, true, worldGroup, false).open(player);
	}

	@Override
	public void onRefresh(InventoryGUI inventoryGUI, Player player) {
		// TODO Auto-generated method stub
		
	}
	
}
