package eu.blockup.GlobalChestShop.Util.GUI;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_CreateShop4 extends SimpleIInventoryGUI {
  public ShopInfoPack infoPack;

  public GUI_CreateShop4(ShopInfoPack infoPack, InventoryGUI parentInventoryGUI) {
    super("Choose a ShopTyp", 6, new ItemStack(Material.ENDER_CHEST), parentInventoryGUI);
    this.infoPack = infoPack;
  }

  @Override
  protected void drawButtons(Player player) {


    // Info
    this.drawButton(4, 1, new Button_Bare(new ItemStack(Material.PAPER), "ShopType", ChatColor.GOLD + "Should players be able to create new auctions themselves inside this shop?")); 



    // Yes
    this.drawButton(3, 3, new Button(new ItemStack(Material.WOOL, 1, (short) 5), "Yes") {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {

      }

      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop4) inventoryGUI).infoPack.clone();
        infoPack.setNewAuctions(true);
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop5(infoPack, inventoryGUI));
      }
    });

    // No
    this.drawButton(5, 3, new Button(new ItemStack(Material.WOOL, 1, (short) 14), "No") {

      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {

      }

      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type,
          InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop4) inventoryGUI).infoPack.clone();
        infoPack.setNewAuctions(false);
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop5(infoPack, inventoryGUI));
      }
    });


  }
}
