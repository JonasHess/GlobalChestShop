package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

enum Visibility {
	AllActive, AllSold, AllBought, AllCanceled, AllExpired;
	
    String getTitle(Visibility v) {
      String title;
      if (v == Visibility.AllActive) {
          title = GlobalChestShop.text.get(GlobalChestShop.text.GUI_History_RunningAuctions);
      } else if (v == Visibility.AllBought) {
          title = GlobalChestShop.text.get(GlobalChestShop.text.GUI_History_BoughtAuctions);
      } else if (v == Visibility.AllCanceled) {
        title = GlobalChestShop.text.get(GlobalChestShop.text.GUI_History_CanceledAuctions);
      } else if (v == Visibility.AllExpired) {
          title = GlobalChestShop.text.get(GlobalChestShop.text.GUI_History_Expired);
        } else {
          title = GlobalChestShop.text.get(GlobalChestShop.text.GUI_History_SoldAuctions);
      }
      return title;
    }
}

class VisibilityChanger extends Button {
	private Visibility visibility;
	



	public VisibilityChanger(Visibility visibility,
			String... description) {
		super(new ItemStack(Material.BOOK), visibility.getTitle(visibility), description);
		this.visibility = visibility;
		
	}

	@Override
	public void onButtonClick(InventoryGUI inventoryGUI, Player player,
			ItemStack cursor, ItemStack current, ClickType type,
			InventoryClickEvent event) {
		((GUI_AuctionHistory) inventoryGUI).changeVisibility(player, visibility);

	}

	@Override
	public void onRefresh(InventoryGUI inventoryGUI, Player player) {

	}
}


class Button_SeeAllAuctionsOfPlayer extends Button {

  public UUID playerUUID;
  private final Visibility visibility;
  private GUI_AuctionHistory historyGUI;

  public Button_SeeAllAuctionsOfPlayer(UUID playerUUID, Visibility visibility, Visibility currentVisibility, GUI_AuctionHistory historyGUI) {
    super(new ItemStack(Material.BOOK), "NULL");
    this.playerUUID = playerUUID;
    this.visibility = visibility;
    this.setTitle(visibility.getTitle(visibility));
    this.historyGUI = historyGUI;
    if (currentVisibility == this.visibility) {
      this.setDisplayIcon(new ItemStack(Material.ENCHANTED_BOOK));
    }
  }

  @Override
  public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

    historyGUI.changeVisibility(player, visibility);

  }
  @Override
  public void onRefresh(InventoryGUI inventoryGUI, Player player) {

  }

}



public class GUI_AuctionHistory extends GUI_AuctionPage {

  private boolean showActiveAuctions;
  private boolean createNewAuctionButton;
  private Integer worldGroup;
  
  public GUI_AuctionHistory(InventoryGUI parentGUI, UUID playerUUID, Visibility visibility, boolean showActiveAuctions, boolean createNewAuctionButton, Integer worldGroup) {
    super(visibility.getTitle(visibility), new ItemStack(Material.PAPER), parentGUI, false, worldGroup, true, 1.0);
    this.playerUUID = playerUUID;
    this.visibility = visibility;
    this.showActiveAuctions = showActiveAuctions;
    this.createNewAuctionButton = createNewAuctionButton;
    this.worldGroup = worldGroup;
    }

  private UUID playerUUID;
  private Visibility visibility;


