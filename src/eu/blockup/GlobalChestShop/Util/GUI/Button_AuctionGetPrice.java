package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_AuctionGetPrice extends Button{

	private int worldGroup;
	private ItemStack auctionItem;
	private boolean sellAll;
	
	public Button_AuctionGetPrice(ItemStack auctionItem, boolean sellAll, int worldGroup, ItemStack displayItem, String title, String... description) {
		super(new ItemStack(Material.ARROW), title, description);
		this.worldGroup = worldGroup;
		this.auctionItem = auctionItem;
		this.sellAll = sellAll;
	}

	@Override
	protected Sound getClickSound(ClickType type) {
		return Sound.ENTITY_ARROW_HIT;
	}

	@Override
	public void onRefresh(InventoryGUI inventoryGUI, Player player) {
	}

	@Override
	public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

		if (!(cursor == null || cursor.getData().getItemType().compareTo(Material.AIR) == 0)) {
			return;
		}

		AuctionPrepare auctionPrepare = new AuctionPrepare();
		Double lastPrice = GlobalChestShop.plugin.getAuctionController(this.worldGroup).getLastPriceForPlayersAuction(player, auctionItem);
		if (lastPrice != null) {
			auctionPrepare.setPriceObject(new StateKeeperPrice(lastPrice, true, false));
		} else {
			double defaultPrice = GlobalChestShop.plugin.getMainConfig().defaultInitialAuctionPrice;
			auctionPrepare.setPriceObject(new StateKeeperPrice(defaultPrice, true, false));
		}
		
		if (sellAll) {
			synchronized (player.getInventory()) {
				for (ItemStack playersItem : player.getInventory()) {
					if (playersItem == null) continue;
					if (!(playersItem.isSimilar(auctionItem))) continue;
					auctionPrepare.addItemStack(playersItem.clone());
				}
			}
		} else {
			auctionPrepare.addItemStack(auctionItem);
		}
		
		int maxAuctions = GlobalChestShop.plugin.getAuctionLimitController().getMaxAmountOfRunningAuctions(player, worldGroup);
		int currentAuctionCount = GlobalChestShop.plugin.getAuctionController(worldGroup).getCountOfActiveAuctionsFromPlayer(player.getUniqueId());
		if (currentAuctionCount + auctionPrepare.getItemStackList().size() > maxAuctions) {
			InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Message_TooManyAuctions, String.valueOf(currentAuctionCount)), true, player, inventoryGUI);
			return;
		}
		new GUI_AuctionGetPrice(auctionPrepare, worldGroup, inventoryGUI).open(player);
	}

}


