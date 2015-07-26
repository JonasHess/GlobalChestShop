package eu.blockup.GlobalChestShop.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;

public class DefaultCategory {

	private ItemStack		displayItem;
	private String			title;
	private List<ItemStack>	itemList;
	private boolean			allwaysShowAllItems;

	public DefaultCategory(ItemStack displayItem, String title, boolean allwaysShowAllItems) {
		super();
		this.allwaysShowAllItems = allwaysShowAllItems;
		this.displayItem = displayItem;
		this.title = title;
		this.itemList = new LinkedList<ItemStack>();
	}

	public Button_Bare toDisplayButton() {
		return new Button_Bare(this.displayItem, this.title);
	}

	public List<ItemStack> getItemList(boolean hideCategoryItemsNotContainingAuctions, int worldGroup) {
		this.sortList();
		if (hideCategoryItemsNotContainingAuctions && !allwaysShowAllItems) {
			List<ItemStack> resultList = new ArrayList<ItemStack>(this.itemList.size());
			synchronized (itemList) {
				for (ItemStack item : itemList) {
					if (GlobalChestShop.plugin.getAuctionController(worldGroup).getAllActiveAuctionForItemStack(item, false).size() != 0) {
						resultList.add(item);
					}
				}
			}
			return resultList;
		} else {
			return this.itemList;
		}
	}

	public void setItemList(List<ItemStack> itemList) {
		this.itemList = itemList;
	}

	public void addItem(ItemStack item) {
		if (!this.itemList.contains(item)) {
			itemList.add(item);
		}
	}

	public void sortList() {
		Collections.sort(this.itemList, new Comparator<ItemStack>() {
			@SuppressWarnings("deprecation")
			@Override
			public int compare(ItemStack arg0, ItemStack arg1) {

				try {
					int itemid0 = arg0.getTypeId();
					int itemid1 = arg1.getTypeId();
					byte damage0 = arg0.getData().getData();
					byte damage1 = arg1.getData().getData();
					if (itemid0 == itemid1)
						return (int) (damage0 - damage1);
					return (itemid0 - itemid1);
				} catch (Exception e) {
					return 1;
				}
			}
		});

	}

	public ItemStack getDisplayItem() {
		return displayItem;
	}

	public void setDisplayItem(ItemStack displayItem) {
		this.displayItem = displayItem;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
