package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects.ButtonEffect_FlashRed;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_DefaultCategoryBanItem extends SimpleIInventoryGUI {

	private ItemStack	item;
	private int			worldGroup;

	public GUI_DefaultCategoryBanItem(ItemStack item, int worldGroup, InventoryGUI parentGUI) {
		super("Item administration", 6, item, parentGUI);
		this.item = item;
		this.worldGroup = worldGroup;
	}

	@Override
	protected void drawButtons(Player player) {

		// Display Item
		this.drawButton(4, 1, new Button_Bare(item));

		
		// Hide Item in Categories
		if (this.item != null && GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
			this.drawButton(3, 3, new Button_BanIItem(player, new BannState(item, worldGroup, false)));
		}

		// Ban Item from LocalShop
		if (this.item != null && GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
			this.drawButton(5, 3, new Button_BanIItem(player, new BannState(item, worldGroup, true)));
		}
		ChatColor c = ChatColor.RED;
		this.addAnimatedButton(new ButtonEffect_FlashRed(0, this.getHeight() -1), new Button_Bare(new ItemStack(Material.PAPER), c + "INFO", c + "Don't forget to reload this plugin to make the changes take place."," ", c + "You can do this with the command:", ChatColor.GREEN + "/GlobalChestShop reload"));

	}

}
