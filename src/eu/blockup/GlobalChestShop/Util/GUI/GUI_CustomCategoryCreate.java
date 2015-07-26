package eu.blockup.GlobalChestShop.Util.GUI;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_GetStringInput;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_CustomCategoryCreate extends SimpleIInventoryGUI {

  public ItemStack itemStack = null;
  public String categoryName = null;

  public GUI_CustomCategoryCreate(InventoryGUI parentInventoryGUI) {
    super("Create new category", 6, new ItemStack(Material.NETHER_STAR), parentInventoryGUI);
  }

  @Override
  protected void drawButtons(Player player) {
    
    // Choose Item
    this.drawButton(3, 3, new Button(new ItemStack(Material.ITEM_FRAME), ChatColor.WHITE + "Choose an item that",  ChatColor.WHITE + "represents your new category") {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        GUI_CustomCategoryCreate gui = (GUI_CustomCategoryCreate) inventoryGUI;
        if (gui.itemStack != null) {
          this.setDisplayIcon(gui.itemStack);
        }

      }


      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CustomCategoryItemStackChooser((GUI_CustomCategoryCreate) inventoryGUI));

      }
    });

    // Get String input
    this.drawButton(5, 3, new Button_GetStringInput(new Button_Bare(new ItemStack(Material.BOOK), "Choose a name for your new category")) {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        if (categoryName != null) {
          this.setTitle(categoryName);
        }
      }

      @Override
      protected boolean shouldPlayerBeUnableToMoveWhileWriting() {
        return true;
      }

      @Override
      public boolean shouldPlayerBeBlindWhileWriting() {
        return true;
      }

      @Override
      public void onTimeout(InventoryGUI prevInventoryGUI, Player player) {
        prevInventoryGUI.open(player);
      }

      @Override
      public void onPlayerTypedString(InventoryGUI prevInventoryGUI, Player player, String value) {
        if (value != null)
          ((GUI_CustomCategoryCreate) prevInventoryGUI).categoryName = value;
        prevInventoryGUI.onPlayerReturnsToThisGUI(player, null);
        prevInventoryGUI.open(player);
      }

      @Override
      public long getTimeoutInTicks() {
        return 20 * 18;
      }

      @Override
      public String[] getPlayersInstructions(Player p) {
        String[] result = new String[1];
        result[0] = "Type the category name into the chat!";
        return result;
      }
    });

    // Submit
    if (categoryName != null && itemStack != null) {
      this.drawButton(this.getWidth() -1, this.getHeight() -1, new Button(new ItemStack(Material.NETHER_STAR), ChatColor.GREEN + "create") {

        @Override
        public void onRefresh(InventoryGUI inventoryGUI, Player player) {

        }

        @Override
        public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
            InventoryClickEvent event) {
          GUI_CustomCategoryCreate gui = (GUI_CustomCategoryCreate) inventoryGUI;
          if (gui.categoryName == null) {
            player.sendMessage("No category name set!");
            return;
          }
          if (gui.itemStack == null) {
            player.sendMessage("No representing item set!");
            return;
          }
          GlobalChestShop.plugin.getCustomCategoryController().createNewCategory(gui.categoryName, gui.itemStack, false); //TODO false ist adminonlyshop ?
          inventoryGUI.returnToParentGUI(player);
        }
      });
    }
  }

  public void setChoosenItemStack(ItemStack newItem) {
    this.itemStack = newItem;

  }

  @Override
  public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
    this.redrawAllButtons(player);
  }



}
