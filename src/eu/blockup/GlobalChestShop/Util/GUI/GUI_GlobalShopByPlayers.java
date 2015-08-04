package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_GlobalShopByPlayers extends GUI_PageView<UUID> {

	private int		worldGroup;
	private boolean	newAuctions;
	private double multiplier;

	public GUI_GlobalShopByPlayers(int worldGroup, boolean newAuctions, InventoryGUI parentGUI, double multiplier) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Title_AllLocalChestShop), GlobalChestShop.plugin.mainConfig.getDisplayItemAllLocalShops(), 1, parentGUI);
		this.worldGroup = worldGroup;
		this.newAuctions = newAuctions;
		this.multiplier = multiplier;
	}

	@Override
	public List<UUID> getRefreshedObjectList() {
		return GlobalChestShop.plugin.getShopVerwaltung().getAllActiveLocalShops(worldGroup);
	}

	@Override
	public boolean shouldObjectListBeRefreshedAutomatically() {
		return true;
	}

	@Override
	public void drawAditionalButtons(Player player) {

		// Display Item
		this.drawButton(4, 0, new Button_Bare(this.getDisplayIcon(), this.getTitle()));

		// Create New Auction
		if (this.newAuctions) {
			this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_AuctionCreate(this.worldGroup));
			this.drawButton(0, this.getHeight() - 1, new Button_AuctionHistory(true, this.worldGroup, true));
		}
		
		//Search
		this.drawButton(0, 1, new Button_SearchAuctions(worldGroup, multiplier));

	}

	@Override
	public Button convertListObjectToButton(UUID obj, Player player) {
		return new Button_openLocalShop(obj);
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
