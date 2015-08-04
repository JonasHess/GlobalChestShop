package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;

public abstract class GUI_StateCangerDouble extends GUI_StateChangerNumeric<Double> {

	protected class Button_changeDoubleState extends Button_changeStateNumeric<Double> {

		public Button_changeDoubleState(ItemStack displayItem, StateKeeper<Double> valueObject, Double value, ModifyTyp modifyType, int amount, double decimalShifter, int resetButtonType) {
			super(value * decimalShifter, modifyType, valueObject, amount, displayItem, (value > -0.0 ? "+ " : "- ") + Double.toString(Math.abs(value * decimalShifter)));
			if (value == 0) {
				if (resetButtonType == 1) {
//					this.setTitle(" 0 " + ChatColor.GREEN + "(FREE)");
					this.setTitle(GlobalChestShop.plugin.formatPrice(0.0, true));
				} else if (resetButtonType == 2) {
					this.setTitle(" 0 " + ChatColor.GREEN + " No discount");
				}
			}
			if (value == -99.0) {
				this.setTitle(ChatColor.RED + "Disable");
			}
		}

		@Override
		public void changeNumericState(Double value, ModifyTyp modifyType, StateKeeper<Double> stateChanger, InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

			if (modifyType == ModifyTyp.addOffsetToValue) {
				valueObject.mofiyState(value, player);
			} else if (modifyType == ModifyTyp.setValue) {
				valueObject.setState(value, player);
			}
		}

		@Override
		public void onRefresh(InventoryGUI inventoryGUI, Player player) {

		}

		@Override
		protected Sound getClickSound(ClickType type) {
			if (this.value < 0)  {
				return Sound.DRINK;
			} else {
				return Sound.EAT;
			}
		}

	}

	private int amount;
	private double decimalShifter;
	private int resetButtonType;
	public GUI_StateCangerDouble(StateKeeper<Double> valueObject, String guiTitle, ItemStack displayItem, InventoryGUI parentGUI, int height, int amount, double decimalShifter, int resetButtonType) {
		super(valueObject, guiTitle, displayItem, parentGUI, height);
		this.amount = amount;
		this.decimalShifter = decimalShifter;
		this.resetButtonType =resetButtonType;
	}

	@Override
	public void drawButtons(Player player) {
		this.drawButton(4, 0, this.getValueObject().formatToDisplayButton(player, amount));

		// +1000
		this.drawButton(0, 2, new Button_changeDoubleState(new ItemStack(Material.EMERALD), this.getValueObject(), +1000.0, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// -1000
		this.drawButton(0, 3, new Button_changeDoubleState(new ItemStack(Material.EMERALD), this.getValueObject(), -1000.0, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// +100
		this.drawButton(1, 2, new Button_changeDoubleState(new ItemStack(Material.DIAMOND), this.getValueObject(), +100.0, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// -100
		this.drawButton(1, 3, new Button_changeDoubleState(new ItemStack(Material.DIAMOND), this.getValueObject(), -100.0, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// +10
		this.drawButton(2, 2, new Button_changeDoubleState(new ItemStack(Material.GOLD_INGOT), this.getValueObject(), +10.0, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// -10
		this.drawButton(2, 3, new Button_changeDoubleState(new ItemStack(Material.GOLD_INGOT), this.getValueObject(), -10.0, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// +1
		this.drawButton(3, 2, new Button_changeDoubleState(new ItemStack(Material.IRON_INGOT), this.getValueObject(), +1.0, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// -1
		this.drawButton(3, 3, new Button_changeDoubleState(new ItemStack(Material.IRON_INGOT), this.getValueObject(), -1.0, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// +0,1
		this.drawButton(4, 2, new Button_changeDoubleState(new ItemStack(Material.REDSTONE), this.getValueObject(), +0.1, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// -0,1
		this.drawButton(4, 3, new Button_changeDoubleState(new ItemStack(Material.REDSTONE), this.getValueObject(), -0.1, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// +0,01
		this.drawButton(5, 2, new Button_changeDoubleState(new ItemStack(Material.COAL), this.getValueObject(), +0.01, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// -0,01
		this.drawButton(5, 3, new Button_changeDoubleState(new ItemStack(Material.COAL), this.getValueObject(), -0.01, ModifyTyp.addOffsetToValue, amount, decimalShifter, resetButtonType));

		// 0 Button
		this.drawButton(6, 3, new Button_changeDoubleState(new ItemStack(Material.DIRT, 0), this.getValueObject(), 0.0, ModifyTyp.setValue, amount, decimalShifter, resetButtonType));

		this.drawAdditionalButtons(player);
	}

	protected abstract void drawAdditionalButtons(Player player);

	@Override
	public void updateOnStateChange(Player player) {
		this.resetAllButtons(player);
		this.refresh(player);

	}

}
