package eu.blockup.GlobalChestShop.Util.Shops;

import org.bukkit.entity.Player;

import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public interface IClickable {
	public  void onInteractLeftClick(final Player player, final InventoryGUI previousGUI);
	public  void onInteractRightClick(final Player player, final InventoryGUI previousGUI);
}
