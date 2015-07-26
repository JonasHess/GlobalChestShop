package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;

public abstract class GUI_PolarQuestion extends InventoryGUI {

	public enum ButtonTypEnum {
		YesButton, NoButton
	}

	private Button	yesButton;
	private Button	noButton;
	private long	timeout;
	private boolean	hasTimeout;
	private boolean	clickYesButtonOnTimeout;

	public GUI_PolarQuestion(String title, InventoryGUI parentGUI) {
		super(title, 5, new ItemStack(Material.PAPER), parentGUI);
		this.timeout = this.getTimeoutInTicks();
		this.clickYesButtonOnTimeout = (this.whichButtonShouldBeClickedOnTimeout() == ButtonTypEnum.YesButton);
		this.hasTimeout = (timeout > 0);
		this.yesButton = new Button(get_YesButton()) {

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				onYesButtonClick(inventoryGUI, player);

			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}

			@Override
			protected Sound getClickSound(ClickType type) {
				return Sound.SHOOT_ARROW;
			}

		};

		this.noButton = new Button(get_NoButton()) {

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				onNoButtonClick(inventoryGUI, player);
			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}

			@Override
			protected Sound getClickSound(ClickType type) {
				return Sound.FALL_SMALL;
			}

		};

	}

	@Override
	protected void drawButtons(Player player) {

		this.drawButton(4, 1, new Button(get_the_Question_Button()) {

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}
		});
		this.drawButton(3, 3, yesButton);
		this.drawButton(5, 3, noButton);

		this.drawAdditionalButtons(player);

	}

	protected abstract void drawAdditionalButtons(Player player);

	protected abstract void onYesButtonClick(InventoryGUI inventoryGUI, Player player);

	protected abstract void onNoButtonClick(InventoryGUI inventoryGUI, Player player);

	public abstract long getTimeoutInTicks();

	public abstract ButtonTypEnum whichButtonShouldBeClickedOnTimeout();

	public abstract String get_GUI_Title();

	public abstract Button_Bare get_the_Question_Button();

	public abstract Button_Bare get_YesButton();

	public abstract Button_Bare get_NoButton();

	@Override
	public void onTick(Player player, int Tickcount) {
		if (this.hasTimeout) {
			int amountJA = 1;
			int amountNein = 1;
			if ((timeout - Tickcount) > 19) {
				if (clickYesButtonOnTimeout) {
					amountJA = ((int) (timeout - Tickcount) / 20) + 1;
				} else {
					amountNein = ((int) (timeout - Tickcount) / 20) + 1;
				}
			}
			this.yesButton.setAmount(amountJA);
			this.noButton.setAmount(amountNein);
			this.refresh(player);

			// If timer has run out, click button
			if (Tickcount >= this.timeout) {
				if (this.clickYesButtonOnTimeout) {
					this.onYesButtonClick(this, player);
				} else {
					this.onNoButtonClick(this, player);
				}
				timeout = -1;
			}
		}
	}
}
