package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_LocalChestShop extends GUI_AuctionPage {

  private Shop shop;
  
  public GUI_LocalChestShop(Shop shop, InventoryGUI parentGUI, Integer worldGroup) {
    super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Title_LocalChestShop), shop.getShopEntityIcon(), parentGUI, false, worldGroup, false);
    this.shop = shop;
  }

  private boolean isPlayerTheOwner(Player player) {
    return (GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(player.getUniqueId()) == shop.getOwner());
  }

  @Override
  public boolean drawAuctionCreateButton(Player player) {
    return isPlayerTheOwner(player);
  }


  @Override
  public boolean drawMoneyButton(Player player) {
    return true;
  }

  @Override
  public boolean drawDisplayItemButton(Player player) {
    return true;
  }


  @Override
  public boolean drawHistoryButton(Player player) {
    return isPlayerTheOwner(player);
  }


  @Override
  public List<Auction> getRefreshedObjectList() {

    return GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllActiveAuctionsFromPlayer(this.shop.getOwnerUUID());
  }

  @Override
  public boolean drawAdminShopCreateButton() {
    return false;
  }

  @Override
  public ItemStack getPresetAdminShopItem() {
    return null;
  }

  @Override
  public boolean showRunningAuctionsInHistory() {
    return false;
  }



}
