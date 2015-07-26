package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Interfaces.StateObserver;




public abstract class GUI_StateChanger<T> extends InventoryGUI implements StateObserver{

  private StateKeeper<T> valueObject;
  
  @SuppressWarnings("hiding")
  protected abstract class ButtonchangeSTate<T> extends Button{
    StateKeeper<T> valueObject;
    public ButtonchangeSTate(StateKeeper<T> valueObject, ItemStack displayItem, String title, String... desctiption) {
      super(displayItem, title, desctiption);
      this.valueObject = valueObject;
    }
    
    public ButtonchangeSTate(StateKeeper<T> valueObject, Button_Bare minimalBUtton) {
        super(minimalBUtton);
        this.valueObject = valueObject;
    }

	@Override
	public void onButtonClick(InventoryGUI inventoryGUI, Player player,
			ItemStack cursor, ItemStack current, ClickType type,
			InventoryClickEvent event) {
		this.changeState(this.valueObject, inventoryGUI, player, cursor, current, type, event);
	}
	
	public abstract void changeState(StateKeeper<T> stateChanger, InventoryGUI inventoryGUI, Player player,
			ItemStack cursor, ItemStack current, ClickType type,
			InventoryClickEvent event);
  }
  
  

  public GUI_StateChanger(StateKeeper<T> valueObject, String guiTitle, ItemStack displayItem, InventoryGUI parentGUI, int height) {
    super(guiTitle, Math.min(6, Math.max(4, height)), displayItem, parentGUI);
    this.valueObject = valueObject;
  }  

  
  public StateKeeper<T> getValueObject() {
    return valueObject;
  }
  



@Override
public void onPlayerOpensTheGUI(Player player) {
	this.getValueObject().registerforStateChangeNotification(this);
	this.onPlayerOpensTheGUI2(player);
//	this.updateOnStateChange(player);
}

public abstract void onPlayerOpensTheGUI2(Player player);


@Override
public void onPlayerLeavesTheGUI(Player player) {
	this.getValueObject().unRegisterGUIforStateChangeNotification(this);
	this.onPlayerLeavesTheGUI2(player);
}

public abstract void onPlayerLeavesTheGUI2(Player player);





}
