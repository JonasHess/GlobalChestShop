package eu.blockup.GlobalChestShop.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.MaterialBlockNotFoundException;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_CreateShop1;
import eu.blockup.GlobalChestShop.Util.GUI.ShopInfoPack;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;
import eu.blockup.GlobalChestShop.Util.Statements.MainConfig;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class ShopEventListener implements Listener {
	public static String													SIGN_HEADER_LOCAL_CHEST_SHOP	= "LocalChestShop";
	public static String													SIGN_HEADER_GLOBAL_CHEST_SHOP	= "GlobalChestShop";
	public static String													SIGN_HEADER_ADMIN_SHOP			= "AdminShop";
	public static String													META_SHOP_KEY					= "GlobalChestShop_ShopObject";
	public static String													META_CHEST_KEY					= "ChestOwner";
	private HashMap<String, HashMap<Integer, HashMap<Integer, List<Shop>>>>	chunkMap;

	public ShopEventListener() {
		super();
		// private HashMap<Integer, HashMap<Integer, Boolean>> chunkMap;
		this.chunkMap = new HashMap<String, HashMap<Integer, HashMap<Integer, List<Shop>>>>();
	}

	public void registerShopToChunkManager(final Shop shop) {
		GlobalChestShop.plugin.executeTaskSynchronous(new Runnable() {
			
			@Override
			public void run() {
				Chunk chunk = shop.getSignLocation().getChunk();
				String worldName = chunk.getWorld().getName();
				Integer x = chunk.getX();
				Integer z = chunk.getZ();
				HashMap<Integer, HashMap<Integer, List<Shop>>> worldMap = chunkMap.get(worldName);
				if (worldMap == null) {
					worldMap = new HashMap<Integer, HashMap<Integer, List<Shop>>>();
					chunkMap.put(worldName, worldMap);
				}
				HashMap<Integer, List<Shop>> xMap = worldMap.get(x);
				if (xMap == null) {
					xMap = new HashMap<Integer, List<Shop>>();
					worldMap.put(x, xMap);
				}
				List<Shop> shopList = xMap.get(z);
				if (shopList == null) {
					shopList = new ArrayList<Shop>();
					xMap.put(z, shopList);
				}
				shopList.add(shop);
				if (chunk.isLoaded()) {
					shop.spawn();
				}
				
			}
		});
	}

	public void deleteShopFromChunkManager(final Shop shop) {
		GlobalChestShop.plugin.executeTaskSynchronous(new Runnable() {
			
			@Override
			public void run() {
				List<Shop> liste = getShopsForChunk(shop.getSignLocation().getChunk());
				if (liste != null) {
					liste.remove(shop);
				}
			}
		});
	
	}

	private List<Shop> getShopsForChunk(Chunk chunk) {
		try {
			String worldName = chunk.getWorld().getName();
			Integer x = chunk.getX();
			Integer z = chunk.getZ();
			return this.chunkMap.get(worldName).get(x).get(z);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onChunkLoadEvent(ChunkLoadEvent event) {
		final List<Shop> x = this.getShopsForChunk(event.getChunk());
		if (x == null) {
			return;
		}
		for (Shop s : x) {
			s.spawn();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onChunkUnloadEvent(ChunkUnloadEvent event) {
		List<Shop> x = this.getShopsForChunk(event.getChunk());
		if (x == null) {
			return;
		}
		for (Shop s : x) {
			s.deSpawn();
		}

	}

	// Place Chest
	@EventHandler
	public void onBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (event.getBlock().getType().compareTo(Material.CHEST) == 0) {
			event.getBlock().setMetadata(ShopEventListener.META_CHEST_KEY, new FixedMetadataValue(GlobalChestShop.plugin, player.getUniqueId().toString()));
		}
	}

	private String unformatText(String coloaredText) {
		String result = ChatColor.stripColor(coloaredText);
		result = result.replace("[", "");
		result = result.replace("]", "");
		result = result.replace("(", "");
		result = result.replace(")", "");
		return result;
	}

	private boolean isHeaderGlobalShop(String headLine) {
		String key = unformatText(headLine);
		MainConfig mainConfig = GlobalChestShop.plugin.getMainConfig();
		return (key.equalsIgnoreCase(SIGN_HEADER_GLOBAL_CHEST_SHOP) || key.equalsIgnoreCase(SIGN_HEADER_ADMIN_SHOP) || key.equalsIgnoreCase(unformatText(mainConfig.firstLineGlobalChestShop)) || key.equalsIgnoreCase(unformatText(mainConfig.firstLineAdminShop)));
	}

	private boolean isHeaderLocalShop(String headLine) {
		String key = unformatText(headLine);
		MainConfig mainConfig = GlobalChestShop.plugin.getMainConfig();
		return (key.equalsIgnoreCase(SIGN_HEADER_LOCAL_CHEST_SHOP) || key.equalsIgnoreCase(unformatText(mainConfig.firstLineLocalChestShop)) || key.equalsIgnoreCase("ChestShop"));
	}

	// Create Sign
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChange(org.bukkit.event.block.SignChangeEvent event) {
		// clicked on a sign and signs enabled?
		if (event.getPlayer() != null) {

			Player player = event.getPlayer();
			String[] lines = event.getLines();

			String firstLine = lines[0].toUpperCase();

			// GlobalChestShop
			if (isHeaderGlobalShop(firstLine)) {
				if (!GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
					GlobalChestShop.plugin.permissionWarning(player);
					event.setCancelled(true);
					return;
				}
				event.setLine(0, ChatColor.RED + "[" + SIGN_HEADER_GLOBAL_CHEST_SHOP + "]");
				Location chestLocation = null;
				try {
					chestLocation = GlobalChestShop.plugin.getNearestBlockOnSameYAxis(Material.ENDER_CHEST, event.getBlock().getLocation()).getLocation();
				} catch (Exception e) {
					chestLocation = null;
				}
				ItemFrame itemFrame = GlobalChestShop.plugin.getNearestItemFrameOnSameYAxis(event.getBlock().getLocation(), true);
				Location itemFrameLocation = null;
				if (itemFrame != null)
					itemFrameLocation = itemFrame.getLocation();
				int worldGroup;
				try {
					worldGroup = GlobalChestShop.plugin.getworldGroup(player.getLocation());
				} catch (WorldHasNoWorldGroupException e) {
					InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Message_worldNotFound), true, player, null);
					if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
						player.sendMessage("You have to configure /plugins/GlobalChestShop/worldGroups.yml");
					}
					return;
				}
				Integer npcID = null;
				if (GlobalChestShop.plugin.isCitezensEnabled()) {
					npcID = GlobalChestShop.plugin.getnPC_Listener().findNearestNPC(event.getBlock().getLocation());
				}
				GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop1(new ShopInfoPack(EShopTyp.GlobalSignShop, worldGroup, player.getLocation(), event.getBlock().getLocation(), chestLocation, itemFrameLocation, npcID), null, worldGroup));
				return;
			}

			// LocalChestShop
			if (isHeaderLocalShop(firstLine)) {
				Chest chest = null;
				try {
					chest = (Chest) GlobalChestShop.plugin.getNearestBlockOnSameYAxis(Material.CHEST, event.getBlock().getLocation()).getState();
				} catch (MaterialBlockNotFoundException e) {
					event.setCancelled(true);
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_noChestFound));
					return;
				}
				if (!chest.hasMetadata(META_CHEST_KEY)) {
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_noChestOwner));
					event.setCancelled(true);
					return;
				}
				List<MetadataValue> data = chest.getMetadata(ShopEventListener.META_CHEST_KEY);
				if (data.size() == 0) {
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_noChestOwner));
					event.setCancelled(true);
					return;
				}
				UUID owner = UUID.fromString(data.get(0).asString());
				if (owner.compareTo(player.getUniqueId()) != 0) {
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_noChestOwner));
					event.setCancelled(true);

					return;
				}
				int worldGroup;
				try {
					worldGroup = GlobalChestShop.plugin.getworldGroup(player.getLocation());
				} catch (WorldHasNoWorldGroupException e) {
					InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Message_worldNotFound), true, player, null);
					if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
						player.sendMessage("You have to configure /plugins/GlobalChestShop/worldGroups.yml");
					}
					event.setCancelled(true);
					return;
				}

				if (!GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.PLAYER_CREATE_CHEST_SHOP + "." + worldGroup)) {
					player.sendMessage(Permissions.PLAYER_CREATE_CHEST_SHOP + "." + worldGroup);
					GlobalChestShop.plugin.permissionWarning(player);
					event.setCancelled(true);
					return;
				}
				Integer playerID = GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(player.getUniqueId());
				if (GlobalChestShop.plugin.getShopVerwaltung().getAmountOfShopPlayerOwnsInThisWorldGroup(playerID, worldGroup) >= GlobalChestShop.plugin.getMainConfig().amountOfLocalChestShopsPerPlayer) {
					if (!(GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN))) {
						Shop s = GlobalChestShop.plugin.getShopVerwaltung().getBrokenPlayerShop(player.getUniqueId());
						if (s == null) {
							InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Message_tooManyShops), true, player, null);
							event.setCancelled(true);
							return;
						} else {
							try {
								s.delete();
							} catch(Exception e) {
								GlobalChestShop.plugin.getLogger().log(Level.WARNING, e.getMessage() + "  " + e.getStackTrace());
							}
						}
					}
				}

				player.sendMessage(ChatColor.AQUA + "Shop created (" + ChatColor.RED + "" + GlobalChestShop.plugin.getShopVerwaltung().getAmountOfShopPlayerOwnsInThisWorldGroup(playerID, worldGroup) + ChatColor.AQUA + " / " + GlobalChestShop.plugin.getMainConfig().amountOfLocalChestShopsPerPlayer + ")");
				// player.sendMessage("Local Shop created");
				event.setCancelled(true);
				GlobalChestShop.plugin.getShopVerwaltung().createNewLocalChestShop(player, event.getBlock().getLocation(), chest.getLocation(), worldGroup);
				return;
			}
		}
	}

	// Block Click
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerClicksSign(PlayerInteractEvent event) {
		if (event.getPlayer() != null && event.getClickedBlock() != null && event.getClickedBlock().getType().compareTo(Material.AIR) != 0) {
			if (!event.getClickedBlock().hasMetadata(META_SHOP_KEY)) {
				return;
			}
			Player player = event.getPlayer();
			List<MetadataValue> data = event.getClickedBlock().getMetadata(ShopEventListener.META_SHOP_KEY);
			Shop shop;
			try {
				shop = (Shop) data.get(0).value();
			} catch (ClassCastException e) {
				return;
			}
			event.setCancelled(true);
			if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				shop.onInteractLeftClick(player, null);
			} else {
				shop.onInteractRightClick(player, null);
			}
		}
	}

	// Entity Interact
	@EventHandler
	public void onPlayerInteractFrame(PlayerInteractEntityEvent event) {
		if (!event.getRightClicked().hasMetadata(META_SHOP_KEY)) {
			return;
		}
		Player player = event.getPlayer();
		List<MetadataValue> data = event.getRightClicked().getMetadata(ShopEventListener.META_SHOP_KEY);
		Shop shop;
		try {
			shop = (Shop) data.get(0).value();
		} catch (ClassCastException e) {
			return;
		}
		if (player.isSneaking()) {
			if (event.getRightClicked() instanceof ItemFrame) {
				ItemFrame itemframe = (ItemFrame) event.getRightClicked();
				itemframe.setRotation(itemframe.getRotation().rotateCounterClockwise());
				return;
			}
		}
		event.setCancelled(true);
		shop.onInteractRightClick(player, null);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPickup(PlayerPickupItemEvent event) {
		if (event.getItem().hasMetadata(ShopEventListener.META_SHOP_KEY)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getEntity().hasMetadata(ShopEventListener.META_SHOP_KEY)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if (event.getBlock().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if (event.getBlock().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
		if (event.getBlock().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockGrowEvent(BlockGrowEvent event) {
		if (event.getBlock().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockIgniteEvent(BlockIgniteEvent event) {
		if (event.getBlock().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockDamageEvent(BlockDamageEvent event) {
		if (event.getBlock().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		List<Block> list = new LinkedList<Block>();
		for (Block b : event.blockList()) {
			if (b.hasMetadata(META_SHOP_KEY)) {
				list.add(b);
			}
		}
		for (Block b : list) {
			event.blockList().remove(b);
		}
		list.clear();
		list = null;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPistonDupe(BlockPistonExtendEvent event) {
		for (Block b : event.getBlocks()) {
			if (b.hasMetadata(META_SHOP_KEY)) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent event) {
		if (event.getBlock().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.getEntity().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			Entity damager = event.getDamager();
			if (damager instanceof Player) {
				List<MetadataValue> data = event.getEntity().getMetadata(ShopEventListener.META_SHOP_KEY);
				Shop shop;
				try {
					shop = (Shop) data.get(0).value();
				} catch (ClassCastException e) {
					return;
				}
				Player player = (Player) damager;
				if (player.isSneaking()) {
					if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
						if (event.getEntity() instanceof ItemFrame) {
							ItemFrame itemframe = (ItemFrame) event.getEntity();
							itemframe.setRotation(itemframe.getRotation().rotateClockwise());
							;
							return;
						}
					}
				}
				shop.onInteractLeftClick(player, null);
			}
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingPlaceEvent(HangingPlaceEvent event) {
		if (event.getEntity().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onItemDespawnEvent(ItemDespawnEvent event) {
		if (event.getEntity().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event) {
		if (event.getEntity().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingBreakEvent(HangingBreakEvent event) {
		if (event.getEntity().hasMetadata(META_SHOP_KEY)) {
			event.setCancelled(true);
			return;
		}
	}

}