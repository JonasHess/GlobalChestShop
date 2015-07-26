package eu.blockup.GlobalChestShop.Util.GUI;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_CreateShop2_1  extends SimpleIInventoryGUI{
  public ShopInfoPack infoPack;
  public GUI_CreateShop2_1(ShopInfoPack infoPack, InventoryGUI parentInventoryGUI) {
    super("Choose a ShopType", 6, new ItemStack(Material.ENDER_CHEST), parentInventoryGUI);
    this.infoPack = infoPack;
  }

  @Override
  protected void drawButtons(Player player) {
    
    // Info
    this.drawButton(4, 1, new Button_Bare(new ItemStack(Material.PAPER), "ShopyType", ChatColor.GOLD + "How do you want the shop to be layed out?"));
    
    
    //Creative Categories
    this.drawButton(2, 3, new Button(new ItemStack(Material.DIAMOND_BLOCK),"Like the vanilla creative menu.") {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        
      }
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop2_1)inventoryGUI).infoPack.clone();
        infoPack.setAppearance(0);
        new GUI_CreateShop3(infoPack, inventoryGUI).open(player);
      }
    });
    
    // Per player
    this.drawButton(4, 3, new Button(new ItemStack(Material.CHEST),"Show all players that have auctions running.") {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        
      }
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop2_1)inventoryGUI).infoPack.clone();
        infoPack.setAppearance(1);
        new GUI_CreateShop4(infoPack, inventoryGUI).open(player);
      }
    });
    
    // All auctions
    this.drawButton(6, 3, new Button(new ItemStack(Material.DIAMOND_PICKAXE),"Show all running auctions at once.") {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        
      }
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop2_1)inventoryGUI).infoPack.clone();
        infoPack.setAppearance(2);
        new GUI_CreateShop4(infoPack, inventoryGUI).open(player);
      }
    });
//    //ItemStack
//    this.drawButton(6, 3, new Button(new ItemStack(Material.APPLE),"By the itemID of each auction") {
//      
//      @Override
//      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
//        
//      }
//      @Override
//      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
//        ShopInfoPack infoPack = ((GUI_CreateShop2_1)inventoryGUI).infoPack.clone();
//        infoPack.setAppearance(2);
//        new GUI_CreateShop3(infoPack, inventoryGUI).open(player);
//      }
//    });
    
    // ItemStack

  }
}