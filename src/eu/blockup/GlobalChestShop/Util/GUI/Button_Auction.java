package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class Button_Auction extends Button {

  private Auction auction;
  private Integer worldGroup;
  private boolean active = true;
  private boolean showStatus;
  public Button_Auction(Auction auction, boolean active, boolean highlightAdminShops, Integer worldGroup, boolean showStatus) {
    super(auction.getItemStack(auction.getAmount()));
    this.worldGroup = worldGroup;
    this.showStatus = showStatus;
    this.active = active;
    if (highlightAdminShops) {
      if (auction.isAdminshop()) {
        this.setDisplayIcon(new ItemStack(Material.ENDER_CHEST, 1, (short) 0));      
    }
    }
    this.auction = auction;
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    if (!active) return;
    
    
    if (type == ClickType.SHIFT_LEFT) {
    	if (GlobalChestShop.plugin.getMainConfig().allowShiftClicksForQuickBuy && ! auction.isAdminshop() && ! auction.isEndent() && auction.getPlayerStarter().compareTo(player.getUniqueId()) != 0) {
    		GlobalChestShop.plugin.getAuctionController(worldGroup).buyAuction(auction.getAmount(), auction, inventoryGUI, player);
    	}
    	return;
    }
    
    
    boolean DEBUG = false;   // TODO debug modus
    if (DEBUG) {
      player.sendMessage("Debug Modus is activce! please inform the Developer!!!");
    }
    if (auction.isEndent()) {
      if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN) && auction.isAdminshop()) {
        new GUI_AdminShopEdit(auction, inventoryGUI, worldGroup).open(player);
      } else {
        new GUI_AuctionEnded(auction, inventoryGUI, this.worldGroup).open(player);
      }
    } else if (auction.getPlayerStarter().compareTo(player.getUniqueId()) == 0) {
//      if (DEBUG && type == ClickType.SHIFT_LEFT && !auction.isAdminshop()) {
//        new GUI_AuctionBuy(auction, inventoryGUI, this.worldGroup).open(player);
//        return;
//      }
      // Player is the Owner
     new GUI_AuctionDelete(auction, inventoryGUI, this.worldGroup).open(player);
     return;
    } else if (auction.isAdminshop()) {
     new GUI_AdminShopBuy(auction, inventoryGUI, this.worldGroup).open(player);
    } else {
     new GUI_AuctionBuy(auction, inventoryGUI, this.worldGroup).open(player);
    }
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
    
    // Preload informations to reduce Database requests
    List<String> desc = new LinkedList<String>();
    boolean ended = auction.isEndent();
    UUID starter = auction.getPlayerStarter();
    UUID ender = null;
    boolean canceled = false;
    if (ended) {
      ender = auction.getPlayerEnder();
      if (starter.compareTo(ender) == 0) {
        canceled = true;
      }
    }
    boolean auctionAdminShop = auction.isAdminshop();
    int auctionAmount = auction.getAmount();
    this.setAmount(auctionAmount);
    
 // Set Title
    if (auctionAdminShop) {
    	this.setTitle(ChatColor.WHITE + GlobalChestShop.plugin.getItemStackDisplayName(this.auction.getItemStack(1)));
    	desc.add(ChatColor.BLUE +  GlobalChestShop.text.get(GlobalChestShop.text.Auction_Title_AdminShop));
    } else {
    	//
    }
    
    

    desc.add(ChatColor.GOLD + "--------------");
    
    // Status
    if (this.showStatus) {
      
    if (ended) {
      if (canceled) {
        desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Status_Canceled));
      } else {
        desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Status_Ended));
      }
    } else {
      desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Status_Active));
    }
    }
    
    //Amount
    if (!auctionAdminShop || ended) {
      desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Amount, String.valueOf(auctionAmount)));
    }
    
    // Price
    if (!canceled) {
    	
      if (!auctionAdminShop || ended) {
    	  //ShopToPlayerPrice
    	  double shopToPlayerPriceEach = auction.getShopToPlayerPrice(1);
    	  if (shopToPlayerPriceEach >= 0) {
    		  // Price 1
    		  desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceEach, GlobalChestShop.plugin.formatPrice(shopToPlayerPriceEach, true)));
    		  // PriceTotal
    		  if (auctionAmount > 1)
    			  desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceTotal, GlobalChestShop.plugin.formatPrice(shopToPlayerPriceEach * auctionAmount, true)));
    	  }
      } else {
    	//ShopToPlayerPrice
    	  double shopToPlayerPriceEach = auction.getShopToPlayerPrice(1);
    	  if (shopToPlayerPriceEach >= 0) {
    		  // Price 1
    		  desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_BuyPriceEach, GlobalChestShop.plugin.formatPrice(shopToPlayerPriceEach, true)));
    	  }
    	  
          //ShopToPlayerPrice
          double playerToShopPriceEach = auction.getPlayerToShopPrice(1);
          if (auctionAdminShop && playerToShopPriceEach >= 0) {
            // Price 1
            desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_SellPriceEach, GlobalChestShop.plugin.formatPrice(playerToShopPriceEach, true)));
          }
      }
      

      
      
      
      // Owner
      String seller = GlobalChestShop.plugin.getNameOfPlayer(starter);
      String buyer = GlobalChestShop.plugin.getNameOfPlayer(ender);

        // Seller
        desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Seller, seller));

        // Buyer
        if (ended) {
          desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Buyer, buyer));
        }
    }


    
    // Started
    if (!(auctionAdminShop && !ended)) {
      desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Date_Started, GlobalChestShop.plugin.formatDate(auction.getStartDate(), auction.getStartTime())));
    }

    // Ended
    if (ended) {
      desc.add(GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Date_Ended, GlobalChestShop.plugin.formatDate(auction.getEndDate(), auction.getEndTime())));
    }
    desc.add(ChatColor.GOLD + "--------------");
    // Set Description
    this.setDescription(desc);
    
    
  }

}
