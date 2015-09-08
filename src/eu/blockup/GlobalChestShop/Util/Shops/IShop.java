package eu.blockup.GlobalChestShop.Util.Shops;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;

public interface IShop{
	
	public  ItemStack getItemStack();
	public String getShopName();
	public String getFirstSignLine();
	public  UUID getOwner();
	public  Integer getOwnerId();
	public  Button getAsButton();
	public void delete() ;
	public Location getDebugLocation();
	public boolean isAdminShop();
	public void spawn() throws CorupWorldDataException;
	public void despawn() throws CorupWorldDataException;
}
