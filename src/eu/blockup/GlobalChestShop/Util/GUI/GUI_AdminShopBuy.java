package eu.blockup.GlobalChestShop.Util.GUI;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Interfaces.BuyAbleInterface;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_AdminShopBuy extends GUI_Shop{

	private Auction auction;
	private final Integer worldGroup;
	public Auction getAuction() {
		return auction;
	}

	public GUI_AdminShopBuy(Auction auction, double multiplier,  InventoryGUI parentInventoryGUI, Integer worldGroup) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Title_AdminShop), auction.getItemStack(1), parentInventoryGUI, auction, GlobalChestShop.plugin.getEconomy(), true, true, true, multiplier);
		this.auction = auction;
		this.worldGroup = worldGroup;
		if (auction.getPlayerToShopPrice(1, multiplier) < 0) {
		  this.setSell(false);
		  this.setSellAll(false);
		}
		if (auction.getShopToPlayerPrice(1, multiplier) < 0) {
		  this.setBuy(false);
		}
	}

	/*
	@Override
	protected Button_Bare getBuyButton(BuyAbleInterface buyItem, Economy economy, int amount, Player player) {
	    return buyItem.getBuyButton(amount, player);
	}

	@Override
	protected Button_Bare getSellButton(BuyAbleInterface buyItem, Economy economy, int amount, Player player) {
	    return buyItem.getSellButton(amount, player);
	}
	
	@Override
	protected Button_Bare getSellAllButton(BuyAbleInterface buyItem, Economy economy, int amount, Player player) {
		return buyItem.getSellAllButton(amount, player);
	}
	 
	*/

	@Override
	protected Button_Bare getAmountButton(BuyAbleInterface buyItem, Economy economy) {
		return new Button_Bare(GlobalChestShop.plugin.getMainConfig().getAmountButton(), GlobalChestShop.text.get(GlobalChestShop.text.GUI_BuyAdminShop_Amount_Button), GlobalChestShop.text.get(GlobalChestShop.text.GUI_BuyAdminShop_Amount_Button_DESC));
	}
	
	

	@Override
	protected void drawButtons(Player player) {
		if (player == null) {
			throw new NullPointerException("Player was null");
		}
		super.drawButtons(player);
		// Money Button
        this.drawButton(0, this.getHeight() -1, new Button_Money(player.getUniqueId()));
        
        //Disable AdminShop
        if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN))  {
          this.drawButton(this.getWidth() -1 , this.getHeight() -2, new Button(new ItemStack(Material.ENDER_CHEST), "Edit AdminShop") {
            
            @Override
            public void onRefresh(InventoryGUI inventoryGUI, Player player) {
              
            }
            
            @Override
            public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
              new GUI_AdminShopEdit(((GUI_AdminShopBuy)inventoryGUI).auction, inventoryGUI, worldGroup).open(player);
            }
          });
        }
	}

	@Override
	protected void afterBuy(Player player) {
		
	}

	@Override
	protected void afterSell(Player player) {
		
	}

	@Override
	public boolean shouldBackgroundBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldEscKeyClosesTheGUI() {
		return false;
	}

	@Override
	public boolean shouldEscKeyLeadsToPreviousGUI() {
		return true;
	}

	@Override
	public boolean shouldReturnButtonBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldCloseButtonBeDrawn() {
		return true;
	}

	@Override
	public void onPlayerOpensTheGUI(Player player) {
		
	}

	@Override
	public void onPlayerLeavesTheGUI(Player player) {
		
	}

  @Override
  public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
    this.redrawAllButtons(player);
    this.refresh(player);
  }


	

}
