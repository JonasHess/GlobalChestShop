package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.Recipies;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeController {
	
	class RecipieListRelation {
		private List<EvaluatedRecipe> value;
		private ItemStack key;
		
		public RecipieListRelation(List<EvaluatedRecipe> value, ItemStack key) {
			this.value = value;
			this.key = key;
		}
	}
	
	
	private List<RecipieListRelation> cacheList;
	
	public RecipeController() {
		this.cacheList = new ArrayList<RecipieListRelation>(20);
	}

	
	public List<EvaluatedRecipe> findRecipesOfItem(ItemStack searchItem) {
		for (RecipieListRelation cache : this.cacheList) {
			if (cache.key.isSimilar(searchItem)) {
				System.out.println("CACHE USED!");
				return cache.value;
			}
		}
		List<EvaluatedRecipe> resultList = this.generateNewRecipesOfItem(searchItem);
		this.cacheList.add(new RecipieListRelation(resultList, searchItem));
		return resultList;
	}
	
	private List<EvaluatedRecipe> generateNewRecipesOfItem(ItemStack searchItem) {
		List<EvaluatedRecipe> result = new LinkedList<EvaluatedRecipe>();
		ItemStack searchItem_Changed = searchItem.clone();
		searchItem_Changed.setAmount(1);
		List<Recipe> recipies = Bukkit.getServer().getRecipesFor(
				searchItem_Changed);
		EvaluatedRecipe tempEvaluatedRecepie;

		for (Recipe recipe : recipies) {

			// ShapedRecipe
			if (recipe instanceof ShapedRecipe) {
				tempEvaluatedRecepie = (new EvaluatedRecipe("ShapedRecipe",
						recipe.getResult()));
				Map<Character, ItemStack> itemMap = ((ShapedRecipe) recipe)
						.getIngredientMap();
				for (Character key : itemMap.keySet()) {
					if (itemMap.get(key) != null) {
						tempEvaluatedRecepie.addImputItem(itemMap.get(key));
					}
				}
				itemMap.clear();
				result.add(tempEvaluatedRecepie);
				tempEvaluatedRecepie = null;
			}

			// FurnaceRecipe
			if (recipe instanceof FurnaceRecipe) {
				tempEvaluatedRecepie = (new EvaluatedRecipe("FurnaceRecipe",
						recipe.getResult()));
				tempEvaluatedRecepie.addImputItem(((FurnaceRecipe) recipe)
						.getInput());
				result.add(tempEvaluatedRecepie);
				tempEvaluatedRecepie = null;
			}

			// ShapelessRecipe
			if (recipe instanceof ShapelessRecipe) {
				tempEvaluatedRecepie = (new EvaluatedRecipe("FurnaceRecipe",
						recipe.getResult()));
				List<ItemStack> itemlist = ((ShapelessRecipe) recipe)
						.getIngredientList();
				for (ItemStack listelement : itemlist) {
					if (listelement.clone() != null) {
						tempEvaluatedRecepie.addImputItem(listelement.clone());
					}
				}
				result.add(tempEvaluatedRecepie);
				tempEvaluatedRecepie = null;
			}
		}
		return result;
	}
	
}
