package eu.blockup.GlobalChestShop.Util.GUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_SubmitBuy extends GUI_PolarQuestion {
	private Auction	auction;
	private int		amount;
	private double multiplier;

	public GUI_SubmitBuy(Auction auction, int amount, double multiplier, InventoryGUI parentGUI) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitBuy_Title), parentGUI);
		this.auction = auction;
		this.amount = amount;
		this.multiplier = multiplier;
	}

	@Override
	public boolean shouldReturnButtonBeDrawn() {
		return false;
	}

	@Override
	public boolean shouldEscKeyLeadsToPreviousGUI() {
		return false;
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
	public void onPlayerOpensTheGUI(Player player) {
	}

	@Override
	public void onPlayerLeavesTheGUI(Player player) {
	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
	}

	@Override
	public ButtonTypEnum whichButtonShouldBeClickedOnTimeout() {
		return ButtonTypEnum.NoButton;
	}
	


	@Override
	protected void onYesButtonClick(InventoryGUI inventoryGUI, Player player) {
		GlobalChestShop.plugin.getAuctionController(auction.getworldGroup()).buyAuction(amount, auction, multiplier,  inventoryGUI, player);
	}

	@Override
	protected void onNoButtonClick(InventoryGUI inventoryGUI, Player player) {
		this.returnToParentGUI(player);
	}

	@Override
	public Button_Bare get_the_Question_Button() {
		return new Button_Bare(auction.getItemStack(amount), GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitBuy_Sure_to_Buy), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceTotal, GlobalChestShop.plugin.formatPrice(auction.getShopToPlayerPrice(this.amount, multiplier), false)));
	}

	@Override
	public Button_Bare get_YesButton() {
		return new Button_Bare(new ItemStack(Material.WOOL, 1, (byte) 5), GlobalChestShop.text.get(GlobalChestShop.text.PolarQuestion_YES));
	}

	@Override
	public Button_Bare get_NoButton() {
		return new Button_Bare(new ItemStack(Material.WOOL, 1, (byte) 14), GlobalChestShop.text.get(GlobalChestShop.text.PolarQuestion_NO));
	}

	@Override
	public String get_GUI_Title() {
		return GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitBuy_Sure_to_Buy);
	}

	@Override
	public long getTimeoutInTicks() {
		return 0;
	}

	@Override
	protected void drawAdditionalButtons(Player player) {

		// Money Button
		this.drawButton(0, this.getHeight() - 1, new Button_Money(player.getUniqueId()));
	}

	@Override
	public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

	}
}
