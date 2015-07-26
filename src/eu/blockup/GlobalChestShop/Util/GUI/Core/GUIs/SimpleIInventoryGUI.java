package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;

public abstract class SimpleIInventoryGUI extends InventoryGUI{

  public SimpleIInventoryGUI(String title, int lines, ItemStack displayIcon, InventoryGUI parentInventoryGUI) {
    super(title, lines, displayIcon, parentInventoryGUI);
  }

  public SimpleIInventoryGUI(String title, ItemStack displayItem, InventoryGUI parentInventoryGUI) {
    super(title, displayItem, parentInventoryGUI);
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
  public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
  }
  

}
