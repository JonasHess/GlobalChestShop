package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_RemoveAllAuctionsOfPlayerPolarQuestion extends GUI_PolarQuestion{

	private UUID bannedPlayer;
	private int worldGroup;
	
	public GUI_RemoveAllAuctionsOfPlayerPolarQuestion(InventoryGUI parentGUI, UUID bannedPlayer, int worldGroup) {
		super("", parentGUI);
		this.bannedPlayer = bannedPlayer;
		this.worldGroup = worldGroup;
		
	}

	@Override
	protected void drawAdditionalButtons(Player player) {
		
	}

	@Override
	protected void onYesButtonClick(InventoryGUI inventoryGUI, Player player) {
		if (!GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.MODERATOR_DELETE_AUCTIONS)) {
			return;
		}
		List<Auction> activeAuctions = GlobalChestShop.plugin.getAuctionController(worldGroup).getAllActiveAuctionsFromPlayer(this.bannedPlayer);
		for (Auction auction : activeAuctions){
			auction.canceleAuction();
			GlobalChestShop.plugin.logToTradeLogger(player.getName(), player.getUniqueId(), player.getName() + " has canceled the auction : " + auction.toString(1.0));
		}
		player.sendMessage("Removed: " + activeAuctions.size() + " active auctions!");	
		
		List<Auction> expredAuction = GlobalChestShop.plugin.getAuctionController(worldGroup).getAllExpiredAuctionsFromPlayer(this.bannedPlayer);
		for (Auction auction : expredAuction){
			auction.canceleAuction();
			GlobalChestShop.plugin.logToTradeLogger(player.getName(), player.getUniqueId(), player.getName() + " has canceled the auction : " + auction.toString(1.0));
		}
		player.sendMessage("Removed: " + expredAuction.size() + " expired auctions!");	
		
		warning("All auctions were removed", false, player, inventoryGUI.getParentGUI());
	}

	@Override
	protected void onNoButtonClick(InventoryGUI inventoryGUI, Player player) {
		inventoryGUI.returnToParentGUI(player);
		
	}

	@Override
	public long getTimeoutInTicks() {
		return 0;
	}

	@Override
	public ButtonTypEnum whichButtonShouldBeClickedOnTimeout() {
		return ButtonTypEnum.NoButton;
	}

	@Override
	public String get_GUI_Title() {
		return "Remove all auctions of player?";
	}

	@Override
	public Button_Bare get_the_Question_Button() {
		String playerName;
		try {
			playerName = GlobalChestShop.plugin.getServer().getOfflinePlayer(bannedPlayer).getName();
		}catch(Exception e) {
			playerName = "Unknown";
		}
		return new Button_Bare(GlobalChestShop.plugin.getPlayerHead(bannedPlayer, false), this.get_GUI_Title(), "Name: " +  playerName,"UUID: " + bannedPlayer.toString());
	}

	@Override
	public Button_Bare get_YesButton() {
		return new Button_Bare(new ItemStack(Material.WOOL, 1, (short) 5), ChatColor.RED + "YES, remove all auctions of player");
	}

	@Override
	public Button_Bare get_NoButton() {
		return new Button_Bare(new ItemStack(Material.WOOL, 1, (short) 14), ChatColor.GREEN + "NO, cancel");
	}

	@Override
	public boolean shouldBackgroundBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldEscKeyClosesTheGUI() {
		return false;
	}

	@Override
	public boolean shouldEscKeyLeadsToPreviousGUI() {
		return false;
	}

	@Override
	public boolean shouldReturnButtonBeDrawn() {
		return false;
	}

	@Override
	public boolean shouldCloseButtonBeDrawn() {
		return true;
	}

	@Override
	public void onPlayerOpensTheGUI(Player player) {
		
	}

	@Override
	public void onPlayerLeavesTheGUI(Player player) {
		
	}

	@Override
	public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
		
	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
		
	}

}
