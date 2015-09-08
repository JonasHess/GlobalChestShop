package eu.blockup.GlobalChestShop.Util.Shops;

import java.util.UUID;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public abstract class ShopBase implements IShop, IClickable{

	
	
	public ShopBase(Location debugLocation) {
		super();
		this.debugLocation = debugLocation;
	}

	private Location debugLocation;
	
	@Override
	public Location getDebugLocation() {
		return this.debugLocation;
	}

	@Override
	public UUID getOwner() {
		return GlobalChestShop.plugin.getPlayerController().getUuidFromPlayerID(this.getOwnerId());
	}

	@Override
	public Button getAsButton() {
		return new Button(getItemStack(), getShopName(), getFirstSignLine()) {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType clickType, InventoryClickEvent event) {
				if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
					onInteractLeftClick(player, inventoryGUI);
				} else if (clickType == ClickType.RIGHT) {
					onInteractRightClick(player, inventoryGUI);
				}
			}
		};
	}

	@Override
	public void delete() {
		throw new NotImplementedException();		
	}	
	
	public boolean isAdminShop() {
		return (this.getOwner().equals(GlobalChestShop.plugin.getAdminShopUUID()));
	}
}
