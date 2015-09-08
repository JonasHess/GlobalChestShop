package eu.blockup.GlobalChestShop.Util.Shops;

import org.bukkit.Location;

public class CorupWorldDataException extends Exception{
	static final long	serialVersionUID	= 1L;
	
	public Location exprectedLocation;
	public String expectedObject;
	
	
	public CorupWorldDataException(Location exprectedLocation, String expectedObject) {
		super();
		this.exprectedLocation = exprectedLocation;
		this.expectedObject = expectedObject;
	}

}
