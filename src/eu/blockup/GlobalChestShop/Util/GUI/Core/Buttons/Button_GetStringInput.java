package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StringInput;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public abstract class Button_GetStringInput extends Button {

  private StringInput stringInput;


  public Button_GetStringInput(Button_Bare minimalButton) {
    this(minimalButton.getDisplayIcon(), minimalButton.getTitle(), minimalButton.getDescription());
  }

  public Button_GetStringInput(ItemStack displayItem, String name, String... description) {
    super(displayItem, name, description);
    final Button_GetStringInput button = this;
    this.stringInput = new StringInput() {

      @Override
      public boolean shouldPlayerBeUnableToMoveWhileWriting() {
        // TODO Auto-generated method stub
        return button.shouldPlayerBeUnableToMoveWhileWriting();
      }

      @Override
      public boolean shouldPlayerBeBlindWhileWriting() {
        // TODO Auto-generated method stub
        return button.shouldPlayerBeBlindWhileWriting();
      }

      @Override
      protected void onTimeout(InventoryGUI prevInventoryGUI, Player player) {
        button.onTimeout(prevInventoryGUI, player);

      }

      @Override
      protected void onPlayerTypedString(InventoryGUI parentGUI, Player player, String value) {
        button.onPlayerTypedString(parentGUI, player, value);

      }

      @Override
      public long getTimeoutInTicks() {
        return button.getTimeoutInTicks();
      }

      @Override
      public String[] getPlayersInstructions(Player p) {
        return button.getPlayersInstructions(p);
      }
    };
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    this.stringInput.start(player);
  }

  public abstract void onPlayerTypedString(InventoryGUI prevInventoryGUI, Player player, String value);

  public abstract void onTimeout(InventoryGUI prevInventoryGUI, Player player);

  public abstract String[] getPlayersInstructions(Player p);

  public abstract long getTimeoutInTicks();

  public abstract boolean shouldPlayerBeBlindWhileWriting();

  protected abstract boolean shouldPlayerBeUnableToMoveWhileWriting();



}
