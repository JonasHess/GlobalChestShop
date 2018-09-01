package eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding;

import java.sql.Date;
import java.sql.Time;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_PolarQuestion;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_StateCangerDouble;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion.ButtonTypEnum;

public class GUI_PriceChanger extends InventoryGUI {
	public ItemStack		itemStack;
	public PriceTupple		prices;
	private final Integer	worldGroup;
	private final int amount;

	public boolean arePricesValide(Player player) {
		if (prices.getBuyPrice().getCurrentState(player) == -1.0)
			return true;
		if (prices.getSellPrice().getCurrentState(player) == -1.0)
			return true;
		return (prices.getBuyPrice().getCurrentState(player) >= prices.getSellPrice().getCurrentState(player));
	}

	public GUI_PriceChanger(ItemStack itemStack, PriceTupple prices, int amont, InventoryGUI parentInventoryGUI, Integer worldGroup) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Title), 6, itemStack, parentInventoryGUI);
		this.itemStack = itemStack;
		this.worldGroup = worldGroup;
		this.prices = prices;
		this.amount = amont;
	}

	@Override
	protected void drawButtons(Player player) {
		// Display Item
		this.drawButton(4, 1, new Button_Bare(this.getDisplayIcon()));

		// Buy Price
		this.drawButton(5, 3, new Button() {

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				this.setAppearance(((GUI_PriceChanger) inventoryGUI).prices.getBuyPrice().formatToDisplayButton(player, 1).addDescriptionLine(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Button_BuyPrice_DESC)));
			}

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_StateCangerDouble(((GUI_PriceChanger) inventoryGUI).prices.getBuyPrice(), GlobalChestShop.text.get(GlobalChestShop.text.GUI_Button_BuyPrice_Title), new ItemStack(Material.NAME_TAG), inventoryGUI, 4, amount, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1) {

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
						// -1.0 Button
						this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_changeDoubleState(new ItemStack(XMaterial.RED_WOOL.parseItem()), this.getValueObject(), -99.0D, ModifyTyp.setValue, amount, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1));

					}

					@Override
					public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

					}

					@Override
					public void onTick(Player player, int tickCount) {

					}
				});
			}
		});

		// Sell Price
		this.drawButton(3, 3, new Button() {

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				this.setAppearance(((GUI_PriceChanger) inventoryGUI).prices.getSellPrice().formatToDisplayButton(player, 1).addDescriptionLine(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Button_SellPrice__DESC)));
			}

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_StateCangerDouble(((GUI_PriceChanger) inventoryGUI).prices.getSellPrice(), GlobalChestShop.text.get(GlobalChestShop.text.GUI_Button_SellPrice_Title), new ItemStack(Material.NAME_TAG), inventoryGUI, 4, amount, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1) {

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
						// -1.0 Button
						this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_changeDoubleState(new ItemStack(XMaterial.RED_WOOL.parseItem()), this.getValueObject(), -99.0D, ModifyTyp.setValue, amount, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1));

					}

					@Override
					public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

					}

					@Override
					public void onTick(Player player, int tickCount) {

					}
				});
			}
		});

		// Leave button
		this.drawButton(0, 0, new Button_PolarQuestion(new ItemStack(XMaterial.OAK_DOOR.parseItem()), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_ExitSave), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_ExitSave_DESC)) {

			@Override
			public ButtonTypEnum whichButtonShouldBeClickedOnTimeout() {
				return ButtonTypEnum.YesButton;
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
			protected void onYesButtonClick(InventoryGUI inventoryGUI, Player player) {
				// GUI_PriceChanger gui = (GUI_PriceChanger)
				// inventoryGUI.getParentGUI();

				if (!arePricesValide(player)) {
					InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Warning_BuyPriceGreaterSellPrice), true, player, inventoryGUI);
					return;
				}

				Auction adminShop = GlobalChestShop.plugin.getAuctionController(worldGroup).getAdminShopFromItemStack(itemStack);
				if (adminShop == null) {
					Date date = new Date(System.currentTimeMillis());
					Time time = new Time(System.currentTimeMillis());
					Auction.createNewAuction(itemStack, 1, prices.sellPrice.getCurrentState(player), prices.buyPrice.getCurrentState(player), GlobalChestShop.plugin.adminShopUUID, GlobalChestShop.plugin.adminShopUUID, true, date, time, date, time, true, worldGroup);
					GlobalChestShop.plugin.getDefaultCategoryController(worldGroup).addItemToProtfolio(itemStack);
				} else {
					adminShop.setPlayerToShopPriceEach(prices.getSellPrice().getCurrentState(player));
					adminShop.setShopToPlayerPriceEach(prices.getBuyPrice().getCurrentState(player));
				}
				if (inventoryGUI.getFirstGUI() instanceof GUI_PriceChanger || inventoryGUI.getFirstGUI() instanceof GUI_PriceChanger) {
					inventoryGUI.close(player);
					return;
				}
				inventoryGUI.returnToParentGUI(player, 2);
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
				if (inventoryGUI.getFirstGUI() instanceof GUI_PriceChanger || inventoryGUI.getFirstGUI() instanceof GUI_PriceChanger) {
					inventoryGUI.close(player);
					return;
				}
				inventoryGUI.returnToParentGUI(player, 2);
			}

			@Override
			public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

			}

			@Override
			public Button_Bare get_the_Question_Button() {
				return new Button_Bare(new ItemStack(Material.PAPER), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_WQuestion_Save_Changes));
			}

			@Override
			public Button_Bare get_YesButton() {
				return new Button_Bare(new ItemStack(XMaterial.LIME_WOOL.parseItem()), GlobalChestShop.text.get(GlobalChestShop.text.PolarQuestion_YES));
			}

			@Override
			public Button_Bare get_NoButton() {
				return new Button_Bare(new ItemStack(XMaterial.RED_WOOL.parseItem()), GlobalChestShop.text.get(GlobalChestShop.text.PolarQuestion_NO_Leave_NO_SAVE));
			}

			@Override
			public String get_GUI_Title() {
				return GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_WQuestion_Save_Changes);
			}

			@Override
			public long getTimeoutInTicks() {
				return 0;
			}

			@Override
			protected void drawAdditionalButtons(Player player, InventoryGUI inventoryGUI) {

			}
		});

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
		return false;
	}

	@Override
	public boolean shouldReturnButtonBeDrawn() {
		return false;
	}

	@Override
	public boolean shouldCloseButtonBeDrawn() {
		return false;
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

	@Override
	public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

	}

}
