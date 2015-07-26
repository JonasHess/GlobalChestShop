package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_ShopDelete extends SimpleIInventoryGUI{

  
  public  Shop shop;
  public GUI_ShopDelete(Shop shop, InventoryGUI parentInventoryGUI) {
    super(GlobalChestShop.text.get(GlobalChestShop.text.Button_DeleteShop), 6, new ItemStack(Material.SIGN), parentInventoryGUI);
    this.shop = shop;
  }

  
  private void playSoundDeleteShop(Player player) {
	  player.getLocation().getWorld().playSound(player.getLocation(), Sound.GHAST_SCREAM, 1, 1);
  }
  
  
  @Override
  protected void drawButtons(Player player) {
    this.addButton(4, 1, new Button_Bare(new ItemStack(Material.SIGN), GlobalChestShop.text.get(GlobalChestShop.text.Button_DeleteShop)));
    this.addButton(4, 3, new Button(new ItemStack(Material.WOOL, 1, (short) 14), GlobalChestShop.text.get(GlobalChestShop.text.Button_DeleteShop)) {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        
      }
      
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    	if (shop.getShopTyp() == EShopTyp.LocalChestShop) {
    		UUID owner = shop.getOwnerUUID();
    		if (!(GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN))) {
    			if (owner.compareTo(player.getUniqueId()) != 0) {
    				return;
    			}
    			
	    		if (!GlobalChestShop.plugin.getMainConfig().playersCanDeleteShopThatStillContainAuctions && GlobalChestShop.plugin.getAuctionController(shop.getworldGroup()).getCountOfActiveAuctionsFromPlayer(owner) > 0) {
	    			InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Message_WarningDeleteShopStillHaveAuctions), true, player, inventoryGUI);
	    			return;
	    		}
    		}
    	}
    	playSoundDeleteShop(player);
        shop.delete();
        inventoryGUI.returnToParentGUI(player);
      }
    });
    
    if (shop.getShopTyp() == EShopTyp.GlobalItemframeShop) {
      this.addButton(0, this.getHeight() -1 , new Button(new ItemStack(Material.SIGN), "Only remove the sign") {
        
        @Override
        public void onRefresh(InventoryGUI inventoryGUI, Player player) {
          
        }
        
        @Override
        public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        	GlobalChestShop.plugin.executeTaskSynchronous(new Runnable() {
				
				@Override
				public void run() {
					shop.breakSign();
				}
			});
          
          inventoryGUI.returnToParentGUI(player);
        }
      });
    }
    
  }

}
