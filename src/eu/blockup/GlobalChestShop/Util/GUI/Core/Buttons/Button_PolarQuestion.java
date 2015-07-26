package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion.ButtonTypEnum;

public abstract class Button_PolarQuestion extends Button{

  

  public Button_PolarQuestion() {
    super();
  }

  public Button_PolarQuestion(Button_Bare button) {
    super(button);
  }

  public Button_PolarQuestion(Button button) {
    super(button);
  }

  public Button_PolarQuestion(ItemStack displayItem, String title, String... description) {
    super(displayItem, title, description);
  }

  public Button_PolarQuestion(ItemStack itemStack) {
    super(itemStack);
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
    
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    
    final Button_PolarQuestion button = this;
    GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_PolarQuestion(this.get_GUI_Title(), inventoryGUI) {
      
      @Override
      public boolean shouldReturnButtonBeDrawn() {
        return button.shouldReturnButtonBeDrawn();
      }
      
      @Override
      public boolean shouldEscKeyLeadsToPreviousGUI() {
        return button.shouldEscKeyLeadsToPreviousGUI();
      }
      
      @Override
      public boolean shouldEscKeyClosesTheGUI() {
        return button.shouldEscKeyClosesTheGUI();
      }
      
      @Override
      public boolean shouldCloseButtonBeDrawn() {
        return button.shouldCloseButtonBeDrawn();
      }
      
      @Override
      public boolean shouldBackgroundBeDrawn() {
        return button.shouldBackgroundBeDrawn();
      }
      
      @Override
      public void onPlayerOpensTheGUI(Player player) {
        button.onPlayerOpensTheGUI(player);
        
      }
      
      @Override
      public void onPlayerLeavesTheGUI(Player player) {
        button.onPlayerLeavesTheGUI(player);
        
      }
      
      @Override
      public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        button.onClickInLowerInventory(player, clicked, cursor, current, type, event);
        
      }

      @Override
      protected void onYesButtonClick(InventoryGUI inventoryGUI, Player player) {
        button.onYesButtonClick(inventoryGUI, player);
        
      }

      @Override
      protected void onNoButtonClick(InventoryGUI inventoryGUI, Player player) {
        button.onNoButtonClick(inventoryGUI, player);
      }

      @Override
      public long getTimeoutInTicks() {
        return button.getTimeoutInTicks();
      }

      @Override
      public ButtonTypEnum whichButtonShouldBeClickedOnTimeout() {
        return button.whichButtonShouldBeClickedOnTimeout();
      }

      @Override
      public String get_GUI_Title() {
        return button.get_GUI_Title();
      }

      @Override
      public Button_Bare get_the_Question_Button() {
        return button.get_the_Question_Button();
      }

      @Override
      public Button_Bare get_YesButton() {
        return button.get_YesButton();
      }

      @Override
      public Button_Bare get_NoButton() {
        return button.get_NoButton();
      }

      @Override
      protected void drawAdditionalButtons(Player player) {
        button.drawAdditionalButtons(player, this);
      }

      @Override
      public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
        
      }
      

    });
    
  }

  protected abstract void onYesButtonClick(InventoryGUI inventoryGUI, Player player);


  protected abstract void onNoButtonClick(InventoryGUI inventoryGUI, Player player);
  
  /**
   * @return amount of tick until one button gets clicked automatic.
   *  Return -1 to disable the timeout.
   */
  public abstract long getTimeoutInTicks();
  
  public abstract ButtonTypEnum whichButtonShouldBeClickedOnTimeout();
  
  public abstract String get_GUI_Title();
  
  public abstract Button_Bare get_the_Question_Button();
  
  public abstract Button_Bare get_YesButton();

  public abstract Button_Bare get_NoButton();

  
  public abstract boolean shouldBackgroundBeDrawn();
  public abstract boolean shouldEscKeyClosesTheGUI();
  public abstract boolean shouldEscKeyLeadsToPreviousGUI();
  public abstract boolean shouldReturnButtonBeDrawn();
  public abstract boolean shouldCloseButtonBeDrawn();

  protected abstract void drawAdditionalButtons(Player player, InventoryGUI inventoryGUI);
  
  public abstract void onPlayerOpensTheGUI(Player player);
  public abstract void onPlayerLeavesTheGUI(Player player);
  public abstract void onTick(Player player, int tickCount);
  abstract public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event);

}
