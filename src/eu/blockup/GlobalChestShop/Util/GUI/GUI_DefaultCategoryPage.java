package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.DefaultCategory;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_DefaultCategoryPage extends GUI_PageView<ItemStack> {


  private DefaultCategory category;
  private boolean adminShopsOnly;
  private Integer worldGroup;
  private boolean newAuctions;

  public GUI_DefaultCategoryPage(DefaultCategory category, InventoryGUI parentGUI, boolean onlyAdminShops, Integer worldGroup, boolean newAuctions) {
    super(category.getTitle(), category.getDisplayItem(), 1, parentGUI);
    this.category = category;
    this.adminShopsOnly = onlyAdminShops;
    this.worldGroup = worldGroup;
    this.newAuctions = newAuctions;
  }

  @Override
  public void drawAditionalButtons(Player player) {
    // Display Item
    this.drawButton(4, 0, new Button_Bare(this.getDisplayIcon(), this.getTitle(), ""));
    
    // Create Auction
    if (this.newAuctions) {
      this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_AuctionCreate(this.worldGroup));
      this.drawButton(0, this.getHeight() - 1, new Button_AuctionHistory(true, this.worldGroup, true));
    }
    
    // Create new AdminShop
    if (((!adminShopsOnly && GlobalChestShop.plugin.getMainConfig().showAdminshopsInsideGlobalShops) || adminShopsOnly) && GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
      this.drawButton(this.getWidth() - 2, this.getHeight() - 1, new Button_AdminShopCreate(null, worldGroup));
    }
    
    
  }

  @Override
  public Button convertListObjectToButton(ItemStack obj, Player player) {
    return new Button_AuctionList(obj, this.adminShopsOnly, this.worldGroup, this.newAuctions, true);
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
    if (type == ClickType.SHIFT_LEFT) {
      if (this.adminShopsOnly) {
        GlobalChestShop.plugin.openAdminShopOnlyGUI(this, player, clicked, worldGroup);
      } else {
        GlobalChestShop.plugin.openNormalAuctionGUI(this, player, clicked, worldGroup, true, this.adminShopsOnly);
      }
    }
  }

  @Override
  public List<ItemStack> getRefreshedObjectList() {
    return category.getItemList(GlobalChestShop.plugin.getMainConfig().hideCategoryItemsNotContainingAuctions, worldGroup);
  }

  @Override
  public boolean shouldObjectListBeRefreshedAutomatically() {
    return true;
  }

}
