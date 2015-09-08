package eu.blockup.GlobalChestShop.Util.Shops;

import java.util.List;

import org.bukkit.Location;


public abstract class SpawnAbleShop extends ShopBase{
	
	private List<ShopManifest> shopManifests;
	
	public SpawnAbleShop(Location debugLocation, List<ShopManifest> shopManifests) {
		super(debugLocation);
		this.shopManifests = shopManifests;
	}

	@Override
	public void spawn() throws CorupWorldDataException{
		for (ShopManifest manifest : this.shopManifests){
			manifest.spawn();
		}
	}

	@Override
	public void despawn() throws CorupWorldDataException{
		for (ShopManifest manifest : this.shopManifests){
			manifest.despawn();;
		}
	}
}
