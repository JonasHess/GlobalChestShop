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
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class StateKeeperPrice extends StateKeeper<Double> {

	private double value = 0;
	private boolean buy;
	private boolean adminShop;

	public StateKeeperPrice(double startValue, boolean buy, boolean adminShop) {
		super();
		this.value = startValue;
		this.adminShop = adminShop;
		this.buy = buy;
	}

	@Override
	protected void onMofiyState(Double offsetValue, Player player) {
		this.onSetState(this.value + offsetValue, player);
	}

	@Override
	protected void onSetState(Double absoluteValue, Player player) {
		if (absoluteValue < 0.0) {
		  if (adminShop) {
		    this.value = -1;
		    player.sendMessage(ChatColor.RED + "DISABLED");
		    return;
		  } else {
		    player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.StateKeeperPrice_PriceCantBeLessThanZero));
		    InventoryGUI gui = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedInventoryGui(player);
		    InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.StateKeeperPrice_PriceCantBeLessThanZero), false, player, gui);
		    return;
		  }
		}
		if (absoluteValue > GlobalChestShop.plugin.getMainConfig().maximalPriceAnAuctionCanSellFor) {
		    return;
		}
		if (this.value < 0.0 && absoluteValue >= 0) {
		  player.sendMessage(ChatColor.GREEN + "ENABLED");
		}
		this.value = absoluteValue;
	}

	@Override
	public Double getCurrentState(Player player) {
		return this.value;
	}

	@Override
	public Button_Bare formatToDisplayButton(Player player, int amount) {
		Button_Bare button =  new Button_Bare( new ItemStack(Material.NAME_TAG));
		if (buy) {
			button.setTitle(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Button_BuyPrice_Title));
		} else {
			button.setTitle(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Button_SellPrice_Title));
		}
		button.setDescription(this.stateToString(amount));
//		if (amount > 1 && !this.adminShop) {
//			button.addDescriptionLine(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceTotal, GlobalChestShop.plugin.formatPrice(this.value * amount, true)));
//		}
		return button;
	}

	@Override
	public List<String> stateToString(int amount) {
		List<String> list = new ArrayList<String>(2);
		list.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Amount, String.valueOf(amount)));
		if (amount > 1) {
			list.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceEach, GlobalChestShop.plugin.formatPrice(this.value, true)));
		}
		list.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceTotal, GlobalChestShop.plugin.formatPrice(this.value * (double) amount, true)));
		return list;
	}

	

}