  @Override
  public void drawAditionalButtons(Player player) {
    super.drawAditionalButtons(player);
    
    int i = 8;
    this.drawButton(i--, 1, new Button_SeeAllAuctionsOfPlayer(playerUUID, Visibility.AllCanceled, this.visibility, this));
    this.drawButton(i--, 1, new Button_SeeAllAuctionsOfPlayer(playerUUID, Visibility.AllBought, this.visibility, this));
    this.drawButton(i--, 1, new Button_SeeAllAuctionsOfPlayer(playerUUID, Visibility.AllSold, this.visibility, this));
    this.drawButton(i--, 1, new Button_SeeAllAuctionsOfPlayer(playerUUID, Visibility.AllExpired, this.visibility, this));
    
    if (this.showActiveAuctions) {
      this.drawButton(i--, 1, new Button_SeeAllAuctionsOfPlayer(playerUUID, Visibility.AllActive, this.visibility,this));
    }

    
    // Money Button
    this.drawButton(0, this.getHeight() -1, new Button_Money(playerUUID));
    
    if(this.playerUUID.compareTo(player.getUniqueId()) == 0) {
      if (this.createNewAuctionButton) {
        this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_AuctionCreate(this.worldGroup));
      }
      if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.MODERATOR_SEE_PLAYERS_SHOPS + "." + worldGroup)) {
        this.drawButton(1, this.getHeight() -1, new Button_AllPlayers(this.worldGroup));
      }
    }
  }

  public void changeVisibility(Player player, Visibility visibility) {
  if (visibility != this.visibility) {
      this.pagenumber = 1;
  }
  this.visibility = visibility;
  this.setTitle(this.visibility.getTitle(this.visibility));
  this.reloadObjectList();
  this.redrawAllButtons(player);
  this.refresh(player);
}


  @Override
  public boolean drawMoneyButton(Player player) {
    return false;
  }

  @Override
  public boolean drawDisplayItemButton(Player player) {
    return false;
  }

  @Override
  public boolean drawHistoryButton(Player player) {
    return false;
  }

  @Override
  public List<Auction> getRefreshedObjectList() {
    List<Auction> auctionList;
    if (visibility == Visibility.AllActive) {
      auctionList = GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllActiveAuctionsFromPlayer(this.playerUUID);
    } else if (visibility == Visibility.AllBought) {
      auctionList = GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllBoughtAuctionFromPlayer(this.playerUUID);
    } else if (visibility == Visibility.AllSold) {
      auctionList = GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllSoldAuctionsByPlayer(this.playerUUID);
    } else if (visibility == Visibility.AllExpired) {
        auctionList = GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllExpiredAuctionsFromPlayer(this.playerUUID);
    } else {
      auctionList = GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAllCanceledAuctions(this.playerUUID);
    }
    return auctionList;
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
  public boolean drawAuctionCreateButton(Player player) {
    return false;
  }

  @Override
  public boolean showRunningAuctionsInHistory() {
    return false;
  }

}


//public class GUI_AuctionHistory extends GUI__AuctionPageView {
//
//	private UUID playersUUID;
//	private Visibility visibility;
//
//	public Visibility getVisibility() {
//		return visibility;
//	}
//
//	public void reloadAuctionList(Visibility visibility, int pagenumber) {
//		if (visibility != this.visibility) {
//			this.pagenumber = 1;
//		} else {
//			this.pagenumber = pagenumber;
//		}
//		this.visibility = visibility;
//		if (visibility == Visibility.AllActive) {
//			this.setAuctionList(GlobalChestShop.plugin.auctionVerwaltung
//					.getAllActiveAuctionsFromPlayer(this.playersUUID));
//		} else if (visibility == visibility.AllBought) {
//			this.setAuctionList(GlobalChestShop.plugin.auctionVerwaltung
//					.getAllBoughtAuctionFromPlayer(this.playersUUID));
//		} else if (visibility == visibility.AllCanceled) {
//          this.setAuctionList(GlobalChestShop.plugin.auctionVerwaltung
//                  .getAllCanceledAuctions(this.playersUUID));
//		} else {
//			this.setAuctionList(GlobalChestShop.plugin.auctionVerwaltung
//					.getAllSoldAuctionsByPlayer(this.playersUUID));
//		}
//		this.setTitle(visibility.getTitle(visibility));
//		this.listChanged = true;
//	}
//
//	public GUI_AuctionHistory(UUID playersUUID, Visibility visibility,
//			InventoryGUI parentGUI) {
//		super(new ItemStack(Material.PAPER), 1, parentGUI, new AuctionList(
//				SortTyp.BEST_SINGLE_PRICE), false);
//		this.playersUUID = playersUUID;
//		
//		this.setTitle(visibility.getTitle(visibility));
//		this.reloadAuctionList(visibility, this.pagenumber);
//	}
//
//	@Override
//	protected void drawAditionalButtons(Player player) {
//		super.drawAditionalButtons(player);
//
//		this.drawButton(5, 1, new VisibilityChanger(visibility.AllActive, "desc"));
//		this.drawButton(6, 1, new VisibilityChanger(visibility.AllSold, "desc"));
//		this.drawButton(7, 1, new VisibilityChanger(visibility.AllBought, "desc"));
//		this.drawButton(8, 1, new VisibilityChanger(visibility.AllCanceled, "desc"));
//
//		this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_CreateAuction());
//		
//		if(playersUUID == player.getUniqueId())
//			this.drawButton(0, this.getHeight() - 1, new Button_AllPlayers());
//	}
//
//	@Override
//	public void onPlayerOpensTheGUI(Player player) {
//		super.onPlayerOpensTheGUI(player);
//		this.reloadAuctionList(visibility, this.pagenumber);
//	}
//
//	@Override
//	public void onTick(Player player, int tickCount) {
//		if (this.listChanged) {
//			this.reloadAuctionList(this.visibility, this.pagenumber);
//			this.resetAllButtons(player);
//			this.refresh(player);
//		}
//	}
//
//}
