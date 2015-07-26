package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.IngredientTree;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation.TreeAnalyser;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.Recipies.EvaluatedRecipe;

public class Ingredient {
	private PRecipe		parentRecipe;
	private ItemStack	resultingItemStack;
	// private boolean isEvil;
	private boolean		treeContainsBackloop;
	private PRecipeList	recipeList;


	public Ingredient(ItemStack itemStack, PRecipe parentRecipe) {
		this.parentRecipe = parentRecipe;
		resultingItemStack = this.correctDurability(itemStack);
		this.recipeList = null;
		boolean isBackloop = false;
		try {
			if (resultingItemStack.isSimilar(parentRecipe.parentZutat.parentRecipe.parentZutat.resultingItemStack)) {
				isBackloop = true;
				this.informAboutBackloop();
			} else
				isBackloop = false;
		} catch (NullPointerException e) {
			isBackloop = false;
		}
		List<EvaluatedRecipe> evaluatedRecipeList = GlobalChestShop.plugin.getPriceEngine().getRecipeController().findRecipesOfItem(resultingItemStack);
		for (EvaluatedRecipe r : evaluatedRecipeList) {
			List<ItemStack> imputItemList = r.getImputItemList();
			if (!isBackloop) {
				if (recipeList == null) {
					recipeList = new PRecipeList();
				}
				this.recipeList.recipies.add(new PRecipe(imputItemList, this, r.getResultedItem()));
			}
		}
//		// DEBUG
//		Bukkit.getServer().broadcastMessage(ausgabe + " - " + GlobalChestShop.plugin.getItemStackDisplayName(resultingItemStack) + " Dur: " + resultingItemStack.getDurability() + " " + (doesTreeContainBackloop() ? " - evilTree" : "") + " " + (isBackloop ? " - evilNode" : "") + (evaluatedRecipeList.size() == 0 ? " - Blatt" : ""));
	}

	public void analyseTree(TreeAnalyser analysingInterface, double amount) {
		analysingInterface.foundTreeBranch(this.resultingItemStack.clone());
		if (this.isLeave()) {
			System.out.println("Kaufe " + GlobalChestShop.plugin.getItemStackDisplayName(resultingItemStack) + " amount: " + amount);
			analysingInterface.foundTreeLeave(resultingItemStack.clone(), amount);
			return;
		} else {
			PRecipe bestRecipe = recipeList.getBestRecipie();
			bestRecipe.analyseTree(analysingInterface, amount);
		}
	}

	public boolean isLeave() {
		return this.recipeList == null;
	}

	public boolean doesTreeContainBackloop() {
		return treeContainsBackloop;
	}

	public void informAboutBackloop() {
		this.treeContainsBackloop = true;
		if (this.parentRecipe != null) {
			this.parentRecipe.informAboutBackloop();
		}
	}

	private ItemStack correctDurability(ItemStack original) {
		short originalDurab = original.getDurability();
		if (originalDurab == 32767 || originalDurab == 0) {
			ItemStack item0, item1;
			item0 = original.clone();
			item0.setDurability((short) 0);
			int e0, e1;
			item1 = original.clone();
			item1.setDurability((short) 1);

			e0 = GlobalChestShop.plugin.getPriceEngine().getRecipeController().findRecipesOfItem(item0).size();
			e1 = GlobalChestShop.plugin.getPriceEngine().getRecipeController().findRecipesOfItem(item1).size();
			if (originalDurab != 32767 && (e0 + e1) == 0) {
				return original;
			}
			if (e0 >= e1) {
				return item0;
			} else {
				return item1;
			}
		} else {
			return original.clone();
		}
	}

	public ItemStack getResultingItemStack() {
		return resultingItemStack;
	}

}