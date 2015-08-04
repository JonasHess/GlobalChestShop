package eu.blockup.GlobalChestShop.Util.GUI;

import net.milkbowl.vault.chat.plugins.Chat_bPermissions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects.ButtonEffect_FlashRed;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_StateCangerDouble;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_PriceMultiplierPicker extends GUI_StateCangerDouble {

	private Shop								shop;
	private final PriceMultiplierStateKeeper	priceMultiplier;

	public GUI_PriceMultiplierPicker(PriceMultiplierStateKeeper stateKeeper, InventoryGUI parentGUI, Shop shop) {
		super(stateKeeper, "Discount configuration", new ItemStack(Material.GOLDEN_APPLE), parentGUI, 6, 1, 1.0, 2);
		this.shop = shop;
		this.priceMultiplier = stateKeeper;
	}

	@Override
	protected void drawAdditionalButtons(Player player) {
		this.removeButton(0, 2);
		this.removeButton(0, 3);
		this.removeButton(1, 2);
		this.removeButton(1, 3);
		if (this.getValueObject().getCurrentState(player) >= 50.0) {
			this.removeAllAnimatedButtons();
			for (int i = 2; i < this.getWidth() - 2; i++) {
				this.addAnimatedButton(new ButtonEffect_FlashRed(i, this.getHeight() - 2), new Button_Bare(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 6), ChatColor.RED + "Warning!", "Be careful!", "You are about to give a lot of discount"));
			}
		} else {
			this.removeAllAnimatedButtons();
		}

		this.addButton(this.getWidth() - 1, this.getHeight() - 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), "Save changes") {

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				shop.setMultiplier(priceMultiplier.getEffectiveMultiplierValue());
				player.sendMessage("[GlobalChestShop] " +  ChatColor.DARK_GREEN + "discount saved");
				for (String s : priceMultiplier.stateToString(1)) {
					player.sendMessage("[GlobalChestShop] " + ChatColor.GRAY +  s);
				}
				returnToParentGUI(player);
			}
		});
	}

	@Override
	public void onPlayerOpensTheGUI2(Player player) {

	}

	@Override
	public void onPlayerLeavesTheGUI2(Player player) {

	}

	@Override
	public boolean shouldBackgroundBeDrawn() {
		return false;
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
		return true;
	}

	@Override
	public boolean shouldCloseButtonBeDrawn() {
		return true;
	}

	@Override
	public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

	}

	@Override
	public void onTick(Player player, int tickCount) {

	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

	}

}
