package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;

public class PriceMultiplierStateKeeper extends StateKeeper<Double> {

	private double	state;

	public PriceMultiplierStateKeeper(double multiplier) {
		super();
//		this.state = multiplier;
		this.state = ((multiplier  -1.0) * 100.0) * -1.0;
	}

	private boolean validateNewState(double state) {
		return (state <= 100.0);
	}

	@Override
	protected void onMofiyState(Double offsetValue, Player player) {
		double newState = this.getCurrentState(player) + offsetValue;
		this.onSetState(newState, player);
	}

	@Override
	protected void onSetState(Double absoluteValue, Player player) {
		if (validateNewState(absoluteValue)) {
			this.state = absoluteValue;
		}
	}

	@Override
	public Double getCurrentState(Player player) {
		return state;
	}

	@Override
	public Button_Bare formatToDisplayButton(Player player, int amount) {
		return new Button_Bare(new ItemStack(Material.GOLDEN_APPLE), "Discount");
	}

	@Override
	public List<String> stateToString(int amount) {
		ArrayList<String> result = new ArrayList<String>(3);
		result.add(ChatColor.AQUA + "Discount: " + (getCurrentState(null) > 0 ? ChatColor.RED : ChatColor.GREEN) + String.valueOf(Math.round(state * 100.0) / 100.0) + "%");
		result.add("");
		result.add(ChatColor.GRAY + "An item in that usually cost " + GlobalChestShop.plugin.formatPriceWithoutColor(100.0, false) + ChatColor.GRAY + " will now sell for " + ChatColor.DARK_PURPLE + GlobalChestShop.plugin.formatPriceWithoutColor(100 * getEffectiveMultiplierValue(), true));
		return result;
	}

	public double getEffectiveMultiplierValue() {
		return ((state * -1.0) / 100.0) + 1.0;
	}

}