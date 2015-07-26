package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;

public abstract class GUI_StateChangerNumeric<T> extends GUI_StateChanger<T> {

	public enum ModifyTyp {
		addOffsetToValue, setValue
	}
	
	abstract class Button_changeStateNumeric<S> extends ButtonchangeSTate<T> {
		
		private ModifyTyp modifyType;
		protected T value;
		public Button_changeStateNumeric(T value, ModifyTyp modifyType, StateKeeper<T> valueObject, int amount, 
				ItemStack displayItem, String title, String... desctiption) {
			super(valueObject, displayItem, title, desctiption);
			this.modifyType = modifyType;
			this.value = value;
			this.addDescriptionLine(valueObject.stateToString(amount));	
		}

		public Button_changeStateNumeric(T value, ModifyTyp modifyType, StateKeeper<T> valueObject,
				Button_Bare minimalBUtton) {
			super(valueObject, minimalBUtton);
			this.modifyType = modifyType;
			this.value = value;
		}

		@Override
		public void changeState(StateKeeper<T> stateChanger,
				InventoryGUI inventoryGUI, Player player, ItemStack cursor,
				ItemStack current, ClickType type, InventoryClickEvent event) {
			this.changeNumericState(value, modifyType, stateChanger, inventoryGUI, player, cursor, current, type, event);
		}
		
		public abstract void changeNumericState(T value, ModifyTyp modifyType, StateKeeper<T> stateChanger,
				InventoryGUI inventoryGUI, Player player, ItemStack cursor,
				ItemStack current, ClickType type, InventoryClickEvent event);


		
		

	}

	public GUI_StateChangerNumeric(StateKeeper<T> valueObject,
			String guiTitle, ItemStack displayItem, InventoryGUI parentGUI, int height) {
		super(valueObject, guiTitle, displayItem, parentGUI, height);
		
	}
	
	

}
