package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_AuctionHistory extends Button{  
    
  private boolean createNewAuctionButton;
  private Integer worldGroup;
  private boolean showRunningAuctions;
    public Button_AuctionHistory(boolean createNewAuctionButton, Integer worldGroup, boolean showRunningAuctions) {
    super(new ItemStack(Material.ENCHANTED_BOOK),GlobalChestShop.text.get(GlobalChestShop.text.GUI_ChooseCategory_MyAuction_Button), GlobalChestShop.text.get(GlobalChestShop.text.GUI_ChooseCategory_MyAuction_Button_DESC));
    this.createNewAuctionButton = createNewAuctionButton;
    this.worldGroup = worldGroup;
    this.showRunningAuctions = showRunningAuctions;
  }

    @Override
    public void onRefresh(InventoryGUI inventoryGUI, Player player) {           
    }
    
    @Override
    public void onButtonClick(InventoryGUI inventoryGUI, Player player,
            ItemStack cursor, ItemStack current, ClickType type,
            InventoryClickEvent event) {
      Visibility visibility;
      if (this.showRunningAuctions){
        visibility = Visibility.AllActive;
      } else {
        visibility = Visibility.AllSold;
      }
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AuctionHistory(inventoryGUI, player.getUniqueId(), visibility, this.showRunningAuctions, createNewAuctionButton, this.worldGroup));
    }
}
