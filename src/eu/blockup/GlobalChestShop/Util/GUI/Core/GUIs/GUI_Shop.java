package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_AmountChanger;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Interfaces.BuyAbleInterface;

public abstract class GUI_Shop extends InventoryGUI {

	private BuyAbleInterface	buyItem;
	private Economy				economy;
	private boolean				buy;
	private boolean				sell;
	private boolean				sellAll;
	private Button				amountButton;
	private Button				buyButton;
	private Button				displayButton;
	private Button				sellButton;
	private Button				sellAllButton;

	public GUI_Shop(String title, ItemStack displayItem, InventoryGUI parentInventoryGUI, BuyAbleInterface buyItem, Economy economy, boolean buy, boolean sell, boolean sellAll) {
		this(title, displayItem, parentInventoryGUI, buyItem, economy, buy, sell, sellAll, 1);
	}

	public GUI_Shop(String title, ItemStack displayItem, InventoryGUI parentInventoryGUI, final BuyAbleInterface buyItem, Economy economy, boolean buy, boolean sell, boolean sellAll, int startAmount) {
		super(title, 6, displayItem, parentInventoryGUI);
		this.buyItem = buyItem;
		this.economy = economy;
		this.buy = buy;
		this.sell = sell;
		this.sellAll = sellAll;

		this.amountButton = new Button_AmountChanger(startAmount, this.getAmountButton(buyItem, economy)) {

			@Override
			public void onAmountChange(InventoryGUI inventoryGUI, Player player, int count) {
				((GUI_Shop) inventoryGUI).setAmount(count);
				inventoryGUI.resetAllButtons(player);
				inventoryGUI.refresh(player);
			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}

			@Override
			public int getMaxAmount(InventoryGUI inventoryGUI, Player player) {
				return ((GUI_Shop) inventoryGUI).buyItem.getMaxAmount();
			}
		};

		displayButton = buyItem.getItemStackAsButton();

		buyButton = new Button((ItemStack) null) {

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				((GUI_Shop) inventoryGUI).buy(player);
				;
			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
			}
		};

		sellButton = new Button((ItemStack) null) {

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				((GUI_Shop) inventoryGUI).sell(player);
				;
			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}
		};

		sellAllButton = new Button((ItemStack) null) {

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				((GUI_Shop) inventoryGUI).sellAll(player, buyItem.getAmountOfItemInInventory(player));
				;
			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}
		};

	}
	
	@Override
	protected void drawButtons(Player player) {
		this.displayButton.setAppearance(this.buyItem.getItemStackAsButton());
		this.buyButton.setAppearance(this.buyItem.getBuyButton(this.getAmount(), player));
		this.sellButton.setAppearance(this.buyItem.getSellButton( this.getAmount(), player));
		this.sellAllButton.setAppearance(this.buyItem.getSellAllButton(player));
		this.layout(displayButton, amountButton, buyButton, sellButton, sellAllButton);
	}

	@Override
	public void onTick(Player player, int tickCount) {

	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

	}

	protected void layout(Button displayItem, Button amountButton, Button buyButton, Button sellButton, Button sellAllButton) {
		if (buy && sell) {
			this.drawButton(4, 1, displayItem);
			if (buyItem.getMaxAmount() > 1) {
				this.drawButton(4, 3, amountButton);
			}
			this.drawButton(5, 3, buyButton);
			this.drawButton(3, 3, sellButton);
		} else if (buy) {
			this.drawButton(4, 1, displayItem);
			if (buyItem.getMaxAmount() > 1) {
				this.drawButton(3, 3, amountButton);
				this.drawButton(5, 3, buyButton);
			} else {
				this.drawButton(4, 3, buyButton);
			}
		} else if (sell) {
			this.drawButton(4, 1, displayItem);
			if (buyItem.getMaxAmount() > 1) {
				this.drawButton(3, 3, sellButton);
				this.drawButton(5, 3, amountButton);
			} else {
				this.drawButton(4, 3, sellButton);
			}
		} else {
			// No Buttons :(
		}

		if (sellAll) {
			this.drawButton(2, 3, sellAllButton);
		}
	}

	//protected abstract Button_Bare getBuyButton(BuyAbleInterface buyItem, Economy economy, int amount, Player player);

	//protected abstract Button_Bare getSellButton(BuyAbleInterface buyItem, Economy economy, int amount, Player player);

	//protected abstract Button_Bare getSellAllButton(BuyAbleInterface buyItem, Economy economy, int amount, Player player);

	protected abstract Button_Bare getAmountButton(BuyAbleInterface buyItem, Economy economy);

	protected abstract void afterBuy(Player player);

	protected abstract void afterSell(Player player);

	public int getAmount() {
		return this.getAmountButton().getAmount();
	}

	public void setAmount(int amount) {
		this.amountButton.setAmount(amount);
	}

	protected void buy(Player player) {
		this.buyItem.buy(player, economy, this.getAmount());
		this.afterBuy(player);
		// this.resetAllButtons(player); // TODO
		// this.refresh(player);
	}

	protected void sell(Player player) {
		this.buyItem.sell(player, economy, this.getAmount());
		this.resetAllButtons(player);
		// this.afterSell(player); // TODO
		// this.refresh(player);
	}

	protected void sellAll(Player player, int sellAllAmount) {
		this.buyItem.sellAll(player, economy, sellAllAmount);
		this.resetAllButtons(player);
		// this.afterSell(player); // TODO
		// this.refresh(player);
	}

	public Economy getEconomy() {
		return economy;
	}

	public void setEconomy(Economy economy) {
		this.economy = economy;
	}

	public boolean isBuy() {
		return buy;
	}

	public void setBuy(boolean buy) {
		this.buy = buy;
	}

	public boolean isSell() {
		return sell;
	}

	public void setSell(boolean sell) {
		this.sell = sell;
	}

	public BuyAbleInterface getBuyItem() {
		return buyItem;
	}

	public Button getAmountButton() {
		return amountButton;
	}

	public void setSellAll(boolean sellAll) {
		this.sellAll = sellAll;
	}

	public boolean isSellAll() {
		return sellAll;
	}

}
