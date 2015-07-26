package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.entity.Player;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_AuctionEnded extends SimpleIInventoryGUI{

	private Auction auction;
	private Integer worldGroup;
	public GUI_AuctionEnded(Auction auction, InventoryGUI parentInventoryGUI, Integer worldGroup) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_EndedAuction_Title), 4, auction.getItemStack(auction.getAmount()), parentInventoryGUI);
		this.auction = auction;
		this.worldGroup = worldGroup;
	}

	@Override
	protected void drawButtons(Player player) {
	
		this.addButton(4, 2, new Button_Auction(auction, false, false, this.worldGroup, true));
//		this.addButton(0, this.getHeight()-1, new Button_VergleicheMitAnderen(auction.getItemStack(1)));
	}

}
