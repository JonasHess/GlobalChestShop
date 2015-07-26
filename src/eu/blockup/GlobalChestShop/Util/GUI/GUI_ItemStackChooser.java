package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

// TODO Ausarbeiten und in AuctionCreate einbinden!

public class GUI_ItemStackChooser extends SimpleIInventoryGUI{
  private ItemStack itemStack;
  public ShopInfoPack infoPack;


  public GUI_ItemStackChooser(ShopInfoPack infoPack, InventoryGUI parentInventoryGUI) {
    super("choose a item", 6, new ItemStack(Material.ARROW), parentInventoryGUI);
    this.infoPack = infoPack;
  }
  
  public void setItemStack(ItemStack item, Player player) {
    if (item != null && item.getData().getItemType() != Material.AIR) {
      ItemStack newItem = item.clone();
      newItem.setAmount(1);
      this.itemStack = newItem;
      if (player != null) {
        this.redrawAllButtons(player);
        this.refresh(player);
      }
    }
  }
  
  

  @Override
  protected void drawButtons(Player player) {
    // Item Holder
    
    ItemStack chousenItem;
    String title = "";
    if (this.itemStack == null) {
      chousenItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
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
        
     
        GUI_ItemStackChooser gui = (GUI_ItemStackChooser) inventoryGUI;
        if (cursor == null || cursor.getData().getItemType() == Material.AIR) {
          player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_Holding_Item_ClickMessage));
          return;
        }
        gui.setItemStack(cursor, player);
        ItemStack tmp = cursor;
        player.setItemOnCursor(new ItemStack(Material.AIR));
        player.getInventory().addItem(tmp);

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
          ShopInfoPack infoPack = ((GUI_ItemStackChooser)inventoryGUI).infoPack.clone();
          infoPack.setItemStack(((GUI_ItemStackChooser)inventoryGUI).getItemStack());
          GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop3(infoPack, inventoryGUI));
        }
      });
      
    }
  }

  @Override
  public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    if (type == ClickType.SHIFT_LEFT) {
      this.setItemStack(clicked, player);
    }
  }
  

  public ItemStack getItemStack() {
    return itemStack;
  }
}
