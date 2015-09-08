package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.Exceptions.NoFreeSlotInInventoryException;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_PolarQuestion;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion.ButtonTypEnum;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_AuctionDelete extends SimpleIInventoryGUI {

	private final Auction	auction;
	private Integer			worldGroup;
	private double multiplier;

	public GUI_AuctionDelete(Auction auction, InventoryGUI parentInventoryGUI, Integer worldGroup, double multiplier) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAuction_Title), 6, auction.getItemStack(1), parentInventoryGUI);
		this.auction = auction;
		this.worldGroup = worldGroup;
		this.multiplier = multiplier;
	}

	@Override
	protected void drawButtons(Player player) {
			if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.MODERATOR_DELETE_AUCTIONS)) {
				// Remove ALl Auction of this player
				this.drawButton(getWidth() -1, getHeight() -1 ,new Button(new ItemStack (Material.WOOL, 1, (short) 14), "Cancel all auctions of this player!") {
					
					@Override
					public void onRefresh(InventoryGUI inventoryGUI, Player player) {
						
					}
					
					@Override
					public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
						new GUI_RemoveAllAuctionsOfPlayerPolarQuestion(inventoryGUI.getFirstGUI(),  auction.getPlayerStarter(), worldGroup).open(player);
					}
				});
			}
		
		// Acution Display Item
		this.drawButton(4, 1, new Button_Auction(auction, 1.0, true, false, this.worldGroup, true));

		// Cancel Auction
		this.drawButton(4, 3, new Button_PolarQuestion(new ItemStack(Material.WOOL, 1, (short) 14), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAuction_Cancel_Auction)) {

			@Override
			public ButtonTypEnum whichButtonShouldBeClickedOnTimeout() {
				return ButtonTypEnum.NoButton;
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
			protected void onYesButtonClick(InventoryGUI inventoryGUI, Player player) {
				synchronized (auction) {

					if (auction.isEndent()) {
						InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAuction_Auction_Allready_Ended), false, player, inventoryGUI.getParentGUI());
						return;
					}
					try {
						GlobalChestShop.plugin.validateFreeInventorySlot(player);
						auction.canceleAuction();
						player.getInventory().addItem(auction.getItemStack(auction.getAmount()));
						player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Message_RemovedAuction));
						GlobalChestShop.plugin.logToTradeLogger(player.getName(), player.getUniqueId(), player.getName() + " has canceled the auction : " + auction.toString(multiplier));
						inventoryGUI.getParentGUI().returnToParentGUI(player);
					} catch (NoFreeSlotInInventoryException e) {
						InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoSpace), false, player, inventoryGUI.getParentGUI());
					}
				}
				inventoryGUI.getParentGUI().returnToParentGUI(player);
			}

			@Override
			public void onTick(Player player, int tickCount) {

			}

			@Override
			public void onPlayerOpensTheGUI(Player player) {

			}

			@Override
			public void onPlayerLeavesTheGUI(Player player) {

			}

			@Override
			protected void onNoButtonClick(InventoryGUI inventoryGUI, Player player) {
				inventoryGUI.returnToParentGUI(player);

			}

			@Override
			public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

			}

			@Override
			public Button_Bare get_the_Question_Button() {
				return new Button_Bare(new ItemStack(Material.PAPER), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAuction_Cancel_Auction_Question));
			}

			@Override
			public Button_Bare get_YesButton() {
				return new Button_Bare(new ItemStack(Material.WOOL, 1, (short) 5), GlobalChestShop.text.get(GlobalChestShop.text.PolarQuestion_YES));
			}

			@Override
			public Button_Bare get_NoButton() {
				return new Button_Bare(new ItemStack(Material.WOOL, 1, (short) 14), GlobalChestShop.text.get(GlobalChestShop.text.PolarQuestion_NO));
			}

			@Override
			public String get_GUI_Title() {
				return GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAuction_Cancel_Auction);
			}

			@Override
			public long getTimeoutInTicks() {
				return 20 * 15;
			}

			@Override
			protected void drawAdditionalButtons(Player player, InventoryGUI inventoryGUI) {
				
			}

		});
	}

}
