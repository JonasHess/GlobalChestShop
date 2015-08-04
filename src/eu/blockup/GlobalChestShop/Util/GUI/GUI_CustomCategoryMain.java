package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.CustomCategory;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

class Button_CategoryChoos extends Button {

  private CustomCategory customCategory;

  public Button_CategoryChoos(CustomCategory customCategory) {
    super(GlobalChestShop.plugin.itemController.formatInternalItemIdToItemStack(customCategory.getIconID()), customCategory.getName());
    this.customCategory = customCategory;
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    if (type == ClickType.LEFT) {
      ShopInfoPack infoPack = ((GUI_CustomCategoryMain) inventoryGUI).infoPack;
      infoPack.setCategory(customCategory);
      GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop3(infoPack, inventoryGUI));
    } else {
      GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CustomCategoryDelete(customCategory, inventoryGUI));
    }
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {

  }

}


public class GUI_CustomCategoryMain extends GUI_PageView<CustomCategory> {

  public ShopInfoPack infoPack;

  public GUI_CustomCategoryMain(ShopInfoPack infoPack, InventoryGUI parentGUI) {
    super("choose a category", new ItemStack(Material.COMPASS), 1, parentGUI);
    this.infoPack = infoPack;
  }


  @Override
  public void drawAditionalButtons(Player player) {
    
    
	  // Create new Auction
	  this.drawButton(this.getWidth() -1 , this.getHeight() -1 , new Button(new ItemStack(Material.NETHER_STAR), "create a new category") {
	      @Override
	      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
	        
	      }
	      
	      @Override
	      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
	        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CustomCategoryCreate(inventoryGUI));
	      }
    });
	  
	  
  }



  @Override
  public Button convertListObjectToButton(CustomCategory obj, Player player) {
    return new Button_CategoryChoos(obj);
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

  }


  @Override
  public List<CustomCategory> getRefreshedObjectList() {
    return GlobalChestShop.plugin.getCustomCategoryController().getAllCategories();
  }


  @Override
  public boolean shouldObjectListBeRefreshedAutomatically() {
    return true;
  }

}
