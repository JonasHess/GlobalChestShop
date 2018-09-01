package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;


import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.CustomCategory;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

class Button_CustomCategoryItem extends Button{
  private Integer worldGroup;
  private final ItemStack itemStack;
  private CustomCategory customCategory;
  private boolean adminShopOnly;
  private boolean newAuctions;
  private double multiplier;
  
  public Button_CustomCategoryItem(ItemStack itemStack, CustomCategory customCategory, boolean adminShopOnly, Integer worldGroup, boolean newAuctions, double multiplier) {
    super(itemStack.clone());
    this.worldGroup = worldGroup;
    this.itemStack = itemStack.clone();
    this.customCategory = customCategory;
    this.adminShopOnly = adminShopOnly;
    this.newAuctions = newAuctions;
    this.multiplier = multiplier;
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
      Integer itemID = GlobalChestShop.plugin.itemController.getInteralIdOfItemStack(this.itemStack);
      if (type == ClickType.SHIFT_LEFT) {
       this.customCategory.swapLeft(itemID);
       inventoryGUI.onPlayerReturnsToThisGUI(player, null);
       inventoryGUI.refresh(player);
       return;
      }
      if (type == ClickType.SHIFT_RIGHT) {
        this.customCategory.swapRight(itemID);
        inventoryGUI.onPlayerReturnsToThisGUI(player, null);
        inventoryGUI.refresh(player);
        return;
      }
      if (type == ClickType.RIGHT) {
        new GUI_CustomCategoryRemoveItem(customCategory, this.itemStack, inventoryGUI).open(player);
        return;
      }
    }
    if (this.adminShopOnly) {
      GlobalChestShop.plugin.openAdminShopOnlyGUI(inventoryGUI, player, itemStack, worldGroup, multiplier);
    } else {
      
      GlobalChestShop.plugin.openNormalAuctionGUI(inventoryGUI, player, itemStack, worldGroup, newAuctions, this.adminShopOnly, multiplier);
    }
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
    if (GlobalChestShop.plugin.getMainConfig().indicateAuctionAmount) {
      this.setAmount(Math.max(GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllActiveAuctionForItemStack(itemStack, this.adminShopOnly).size(), 0));
    } else {
      this.setAmount(1);
    }
  }

}

public class GUI_CustomCategoryPage extends GUI_PageView<Integer> {

  public CustomCategory customCategory;
  private boolean onlyAdminShops;
  private Integer worldGroup;
  private boolean newAuctions;
  private double multiplier;
  private Player player;
  
  public GUI_CustomCategoryPage(CustomCategory customCategory, InventoryGUI parentGUI, Boolean onlyAdminShops, Integer worldGroup, boolean newAuctions, double multiplier, Player player) {
    super(customCategory.getName(), customCategory.getIconItemStack(), 1, parentGUI);
    this.customCategory = customCategory;
    this.onlyAdminShops = onlyAdminShops;
    this.worldGroup = worldGroup;
    this.newAuctions = newAuctions;
    this.multiplier = multiplier;
    this.player = player;
    		
  }





  @Override
  public Button convertListObjectToButton(Integer obj, Player player) {
    return new Button_CustomCategoryItem(GlobalChestShop.plugin.itemController.formatInternalItemIdToItemStack(obj), this.customCategory, this.onlyAdminShops,
        this.worldGroup, newAuctions, multiplier);
  }




  @Override
  public void drawAditionalButtons(Player player) {


    // Display Icon
    this.drawButton(4, 0, new Button_Bare(customCategory.getIconItemStack(), customCategory.getName()));

    // Add Item to Categroy
    if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
      this.drawButton(getWidth() - 1, getHeight() - 1, new Button(new ItemStack(Material.NETHER_STAR), ChatColor.GREEN + "Add item to category") {

        @Override
        public void onRefresh(InventoryGUI inventoryGUI, Player player) {

        }

        @Override
        public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
            InventoryClickEvent event) {
          GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player,
              new GUI_CustomCategoryAddItem(((GUI_CustomCategoryPage) inventoryGUI).customCategory, inventoryGUI));
        }
      });
      if (customCategory.isShownInCreativeMenu() > 0) {
    	  // Is in shown in creative
    	  this.drawButton(getWidth() - 2, getHeight() - 1, new Button(new ItemStack(XMaterial.RED_WOOL.parseItem()), ChatColor.RED + "Hide from the CreativeMenu") {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				customCategory.setShownInCreativeMenu(0);
				inventoryGUI.redrawAllButtons(player);
				inventoryGUI.refresh(player);
				
			}
		});    	  
      } else {
    	  // Is not shown in creative
    	  this.drawButton(getWidth() - 2, getHeight() - 1, new Button(new ItemStack(XMaterial.LIME_WOOL.parseItem()), ChatColor.GREEN + "Show in the CreativeMenu") {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				customCategory.setShownInCreativeMenu(1);
				inventoryGUI.redrawAllButtons(player);
				inventoryGUI.refresh(player);
				
			}
		});   
      }
    }

  }

  @Override
  public List<Integer> getRefreshedObjectList() {
    return this.customCategory.getAllItems(GlobalChestShop.plugin.mainConfig.hideCategoryItemsNotContainingAuctions && ! GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN), worldGroup, onlyAdminShops);
  }
  @Override
  public boolean shouldObjectListBeRefreshedAutomatically() {
    return true;
  }
  @Override
  public boolean shouldBackgroundBeDrawn() {
    return true;
  }

  @Override
  public boolean shouldEscKeyClosesTheGUI() {
    return false;
  }

  @Override
  public boolean shouldEscKeyLeadsToPreviousGUI() {
    return true;
  }
  @Override
  public boolean shouldReturnButtonBeDrawn() {
    return true;
  }

  @Override
  public boolean shouldCloseButtonBeDrawn() {
    return true;
  }
  @Override
  public void onPlayerOpensTheGUI(Player player) {
    
  }
  @Override
  public void onPlayerLeavesTheGUI(Player player) {
    
  }
  @Override
  public void onTick(Player player, int tickCount) {
    
  }
  @Override
  public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
      if (type == ClickType.SHIFT_LEFT) {
        this.customCategory.addItem(GlobalChestShop.plugin.getItemController().getInteralIdOfItemStack(clicked));
		this.reloadObjectList();
		this.draw_ButtonList(player);
        this.redrawAllButtons(player);
        this.refresh(player);
      }
    }
  }

}
