package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;

public abstract class GUI_StateChangerInteger extends GUI_StateChangerNumeric<Integer> {

  class Button_changeDoubleState extends Button_changeStateNumeric<Integer> {

    public Button_changeDoubleState(ItemStack displayItem, StateKeeper<Integer> valueObject, Integer value, ModifyTyp modifyType) {
      super(value, modifyType, valueObject, 1 , displayItem, (value > 0.0 ? "+ " : "- ") + Integer.toString(Math.abs(value)));
    }

    @Override
    public void changeNumericState(Integer value, ModifyTyp modifyType, StateKeeper<Integer> stateChanger, InventoryGUI inventoryGUI, Player player,
        ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

      if (modifyType == ModifyTyp.addOffsetToValue) {
        valueObject.mofiyState(value, player);
      } else if (modifyType == ModifyTyp.setValue) {
        valueObject.setState(value, player);
      }
    }

    @Override
    public void onRefresh(InventoryGUI inventoryGUI, Player player) {

    }

  }

  public GUI_StateChangerInteger(StateKeeper<Integer> valueObject, String guiTitle, ItemStack displayItem, InventoryGUI parentGUI, int height) {
    super(valueObject, guiTitle, displayItem, parentGUI, height);
  }

  @Override
  public void drawButtons(Player player) {
    this.drawButton(4, 0, this.getValueObject().formatToDisplayButton(player, 1));

    // +1000
    this.drawButton(2, 2, new Button_changeDoubleState(new ItemStack(Material.EMERALD), this.getValueObject(), +1000, ModifyTyp.addOffsetToValue));

    // -1000
    this.drawButton(2, 3, new Button_changeDoubleState(new ItemStack(Material.EMERALD), this.getValueObject(), -1000, ModifyTyp.addOffsetToValue));

    // +100
    this.drawButton(3, 2, new Button_changeDoubleState(new ItemStack(Material.DIAMOND), this.getValueObject(), +100, ModifyTyp.addOffsetToValue));

    // -100
    this.drawButton(3, 3, new Button_changeDoubleState(new ItemStack(Material.DIAMOND), this.getValueObject(), -100, ModifyTyp.addOffsetToValue));

    // +10
    this.drawButton(4, 2, new Button_changeDoubleState(new ItemStack(Material.GOLD_INGOT), this.getValueObject(), +10, ModifyTyp.addOffsetToValue));

    // -10
    this.drawButton(4, 3, new Button_changeDoubleState(new ItemStack(Material.GOLD_INGOT), this.getValueObject(), -10, ModifyTyp.addOffsetToValue));

    // +1
    this.drawButton(5, 2, new Button_changeDoubleState(new ItemStack(Material.IRON_INGOT), this.getValueObject(), +1, ModifyTyp.addOffsetToValue));

    // -1
    this.drawButton(5, 3, new Button_changeDoubleState(new ItemStack(Material.IRON_INGOT), this.getValueObject(), -1, ModifyTyp.addOffsetToValue));

    this.drawAdditionalButtons(player);
  }


  protected abstract void drawAdditionalButtons(Player player);

  @Override
  public void updateOnStateChange(Player player) {
//    this.resetAllButtons(player);
//    this.refresh(player);

  }
}
