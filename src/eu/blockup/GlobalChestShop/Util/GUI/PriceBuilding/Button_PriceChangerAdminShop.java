package eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_PriceChangerAdminShop extends Button{
	
	private ItemStack itemStack;
	private int worldGroup;
	
	
	public Button_PriceChangerAdminShop(ItemStack itemStack, int worldGroup) {
		super(itemStack);
		this.itemStack = itemStack;
		this.worldGroup = worldGroup;
	}

	@Override
	public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
		GlobalChestShop.plugin.getPriceEngine().openPriceChangeMenuForItem(player, inventoryGUI, itemStack, worldGroup);
	}

	@Override
	public void onRefresh(InventoryGUI inventoryGUI, Player player) {
		this.clearDesacription();
		Auction  adminAuction = GlobalChestShop.plugin.getAuctionController(worldGroup).getAdminShopFromItemStack(itemStack);
		if (adminAuction == null) {
			this.setAmount(0);
			this.addDescriptionLine(ChatColor.RED + "No price defined"); // TODO
		} else {
			this.setAmount(1);
			this.addDescriptionLine(ChatColor.GRAY + "BuyPrice: " + GlobalChestShop.plugin.formatPrice(adminAuction.getShopToPlayerPrice(1), true)); // TODO
			this.addDescriptionLine(ChatColor.GRAY + "SellPrice: " + GlobalChestShop.plugin.formatPrice(adminAuction.getPlayerToShopPrice(1), true)); // TODO
		}
		
	}

}
