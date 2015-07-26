package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public abstract class GUI_AuctionPage extends GUI_PageView<Auction> {

	protected boolean		highlightAdminShops;
	protected final Integer	worldGroup;
	private boolean			showStatusLine;

	public GUI_AuctionPage(String title, ItemStack displayItem, InventoryGUI parentGUI, boolean highlightAdminShops, Integer worldGroup, boolean showStatusLine) {
		super(title, displayItem, 1, parentGUI);
		this.worldGroup = worldGroup;
		this.showStatusLine = showStatusLine;
		this.highlightAdminShops = highlightAdminShops;
	}

	public abstract boolean drawAuctionCreateButton(Player player);

	public abstract boolean drawAdminShopCreateButton();

	public abstract boolean showRunningAuctionsInHistory();

	public abstract ItemStack getPresetAdminShopItem();

	public abstract boolean drawMoneyButton(Player player);

	public abstract boolean drawDisplayItemButton(Player player);

	public abstract boolean drawHistoryButton(Player player);

	@Override
	public void drawAditionalButtons(Player player) {

		// Display Item
		if (drawDisplayItemButton(player)) {
			this.drawButton(4, 0, new Button_Bare(this.getDisplayIcon()));
		}

		// Create New Auction
		if (this.drawAuctionCreateButton(player)) {
			this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_AuctionCreate(this.worldGroup));
		}
		// Create new AdminShop
		if (drawAdminShopCreateButton() && GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
			this.drawButton(this.getWidth() - 2, this.getHeight() - 1, new Button_AdminShopCreate(this.getPresetAdminShopItem(), worldGroup));
			// this.drawButton(this.getWidth() - 2, this.getHeight() - 1,
			// new Button(new ItemStack(Material.ENDER_CHEST),
			// GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAdminShop_SubmitButton),GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAdminShop_SubmitButton_DESC))
			// {
			//
			// @Override
			// public void onRefresh(InventoryGUI inventoryGUI, Player player)
			// {}
			//
			// @Override
			// public void onButtonClick(InventoryGUI inventoryGUI, Player
			// player, ItemStack cursor, ItemStack current, ClickType type,
			// InventoryClickEvent event) {
			// if (presetAdminShopItem == null) {
			// new GUI_AdminShopChooseItem(inventoryGUI,
			// worldGroup).open(player);
			// return;
			// }
			// Auction adminShop =
			// GlobalChestShop.plugin.getAuctionController(worldGroup).getAdminShopFromItemStack(presetAdminShopItem);
			// if (adminShop == null) {
			// new GUI_AdminShopCreate(presetAdminShopItem, inventoryGUI,
			// worldGroup).open(player);
			// } else {
			// new GUI_AdminShopEdit(adminShop, inventoryGUI,
			// worldGroup).open(player);
			// }
			// }
			// });
		}

		// Money Button
		if (drawMoneyButton(player)) {
			this.drawButton(0, this.getHeight() - 1, new Button_Money(player.getUniqueId()));
		}

		// History Button
		if (drawHistoryButton(player)) {
			this.drawButton(1, this.getHeight() - 1, new Button_AuctionHistory(true, this.worldGroup, this.showRunningAuctionsInHistory()));
		}

	}

	@Override
	public Button convertListObjectToButton(Auction obj, Player player) {
		return new Button_Auction(obj, true, this.highlightAdminShops, this.worldGroup, this.showStatusLine);
	}

	@Override
	public boolean shouldBackgroundBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldEscKeyClosesTheGUI() {
		return false;
	}

	@Override
	public boolean shouldEscKeyLeadsToPreviousGUI() {
		return true;
	}

	@Override
	public boolean shouldReturnButtonBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldCloseButtonBeDrawn() {
		return true;
	}

	@Override
	public void onPlayerOpensTheGUI(Player player) {
	}

	@Override
	public void onPlayerLeavesTheGUI(Player player) {
	}

	@Override
	public void onTick(Player player, int tickCount) {
	}

	@Override
	public boolean shouldObjectListBeRefreshedAutomatically() {
		return true;
	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
		if (type == ClickType.SHIFT_LEFT && this.drawAuctionCreateButton(player)) {
			GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AuctionCreate(this, clicked, this.worldGroup, player));
		}
	}

}
