package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.IngredientTree;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation.TreeAnalyser;

public class PRecipe {
	public Ingredient							parentZutat;
	public ItemStack							resultingItemStack;
	private boolean								treeContainsBackloop = false;
	private List<Ingredient>		ingridientList;

	public PRecipe(List<ItemStack> bestandteileListe, Ingredient parentZutat, ItemStack resultingItemStack) {
		ingridientList = new ArrayList<Ingredient>(bestandteileListe.size());
		this.parentZutat = parentZutat;
		this.resultingItemStack = resultingItemStack;
		for (ItemStack i : bestandteileListe) {
			ingridientList.add(new Ingredient(i, this));
		}
	}

	public boolean doesTreeContainBackloop() {
		return treeContainsBackloop;
	}

	public void informAboutBackloop() {
		this.treeContainsBackloop = true;
		if (this.parentZutat != null) {
			this.parentZutat.informAboutBackloop();
		}

	}

	public void analyseTree(TreeAnalyser analysingInterface, double amount) {
		for (Ingredient ingridient : this.ingridientList) {
			double childAmount  = ingridient.getResultingItemStack().getAmount();
			double parentAmount = resultingItemStack.getAmount();
			double percentalItemAmount = amount *( childAmount / parentAmount);
			ingridient.analyseTree(analysingInterface, percentalItemAmount);
		}
	}

}