package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class Button_AdminShopCreate extends Button {

  private ItemStack presetAdminShopItem;
  private Integer worldGroup;


  public Button_AdminShopCreate(ItemStack presetAdminShopItem, Integer worldGroup) {
    super(new ItemStack(Material.ENDER_CHEST), GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAdminShop_SubmitButton), GlobalChestShop.text
        .get(GlobalChestShop.text.GUI_CreateAdminShop_SubmitButton_DESC));
    this.presetAdminShopItem = presetAdminShopItem;
    this.worldGroup = worldGroup;
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    if (!GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
      return;
    }
    if (presetAdminShopItem == null) {
      new GUI_AdminShopChooseItem(inventoryGUI, worldGroup).open(player);
      return;
    }
    Auction adminShop = GlobalChestShop.plugin.getAuctionController(worldGroup).getAdminShopFromItemStack(presetAdminShopItem);
    if (adminShop == null) {
      new GUI_AdminShopCreate(presetAdminShopItem, inventoryGUI, worldGroup).open(player);
    } else {
      new GUI_AdminShopEdit(adminShop, inventoryGUI, worldGroup).open(player);
    }

  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {

  }

}
