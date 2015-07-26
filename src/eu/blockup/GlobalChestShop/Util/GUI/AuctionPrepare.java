package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;


public class AuctionPrepare {
	private StateKeeper<Double> priceObject;
	private List<ItemStack> itemStackList;
	
	
	public AuctionPrepare() {
		super();
		this.itemStackList = new ArrayList<ItemStack>();
	}

	
	public void addItemStack(ItemStack itemStack) {
		if (itemStack == null) {
			throw new NullPointerException();
		}
		this.itemStackList.add(itemStack);
	}
	
	

	public List<ItemStack> getItemStackList() {
		return itemStackList;
	}


	public StateKeeper<Double> getPriceObject() {
		return priceObject;
	}


	public void setPriceObject(StateKeeperPrice priceObject) {
		this.priceObject = priceObject;
	}


	public void clearItemList() {
		this.itemStackList.clear();
	}
	public int getTotalAmount() {
		int result = 0;
		for (ItemStack item : this.itemStackList) {
			result += item.getAmount();
		}
		return result;
	}
	
	public ItemStack getDisplayItem() {
		if (this.itemStackList.isEmpty()) {
			return new ItemStack (Material.STAINED_GLASS_PANE, 1, (short) 8);
		} else {
			ItemStack item = this.itemStackList.get(0).clone();
			item.setAmount(1);
			return item;
			
		}
		
	}
}
