package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_GetStringInput;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;

public abstract class GUI_StateChangerString extends GUI_StateChanger<String>{

  public GUI_StateChangerString(StateKeeper<String> valueObject, String guiTitle, ItemStack displayItem, InventoryGUI parentGUI, int height) {
    super(valueObject, guiTitle, displayItem, parentGUI, height);
  }

  @Override
  public void updateOnStateChange(Player player) {
//    this.resetAllButtons(player);
//    this.refresh(player);
  }



  protected abstract Button_Bare getInputRequest(Player player);
  
  @Override
  protected void drawButtons(Player player) {
    this.drawButton(3, 2, this.getValueObject().formatToDisplayButton(player, 1));
    final GUI_StateChangerString gui = this;
    this.drawButton(5, 2, new Button_GetStringInput(this.getInputRequest(player)) {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public boolean shouldPlayerBeUnableToMoveWhileWriting() {
        // TODO Auto-generated method stub
        return gui.shouldPlayerBeUnableToMoveWhileWriting();
      }
      
      @Override
      public boolean shouldPlayerBeBlindWhileWriting() {
        // TODO Auto-generated method stub
        return gui.shouldPlayerBeBlindWhileWriting();
      }
      
      @Override
      public long getTimeoutInTicks() {
        return gui.getTimeoutInTicks();
      }
      
      @Override
      public String[] getPlayersInstructions(Player p) {
        return gui.getPlayersInstructions(p);
      }

      @Override
      public void onPlayerTypedString(InventoryGUI prevInventoryGUI, Player player, String value) {        
//        gui.onPlayerTypedString((GUI_StateChangerString) prevInventoryGUI, player, value);
        gui.getValueObject().setState(value, player);
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, prevInventoryGUI);
       
      }

      @Override
      public void onTimeout(InventoryGUI prevInventoryGUI, Player player) {
        gui.onTimeout((GUI_StateChangerString) prevInventoryGUI, player);
      }
      
    });
    this.drawAdditionalButtons();
  }

//  public abstract void onPlayerTypedString(GUI_StateChangerString prevInventoryGUI, Player player, String value);

  public abstract void drawAdditionalButtons();
  
  public abstract void onTimeout(GUI_StateChangerString prevInventoryGUI, Player player);

  public abstract String[] getPlayersInstructions(Player p);

  public abstract long getTimeoutInTicks();

  public abstract boolean shouldPlayerBeBlindWhileWriting();

  public abstract boolean shouldPlayerBeUnableToMoveWhileWriting();
}
