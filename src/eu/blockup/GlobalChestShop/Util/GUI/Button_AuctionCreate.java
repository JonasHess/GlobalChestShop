package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_AuctionCreate extends Button {



  private Integer worldGroup;

  public Button_AuctionCreate(Integer worldGroup) {
    super(new ItemStack(Material.NETHER_STAR));
    this.worldGroup = worldGroup;
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
    if (cursor != null && cursor.getType().compareTo(Material.AIR) != 0) {
      GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AuctionCreate(inventoryGUI, cursor.clone(), this.worldGroup, player));
      return;
    }
    GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AuctionCreate(inventoryGUI, this.worldGroup, player));
  }

  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {
	  this.setDescription(GlobalChestShop.text
        .get(GlobalChestShop.text.Auction_Create_Description));
	  
	  String secondLine;
	  ChatColor c = ChatColor.GRAY;
	  int maxAuctions = GlobalChestShop.plugin.getAuctionLimitController().getMaxAmountOfRunningAuctions(player, worldGroup);
	  int currentAuctionCount = GlobalChestShop.plugin.getAuctionController(worldGroup).getCountOfActiveAuctionsFromPlayer(player.getUniqueId());
	  if (currentAuctionCount >= maxAuctions) {
		  secondLine = ChatColor.RED + "" + currentAuctionCount;
	  }else {
		  secondLine = "" + currentAuctionCount;
	  }
	  this.setTitle(GlobalChestShop.text.get(GlobalChestShop.text.Button_Auction_Create_Title) + c + "   (" + secondLine + c + "/" + maxAuctions + ")");
  }

}
