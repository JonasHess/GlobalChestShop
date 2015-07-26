package eu.blockup.GlobalChestShop.Util.GUI.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.EventListener;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StringInput;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.TextInput;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

//class Freezer<T> {
//  private boolean finished = false;
//  private T data = null;
//  
//  public synchronized boolean isFinished() {
//    System.out.println("Freezer responds: " + this.finished);
//    return finished;
//  }
//  public synchronized void setFinished(boolean finished) {
//    System.out.println("Freezer finished!");
//    this.finished = finished;
//  }
//  public synchronized T getData() {
//    return data;
//  }
//  public synchronized void setData(T data) {
//    this.data = data;
//  }
//  
//}

public class GuiCore implements Listener {

	// HashMaps, in denen geöffnete GUIS hinterlegt werden
	private Map<UUID, InventoryGUI>	hashMap_Player_InventoryGUI;
	private Map<UUID, Inventory>	hashMap_Player_BukkitInventory;
	public Map<UUID, StringInput>	hashMap_String_Input_Awaiting;

	public Map<UUID, TextInput>		hashMap_textFields;
	public Map<UUID, ItemStack>		hashMap_handSave;
	public static boolean			isEnabled;

	public void addGuiToHashMaps(final InventoryGUI gui, final Inventory inv, final UUID uuid) {
		synchronized (hashMap_Player_BukkitInventory) {
			hashMap_Player_BukkitInventory.put(uuid, inv);
		}
		synchronized (hashMap_Player_InventoryGUI) {
			hashMap_Player_InventoryGUI.put(uuid, gui);
		}
	}

	public Inventory getPlayersOpenedBukkitInventory(final UUID uuid) {
		Inventory result;
		synchronized (hashMap_Player_BukkitInventory) {
			result =  hashMap_Player_BukkitInventory.get(uuid);
		}
		return result;
	}

	public InventoryGUI getPlayersOpenedInventoryGui(final UUID uuid) {
		InventoryGUI result;
		synchronized (hashMap_Player_InventoryGUI) {
			result =hashMap_Player_InventoryGUI.get(uuid);
		}
		return result;
	}

	// public void addGuiToHashMaps(final InventoryGUI gui, final Inventory inv,
	// final UUID uuid) {
	// final Freezer<Inventory> freezer = new Freezer<Inventory>();
	// Bukkit.getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new
	// Runnable() {
	// @Override
	// public void run() {
	// hashMap_Player_BukkitInventory.put(uuid, inv);
	// hashMap_Player_InventoryGUI.put(uuid, gui);
	// freezer.setFinished(true);
	// }
	// });
	//
	// while (!freezer.isFinished()) {
	// try {
	// System.out.println("sleep");
	// Thread.sleep(20);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// public Inventory getPlayersOpenedBukkitInventory(final UUID uuid) {
	// final Freezer<Inventory> freezer = new Freezer<Inventory>();
	// Bukkit.getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new
	// Runnable() {
	// @Override
	// public void run() {
	// freezer.setData(hashMap_Player_BukkitInventory.get(uuid));
	// freezer.setFinished(true);
	// }
	// });
	//
	// while (!freezer.isFinished()) {
	// try {
	// System.out.println("sleep");
	// Thread.sleep(20);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// return freezer.getData();
	// }
	// public InventoryGUI getPlayersOpenedInventoryGui(final UUID uuid) {
	// final Freezer<InventoryGUI> freezer = new Freezer<InventoryGUI>();
	// Bukkit.getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new
	// Runnable() {
	// @Override
	// public void run() {
	// freezer.setData(hashMap_Player_InventoryGUI.get(uuid));
	// freezer.setFinished(true);
	// }
	// });
	//
	// while (!freezer.isFinished()) {
	// try {
	// System.out.println("sleep");
	// Thread.sleep(20);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// return freezer.getData();
	// }

	public InventoryGUI getPlayersOpenedInventoryGui(Player player) {
		return getPlayersOpenedInventoryGui(player.getUniqueId());
	}

	public boolean has_Player_opened_InventoyGUI(Player player) {
		return getPlayersOpenedInventoryGui(player) != null;
	}

