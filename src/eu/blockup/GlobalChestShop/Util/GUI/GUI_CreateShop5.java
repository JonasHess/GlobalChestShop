package eu.blockup.GlobalChestShop.Util.GUI;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;



import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_CreateShop5 extends SimpleIInventoryGUI{
  public ShopInfoPack infoPack;
  public GUI_CreateShop5(ShopInfoPack infoPack, InventoryGUI parentInventoryGUI) {
    super("Create new GlobalShop", 6, new ItemStack(Material.ENDER_CHEST), parentInventoryGUI);
    this.infoPack = infoPack;
  }

  @Override
  protected void drawButtons(Player player) {
    this.drawFrame(4, 3, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5));
    this.drawButton(4, 3, new Button(new ItemStack(Material.NETHER_STAR),"Create Shop") {
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
      }
      
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        ShopInfoPack infoPack = ((GUI_CreateShop5)inventoryGUI).infoPack;
        GlobalChestShop.plugin.getShopVerwaltung().createNewGlobalShop(infoPack);
        inventoryGUI.close(player);
      }
    });


  }
}