package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;

public abstract class GUI_StateChangeBoolean extends GUI_StateChanger<Boolean>{
  
  
  protected class ButtonChangeStateBoolean extends ButtonchangeSTate<Boolean> {

    private Boolean value;
    
    public ButtonChangeStateBoolean(Boolean value, StateKeeper<Boolean> valueObject, Button_Bare minimalBUtton) {
      super(valueObject, minimalBUtton);
      this.value = value;
    }

    public ButtonChangeStateBoolean(Boolean value, StateKeeper<Boolean> valueObject, ItemStack displayItem, String title, String... desctiption) {
      super(valueObject, displayItem, title, desctiption);
      this.value = value;
    }

    @Override
    public void changeState(StateKeeper<Boolean> stateChanger, InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current,
        ClickType type, InventoryClickEvent event) {
      stateChanger.setState(value, player);
    }

    @Override
    public void onRefresh(InventoryGUI inventoryGUI, Player player) {
      
    }

    
    
  }

  public GUI_StateChangeBoolean(StateKeeper<Boolean> valueObject, String guiTitle, ItemStack displayItem, InventoryGUI parentGUI, int height) {
    super(valueObject, guiTitle, displayItem, parentGUI, height);
  }

  
  @Override
  public void updateOnStateChange(Player player) {
//    this.resetAllButtons(player);
//    this.refresh(player);
  }


  @Override
  protected void drawButtons(Player player) {
    this.drawButton(4, 0, this.getValueObject().formatToDisplayButton(player, 1));
    this.drawButton(3, 3, new ButtonChangeStateBoolean(true, getValueObject(), this.getTrueButton(player, getValueObject())));
    this.drawButton(5, 3, new ButtonChangeStateBoolean(false, getValueObject(), this.getFalseButton(player, getValueObject())));
  }
  
  protected abstract Button_Bare getTrueButton(Player player, StateKeeper<Boolean> stateChanger);
  protected abstract Button_Bare getFalseButton(Player player, StateKeeper<Boolean> stateChanger);
  
  
  
 

}
