package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.Exceptions.PriceNotInitializedException;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.IngredientTree.IngredientTree;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation.ItemAmountRelation;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation.TreeAnalyser;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.Recipies.RecipeController;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding.GUI_IngedientList;
import eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding.GUI_PriceChanger;
import eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding.PriceTupple;

public class PriceEngine {

	class TreeItemRelation {
		private IngredientTree	value;
		private ItemStack		key;

		public TreeItemRelation(IngredientTree value, ItemStack key) {
			this.value = value;
			this.key = key;
		}
	}

	private List<TreeItemRelation>	cacheList_IngedientTree;
	private RecipeController		recipeController;

	// Konstructor

	public PriceEngine() {
		this.recipeController = new RecipeController();
		this.cacheList_IngedientTree = new ArrayList<TreeItemRelation>(20);
	}

	private IngredientTree getIngredientTreeOfItem(ItemStack searchItem) {
		for (TreeItemRelation cache : this.cacheList_IngedientTree) {
			if (cache.key.isSimilar(searchItem)) {
				System.out.println("TREE CACHE USED!");
				return cache.value;
			}
		}
		IngredientTree resultList = this.generateNewIngredientTree(searchItem);
		this.cacheList_IngedientTree.add(new TreeItemRelation(resultList, searchItem));
		return resultList;
	}

	private IngredientTree generateNewIngredientTree(ItemStack itemStack) {
		return new IngredientTree(itemStack);
	}

	public RecipeController getRecipeController() {
		return recipeController;
	}

	private TreeAnalyser getTreeAnalyserFor(ItemStack itemStack) {
		IngredientTree tree = this.getIngredientTreeOfItem(itemStack);
		return new TreeAnalyser(tree, itemStack);
	}

	public List<ItemStack> getIngedientListOfItem(ItemStack itemStack) {
		return getTreeAnalyserFor(itemStack).getIngedientList();
	}

	public List<ItemAmountRelation> getLeaveListOfItem(ItemStack itemStack) {
		return getTreeAnalyserFor(itemStack).getLeaveList();
	}

	public void openPriceChangeMenuForItem(Player player, InventoryGUI parentGUI, ItemStack itemStack, int worldGroup) {
		if (this.getLeaveListOfItem(itemStack).size() == 1) {
			Auction adminAuction = GlobalChestShop.plugin.getAuctionController(worldGroup).getAdminShopFromItemStack(itemStack);
			if (adminAuction == null) {
				new GUI_PriceChanger(itemStack, new PriceTupple(100.0, 1.0), 1, parentGUI, worldGroup).open(player);
				System.out.println("No Admin Auction "); // TODO
			} else {
				new GUI_PriceChanger(itemStack, new PriceTupple(adminAuction.getShopToPlayerPrice(1, 1.0), adminAuction.getPlayerToShopPrice(1, 1.0)), 1, parentGUI, worldGroup).open(player);
			}
		} else {
			new GUI_IngedientList(itemStack, parentGUI, worldGroup).open(player);
		}
	}

	public double getBuyPriceOfItem(ItemStack itemStack, int amount, int worldGroup) throws PriceNotInitializedException {
		return getTreeAnalyserFor(itemStack).getBuyPrice(worldGroup) * (double) amount;
	}

	public double getSellPriceOfItem(ItemStack itemStack, int amount, int worldGroup) throws PriceNotInitializedException {
		return getTreeAnalyserFor(itemStack).getSellPrice(worldGroup) * (double) amount;
	}

}
