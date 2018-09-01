package eu.blockup.GlobalChestShop.Util.GUI;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_CreateShop2_2 extends SimpleIInventoryGUI{
	
	  public ShopInfoPack infoPack;
	  
	  public GUI_CreateShop2_2(ShopInfoPack infoPack, InventoryGUI parentInventoryGUI) {
	    super("Choose a ShopType", 6, new ItemStack(Material.ENDER_CHEST), parentInventoryGUI);
	    this.infoPack = infoPack;
	  }

	  @Override
	  protected void drawButtons(Player player) {
	    
	    // Info
	    this.drawButton(4, 1, new Button_Bare(new ItemStack(Material.PAPER), "CategoryType", ChatColor.GOLD + "What kind of category", ChatColor.GOLD + "do you want to create?"));
	    
	    
	    
	    // CustomCategory
	    this.drawButton(3, 3, new Button(new ItemStack(XMaterial.CRAFTING_TABLE.parseMaterial()),"Custom Categories") {
	      
	      @Override
	      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
	        
	      }
	      @Override
	      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
	        ShopInfoPack clonePack = infoPack.clone();
	       new GUI_CustomCategoryMain(clonePack, inventoryGUI).open(player);
	      }
	    });
	    
	    // DefaultCategory
	    this.drawButton(5, 3, new Button(new ItemStack(Material.BOOKSHELF),"Predefined Categories") {
	      
	      @Override
	      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
	        
	      }
	      @Override
	      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
	    	  ShopInfoPack clonePack = infoPack.clone();
	        new GUI_CreateShop2_3(inventoryGUI, clonePack).open(player);
	      }
	    });
	    

	  }
	}