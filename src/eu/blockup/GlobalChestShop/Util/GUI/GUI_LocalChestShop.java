package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_LocalChestShop extends GUI_AuctionPage {

  private UUID owner;
  
  public GUI_LocalChestShop(UUID owner, InventoryGUI parentGUI, Integer worldGroup) {
    super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Title_LocalChestShop), GlobalChestShop.plugin.getPlayerHead(owner, false), parentGUI, false, worldGroup, false, 1.0);
    this.owner = owner;
  }

  private boolean isPlayerTheOwner(Player player) {
    return (player.getUniqueId().compareTo(owner) == 0);
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

    return GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllActiveAuctionsFromPlayer(owner);
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
