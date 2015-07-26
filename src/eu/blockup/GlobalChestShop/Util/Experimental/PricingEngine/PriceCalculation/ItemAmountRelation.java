package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation;

import org.bukkit.inventory.ItemStack;

public class ItemAmountRelation {
	public ItemStack	itemStack;
	public double		amount;

	public ItemAmountRelation(ItemStack itemStack, double amount) {
		super();
		this.itemStack = itemStack;
		this.amount = amount;
	}
}
