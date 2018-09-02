package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class Button_AuctionList extends Button {
	private Integer		worldGroup;
	private boolean		adminShopsOnly;
	private ItemStack	auctionItem;
	private boolean		newAuctions;
	private boolean		inDefaultCategory;
	private double multiplier;

	public Button_AuctionList(ItemStack actionItem, boolean onlyAdminShops, Integer worldGroup, boolean newAuctions, boolean inDefaultCategory, double multiplier) {
		super(actionItem.clone(), "");
		this.worldGroup = worldGroup;
		this.auctionItem = actionItem.clone();
		this.adminShopsOnly = onlyAdminShops;
		this.newAuctions = newAuctions;
		this.inDefaultCategory = inDefaultCategory;
		this.multiplier = multiplier;
		// this.auctionItem = actionItem;
	}

	@Override
	public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

		if (type != ClickType.LEFT) {
			if (this.inDefaultCategory && GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
				new GUI_DefaultCategoryBanItem(this.auctionItem, this.worldGroup, inventoryGUI).open(player);
				return;
			}
		}

		if (this.adminShopsOnly) {
			GlobalChestShop.plugin.openAdminShopOnlyGUI(inventoryGUI, player, auctionItem, worldGroup, multiplier);
		} else {
			GlobalChestShop.plugin.openNormalAuctionGUI(inventoryGUI, player, auctionItem, worldGroup, this.newAuctions, this.adminShopsOnly, multiplier);
		}
	}

	@Override
	public void onRefresh(InventoryGUI inventoryGUI, Player player) {
		if (GlobalChestShop.plugin.getMainConfig().indicateAuctionAmount) {
			this.setAmount(Math.max(GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllActiveAuctionForItemStack(auctionItem, this.adminShopsOnly).size(), 1));
		} else {
			this.setAmount(1);
		}
	}

}
