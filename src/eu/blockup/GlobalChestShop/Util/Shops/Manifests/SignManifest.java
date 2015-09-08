package eu.blockup.GlobalChestShop.Util.Shops.Manifests;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.SignNotFoundException;
import eu.blockup.GlobalChestShop.Util.Shops.CorupWorldDataException;
import eu.blockup.GlobalChestShop.Util.Shops.IClickable;
import eu.blockup.GlobalChestShop.Util.Shops.ShopManifest;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;

public class SignManifest extends ShopManifest{

	
	private Location signLocation;
	private boolean supressExceptions;
	

	

	public SignManifest(IClickable shop, String shopType, String shopName, ItemStack displayItem, Location signLocation, boolean supressExceptions) {
		super(shop, shopType, shopName, displayItem);
		this.signLocation = signLocation;
		this.supressExceptions = supressExceptions;
	}




	@Override
	public void spawn() throws CorupWorldDataException {
		this.updateSignText(true);
	}


	@Override
	public void despawn() throws CorupWorldDataException {
		this.updateSignText(false);
	}
	
	@Override
	public void remove() {
		throw new NotImplementedException();
		
	}
	
	private void updateSignText(final boolean enabled) throws CorupWorldDataException {

		try {
			final Sign sign = (Sign) signLocation.getBlock().getState();

			GlobalChestShop.plugin.executeTaskSynchronous(new Runnable() {
					@Override
					public void run() {
							sign.setLine(0, getFirstSignLine(enabled));
							sign.setLine(1, getSecondSignLine(enabled));
							sign.update();
						}
	
					private String getFirstSignLine(boolean enabled) {
						if (enabled) {
							return getShopType();
						} else {
							return ChatColor.RED + "[Shop]"; 
						}
					}
	
					private String getSecondSignLine(boolean enabled) {
						if (enabled) {
							return getShopName();
						} else {
							return ChatColor.RED + "DISABLED";
						}
					}
				}
			);
		} catch (Exception e) {
			if ( ! supressExceptions) {
				throw new CorupWorldDataException(signLocation, "SIGN");
			}
		}
	}











}