	public void onEnable() {

		GlobalChestShop.plugin.getServer().getPluginManager().registerEvents(this, GlobalChestShop.plugin);

		hashMap_Player_InventoryGUI = java.util.Collections.synchronizedMap(new HashMap<UUID, InventoryGUI>());
		hashMap_Player_BukkitInventory = java.util.Collections.synchronizedMap(new HashMap<UUID, Inventory>());
		hashMap_String_Input_Awaiting = java.util.Collections.synchronizedMap(new HashMap<UUID, StringInput>());

		this.hashMap_textFields = java.util.Collections.synchronizedMap(new HashMap<UUID, TextInput>());
		this.hashMap_handSave = java.util.Collections.synchronizedMap(new HashMap<UUID, ItemStack>());
		// hashMap_Player_InventoryGUI = new HashMap<UUID, InventoryGUI>();
		// hashMap_Player_BukkitInventory = new HashMap<UUID, Inventory>();
		// hashMap_String_Input_Awaiting = new HashMap<UUID, StringInput>();
		//
		// this.hashMap_textFields = new HashMap<UUID, TextInput>();
		// this.hashMap_handSave = new HashMap<UUID, ItemStack>();

		GlobalChestShop.plugin.getServer().getPluginManager().registerEvents(new EventListener(), GlobalChestShop.plugin);

		GlobalChestShop.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(GlobalChestShop.plugin, new Runnable() {

			@Override
			public void run() {
				synchronized (hashMap_Player_InventoryGUI) {
					for (Map.Entry<UUID, InventoryGUI> entry : hashMap_Player_InventoryGUI.entrySet()) {
						UUID key = entry.getKey();
						InventoryGUI value = entry.getValue();
						value.setTickCount(value.getTickCount() + 1);
						value.onPreTickEvent(GlobalChestShop.plugin.getServer().getPlayer(key), value.getTickCount());
					}
				}
			}
		}, 1L, 1L);
		isEnabled = true;
	}

