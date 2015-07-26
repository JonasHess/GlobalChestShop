package eu.blockup.GlobalChestShop.Util.GUI.Core.Core;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffectType;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class EventListener implements Listener {

	private Map<UUID, Timestamp>	clickSpamMap;

	public EventListener() {
		super();
		this.clickSpamMap = java.util.Collections.synchronizedMap(new HashMap<UUID, Timestamp>());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (event.isCancelled()) {
			return;
		}
		HumanEntity he = event.getWhoClicked();
		if (he instanceof Player) {
			Player player = (Player) he;

			// Text Book Click Handling
			if (GlobalChestShop.plugin.getGuiCore().playerEntersTextInput(player)) {
				this.cancelEvent(event);
				return;
			}

			// Handle only clicks related to this Plugin
			if (!GlobalChestShop.plugin.getGuiCore().has_Player_opened_InventoyGUI(player)) {
				return;
			}

			// // Prevent Player from loosing Items inside the GUI
			// if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR ||
			// event.getAction() == InventoryAction.NOTHING) {
			// this.cancelEvent(event);
			// return;
			// }

			// What ClickType was it?
			ClickType type = null;
			boolean left = event.isLeftClick();
			boolean shift = event.isShiftClick();
			if (left && shift) {
				type = ClickType.SHIFT_LEFT;
			} else if (left && !shift) {
				type = ClickType.LEFT;
			} else if (!left && shift) {
				type = ClickType.SHIFT_RIGHT;
			} else if (!left && !shift) {
				type = ClickType.RIGHT;
			} else {
				type = ClickType.UNKNOWN;
			}

			// What items was the player holding while he clicked?
			ItemStack cursor = event.getCursor();
			ItemStack current = event.getCurrentItem();

			// In what GUI did he click?
			InventoryGUI inventoryGUI = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedInventoryGui(player);

			// On what position in the GUI did he click?
			int slot = event.getSlot();
			int slot_raw = event.getRawSlot();

			/**
			 * We decide in two different types of clicks. 1. Click in the lower
			 * inventory, owned by the player. 2. Click in the upper inventory,
			 * owned by the system.
			 */

			/**
			 * Lower Inventory (Players Inventory)
			 */
			if (slot != slot_raw || slot >= inventoryGUI.getSlots()) {
				// Inv slots are allowedBut, so the player can move some of his
				// items arround. Shift clicks
				// are get canceled, since they make items to push up inside
				// upper inventory
				if (event.isShiftClick()) {
					this.cancelEvent(event);
				}
				// Get the clicked ItemStack
				ItemStack clicked;
				try {
					clicked = event.getWhoClicked().getInventory().getItem(slot);
				} catch (Exception e) {
					clicked = null;
				}

				// Now it gets messy :(
				// The plan is to call the "onClickInLowerInventory(...)"
				// function of the currently
				// oplened GUI.
				// We could just do this by inventoryGUI.onClickInLow... but
				// this leads to some very
				// frustrating exceptions when someone trys to open or close new
				// GUIs inside the called
				// function. The workaround is simple. We store all important
				// variables in a final state and
				// wait with the execution for the next tick.
				final Player playerFinal = player;
				final ItemStack clickedFinal = clicked;
				final ItemStack cursorFinal = cursor;
				final ItemStack currentFinal = current;
				final ClickType clickTypFinal = type;
				final InventoryClickEvent eventFinal = event;
				final InventoryGUI inventoryGuiFinal = inventoryGUI;

				GlobalChestShop.plugin.getServer().getScheduler().runTaskLaterAsynchronously(GlobalChestShop.plugin, new Runnable() {
					public void run() {
						eventFinal.setCancelled(true); // This has only symbolic
														// purpose. The event
														// was already
														// handled last tick...

						// Now we call our function
						synchronized (playerFinal.getInventory()) {
							inventoryGuiFinal.onClickInLowerInventory(playerFinal, clickedFinal, cursorFinal, currentFinal, clickTypFinal, eventFinal);
						}

						// In case some developers try to mess with the event
						if (!eventFinal.isCancelled()) {
							eventFinal.setCancelled(true);
							throw new RuntimeException("You can not uncancel this InventoyClickEvent!");
						}
					}
				}, 0L);
				return;
			}

			/**
			 * Upper Inventory (System Inventory)
			 */
			// We know the position of the click (slot_raw), so we can ask the
			// inventoryGUI what button it
			// has storing on this position.
			Button button = inventoryGUI.getButton(slot_raw);

			// If there was no button, we stop.
			if (button == null) {
				this.cancelEvent(event);
				return;
			}

			// But if there was a Button, we call the Button's "onButtonClick"
			// function.
			this.cancelEvent(event);
			// Cancel to prevent players from exploiting the GUI.

			// The onButtonClick function is abstract an will be implemented by
			// a sub-class

			// We create a new Async-Thread to reduce lag.
			final Button buttons = button;
			final Player playerFinal = player;
			final ItemStack cursorFinal = cursor;
			final ItemStack currentFinal = current;
			final ClickType clickTypFinal = type;
			final InventoryClickEvent eventFinal = event;
			final InventoryGUI inventoryGuiFinal = inventoryGUI;
			GlobalChestShop.plugin.getServer().getScheduler().runTaskLaterAsynchronously(GlobalChestShop.plugin, new Runnable() {
				public void run() {

					// We measure the time difference since the last click to
					// prevent spam.
					Timestamp currentTime = new Timestamp(System.currentTimeMillis());
					Timestamp lastClick = clickSpamMap.get(playerFinal.getUniqueId());
					if (lastClick != null) {
						long timePassed = (currentTime.getTime() - lastClick.getTime()) / 1000;
						if (timePassed <= 5) {
							//playerFinal.sendMessage(ChatColor.RED + "stop spamming");
							return;
						}
					}
					clickSpamMap.remove(playerFinal.getUniqueId());
					clickSpamMap.put(playerFinal.getUniqueId(), currentTime);

					try {
						buttons.onPreButtonClick(inventoryGuiFinal, playerFinal, cursorFinal, currentFinal, clickTypFinal, eventFinal);
					} catch (Exception e) {
						throw e;
					} finally {
						clickSpamMap.remove(playerFinal.getUniqueId());
					}
					if (!eventFinal.isCancelled()) {
						// In case come devs try to mess with the
						// event.setCanceled();
						throw new RuntimeException("You can not uncancel this InventoyClickEvent!");
					}
				}
			}, 0);

			// //////////////// TEST
			// button.onButtonClick(inventoryGUI, player, cursor, current, type,
			// event);
			// if (!event.isCancelled()) {
			// this.cancelEvent(event);
			// // In case come devs try to mess with the event.setCanceled();
			// throw new
			// RuntimeException("You can not uncancel this InventoyClickEvent!");
			// }
		}
		// ///////////// TEST END
	}

	public void onMiddleClick(InventoryClickEvent event) {
		HumanEntity he = event.getWhoClicked();
		if (he instanceof Player) {
			Player player = (Player) he;
			if (GlobalChestShop.plugin.getGuiCore().has_Player_opened_InventoyGUI(player)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		HumanEntity he = event.getPlayer();
		if (he instanceof Player) {
			Player player = (Player) he;
			if (GlobalChestShop.plugin.getGuiCore().has_Player_opened_InventoyGUI(player)) {
				// At this point we know that the player just closed on of our
				// InventoryGUIs
				// So we can just go and ask the main class for the exact
				// instance.
				InventoryGUI inventoryGUI = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedInventoryGui(player);

				// An InventoryCloseEvent not allays means the GUI should be
				// closed.
				// Only if the player pressed the CloseButton or the ESC key is
				// ment to close, the GUI will
				// close.
				if (inventoryGUI.isCloseButtonPressed() || inventoryGUI.shouldEscKeyClosesTheGUI()) {
					inventoryGUI.close(player);
					// If the ESC Key is meant to lead to the previous GUI, we
					// return to parent GUI
				} else if (inventoryGUI.shouldEscKeyLeadsToPreviousGUI() && GlobalChestShop.plugin.getMainConfig().escButtonLeadsToPreviousWindow) {
					inventoryGUI.returnToParentGUI(player);
				} else if (!GlobalChestShop.plugin.getMainConfig().escButtonLeadsToPreviousWindow) {
					inventoryGUI.close(player);
				} else {
					// In all other cases the Event gets "canceled".
					// But since there is no cancel in InventoryCloseEvents, we
					// just oben the GUI agin.
					GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, inventoryGUI);
				}
			}
		}
	}

	private void cancelEvent(InventoryClickEvent event) {
		event.setCancelled(true);
		((Player) event.getWhoClicked()).updateInventory();
	}

	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent event) {
		if (event.isCancelled())
			return;
		InventoryHolder ih = event.getWhoClicked();
		if (!(ih instanceof Player))
			return;
		Player p = (Player) ih;
		if (GlobalChestShop.plugin.getGuiCore().has_Player_opened_InventoyGUI(p)) {
			// DragEvents will always be canceled. Otherwise players could
			// accidently trap and lose items
			// inside the GUI.
			event.setCancelled(true);
			p.updateInventory();
			return;
		}
	}

	/**
	 * Gets called every time a player signed (finished) a book
	 * 
	 * @param player
	 */
	public void handleBookSign(final Player player) {
		if (GlobalChestShop.plugin.getGuiCore().playerEntersTextInput(player)) {
			GlobalChestShop.plugin.getServer().getScheduler().runTaskLaterAsynchronously(GlobalChestShop.plugin, new Runnable() {
				// If you try to fully understand this, you should try out the
				// TextInput component.
				// It basically gives the player an empty book. While the player
				// has this book, he can do nothing until he filled signed it.
				// This function now removes the book from the players
				// inventory.
				public void run() {
					ItemStack item = player.getItemInHand();
					if (item != null) {
						BookMeta meta = (BookMeta) item.getItemMeta();
						if (meta.hasPages() && meta.getPageCount() >= 1 && !meta.getPage(1).equals("")) {
							TextInput textInput = GlobalChestShop.plugin.getGuiCore().getTextInput(player);
							textInput.onSend(player, meta.hasTitle() ? meta.getTitle() : "", meta.hasPages() ? meta.getPages().toArray(new String[meta.getPageCount()]) : new String[] { "" });
							GlobalChestShop.plugin.getGuiCore().closeTextInput(player);
						} else if (item.getType() == Material.WRITTEN_BOOK) {
							TextInput textInput = GlobalChestShop.plugin.getGuiCore().getTextInput(player);
							GlobalChestShop.plugin.getGuiCore().closeTextInput(player);
							GlobalChestShop.plugin.getGuiCore().openTextInput(player, textInput);
						}
					}
				}
			}, 0L);

		}
	}

	@EventHandler
	public void onSign(PlayerEditBookEvent event) {
		this.handleBookSign(event.getPlayer());
	}

	@EventHandler
	public void onScroll(PlayerItemHeldEvent event) {
		if (GlobalChestShop.plugin.getGuiCore().playerEntersTextInput(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();

		if (GlobalChestShop.plugin.getGuiCore().has_Player_opened_InventoyGUI(player)) {
			event.setCancelled(true);
			return;
		}

		if (GlobalChestShop.plugin.getGuiCore().playerEntersTextInput(player)) {
			event.getItemDrop().remove(); // TODO
			this.handleBookSign(player);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (GlobalChestShop.plugin.getGuiCore().playerEntersTextInput(e.getEntity())) {
			e.getDrops().remove(e.getEntity().getItemInHand());
			handleQuit(e.getEntity());
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.handleQuit(event.getPlayer());
	}

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		this.handleQuit(event.getPlayer());
	}

	private void handleQuit(Player player) {
		if (GlobalChestShop.plugin.getGuiCore().playerEntersTextInput(player)) {
			GlobalChestShop.plugin.getGuiCore().closeTextInput(player);
		}
		if (GlobalChestShop.plugin.getGuiCore().hashMap_String_Input_Awaiting.containsKey(player.getUniqueId())) {
			// If the player was about to use the TextInput component while he
			// left, the BlindnessEffect will be removed of him.
			// and the TextInput gets canceled.
			GlobalChestShop.plugin.getGuiCore().hashMap_String_Input_Awaiting.remove(player.getUniqueId());
			player.removePotionEffect(PotionEffectType.BLINDNESS);
		}
	}
}
