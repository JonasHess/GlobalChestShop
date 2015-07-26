package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;


public abstract class Button_AmountChanger extends Button {


  private boolean checkMaxStackSize = true;
  private int minAmount = 1;
  
  public Button_AmountChanger(int startAmount, Button_Bare minimalButton) {
	    super(minimalButton);
	    this.setAmount(startAmount);
	  }

  
  public Button_AmountChanger(int startAmount, ItemStack displayItem, String name, String... description) {
    super(displayItem, name, description);
    this.setAmount(startAmount);
  }



  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    int amount = this.getAmount();
    if (type == ClickType.LEFT) {
      amount = amount + 1;
    } else if (type == ClickType.SHIFT_LEFT) {
      amount = this.getMaxAmount(inventoryGUI, player);
    } else if (type == ClickType.RIGHT) {
      amount = amount - 1;
    } else if (type == ClickType.SHIFT_RIGHT) {
      amount = this.getMinAmount();
    }
    if (this.getCheckMaxStackSize()) {
      int maxAmount = this.getMaxAmount(inventoryGUI, player);
      if (amount > maxAmount) {
        amount = maxAmount;
      }
    }
    if (amount <= 0) {
      amount = 1;
    }
    this.setAmount(amount);
    this.onAmountChange(inventoryGUI, player, amount);
    inventoryGUI.refresh(player);
  }

  public abstract void onAmountChange(InventoryGUI inventoryGUI, Player player, int amount) ;

  public final boolean getCheckMaxStackSize() {
    return this.checkMaxStackSize;
  }

  public final void setMinAmount(int amount) {
    this.minAmount = amount < 1 ? 1 : amount;
  }

  public final int getMinAmount() {
    return this.minAmount;
  }
  
  public abstract int getMaxAmount(InventoryGUI inventoryGUI, Player player);

}
