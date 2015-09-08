package eu.blockup.GlobalChestShop.Util.Shops;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public abstract class ShopManifest {
	
	private IClickable shop;
	private String shopType;
	private String shopName;
	private ItemStack displayItem;
	

	
	public ShopManifest(IClickable shop, String shopType, String shopName, ItemStack displayItem) {
		super();
		this.shop = shop;
		this.shopType = shopType;
		this.shopName = shopName;
		this.displayItem = displayItem;
	}
	
	// Abstract Methods
	public abstract void spawn() throws CorupWorldDataException;
	public abstract void despawn() throws CorupWorldDataException;
	public abstract void remove();
	
	
	// Normal Methodes();
	
	
	public void respawn() throws CorupWorldDataException{
		this.despawn();
		this.spawn();
	}
	
	
	// Getter - Setter
	
	protected ItemStack getDisplayItemStack() {
		return this.displayItem;
	}
	
	protected String getShopName() {
		return shopName;
	}
	protected String getShopType() {
		return shopType;
	}

	protected IClickable getShop() {
		return shop;
	}

	
}
