package eu.blockup.GlobalChestShop.Util.GUI;

import java.sql.Date;
import java.sql.Time;


import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_StateCangerDouble;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;


public class GUI_AdminShopCreate extends SimpleIInventoryGUI {

  private ItemStack itemStack;
  public StateKeeperPrice buyPrice;
  public StateKeeperPrice sellPrice;
  private Integer worldGroup;

  public GUI_AdminShopCreate(ItemStack displayIcon, InventoryGUI parentInventoryGUI, Integer worldGroup) {
    super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAdminShop_Title), 6, displayIcon, parentInventoryGUI);
    this.itemStack = displayIcon;
    this.buyPrice = new StateKeeperPrice(100, true, true);
    this.sellPrice = new StateKeeperPrice(10, false, true);
    this.worldGroup = worldGroup;
  }


  public void submitAdminShop(Player player) {
    // GlobalChestShop.plugin.auctionVerwaltung.addAuction(new Auction(this.itemStack, 1,
    // this.sellPrice.getCurrentState(player), this.buyPrice.getCurrentState(player), true,
    // GlobalChestShop.plugin.adminShopUUID, null, 1));
    if (sellPrice.getCurrentState(player) != -1 && buyPrice.getCurrentState(player) != -1) {
      if (sellPrice.getCurrentState(player) > buyPrice.getCurrentState(player)) {
        InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Warning_BuyPriceGreaterSellPrice), true, player, this);
        return;
      }

    }
    Auction.createNewAuction(this.itemStack, 1, this.sellPrice.getCurrentState(player), this.buyPrice.getCurrentState(player),
        GlobalChestShop.plugin.adminShopUUID, null, false, new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), null, null,
        true, this.worldGroup);
    GlobalChestShop.plugin.getDefaultCategoryController(this.worldGroup).addItemToProtfolio(this.itemStack);
    this.returnToParentGUI(player);
  }

  @Override
  protected void drawButtons(Player player) {
    this.drawButton(4, 1, new Button_Bare(itemStack));

    // Sell Price
    this.drawButton(3, 3, new Button(sellPrice.formatToDisplayButton(player, 1)) {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {

      }

      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {

        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(
            player,
            new GUI_StateCangerDouble(((GUI_AdminShopCreate) inventoryGUI).sellPrice, GlobalChestShop.text
                .get(GlobalChestShop.text.GUI_CreateAdminShop_SetSellPrice_Title), itemStack, inventoryGUI, 4, 1, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1) {

              @Override
              public boolean shouldReturnButtonBeDrawn() {
                return true;
              }

              @Override
              public boolean shouldEscKeyLeadsToPreviousGUI() {
                return true;
              }

              @Override
              public boolean shouldEscKeyClosesTheGUI() {
                return false;
              }

              @Override
              public boolean shouldCloseButtonBeDrawn() {
                return false;
              }

              @Override
              public boolean shouldBackgroundBeDrawn() {
                return true;
              }

              @Override
              public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type,
                  InventoryClickEvent event) {

              }

              @Override
              public void onPlayerOpensTheGUI2(Player player) {

              }

              @Override
              public void onPlayerLeavesTheGUI2(Player player) {

              }

              @Override
              protected void drawAdditionalButtons(Player player) {
             // -1.0 Button
                this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_changeDoubleState(new ItemStack(XMaterial.RED_WOOL.parseMaterial(), 1, (short)14),
                    this.getValueObject(), -99.0D, ModifyTyp.setValue, 1, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1));
              }

              @Override
              public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {

              }

              @Override
              public void onTick(Player player, int tickCount) {
                
              }
            });
      }
    });

    // Buy Prce
    this.drawButton(5, 3, new Button(this.buyPrice.formatToDisplayButton(player, 1)) {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {

      }

      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {

        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(
            player,
            new GUI_StateCangerDouble(((GUI_AdminShopCreate) inventoryGUI).buyPrice, GlobalChestShop.text
                .get(GlobalChestShop.text.GUI_CreateAdminShop_SetBuyPrice_Title), itemStack, inventoryGUI, 4, 1, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1) {

              @Override
              public boolean shouldReturnButtonBeDrawn() {
                return true;
              }

              @Override
              public boolean shouldEscKeyLeadsToPreviousGUI() {
                return true;
              }

              @Override
              public boolean shouldEscKeyClosesTheGUI() {
                return false;
              }

              @Override
              public boolean shouldCloseButtonBeDrawn() {
                return false;
              }

              @Override
              public boolean shouldBackgroundBeDrawn() {
                return true;
              }

              @Override
              public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type,
                  InventoryClickEvent event) {

              }

              @Override
              public void onPlayerOpensTheGUI2(Player player) {

              }

              @Override
              public void onPlayerLeavesTheGUI2(Player player) {

              }

              @Override
              protected void drawAdditionalButtons(Player player) {
             // -1.0 Button
                this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_changeDoubleState(new ItemStack(XMaterial.RED_WOOL.parseMaterial(), 1, (short)14),
                    this.getValueObject(), -99.0D, ModifyTyp.setValue, 1, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1));
              }

              @Override
              public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
                
              }

              @Override
              public void onTick(Player player, int tickCount) {
                
              }
            });
      }
    });

    // Submit
    this.drawButton(getWidth() - 1, getHeight() - 1, new Button(new ItemStack(XMaterial.LIME_STAINED_GLASS_PANE.parseItem()), ChatColor.GREEN + "Create AdminShop") {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {

      }

      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {
        ((GUI_AdminShopCreate) inventoryGUI).submitAdminShop(player);
      }
    });

  }


  @Override
  public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
    this.redrawAllButtons(player);
  }






}
