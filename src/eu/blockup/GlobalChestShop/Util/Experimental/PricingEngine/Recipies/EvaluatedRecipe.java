package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.Recipies;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class EvaluatedRecipe {
	private ItemStack resultedItem;
	private List<ItemStack> imputItemList;
	private String typeOfRecipie;

	public EvaluatedRecipe(String typeOfRecipie, ItemStack resultedItem) {
		this.imputItemList = new ArrayList<ItemStack>();
		this.resultedItem = resultedItem;
		this.typeOfRecipie = typeOfRecipie;
	}
	

	public void addImputItem(ItemStack itemStack) {
		for (ItemStack listElement : this.imputItemList) {
			if (listElement.isSimilar(itemStack)) {
				listElement.setAmount((listElement.getAmount())
						+ (itemStack.getAmount()));
				return;
			}
		}
		this.imputItemList.add(itemStack);
	}

	public int getAmount_of_ResultItem() {
		return this.resultedItem.getAmount();
	}

	public List<ItemStack> getImputItemList() {
		return imputItemList;
	}

	public void setImputItemList(List<ItemStack> imputItemList) {
		this.imputItemList = imputItemList;
	}

	public ItemStack getResultedItem() {
		return resultedItem;
	}

	public void setResultedItem(ItemStack resultedItem) {
		this.resultedItem = resultedItem;
	}

	public String getTypeOfRecipie() {
		return typeOfRecipie;
	}

	public void setTypeOfRecipie(String typeOfRecipie) {
		this.typeOfRecipie = typeOfRecipie;
	}
}