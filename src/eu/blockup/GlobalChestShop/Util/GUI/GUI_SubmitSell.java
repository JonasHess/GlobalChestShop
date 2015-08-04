package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.Exceptions.PlayerDoesNotOwnClaimedItemException;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_SubmitSell extends GUI_PolarQuestion {
	private Auction	auction;
	private int		amount;
	private boolean	sellAll;
	private double	multiplier;

	public GUI_SubmitSell(Auction auction, int amount, double multiplier,  boolean sellAll, InventoryGUI parentGUI) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitSell_Question), parentGUI);
		this.auction = auction;
		this.amount = amount;
		this.sellAll = sellAll;
		this.multiplier = multiplier;
	}

	@Override
	protected void drawAdditionalButtons(Player player) {

	}

	@Override
	protected void onYesButtonClick(InventoryGUI inventoryGUI, Player player) {
		List<ItemStack> itemStackList = new ArrayList<ItemStack>(sellAll ? 5 : 1);
		List<Double> priceList = new ArrayList<Double>(sellAll ? 5 : 1);
		if (sellAll) {
			synchronized (player.getInventory()) {
				this.amount = 0;
				for (ItemStack itemInPlayersInv : player.getInventory()) {
					if (itemInPlayersInv == null)
						continue;
					if (!(itemInPlayersInv.isSimilar(auction.getItemStack(1))))
						continue;
					this.amount += itemInPlayersInv.getAmount();
					itemStackList.add(itemInPlayersInv);
					priceList.add(auction.getPlayerToShopPrice(itemInPlayersInv.getAmount(), multiplier));
				}
			}
		} else {
			itemStackList.add(auction.getItemStack(amount));
			priceList.add(auction.getPlayerToShopPrice(amount, multiplier));
		}
		try {
			// double priceTotal = sellItem(, , player);
			if (amount == 0)
				throw new PlayerDoesNotOwnClaimedItemException();
			double priceTotal = sellItem(itemStackList, priceList, player);
			if (auction.isAdminshop()) {
				auction.createAdminShopHistoryEntry(auction, false, this.amount, multiplier, player.getUniqueId());
			} else {
				auction.markAsEnded(player.getUniqueId());
			}
			player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitSell_Item_Sold_SUccses));
			if (priceTotal > 0) {
				player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_deposit_Adminshop, GlobalChestShop.plugin.formatPrice(priceTotal, false)));
			}
			inventoryGUI.returnToParentGUI(player);
		} catch (PlayerDoesNotOwnClaimedItemException e1) {
			player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoItem));
			InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoItem), false, player, inventoryGUI.getParentGUI());
		} catch (Exception e) {
			e.printStackTrace();
			InventoryGUI.warning("This item is causing internal problems. Please inform the server administration", true, player, inventoryGUI.getParentGUI());
		}
	}

	// public double sellItem(ItemStack itemStack, Double price, Player player)
	// throws PlayerDoesNotOwnClaimedItemException {
	// List<ItemStack> itemStackList = new ArrayList<ItemStack>(1);
	// List<Double> priceList = new ArrayList<Double>(1);
	// itemStackList.add(itemStack);
	// priceList.add(price);
	// return this.sellItem(itemStackList, priceList, player);
	// }

	public double sellItem(List<ItemStack> itemStackList, List<Double> priceList, Player player) throws PlayerDoesNotOwnClaimedItemException {
		double priceTotal = 0;
		synchronized (player.getInventory()) {
			if (itemStackList.size() != priceList.size()) {
				throw new RuntimeException("itemStackList.size() != prices.size()");
			}
			for (int i = 0; i < itemStackList.size(); i++) {
				ItemStack itemStack = itemStackList.get(i);
				Double price = priceList.get(i);
				Double priceEach = 0.0;
				if (itemStack.getAmount() > 0) {
					priceEach = price / itemStack.getAmount();
				}
				if (itemStack == null || price == null || price < 0) {
					throw new RuntimeException("itemStack == null || price == null || price < 0");
				}
				GlobalChestShop.plugin.validatePlayerIsItemOwner(player, itemStack);
				player.getInventory().removeItem(itemStack);
				this.removeFromPlayerBalance(player, price);
				priceTotal += price;
				try {
					GlobalChestShop.plugin.getDefaultCategoryController(GlobalChestShop.plugin.getworldGroup(player.getUniqueId())).addItemToProtfolio(itemStack);
				} catch (WorldHasNoWorldGroupException e) {
				}
				GlobalChestShop.plugin.logToTradeLogger(player.getName(), player.getUniqueId(), player.getName() + " has sold " + itemStack.getAmount() + "x" + " " + GlobalChestShop.plugin.getItemStackDisplayName(itemStack) + " for " + GlobalChestShop.plugin.formatPrice(priceEach, false) + " each to an AdminShop.");
			}
		}

		return priceTotal;
	}

	private void removeFromPlayerBalance(Player player, Double amount) {
		GlobalChestShop.plugin.getEconomy().depositPlayer(player, player.getLocation().getWorld().getName(), amount);
	}

	@Override
	protected void onNoButtonClick(InventoryGUI inventoryGUI, Player player) {
		this.returnToParentGUI(player);

	}

	@Override
	public long getTimeoutInTicks() {
		return 0;
	}

	@Override
	public ButtonTypEnum whichButtonShouldBeClickedOnTimeout() {
		return ButtonTypEnum.NoButton;
	}

	@Override
	public String get_GUI_Title() {
		return GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitSell_Question);
	}

	@Override
	public Button_Bare get_the_Question_Button() {
		return new Button_Bare(auction.getItemStack(1), GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitSell_Question), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Amount, String.valueOf(amount)), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceEach, GlobalChestShop.plugin.formatPrice(auction.getPlayerToShopPrice(1, multiplier), false)), GlobalChestShop.text.get(
				GlobalChestShop.text.Auction_Info_PriceTotal, GlobalChestShop.plugin.formatPrice(auction.getPlayerToShopPrice(this.amount, multiplier), false)));
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
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

	}

	@Override
	public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

	}

}
