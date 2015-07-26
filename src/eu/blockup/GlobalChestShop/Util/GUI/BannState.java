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

public class BannState extends StateKeeper<Boolean>  {

	private ItemStack item;
	private int worldGroup;
	private boolean inLocalShop;
	

	public BannState(ItemStack item, int worldGroup, boolean inLocalShop) {
		super();
		this.item = item;
		this.worldGroup = worldGroup;
		this.inLocalShop = inLocalShop;
	}

	@Override
	protected void onMofiyState(Boolean offsetValue, Player player) {
		this.onSetState(offsetValue, player);
	}

	@Override
	protected void onSetState(Boolean absoluteValue, Player player) {
		if (getCurrentState(player) != absoluteValue) {
			if (absoluteValue) {
				GlobalChestShop.plugin.getItemController().bannItemFromShops(item, worldGroup, inLocalShop);
			} else {
				GlobalChestShop.plugin.getItemController().unBannItemFromShops(item, worldGroup, inLocalShop);
			}
		}
	}

	@Override
	public Boolean getCurrentState(Player player) {
		return GlobalChestShop.plugin.getItemController().isItemBannedFromShops(item, worldGroup, inLocalShop);
	}

	@Override
	public Button_Bare formatToDisplayButton(Player player, int amount) {
		ItemStack displayItem;
		if (this.getCurrentState(player)) {
			displayItem = new ItemStack(Material.WOOL, 1, (short) 14);
		} else {
			displayItem = new  ItemStack(Material.WOOL, 1, (short) 5);
		}
		return  new Button_Bare(displayItem, (this.inLocalShop ? "Allow creating acutions?" : "Show in GlobalShop?" ), this.stateToString(1).get(0));
	}

	@Override
	public List<String> stateToString(int amount) {
		List<String> list = new ArrayList<String>(1);
		String suffix;
		if (this.getCurrentState(null)) { 
			suffix = ChatColor.RED + "false";
		} else {
			suffix = ChatColor.GREEN + "true";
		}
		ChatColor color = ChatColor.AQUA;
		if (this.inLocalShop) {
			list.add( color + "Allow new auctions with this item: " + suffix);
		} else {
			list.add(color + "Show item in GlobalShop: " + suffix);
		}
		return list;
	}
	
}