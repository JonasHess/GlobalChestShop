package eu.blockup.GlobalChestShop.Util.GUI;

import java.sql.Date;
import java.sql.Time;
import java.util.logging.Level;

import eu.blockup.GlobalChestShop.Util.Sounds;
import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.Exceptions.PlayerDoesNotOwnClaimedItemException;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects.ButtonEffect_FlashRed;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_AuctionCreateSubmit extends InventoryGUI {

	private Integer			worldGroup;
	private AuctionPrepare	auctionPrepare;

	public GUI_AuctionCreateSubmit(AuctionPrepare auctionPrepare, Integer worldGroup, InventoryGUI parentInventoryGUI) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_AuctionSellItem), 6, auctionPrepare.getDisplayItem(), parentInventoryGUI);
		this.worldGroup = worldGroup;
		this.auctionPrepare = auctionPrepare;
	}

	@Override
	protected void drawButtons(Player player) {
		// Frame
		this.drawFrame(6, 3, new ItemStack(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 5));
		// Item
		this.drawButton(1, 3, new Button_Bare(auctionPrepare.getDisplayItem()));
		// Price

		// this.drawButton(2, 3, this.price.formatToDisplayButton(player));
		this.addAnimatedButton(new ButtonEffect_FlashRed(2, 3), this.auctionPrepare.getPriceObject().formatToDisplayButton(player, this.auctionPrepare.getTotalAmount()));

		// Submit
		this.drawButton(6, 3, new Button(new ItemStack(Material.NETHER_STAR), GlobalChestShop.text.get(GlobalChestShop.text.Button_Finish)) {

			@Override
			protected Sound getClickSound(ClickType type) {
				return Sounds.LEVEL_UP.bukkitSound();
			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

				int soldStacks = 0;
				int soldItems = 0;
				ItemStack itemForSale = null;
				synchronized (player.getInventory()) {
					for (ItemStack auctionItem : auctionPrepare.getItemStackList()) {
						try {
							if (itemForSale == null) {
								itemForSale = auctionItem.clone();
								itemForSale.setAmount(1);
							}
							GlobalChestShop.plugin.validatePlayerIsItemOwner(player, auctionItem);
							player.getInventory().removeItem(auctionItem);
							Auction.createNewAuction(auctionItem, auctionItem.getAmount(), -1, auctionPrepare.getPriceObject().getCurrentState(player), player.getUniqueId(), null, false, new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), null, null, false, worldGroup);
							soldStacks++;
							soldItems += auctionItem.getAmount();
							GlobalChestShop.plugin.getDefaultCategoryController(worldGroup).addItemToProtfolio(auctionItem);
						} catch (PlayerDoesNotOwnClaimedItemException e) {
						}
					}
				}
				InventoryGUI fallBackGUI = inventoryGUI.getFirstGUI();
				if (soldStacks == 0) {
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoItem));
					InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoItem), false, player, inventoryGUI);
					return;
				} else {
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitCreateAuction_Succsess));
					
					// BroadCast																													//player, 64, stone, price
					String broadcastMessage = GlobalChestShop.text.get(GlobalChestShop.text.Message_BroadcastCreationOfNewAuction, player.getName(), String.valueOf(soldItems), GlobalChestShop.plugin.getItemStackDisplayName(itemForSale), GlobalChestShop.plugin.formatPrice(auctionPrepare.getPriceObject().getCurrentState(player), false));
					GlobalChestShop.plugin.logToTradeLogger(player.getName(), player.getUniqueId(), broadcastMessage);
					GlobalChestShop.plugin.getLogger().log(Level.INFO, ChatColor.stripColor(broadcastMessage));
					if (GlobalChestShop.plugin.getMainConfig().broadcastCreationOfNewAuctions) {
						for (Player p : GlobalChestShop.plugin.getServer().getOnlinePlayers()) {
							try {
								if (GlobalChestShop.plugin.getworldGroup(p.getLocation()) == worldGroup) {
									if (p.getUniqueId().compareTo(player.getUniqueId()) == 0) {
										 continue;
									}
									p.sendMessage(broadcastMessage);
								}
							} catch (WorldHasNoWorldGroupException e) {
								continue;
							}

						}
					}
					
					////
					
					if (fallBackGUI instanceof GUI_AuctionCreate) {
						((GUI_AuctionCreate) fallBackGUI).unsetItem();
					}
					inventoryGUI.returnToFirstGUI(player);
				}

			}
		});

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
		return true;
	}

	@Override
	public boolean shouldReturnButtonBeDrawn() {
		return true;
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
	public void onTick(Player player, int tickCount) {

	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

	}
}