	public void onDisable() {
		isEnabled = false;
		synchronized (hashMap_Player_BukkitInventory) {
			this.hashMap_Player_BukkitInventory.clear();
			for (Map.Entry<UUID, Inventory> entry : hashMap_Player_BukkitInventory.entrySet()) {
				entry.getValue().clear();
				OfflinePlayer playerOffline = Bukkit.getServer().getOfflinePlayer(entry.getKey());
				if (playerOffline.isOnline()) {
					Player playerOnline = playerOffline.getPlayer();
					playerOnline.updateInventory();
				}
			}
		}
		this.hashMap_handSave.clear();
		this.hashMap_Player_InventoryGUI.clear();

		try {
			for (Player player : GlobalChestShop.plugin.getServer().getOnlinePlayers()) {
				player.closeInventory();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void open_InventoryGUI_infronofOtherGUI(final Player player, InventoryGUI oldGUI, InventoryGUI newGUI) {
		newGUI.setParentGUI(oldGUI);
		this.open_InventoyGUI(player, newGUI);
	}

	public void open_InventoryGUI_whileOtherGUIisOpen(final Player player, InventoryGUI newGUI) {
		InventoryGUI oldGUI = this.getPlayersOpenedInventoryGui(player);
		this.open_InventoryGUI_infronofOtherGUI(player, oldGUI, newGUI);
	}

	public void open_InventoyGUI(final Player player, InventoryGUI inventoryGUI) {
		if (!isEnabled) {
			return;
		}
		if (player == null) {
			return;
		}

		close_InventoyGUI(player);
		inventoryGUI.isOpened = true;
		final InventoryGUI menu = inventoryGUI;
		GlobalChestShop.plugin.getServer().getScheduler().runTaskAsynchronously(GlobalChestShop.plugin, new Runnable() {
			@Override
			public void run() {
				menu.onPlayerOpensTheGUI(player);
				menu.refresh(player);
				menu.setTickCount(0);
			}
		});
	}

	public void close_InventoyGUI(final Player player) {
		if (player == null) {
			return;
		}
		if (has_Player_opened_InventoyGUI(player)) {
			synchronized (hashMap_Player_InventoryGUI) {
				InventoryGUI gui = hashMap_Player_InventoryGUI.remove(player.getUniqueId());
				gui.onPlayerLeavesTheGUI(player);
				gui.isOpened = false;
			}
			synchronized (hashMap_Player_BukkitInventory) {
				hashMap_Player_BukkitInventory.remove(player.getUniqueId());
			}
			if (!GuiCore.isEnabled) {
				return;
			}
			// Bukkit.getScheduler().runTask(GlobalChestShop.plugin, new
			// Runnable() {
			// @Override
			// public void run() {
			ItemStack cursor = player.getItemOnCursor();
			player.setItemOnCursor(null);
			player.closeInventory();
			player.setItemOnCursor(cursor);
			// }
			// });

		}
	}

	// public void close_InventoyGUI(final Player player) {
	//
	//
	// System.out.println("CLOSE GUI");
	// try {
	// // System.out.println("sleep");
	// Thread.sleep(0);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// if (has_Player_opened_InventoyGUI(player)) {
	// hashMap_Player_InventoryGUI.remove(player.getUniqueId()).onPlayerLeavesTheGUI(player);
	// hashMap_Player_BukkitInventory.remove(player.getUniqueId());
	//
	//
	// final Freezer freezer = new Freezer();
	// Bukkit.getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new
	// Runnable() {
	// @Override
	// public void run() {
	// ItemStack cursor = player.getItemOnCursor();
	// player.setItemOnCursor(null);
	// player.closeInventory();
	// player.setItemOnCursor(cursor);
	// freezer.setFinished(true);
	// }
	// });
	//
	// while (!freezer.isFinished()) {
	// try {
	// System.out.println("sleep");
	// Thread.sleep(20);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e) {

		StringInput u = this.hashMap_String_Input_Awaiting.get(e.getPlayer().getUniqueId());
		if (u != null) {
			u.onPlayerChatEvent(e);
			;
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		StringInput u;
		synchronized (hashMap_String_Input_Awaiting) {
			u = this.hashMap_String_Input_Awaiting.get(e.getPlayer().getUniqueId());
		}
		if (u != null) {
			u.onPlayerMove(e);
		}
	}

	public boolean openTextInput(Player player, TextInput textInput) {

		if (player == null) {
			return false;
		}

		if (this.playerEntersTextInput(player)) {
			return false;
		}
		int firstEmptySlot = player.getInventory().firstEmpty();
		if (firstEmptySlot == -1) {
			player.sendMessage("No free slot in your inventory!");
			return false;
		}

		close_InventoyGUI(player);

		PlayerInventory pi = player.getInventory();
		int slot = 0;
		ItemStack item = null;
		while (slot < 9) {
			item = pi.getItem(slot);

			if (item == null || item.getType() == Material.AIR) {
				break;
			}
			slot++;
		}
		if (slot == 9) {
			slot = 0;
		}
		pi.setHeldItemSlot(slot);
		ItemStack firstItem = pi.getItemInHand();
		this.hashMap_handSave.put(player.getUniqueId(), firstItem);
		pi.setItem(slot, textInput.toItemStack());
		pi.addItem(firstItem);
		player.updateInventory();

		if (textInput.hasStartMessage()) {
			player.sendMessage(textInput.getStartMessage());
		}
		this.hashMap_textFields.put(player.getUniqueId(), textInput);
		return true;
	}

	public boolean closeTextInput(Player player) {
		if (player == null) {
			return false;
		}

		if (!this.playerEntersTextInput(player)) {
			return false;
		}

		player.sendMessage("removed " + player.getItemInHand());
		player.getInventory().remove(player.getItemInHand());
		ItemStack prevMovedItem = this.hashMap_handSave.get(player.getUniqueId());
		if (player.getInventory().contains(prevMovedItem)) {
			player.getInventory().remove(prevMovedItem);
			player.setItemInHand(prevMovedItem);
		}
		player.updateInventory();

		this.hashMap_textFields.remove(player.getUniqueId());
		this.hashMap_handSave.remove(player.getUniqueId());
		return true;
	}

	public TextInput getTextInput(Player player) {
		return this.hashMap_textFields.get(player.getUniqueId());
	}

	public boolean playerEntersTextInput(Player player) {
		return this.getTextInput(player) != null;
	}

}
