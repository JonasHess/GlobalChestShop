package eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding;

import eu.blockup.GlobalChestShop.Util.GUI.StateKeeperPrice;

public class PriceTupple {
	 public StateKeeperPrice buyPrice;
	  public StateKeeperPrice sellPrice;
	  
	  
	public PriceTupple(Double buyPrice, Double sellPrice) {
		super();
	    this.buyPrice = new StateKeeperPrice(buyPrice, true, true);
	    this.sellPrice = new StateKeeperPrice(sellPrice, false, true);
	}
	
	public StateKeeperPrice getBuyPrice() {
		return buyPrice;
	}
	public StateKeeperPrice getSellPrice() {
		return sellPrice;
	}
	  
	  
}
