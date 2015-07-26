package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.Exceptions.PriceNotInitializedException;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.IngredientTree.IngredientTree;



public class TreeAnalyser {

	private List<ItemStack>				ingedientList;
	private List<ItemAmountRelation>	leaveList;
	private ItemStack					itemStack;

	public TreeAnalyser(IngredientTree tree, ItemStack itemStack) {
		this.ingedientList = new ArrayList<ItemStack>();
		this.leaveList = new ArrayList<ItemAmountRelation>();
		this.itemStack = itemStack;
		tree.analyseTree(this);
	}

	public void foundTreeLeave(ItemStack itemStack, double amount) {
		this.leaveList.add(new ItemAmountRelation(itemStack, amount));
	}

	public void foundTreeBranch(ItemStack itemStack) {
		this.addItemToList(itemStack, ingedientList);
	}

	private void addItemToList(ItemStack itemStack, List<ItemStack> list) {
		if (itemStack.isSimilar(this.itemStack)) {
			return;
		}
		for (ItemStack listElement : list) {
			if (listElement.isSimilar(itemStack)) {
				listElement.setAmount((listElement.getAmount()) + (itemStack.getAmount()));
				return;
			}
		}
		list.add(itemStack);
	}

	private void setAllListItemsAmount(int amount, List<ItemStack> list) {
		for (ItemStack listElement : list) {
			listElement.setAmount(amount);
		}
	}

	public List<ItemStack> getIngedientList() {
		this.setAllListItemsAmount(1, ingedientList);
		return ingedientList;
	}

	public List<ItemAmountRelation> getLeaveList() {
		return leaveList;
	}
	
	private double getPrice(boolean buy, int worldGroup) throws PriceNotInitializedException {
		double result = 0.0;
		for (ItemAmountRelation r : this.leaveList) {
			Auction  adminAuction = GlobalChestShop.plugin.getAuctionController(worldGroup).getAdminShopFromItemStack(r.itemStack);
			if (adminAuction == null) {
				throw new PriceNotInitializedException();
			}
			if (buy) {
				result += adminAuction.getShopToPlayerPrice(worldGroup) * r.amount;
			} else {
				result += adminAuction.getPlayerToShopPrice(worldGroup) * r.amount;
			}
		}
		return result;
	}
	
	public double getBuyPrice (int worldGroup) throws PriceNotInitializedException {
		return this.getPrice(true, worldGroup);
	}
	
	public double getSellPrice (int worldGroup) throws PriceNotInitializedException {
		return this.getPrice(false, worldGroup);
	}

}
