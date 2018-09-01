package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.UUID;

import eu.blockup.GlobalChestShop.Util.Sounds;
import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_ShopDelete extends SimpleIInventoryGUI{

  
  public  Shop shop;
  public GUI_ShopDelete(Shop shop, InventoryGUI parentInventoryGUI) {
    super(shop.isGlobalShop() ? GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Title) : GlobalChestShop.text.get(GlobalChestShop.text.Button_DeleteShop), 6, new ItemStack(Material.SIGN), parentInventoryGUI);
    this.shop = shop;
  }

  
  private void playSoundDeleteShop(Player player) {
	  player.getLocation().getWorld().playSound(player.getLocation(), Sounds.GHAST_SCREAM.bukkitSound(), 1, 1);
  }
  
  
  @Override
  protected void drawButtons(Player player) {
	  
	  // Delete Shop Button
    this.addButton(4, 3, new Button(new ItemStack(XMaterial.RED_WOOL.parseItem()), GlobalChestShop.text.get(GlobalChestShop.text.Button_DeleteShop)) {
      
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
    
    // Discount button
    if ((GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN))) {
    	if (shop.isGlobalShop()) {
    		this.addButton(2, 3, new Button(new ItemStack(Material.GOLDEN_APPLE), "Give discount for this shop") {
				
				@Override
				public void onRefresh(InventoryGUI inventoryGUI, Player player) {
					
				}
				
				@Override
				public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
					new GUI_PriceMultiplierPicker(new PriceMultiplierStateKeeper(shop.getMultiplier()), inventoryGUI, shop).open(player);
					
				}
			});
    	}
    }
    
    // Remove ItemFrame Button
    if (shop.getShopTyp() == EShopTyp.GlobalItemframeShop) {
      this.addButton(6, 3 , new Button(new ItemStack(Material.SIGN), "Remove the sign, but keep the ItemFrame") {
        
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
