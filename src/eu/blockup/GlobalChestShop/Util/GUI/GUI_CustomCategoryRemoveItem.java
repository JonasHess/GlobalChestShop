package eu.blockup.GlobalChestShop.Util.GUI;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.CustomCategory;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_CustomCategoryRemoveItem extends SimpleIInventoryGUI{
  
  public CustomCategory customCategory;
  public ItemStack itemStack;


  public GUI_CustomCategoryRemoveItem(CustomCategory customCategory, ItemStack itemStack, InventoryGUI parentInventoryGUI) {
    super("remove item", 6, itemStack, parentInventoryGUI);
    this.customCategory = customCategory;
    this.itemStack = itemStack;
  }

  @Override
  protected void drawButtons(Player player) {
    
    this.addButton(4, 1, new Button_Bare(this.itemStack));
    this.drawButton(4, 3, new Button(new ItemStack(XMaterial.RED_WOOL.parseItem()), "DELETE") {
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        
      }
      
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        ((GUI_CustomCategoryRemoveItem)inventoryGUI).customCategory.removeItem(GlobalChestShop.plugin.getItemController().getInteralIdOfItemStack(itemStack));
        inventoryGUI.returnToParentGUI(player);
      }
    });
  }

}
