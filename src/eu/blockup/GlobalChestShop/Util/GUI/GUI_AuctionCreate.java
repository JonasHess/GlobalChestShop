package eu.blockup.GlobalChestShop.Util.GUI;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.ItemIsDamagedException;
import eu.blockup.GlobalChestShop.Util.Exceptions.ItemIsNotAlloedInThisWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects.ButtonEffect_FadeIn;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_AuctionCreate extends InventoryGUI {

	// private Button itemHolder;
	private ItemStack				itemStack;
	private int						amount				= 1;
//	public boolean					priceWasInitialized	= false;
	protected Integer				worldGroup;
	private ItemStack originalItem = null;
	public GUI_AuctionCreate(InventoryGUI parentInventoryGUI, Integer worldGroup, Player player) {
		this(parentInventoryGUI, null, worldGroup, player);
	}

	public GUI_AuctionCreate(InventoryGUI parentInventoryGUI, ItemStack presetItem, int worldGroup, Player player) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_Title), 6, new ItemStack(Material.PAPER), parentInventoryGUI);
		this.worldGroup = worldGroup;
//		this.price = new StateKeeperPrice(100.0, true, false);
		if (presetItem != null) {
			try {
				this.setItemStack(presetItem, player);
				this.amount = presetItem.getAmount();
			} catch (ItemIsDamagedException e) {
			} catch (ItemIsNotAlloedInThisWorldGroupException e) {
			}
		}

	}

	public void setItemStack(ItemStack item, Player player) throws ItemIsDamagedException, ItemIsNotAlloedInThisWorldGroupException {
		if (originalItem !=null && !originalItem.getType().equals(Material.AIR)){
			player.getInventory().addItem(originalItem);
			originalItem = null;
		}
		if (item != null && !item.getType().equals(Material.AIR)) {
			if (item.getType().getMaxDurability() != 0 && item.getDurability() != 0) {
				updateAfterChange(player);
				throw new ItemIsDamagedException();
			}
			if (GlobalChestShop.plugin.itemController.isItemBannedFromShops(item, worldGroup, true)) {
				if (!(GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN))) {
					updateAfterChange(player);
					throw new ItemIsNotAlloedInThisWorldGroupException();
				}
			}
			originalItem = item;
			ItemStack newItem = item.clone();
			this.setAmount(item.getAmount());
			this.itemStack = newItem;
			if (player.getItemOnCursor()==null||player.getItemOnCursor().getType().equals(Material.AIR)) {
				player.getInventory().removeItem(originalItem);
			}
			player.setItemOnCursor(null);
//			Double lastPrice = GlobalChestShop.plugin.getAuctionController(this.worldGroup).getLastPriceForPlayersAuction(player, newItem);
//			if (lastPrice != null) {
//				this.price = new StateKeeperPrice(lastPrice, true, false);
//			}
		} else {
			this.itemStack = null;
			this.removeAllAnimatedButtons();
		}
		this.setTickCount(0);
		updateAfterChange(player);
	}

	public void unsetItem() {
		this.itemStack = null;
//		this.priceWasInitialized = false;
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
	protected void drawButtons(Player player) {

		// Holding Item
		ItemStack chousenItem;
		String title = "";
		String desc = "";

		if (this.itemStack == null) {
			chousenItem = new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 0);
			title = GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_Holding_Item_Title);
			desc = GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_Holding_Item_DESC);
		} else {
			chousenItem = this.itemStack.clone();
			chousenItem.setAmount(this.amount);
		}
		if (this.itemStack == null) {
			this.drawFrame(4, 2, new ItemStack(XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 11));
		} else {
			this.drawFrame(4, 2, new ItemStack(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 5));
		}
		this.addButton(4, 2, new Button(chousenItem, title, desc) {

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				GUI_AuctionCreate gui = (GUI_AuctionCreate) inventoryGUI;
				synchronized (player) {
					if (cursor == null || cursor.getData().getItemType() == Material.AIR) {
						if (itemStack != null) {
							try {
								setItemStack(null, player);
							} catch (ItemIsDamagedException e) {
							} catch (ItemIsNotAlloedInThisWorldGroupException e) {
							}
							return;
						} else {
							player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.GUI_CreateAuction_Holding_Item_ClickMessage));
							return;
						}
					}
					try {
						gui.setItemStack(cursor, player);
					} catch (ItemIsDamagedException e) {
						InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Message_ItemDamaged), false, player, inventoryGUI);
					} catch (ItemIsNotAlloedInThisWorldGroupException e) {
						InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Message_ItemNotAllowed), false, player, inventoryGUI);
					}
					inventoryGUI.resetAllButtons(player);
					inventoryGUI.refresh(player);
				}
			}
		});

		// Next Button GOTO Price
		if (this.itemStack != null) {
			
			ItemStack itemWithcorrectAmount = itemStack.clone();
			//itemWithcorrectAmount.setAmount(amount);
			
			this.addAnimatedButton(new ButtonEffect_FadeIn(6, this.getHeight() - 1, 10), new Button_AuctionGetPrice(itemWithcorrectAmount, false,  worldGroup, new ItemStack(Material.ARROW), GlobalChestShop.text.get(GlobalChestShop.text.Button_SellSingle)));
					
					
			// SellAll
			ItemStack tmpItemStack = itemStack.clone();
			tmpItemStack.setAmount(1);
			if (player.getInventory().containsAtLeast(tmpItemStack, 1)) {
				this.addAnimatedButton(new ButtonEffect_FadeIn(2, this.getHeight() - 1, 4), new Button_AuctionGetPrice(itemWithcorrectAmount, true,worldGroup, new ItemStack(Material.ARROW, 64), GlobalChestShop.text.get(GlobalChestShop.text.Button_SellAll)));
			}
				

			// Ban Item
			if (this.itemStack != null && GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
				this.addButton(0, 2, new Button(new ItemStack(XMaterial.REDSTONE_TORCH.parseMaterial()), ChatColor.RED + "Administrate this item") {

					@Override
					public void onRefresh(InventoryGUI inventoryGUI, Player player) {

					}

					@Override
					public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
						new GUI_DefaultCategoryBanItem(itemStack, worldGroup, inventoryGUI).open(player);
					}

				});
			}
		}
	}

	@Override
	public void onPlayerOpensTheGUI(Player player) {

	}

	@Override
	public void onPlayerLeavesTheGUI(Player player) {

	}

	public void updateAfterChange(Player player) {
		if (player != null) {
			this.resetAllButtons(player);
			this.refresh(player);
		}
	}

	@Override
	public void onTick(Player player, int tickCount) {
	}

	public int getMaxAmount(Player player) {
		if (this.itemStack == null)
			return 64;
		if (this.itemStack.getMaxStackSize() < 64) {
			return this.itemStack.getMaxStackSize();
		}
		int tmpAmount = 64;
		while (tmpAmount > 1) {
			if (player.getInventory().containsAtLeast(itemStack, tmpAmount))
				break;
			tmpAmount--;
		}
		return tmpAmount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
		if (type == ClickType.SHIFT_LEFT) {
			try {
				this.setItemStack(clicked, player);
			} catch (ItemIsDamagedException e) {
				player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_ItemDamaged));
			} catch (ItemIsNotAlloedInThisWorldGroupException e) {
				player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_ItemNotAllowed));
			}
		}
	}

	@Override
	public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
		this.redrawAllButtons(player);
	}

}
