package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_GlobalShopByAuctions extends GUI_PageView<Auction> {

	private int		worldGroup;
	private boolean	onlyAdminShops;
	private boolean	newAuctions;
	private double multiplier;

	public GUI_GlobalShopByAuctions(int worldGroup, boolean onlyAdminShops, boolean newAuctions, double multiplier, InventoryGUI parentGUI) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Title_AllAuctions), GlobalChestShop.plugin.mainConfig.getDisplayItemAllLocalShops(), 1, parentGUI);
		this.worldGroup = worldGroup;
		this.onlyAdminShops = onlyAdminShops;
		this.newAuctions = newAuctions;
		this.multiplier = multiplier;
	}

	@Override
	public List<Auction> getRefreshedObjectList() {
		return GlobalChestShop.plugin.getAuctionController(worldGroup).getAllActiveAuction(this.onlyAdminShops);
	}

	@Override
	public boolean shouldObjectListBeRefreshedAutomatically() {
		return true;
	}

	@Override
	public void drawAditionalButtons(Player player) {

		// Search
		this.drawButton(0, 1, new Button_SearchAuctions(worldGroup, multiplier));

		// Display Item
		this.drawButton(4, 0, new Button_Bare(this.getDisplayIcon(), this.getTitle()));

		// Create New Auction
		if (this.newAuctions) {
			this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_AuctionCreate(this.worldGroup));
			this.drawButton(0, this.getHeight() - 1, new Button_AuctionHistory(true, this.worldGroup, true));
		}
	}

	@Override
	public Button convertListObjectToButton(Auction obj, Player player) {
		return new Button_Auction(obj, multiplier, true, false, worldGroup, false);
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
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

	}

}
