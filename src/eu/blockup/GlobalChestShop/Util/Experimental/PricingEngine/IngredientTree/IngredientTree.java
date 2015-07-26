package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.IngredientTree;

import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation.TreeAnalyser;

public class IngredientTree {

	private Ingredient tree;

	public IngredientTree(ItemStack i) {
		ItemStack itemStack = i.clone();
		itemStack.setAmount(1);
		this.tree = new Ingredient(itemStack, null);
	}
	
	public void analyseTree(TreeAnalyser analysingInterface) {
		this.tree.analyseTree(analysingInterface, 1);
	}
	
	

}
