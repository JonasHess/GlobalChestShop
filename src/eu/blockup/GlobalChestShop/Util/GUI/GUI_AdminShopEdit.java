package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_PolarQuestion;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_StateChangerBoolean;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_StateCangerDouble;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion.ButtonTypEnum;

public class GUI_AdminShopEdit extends InventoryGUI {

  private Auction adminShop;
  public StateKeeperPrice buyPrice;
  public StateKeeperPrice sellPrice;
  public StateKeeper<Boolean> booleanState;
  private final Integer worldGroup;


  public boolean arePricesValide(Player player) {
    if (buyPrice.getCurrentState(player) == -1.0)
      return true;
    if (sellPrice.getCurrentState(player) == -1.0)
      return true;
    return (buyPrice.getCurrentState(player) >= sellPrice.getCurrentState(player));
  }

  public GUI_AdminShopEdit(Auction adminAuction,  InventoryGUI parentInventoryGUI, Integer worldGroup) {
    super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Title), 6, adminAuction.getItemStack(1), parentInventoryGUI);
    this.adminShop = adminAuction;
    this.worldGroup = worldGroup;
    if (!adminAuction.isAdminshop())
      throw new RuntimeException("This is not an AdminShop!");
    this.buyPrice = new StateKeeperPrice(adminAuction.getShopToPlayerPrice(1, 1.0), true, true);
    this.sellPrice = new StateKeeperPrice(adminAuction.getPlayerToShopPrice(1, 1.0), false, true);
    this.booleanState = new StateKeeper<Boolean>() {

      private boolean state = true;

      @Override
      protected void onSetState(Boolean absoluteValue, Player player) {
        this.state = absoluteValue;

      }

      @Override
      protected void onMofiyState(Boolean offsetValue, Player player) {
        throw new RuntimeException("Can't modify State of boolean StateKeeper");
      }

      @Override
      public Boolean getCurrentState(Player player) {
        return this.state;
      }

      @Override
      public Button_Bare formatToDisplayButton(Player player, int amount) {
        if (this.state) {
          return new Button_Bare(new ItemStack(XMaterial.LIME_WOOL.parseItem()), GlobalChestShop.text
              .get(GlobalChestShop.text.GUI_AdministrateAdminShop_Toggle_ON), GlobalChestShop.text
              .get(GlobalChestShop.text.GUI_AdministrateAdminShop_Toggle_ON_DESC));
        }
        return new Button_Bare(new ItemStack(XMaterial.RED_WOOL.parseItem()), GlobalChestShop.text
            .get(GlobalChestShop.text.GUI_AdministrateAdminShop_Toggle_OFF), GlobalChestShop.text
            .get(GlobalChestShop.text.GUI_AdministrateAdminShop_Toggle_OFF_DESC));
      }

      @Override
  	public List<String> stateToString(int amount) {
        return null;
      }
    };
    this.booleanState.setState(!adminAuction.isEndent(), null);
  }

  @Override
  protected void drawButtons(Player player) {
    // Display Item
    this.drawButton(4, 1, new Button_Bare(this.getDisplayIcon()));

    // ON OFF Switch
    this.drawButton(this.getWidth() - 1, 4, new Button_StateChangerBoolean(booleanState, player));

    // Buy Price
    this.drawButton(5, 3, new Button() {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        this.setAppearance(((GUI_AdminShopEdit) inventoryGUI).buyPrice.formatToDisplayButton(player, 1).addDescriptionLine(
            GlobalChestShop.text.get(GlobalChestShop.text.GUI_Button_BuyPrice_DESC)));
      }

      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(
            player,
            new GUI_StateCangerDouble(((GUI_AdminShopEdit) inventoryGUI).buyPrice, GlobalChestShop.text
                .get(GlobalChestShop.text.GUI_Button_BuyPrice_Title), new ItemStack(Material.NAME_TAG), inventoryGUI, 4, 1, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1) {

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

    // Sell Price
    this.drawButton(3, 3, new Button() {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        this.setAppearance(((GUI_AdminShopEdit) inventoryGUI).sellPrice.formatToDisplayButton(player, 1).addDescriptionLine(
            GlobalChestShop.text.get(GlobalChestShop.text.GUI_Button_SellPrice__DESC)));
      }

      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(
            player,
            new GUI_StateCangerDouble(((GUI_AdminShopEdit) inventoryGUI).sellPrice, GlobalChestShop.text
                .get(GlobalChestShop.text.GUI_Button_SellPrice_Title), new ItemStack(Material.NAME_TAG), inventoryGUI, 4, 1, GlobalChestShop.plugin.getMainConfig().pricePickerMultiplier, 1) {

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
                this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_changeDoubleState(new ItemStack(XMaterial.RED_WOOL.parseMaterial(), 1,(short)14),
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

    // Leave button
    this.drawButton(0, 0, new Button_PolarQuestion(new ItemStack(XMaterial.LIME_STAINED_GLASS_PANE.parseItem()), GlobalChestShop.text
        .get(GlobalChestShop.text.GUI_AdministrateAdminShop_ExitSave), GlobalChestShop.text
        .get(GlobalChestShop.text.GUI_AdministrateAdminShop_ExitSave_DESC)) {


      @Override
      public ButtonTypEnum whichButtonShouldBeClickedOnTimeout() {
        return ButtonTypEnum.YesButton;
      }

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
      protected void onYesButtonClick(InventoryGUI inventoryGUI, Player player) {
        GUI_AdminShopEdit gui = (GUI_AdminShopEdit) inventoryGUI.getParentGUI();

        if (gui.arePricesValide(player)) {
          gui.adminShop.setPlayerToShopPriceEach(gui.sellPrice.getCurrentState(player));
          gui.adminShop.setShopToPlayerPriceEach(gui.buyPrice.getCurrentState(player));
        } else {
          InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Warning_BuyPriceGreaterSellPrice), true, player, inventoryGUI);
          return;
        }

        if (!gui.booleanState.getCurrentState(player)) {
          gui.adminShop.disableAdminShop();
        } else {
          if (gui.adminShop.isEndent()) {
            GlobalChestShop.plugin.getAuctionController(worldGroup).reenableAdminShop(gui.adminShop);
          }
        }
        if (inventoryGUI.getFirstGUI() instanceof GUI_AdminShopEdit || inventoryGUI.getFirstGUI() instanceof GUI_AdminShopBuy) {
          inventoryGUI.close(player);
          return;
        }
        inventoryGUI.returnToParentGUI(player, 2);
      }

      @Override
      public void onTick(Player player, int tickCount) {

      }

      @Override
      public void onPlayerOpensTheGUI(Player player) {

      }

      @Override
      public void onPlayerLeavesTheGUI(Player player) {

      }

      @Override
      protected void onNoButtonClick(InventoryGUI inventoryGUI, Player player) {
        if (inventoryGUI.getFirstGUI() instanceof GUI_AdminShopEdit || inventoryGUI.getFirstGUI() instanceof GUI_AdminShopBuy) {
          inventoryGUI.close(player);
          return;
        }
        inventoryGUI.returnToParentGUI(player, 2);
      }

      @Override
      public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {

      }

      @Override
      public Button_Bare get_the_Question_Button() {
        return new Button_Bare(new ItemStack(Material.PAPER), GlobalChestShop.text
            .get(GlobalChestShop.text.GUI_AdministrateAdminShop_WQuestion_Save_Changes));
      }

      @Override
      public Button_Bare get_YesButton() {
        return new Button_Bare(new ItemStack(XMaterial.LIME_WOOL.parseItem() ), GlobalChestShop.text.get(GlobalChestShop.text.PolarQuestion_YES));
      }

      @Override
      public Button_Bare get_NoButton() {
        return new Button_Bare(new ItemStack(XMaterial.RED_WOOL.parseItem()), GlobalChestShop.text
            .get(GlobalChestShop.text.PolarQuestion_NO_Leave_NO_SAVE));
      }

      @Override
      public String get_GUI_Title() {
        return GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_WQuestion_Save_Changes);
      }

      @Override
      public long getTimeoutInTicks() {
        return 0;
      }

      @Override
      protected void drawAdditionalButtons(Player player, InventoryGUI inventoryGUI) {

      }
    });

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
    return false;
  }

  @Override
  public boolean shouldReturnButtonBeDrawn() {
    return false;
  }

  @Override
  public boolean shouldCloseButtonBeDrawn() {
    return false;
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
  public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
    
  }


}
