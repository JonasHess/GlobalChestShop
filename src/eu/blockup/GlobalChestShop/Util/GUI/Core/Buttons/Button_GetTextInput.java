package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.TextInput;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;


public class Button_GetTextInput extends Button {

  private TextInput textInput;
  
  public Button_GetTextInput(TextInput textInput, ItemStack displayItem, String title, String... description) {
      super(displayItem, title, description);
      this.setTextField(textInput);
  }
  
  public final Button_GetTextInput setTextField(TextInput textInput) {
      this.textInput = textInput;
      return this;
  }
  
  public final TextInput getTextField() {
      return this.textInput;
  }


  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    if (!GlobalChestShop.plugin.getGuiCore().openTextInput(player, this.textInput)) {
      GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, inventoryGUI);
  }
    
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
    
  }
  
}
