package eu.blockup.GlobalChestShop.Util.GUI;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_CreateShop2_0  extends SimpleIInventoryGUI{
  public ShopInfoPack infoPack;
  public GUI_CreateShop2_0(ShopInfoPack infoPack, InventoryGUI parentInventoryGUI) {
    super("Choose a ShopType", 6, new ItemStack(Material.ENDER_CHEST), parentInventoryGUI);
    this.infoPack = infoPack;
  }

  @Override
  protected void drawButtons(Player player) {
    
    // Info
    this.drawButton(4, 1, new Button_Bare(new ItemStack(Material.PAPER), "ShopyType", ChatColor.GOLD + "how many items do you", ChatColor.GOLD + "want your shop to offer?"));
    
    
    //All Items
    this.drawButton(2, 3, new Button(GlobalChestShop.plugin.getMainConfig().getDisplayItemAllItems(),"all items") {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        
      }
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop2_0)inventoryGUI).infoPack.clone();
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop2_1(infoPack, inventoryGUI));
      }
    });
    // CustomCategory
    this.drawButton(4, 3, new Button(new ItemStack(Material.CHEST),"multiple items") {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        
      }
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop2_0)inventoryGUI).infoPack.clone();
        new GUI_CreateShop2_2(infoPack, inventoryGUI).open(player);;
      }
    });
    //ItemStack
    this.drawButton(6, 3, new Button(new ItemStack(Material.APPLE),"one single item") {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        
      }
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop2_0)inventoryGUI).infoPack.clone();
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_ItemStackChooser(infoPack, inventoryGUI));
      }
    });
    
    // ItemStack

  }
}