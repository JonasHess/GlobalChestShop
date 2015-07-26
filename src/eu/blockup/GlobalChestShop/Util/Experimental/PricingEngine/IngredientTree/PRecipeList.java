package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.IngredientTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PRecipeList {

	public List<PRecipe>	recipies;

	public PRecipeList() {
		this.recipies = new LinkedList<PRecipe>();
	}

	public PRecipe getBestRecipie() {
		boolean random = false;
		if (recipies.size() == 0) {
			return null;
		} else if (recipies.size() == 1) {
			return recipies.get(0);
		}
		List<PRecipe> listWithPossibleRecipies = this.getListWithPossiblePecipies();
		if (!random) {
			return listWithPossibleRecipies.get(0);
		} else {
			int listSize = listWithPossibleRecipies.size();
			int randomNum = (int) (Math.random() * listSize);
			return listWithPossibleRecipies.get(randomNum);
		}
	}

	public int getAmountOfEvilRecipies() {
		int result = 0;
		for (PRecipe r : this.recipies) {
			if (r.doesTreeContainBackloop()) {
				result++;
			}
		}
		return result;
	}

	private List<PRecipe> getListWithPossiblePecipies() {
		List<PRecipe> result = null;
		System.out.println(" There are : " + this.recipies.size() + " possible Recipies"); // TODO
		for (PRecipe r : this.recipies) {
			if (!r.doesTreeContainBackloop()) {
				if (result == null) {
					result = new ArrayList<PRecipe>(this.recipies.size());
				}
				result.add(r);
			}
		}
		if (result == null) {
			result =  this.recipies;
		}
		System.out.println("Resultierende List size: " + result.size());
		return result;
	}
}