package eu.blockup.GlobalChestShop.Util.GUI;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Interfaces.BuyAbleInterface;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_AuctionBuy extends GUI_Shop {

	private Auction	auction;
	private Integer	worldGroup;

	public GUI_AuctionBuy(Auction auction, InventoryGUI parentGUI, Integer worldGroup) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_BuyAuction_Title), auction.getItemStack(auction.getAmount()), parentGUI, auction, GlobalChestShop.plugin.getEconomy(), true, true, true);
		this.auction = auction;
		this.setAmount(auction.getAmount());
		this.worldGroup = worldGroup;
	}

	@Override
	protected void layout(Button displayItem, Button amountButton, Button buyButton, Button sellButton, Button sellAllButton) {

		this.drawButton(4, 1, displayItem);
		this.drawButton(4, 3, buyButton);
	}

	@Override
	public boolean shouldReturnButtonBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldEscKeyLeadsToPreviousGUI() {
		return true;
	}

	@Override
	public boolean shouldEscKeyClosesTheGUI() {
		return false;
	}

	@Override
	public boolean shouldCloseButtonBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldBackgroundBeDrawn() {
		return true;
	}

	@Override
	public void onPlayerOpensTheGUI(Player player) {
		// if (auction.isEndent()) {
		// this.returnToParentGUI(player);
		// }

	}

	@Override
	public void onPlayerLeavesTheGUI(Player player) {

	}

	@Override
	protected Button_Bare getAmountButton(BuyAbleInterface buyItem, Economy economy) {
		return new Button_Bare(new ItemStack(Material.SIGN), GlobalChestShop.text.get(GlobalChestShop.text.GUI_BuyAdminShop_Amount_Button), GlobalChestShop.text.get(GlobalChestShop.text.GUI_BuyAdminShop_Amount_Button_DESC));
	}

	@Override
	protected void afterSell(Player player) {
	}

	@Override
	protected void afterBuy(Player player) {
	}

	@Override
	protected void drawButtons(Player player) {
		super.drawButtons(player);
		// Money Button
		this.drawButton(0, this.getHeight() - 1, new Button_Money(player.getUniqueId()));

		// Amind delete Auction
		if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
			this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button(new ItemStack(Material.REDSTONE), "Delete Auction", "Admins only", "WARNING:", "Players will not get", "their items back") {
				@Override
				public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				}

				@Override
				public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
					GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AuctionDelete(auction, inventoryGUI, worldGroup));
				}
			});
		}
	}

	@Override
	public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

	}

}
