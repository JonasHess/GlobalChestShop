package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects.ButtonEffect_FadeIn;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_StateCangerDouble;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_AuctionGetPrice extends GUI_StateCangerDouble{

	private int worldGroup;
	private AuctionPrepare auctionPrepare;

	public GUI_AuctionGetPrice(AuctionPrepare auctionPrepare, int worldGroup,  InventoryGUI parentGUI) {
		super(auctionPrepare.getPriceObject(), GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_PriceChange_Menu_Title), new ItemStack(Material.PAPER), parentGUI, 6, auctionPrepare.getTotalAmount(), GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1);
		this.worldGroup = worldGroup;
		this.auctionPrepare = auctionPrepare;
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
		return false;
	}

	@Override
	public boolean shouldBackgroundBeDrawn() {
		return true;
	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

	}

	@Override
	public void onPlayerOpensTheGUI2(Player player) {

	}

	@Override
	public void onPlayerLeavesTheGUI2(Player player) {

	}

	@Override
	protected void drawAdditionalButtons(Player player) {
		this.addAnimatedButton(new ButtonEffect_FadeIn(this.getWidth() - 1, this.getHeight() - 1, 20), new Button(new ItemStack(Material.ARROW),
				GlobalChestShop.text.get(GlobalChestShop.text.Button_Next)) {

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}

			@Override
			protected Sound getClickSound(ClickType type) {
				return Sound.ENTITY_ARROW_HIT;
			}

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
					new GUI_AuctionCreateSubmit(auctionPrepare, worldGroup, inventoryGUI).open(player);
			}
		});
	}

	@Override
	public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

	}

	@Override
	public void onTick(Player player, int tickCount) {

	}
}

