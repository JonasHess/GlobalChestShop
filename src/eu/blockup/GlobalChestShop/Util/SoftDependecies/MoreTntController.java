package eu.blockup.GlobalChestShop.Util.SoftDependecies;

import org.bukkit.inventory.ItemStack;

import com.minelazz.moretnts.MoreTNTs;

public class MoreTntController {

	
	public boolean isItemACustomTntBlock(ItemStack itemStack) {
		return MoreTNTs.isBomb(itemStack);
	}
}