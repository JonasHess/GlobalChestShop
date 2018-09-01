package eu.blockup.GlobalChestShop.Util.GUI;


import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_AdminShopChooseItem extends SimpleIInventoryGUI{
  
  private ItemStack itemStack;
  private final Integer worldGroup;
  
  public GUI_AdminShopChooseItem(InventoryGUI parentInventoryGUI, Integer worldGroup) {
    super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAdminShop_SubmitButton), 6, new ItemStack(Material.ITEM_FRAME), parentInventoryGUI);
    this.worldGroup = worldGroup;
  }

  
  public void setItemStack(ItemStack item) {
    if (item != null && item.getData().getItemType() != Material.AIR) {
      ItemStack newItem = item.clone();
      newItem.setAmount(1);
      this.itemStack = newItem;
    }
  }
  
  

  @Override
  protected void drawButtons(Player player) {
    // Item Holder
    
    ItemStack chousenItem;
    String title = "";
    if (this.itemStack == null) {
      chousenItem = new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseItem());
      title = GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_Holding_Item_Title);
    } else {
      chousenItem = this.itemStack.clone();
      chousenItem.setAmount(1);
    }
    this.drawFrame(4, 3, new ItemStack(Material.STICK));
    this.addButton(4, 3, new Button(chousenItem, title, GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_Holding_Item_DESC)) {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {

      }

      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {
        
     
        GUI_AdminShopChooseItem gui = (GUI_AdminShopChooseItem) inventoryGUI;
        if (cursor == null || cursor.getData().getItemType() == Material.AIR) {
          player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_Holding_Item_ClickMessage));
          return;
        }
        gui.setItemStack(cursor);
        ItemStack tmp = cursor;
        player.setItemOnCursor(new ItemStack(Material.AIR));
        player.getInventory().addItem(tmp);
        inventoryGUI.resetAllButtons(player);
        inventoryGUI.refresh(player);
      }
    });
    
    
    // Submit Button
    if (this.itemStack != null) {
      this.drawButton(getWidth() -1 , getHeight() -1 , new Button(new ItemStack(Material.ARROW), "Ok") {
        
        @Override
        public void onRefresh(InventoryGUI inventoryGUI, Player player) {
          
        }
        
        @Override
        public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
          Auction adminShop = GlobalChestShop.plugin.getAuctionController(worldGroup).getAdminShopFromItemStack(itemStack);
          if (adminShop == null) {
            new GUI_AdminShopCreate(itemStack, inventoryGUI, worldGroup).open(player);
          } else {
            new GUI_AdminShopEdit(adminShop, inventoryGUI, worldGroup).open(player);
          }
        }
      });
      
    }
  }
}
