package eu.blockup.GlobalChestShop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.AuctionController;
import eu.blockup.GlobalChestShop.Util.CustomCategoryController;
import eu.blockup.GlobalChestShop.Util.DefaultCategoryController;
import eu.blockup.GlobalChestShop.Util.ItemController;
import eu.blockup.GlobalChestShop.Util.MySqlConnector;
import eu.blockup.GlobalChestShop.Util.PlayerController;
import eu.blockup.GlobalChestShop.Util.ShopCommandExecutor;
import eu.blockup.GlobalChestShop.Util.ShopController;
import eu.blockup.GlobalChestShop.Util.ShopEventListener;
import eu.blockup.GlobalChestShop.Util.TimeAgo;
import eu.blockup.GlobalChestShop.Util.WorldGroupController;
import eu.blockup.GlobalChestShop.Util.Exceptions.*;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceEngine;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_AdminShopBuy;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_AdminShopCreate;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_AdminShopEdit;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_AuctionPage;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GuiCore;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Metrics.Metrics;
import eu.blockup.GlobalChestShop.Util.SoftDependecies.MoreTntController;
import eu.blockup.GlobalChestShop.Util.SoftDependecies.NPC_Listener;
import eu.blockup.GlobalChestShop.Util.Statements.AuctionLimitController;
import eu.blockup.GlobalChestShop.Util.Statements.EMessageTyp;
import eu.blockup.GlobalChestShop.Util.Statements.LanguageController;
import eu.blockup.GlobalChestShop.Util.Statements.MainConfig;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GlobalChestShop extends JavaPlugin {

	public ItemController							itemController;
	private PlayerController						playerController;
	private Map<Integer, AuctionController>			hashMapAuctionController;
	private Map<Integer, DefaultCategoryController>	hashMapDefaultCategoryControllers;
	private Economy									econ;
	public static Permission						perms;
	public UUID										adminShopUUID		= new UUID(0L, 0L);
	public MainConfig								mainConfig;
	public static GlobalChestShop					plugin;
	private MySqlConnector							mysql;
	private GuiCore									guiCore;
	public static LanguageController				text;
	private ShopController							shopController;
	private CustomCategoryController				customCategoryController;
	private WorldGroupController					worldGroupController;
	private boolean									citezensEnabled;
	public boolean									moreTntEnabled;
	public MoreTntController						moreTntController;
	private boolean									holographicDisplaysEnabled;
	private NPC_Listener							nPC_Listener;
	private PriceEngine								priceEngine;
	private AuctionLimitController					auctionLimitController;
	private ShopEventListener						shopEventListener;

	public long										debugTickStartTime;

	public static String							duplicatedItemMeta	= "GCS_GUI_ITEM";

	public void reload() {
		this.getServer().getPluginManager().disablePlugin(this);
		this.getServer().getPluginManager().enablePlugin(this);
	}


	@Override
	public void onEnable() {
		GlobalChestShop.plugin = this;

		// < ----------- DEBUG ----------------->
		boolean DEBUG = false;
		if (DEBUG) {
			debugTickStartTime = System.currentTimeMillis();
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(GlobalChestShop.plugin, new Runnable() {
				@Override
				public void run() {
					long estimatedTime = System.currentTimeMillis() - debugTickStartTime;
					debugTickStartTime = System.currentTimeMillis();
					if (estimatedTime > 0) {
						double ticksPerSecond = 1000 / estimatedTime;
						if (ticksPerSecond < 18.95) {
							System.out.println("LAG!: " + ticksPerSecond + " tps");
						}
					}
				}
			}, 1, 1);
		}
		// < ----------------------------------------->

		// create config files
		try {
			this.copyFileToPluginDir("config.yml");
			this.copyFileToPluginDir("worldGroups.yml");
			this.copyFileToPluginDir("language.yml");
			this.copyFileToPluginDir("auctionLimits.yml");
		} catch (Exception e1) {
			getLogger().log(Level.WARNING, "ERROR reading / creating config files!");
			e1.printStackTrace();
			this.setEnabled(false);
			return;
		}

		// HolographicDisplays
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			getLogger().log(Level.WARNING, "*** HolographicDisplays is not installed or not enabled. ***");
			this.holographicDisplaysEnabled = false;
		} else {
			this.holographicDisplaysEnabled = true;
		}

		// Citizens
		if (getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			getLogger().log(Level.WARNING, "*** Citizens is not installed or not enabled ***");
			this.citezensEnabled = false;
		} else {
			this.citezensEnabled = true;
		}

		// Vault
		if (!setupVault()) {
			getLogger().log(Level.WARNING, "********************************************");
			getLogger().log(Level.WARNING, "[ERROR] " + "   Vault is not installed or not enabled");
			getLogger().log(Level.WARNING, "********************************************");
			this.setEnabled(false);
		}

		// More TNTs Plugin
		if (getServer().getPluginManager().getPlugin("MoreTNTs") == null || getServer().getPluginManager().getPlugin("MoreTNTs").isEnabled() == false) {
			this.moreTntEnabled = false;
		} else {
			this.moreTntEnabled = true;
			this.moreTntController = new MoreTntController();
		}

		// load mainConfig files

		// Language file
		try {
			this.mainConfig = new MainConfig();
			GlobalChestShop.text = new LanguageController();
		} catch (Exception e1) {
			e1.printStackTrace();
			this.setEnabled(false);
			return;
		}

		// Database connect
		try {
			this.mysql = new MySqlConnector();
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().log(Level.WARNING, "********************************************");
			getLogger().log(Level.WARNING, "[ERROR] " + "Unable to connect the database");
			getLogger().log(Level.WARNING, "[ERROR] " + this.getName() + " stopped working!");
			getLogger().log(Level.WARNING, "********************************************");
			this.mysql = null;
			this.setEnabled(false);
			return;
		}

		// Initialize utilities

		// EventListener
		this.shopEventListener = new ShopEventListener();
		this.getServer().getPluginManager().registerEvents(shopEventListener, this);

		// Command Executor
		ShopCommandExecutor ce = new ShopCommandExecutor();
		this.getCommand("globalchestshop").setExecutor(ce);

		this.itemController = new ItemController();
		this.playerController = new PlayerController();
		this.playerController.getPlayerIdFromUUID(adminShopUUID);
		this.worldGroupController = new WorldGroupController();
		this.customCategoryController = new CustomCategoryController();
		this.hashMapAuctionController = java.util.Collections.synchronizedMap(new HashMap<Integer, AuctionController>());
		this.shopController = new ShopController();
		this.hashMapDefaultCategoryControllers = java.util.Collections.synchronizedMap(new HashMap<Integer, DefaultCategoryController>());
		this.playHeadChache = java.util.Collections.synchronizedMap(new HashMap<Integer, ItemStack>());

		this.getAuctionController(1); // Starts caching for more performance.
		this.getAuctionController(2);

		// Citizens
		if (this.isCitezensEnabled()) {
			this.nPC_Listener = new NPC_Listener();
			this.getServer().getPluginManager().registerEvents(nPC_Listener, this);
		}

		// PriceEngine
		this.priceEngine = new PriceEngine();

		// Auction LimitController
		this.auctionLimitController = new AuctionLimitController();

		// GUI Core
		this.guiCore = new GuiCore();
		this.guiCore.onEnable();

		// Metrics
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (Exception e) {
			// Failed to submit the stats :-(
			e.printStackTrace();
		}

	}

	@Override
	public void onDisable() {
		if (mysql == null) {
			return;
		}
		this.guiCore.onDisable();
		this.guiCore = null;
		this.playerController = null;
		this.shopController.disable();
		this.shopController = null;
		this.customCategoryController = null;
		this.itemController = null;
		this.hashMapAuctionController.clear();
		this.hashMapAuctionController = null;
		this.hashMapDefaultCategoryControllers.clear();
		this.hashMapDefaultCategoryControllers = null;
		this.nPC_Listener = null;
		this.econ = null;
		GlobalChestShop.text = null;
		this.worldGroupController = null;
		this.mysql.closeAllConnections();
		this.mysql = null;
		this.mainConfig = null;
		GlobalChestShop.plugin = null;
	}

	public void deleteAllPlayerShops() {
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "-------------------------------------------");
		GlobalChestShop.plugin.getShopVerwaltung().deleteAllPlayerShops();
		Map<Integer, AuctionController> h = GlobalChestShop.plugin.getHashMapAuctionController();
		synchronized (h) {
			for (Map.Entry<Integer, AuctionController> entry : h.entrySet()) {
				entry.getValue().deleteAllPlayerAuctions();
			}
		}
		Bukkit.getServer().broadcastMessage("");
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "Please restart the server as soon as possible");
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "--------------------------------------------");
	}

	private boolean setupVault() {
		if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider == null) {
			return false;
		}

		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider == null) {
			return false;
		}

		perms = permissionProvider.getProvider();
		econ = economyProvider.getProvider();
		return (econ != null && perms != null);
	}

	private void copyFileToPluginDir(String filename) throws Exception {
		File file = new File(getDataFolder() + "/" + filename);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
			copy(getResource(filename), file);
		}
	}

	private void copy(InputStream in, File file) throws Exception {
		OutputStream out = new FileOutputStream(file);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		in.close();
	}

	public void openAdminShopOnlyGUI(InventoryGUI previousGUI, Player player, ItemStack item, int worldGroup, double multiplier) {
		Auction adminShop = this.getAuctionController(worldGroup).getAdminShopFromItemStack(item);
		if (adminShop == null || adminShop.isEndent()) {
			if (validatePermissionCheck(player, Permissions.ADMIN)) {
				if (adminShop == null) {
					new GUI_AdminShopCreate(item, previousGUI, worldGroup).open(player);
				} else {
					new GUI_AdminShopEdit(adminShop, previousGUI, worldGroup).open(player);
				}
				return;
			} else {
				InventoryGUI.warning(GlobalChestShop.text.get(text.Message_AdminShop_under_Construction), false, player, previousGUI);
				return;
			}
		} else {
			new GUI_AdminShopBuy(adminShop, multiplier, previousGUI, worldGroup).open(player);
		}
	}

	public void executeTaskSynchronousWhileBlocking(final Runnable runnable, int i) {
		final CountDownLatch latch = new CountDownLatch(1);
		GlobalChestShop.plugin.getServer().getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new Runnable() {
			public void run() {
				runnable.run();
				latch.countDown();
			}
		}, 1L);
		try {
			latch.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void executeTaskSynchronous(final Runnable runnable) {
		GlobalChestShop.plugin.getServer().getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, runnable, 1L);
	}

	public void executeTaskAsynchronous(final Runnable runnable) {
		GlobalChestShop.plugin.getServer().getScheduler().runTask(GlobalChestShop.plugin, runnable);
	}

	public void openNormalAuctionGUI(InventoryGUI previousGUI, Player player, ItemStack item, int worldGroup, boolean newAuctions, boolean adminShopOnly, double multiplier) {

		class NormalAuction extends GUI_AuctionPage {
			private ItemStack	item;
			private boolean		newAuctions;
			private boolean		adminShopOnly;

			public NormalAuction(String title, ItemStack displayItem, InventoryGUI parentGUI, boolean highlightAdminShops, Integer worldGroup, ItemStack item, boolean newAuctions, boolean adminShopOnly, double multiplier) {
				super(title, displayItem, parentGUI, highlightAdminShops, worldGroup, false, multiplier);
				this.item = item;
				this.newAuctions = newAuctions;
				this.adminShopOnly = adminShopOnly;
			}

			@Override
			public List<Auction> getRefreshedObjectList() {
				return GlobalChestShop.plugin.getAuctionController(worldGroup).getAllActiveAuctionForItemStack(item, false);
			}

			@Override
			public boolean drawMoneyButton(Player player) {
				return true;
			}

			@Override
			public boolean drawHistoryButton(Player player) {
				return this.newAuctions;
			}

			@Override
			public boolean drawDisplayItemButton(Player player) {
				return true;
			}

			@Override
			public boolean drawAuctionCreateButton(Player player) {
				return this.newAuctions;
			}

			@Override
			public boolean drawAdminShopCreateButton() {
				return this.adminShopOnly || GlobalChestShop.plugin.getMainConfig().showAdminshopsInsideGlobalShops;
			}

			@Override
			public ItemStack getPresetAdminShopItem() {
				return this.item;
			}

			@Override
			public boolean showRunningAuctionsInHistory() {
				return true;
			}
		}
		new NormalAuction(this.getItemStackDisplayName(item), item, previousGUI, true, worldGroup, item, newAuctions, adminShopOnly, multiplier).open(player);
		;
	}

	public void handleFatalException(Exception e) {
		GlobalChestShop.plugin.getLogger().log(Level.WARNING, this.getName() + " has problems connecting the database. To prevent further damage, the plugin will disable itself.");
		e.printStackTrace();
		this.setEnabled(false);
		return;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack convertRandomStringToItemStack(String input, CommandSender cs) throws ItemStackNotFoundException {
		ItemStack result = null;
		try {
			if (input == null) {
				throw new ItemStackNotFoundException();
			}

			// Material.Name
			Material mat = Material.getMaterial(input.toUpperCase());
			if (mat != null) {
				result = new ItemStack(mat);

				// Hand
			} else if (cs != null) {
				if (input.equalsIgnoreCase("hand")) {
					if (!(cs instanceof Player)) {
						throw new ItemStackNotFoundException();
					}
					if (((Player) cs).getItemInHand() == null) {
						throw new ItemStackNotFoundException();
					}
					if (((Player) cs).getItemInHand().getType() == Material.AIR) {
						throw new ItemStackNotFoundException();
					}

					return ((Player) cs).getItemInHand().clone();
				}

				// id:data
			} else if (input.contains(":")) {
				String[] a = input.split(":");
				int id = Integer.valueOf(a[0]);
				int damage = 0;
				if (a.length == 2) {
					damage = Integer.valueOf(a[1]);
					// ItemStack result = new ItemStack(id, amount, (short) 0,
					// (byte) damage);
					result = new ItemStack(id);
					result.setDurability((short) damage);
					return result;
				} else {
					return null;
				}

				// id
			} else if (isStringNumeric(input)) {
				try {
					result = new ItemStack(Integer.parseInt(input));
				} catch (NumberFormatException e) {
					result = null;
				}
			}

			if (result == null) {
				throw new ItemStackNotFoundException();
			} else {
				if (result != null && result.getType().equals(Material.AIR)) {
					throw new ItemStackNotFoundException();
				}
			}
		} catch (Exception e) {
			return new ItemStack(Material.STONE);
		}
		return result;
	}

	public static ItemStack convertItemIdStringToItemstack(String itemIDString, int amount) {
		String[] a = itemIDString.split(":");
		int id = Integer.valueOf(a[0]);
		int damage = 0;
		if (a.length == 2) {
			damage = Integer.valueOf(a[1]);
			// ItemStack result = new ItemStack(id, amount, (short) 0, (byte)
			// damage);
			@SuppressWarnings("deprecation")
			ItemStack result = new ItemStack(id, amount);
			result.setDurability((short) damage);
			return result;
		} else {
			return null;
		}
	}

	public static boolean isStringNumeric(final String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public int getworldGroup(Location location) throws WorldHasNoWorldGroupException {
		Integer worldGroup;
		try {
			String world = location.getWorld().getName();
			worldGroup = this.worldGroupController.getworldGroup(world);
			if (worldGroup == null)
				throw new NullPointerException();
		} catch (Exception e) {
			// e.printStackTrace();
			throw new WorldHasNoWorldGroupException();
		}
		return worldGroup;
	}

	public int getworldGroup(UUID uuid) throws WorldHasNoWorldGroupException {
		Integer worldGroup;
		try {
			String world = this.getServer().getPlayer(uuid).getLocation().getWorld().getName();
			worldGroup = this.worldGroupController.getworldGroup(world);
			if (worldGroup == null)
				throw new NullPointerException();
		} catch (Exception e) {
			throw new WorldHasNoWorldGroupException();
		}
		return worldGroup;

	}

	// Get nearest ItemFrame
	public ItemFrame getNearestItemFrameOnSameYAxis(Location signLocation, boolean checkMeta) {
		if (signLocation == null)
			return null;
		double radius = 4.0;
		ItemFrame itemFrame = null;
		double smallestDistance = 99D;
		try {
			Collection<ItemFrame> near = signLocation.getWorld().getEntitiesByClass(ItemFrame.class);
			for (Entity e : near) {
				double distance = e.getLocation().distance(signLocation);
				if (e instanceof ItemFrame && distance <= radius) {
					// if (e.getLocation().getBlockX() ==
					// signLocation.getBlockX()
					// && e.getLocation().getBlockZ() ==
					// signLocation.getBlockZ()) {
					if (checkMeta) {
						if (e.hasMetadata(ShopEventListener.META_SHOP_KEY)) {
							continue;
						}
					}
					if (distance < smallestDistance) {
						itemFrame = (ItemFrame) e;
						smallestDistance = distance;
					}
					// }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemFrame;
	}

	// Get Nearest Block with Material X
	public Block getNearestBlockOnSameYAxis(Material material, Location location) throws MaterialBlockNotFoundException {
		int radius = 2;
		Block result = null;
		double smallestDistance = 99D;
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (x != 0 || y != 0 || z != 0) {
						Block b = location.getBlock().getRelative(x, y, z);
						if (b != null && b.getState().getType().compareTo(material) == 0) {
							double distance = b.getLocation().distance(location);
							if (distance < smallestDistance) {
								result = b;
								smallestDistance = distance;
							}
						}
					}
				}
			}
		}
		if (result == null) {
			throw new MaterialBlockNotFoundException();
		}
		return result;
	}

	private Map<Integer, ItemStack>	playHeadChache;

	
	public ItemStack getPlayerHead(UUID uuid, boolean removeFromInventoryWhenPickedUp) {
		return this.getPlayerHead(GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(uuid), removeFromInventoryWhenPickedUp);
		
	}
	public ItemStack getPlayerHead(Integer playerID, boolean removeFromInventoryWhenPickedUp) {

		if (playerID == null)
			return new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		synchronized (playHeadChache) {
			ItemStack item = playHeadChache.get(playerID);
			if (item != null) {
				return item;
			}
			item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			String playerName = GlobalChestShop.plugin.getNameOfPlayer(playerID);
			try {
				SkullMeta meta = (SkullMeta) item.getItemMeta();
				if (playerName == null) {
					playerName = "Unknown";
				}
				meta.setOwner(playerName);
				if (removeFromInventoryWhenPickedUp) {
					meta.setDisplayName("GlobalChestShop-Protected");
				} else {
					meta.setDisplayName(ChatColor.RESET + playerName);
				}
				item.setItemMeta(meta);
			} catch (Exception e) {
				// can't reach Mojang-Server
			}
			this.playHeadChache.put(playerID, item);
			return item;
		}
	}

	public String getNameOfPlayer(UUID uuid) {
		String result = "Unknown";
		try {
			if (uuid.compareTo(GlobalChestShop.plugin.adminShopUUID) == 0)
				return GlobalChestShop.text.get(GlobalChestShop.text.GUI_Title_AdminShop);
			OfflinePlayer offlinePlayer = this.getServer().getOfflinePlayer(uuid);
			if (offlinePlayer != null && offlinePlayer.getName() != null) {
				result = offlinePlayer.getName();
			}
		} catch (Exception e) {
			// Player was not found .. "Unknown"
		}
		return result;
	}

	public String getNameOfPlayer(int playerID) {
		return this.getNameOfPlayer(this.playerController.getUuidFromPlayerID(playerID));
	}

	
	public String formatPriceWithoutColor(double price, boolean showZeroAsFree) {
		return ChatColor.stripColor(formatPrice(price, showZeroAsFree));
	}
	public String formatPrice(double price, boolean showZeroAsFree) {
		if (showZeroAsFree && price == 0) {
			return GlobalChestShop.text.get(GlobalChestShop.text.Button_Free);
		}
		if (price < 0) {
			return "DISABLED";
		}
		return this.getEconomy().format(price);
	}

	public String formatDate(Date d, Time t) {
		String result = "";
		if (d == null || t == null) {
			return result;
		}
		try {
			// SimpleDateFormat formDate = new
			// SimpleDateFormat(this.mainConfig.dateFormat + " " +
			// this.mainConfig.timeFormat);
			// result += formDate.format(this.convertSqlDate(d, t));
			return TimeAgo.toDuration(convertSqlDate(d, t));
		} catch (IllegalArgumentException e) {
			result = "Error: use dd.mm.yyyy hh:mm:ss";
		}
		return result;
	}

	public static Date convertSqlDate(Date date, Time time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(time);
		calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
		calendar.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY));
		return calendar.getTime();
	}

	public void validateFreeInventorySlot(Player player) throws NoFreeSlotInInventoryException {
		if (player.getInventory().firstEmpty() == -1) {
			throw new NoFreeSlotInInventoryException();
		}
	}

	public void validatePlayerBalance(Player player, double totalPrice) throws PlayerHasNotEnoughMoneyException {
		OfflinePlayer offlinePlayer = this.getServer().getOfflinePlayer(player.getUniqueId());
		if (econ.getBalance(offlinePlayer, player.getLocation().getWorld().getName()) < totalPrice) {
			throw new PlayerHasNotEnoughMoneyException();
		}
	}

	public void validateBuyConditions(Player player, double totalPrice) throws NoFreeSlotInInventoryException, PlayerHasNotEnoughMoneyException {
		validateFreeInventorySlot(player);
		validatePlayerBalance(player, totalPrice);
	}

	public void validatePlayerIsItemOwner(Player player, ItemStack item) throws PlayerDoesNotOwnClaimedItemException {
		if (!player.getInventory().containsAtLeast(item, item.getAmount()))
			throw new PlayerDoesNotOwnClaimedItemException();
	}

	public boolean validatePermissionCheck(CommandSender cs, String permission) {
		if (cs == null)
			return false;
		if (!(cs instanceof Player)) {
			return true;
		}

		Player player = (Player) cs;
		if (perms.has(player, Permissions.ADMIN)) {
			return true;
		}
		if (perms.has(player, permission)) {
			return true;
		}
		String permissionString = new String(permission);
		while (permissionString.contains(".")) {
			permissionString = permissionString.substring(0, permissionString.length() - 1);
			if (permissionString.charAt(permissionString.length() - 1) == '.')
				if (perms.has(player, permissionString + "*") || perms.has(player, permissionString + "all")) {
					return true;
				}
		}
		return false;
	}

	public String getItemStackDisplayName(ItemStack itemStack) {
		if (itemStack == null)
			return "null";
		String itemName;
		itemName = itemStack.getType().toString().replace("_", " ").toLowerCase();
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			itemName = itemStack.getItemMeta().getDisplayName();
		} else if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
			List<String> lore = itemStack.getItemMeta().getLore();
			for (String s : lore) {
				itemName += " " + s;
			}
		}
		StringBuffer res = new StringBuffer();
		String[] strArr = itemName.split(" ");
		for (String str : strArr) {
			char[] stringArray = str.trim().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			str = new String(stringArray);
			res.append(str).append(" ");
		}
		return res.toString().trim();
	}

	public String getItemStackDisplayName(Integer itemID) {
		if (itemID == null) {
			return "null";
		} else {
			return this.getItemStackDisplayName(this.itemController.formatInternalItemIdToItemStack(itemID));
		}
	}

	public void sendMessageToPlayer(CommandSender cs, String message, EMessageTyp typ) {
		if (cs instanceof Player) {
			Player player = (Player) cs;
			if (typ == EMessageTyp.WARNING) {
				InventoryGUI.warning(message, true, player, getGuiCore().getPlayersOpenedInventoryGui(player));
			}
		}
		cs.sendMessage(message);
	}

	public AuctionController getAuctionController(int worldGroup) {
		AuctionController result;
		synchronized (hashMapAuctionController) {
			if (this.hashMapAuctionController.containsKey(worldGroup)) {
				result = this.hashMapAuctionController.get(worldGroup);
			} else {
				result = new AuctionController(worldGroup);
				hashMapAuctionController.put(worldGroup, result);
			}
		}
		return result;
	}

	public DefaultCategoryController getDefaultCategoryController(int worldGroup) {
		DefaultCategoryController result;
		synchronized (hashMapDefaultCategoryControllers) {
			if (this.hashMapDefaultCategoryControllers.containsKey(worldGroup)) {
				result = this.hashMapDefaultCategoryControllers.get(worldGroup);
			} else {
				result = new DefaultCategoryController(worldGroup);
				hashMapDefaultCategoryControllers.put(worldGroup, result);
			}
		}
		return result;
	}
	
	private static final String	newLine	= System.getProperty("line.separator");

	public synchronized void logToTradeLogger(String playername, UUID uuid, String msg) {
		
		if (! this.mainConfig.logTransactionsToFile) {
			return;
		}
		try {
			
			Format dayFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String day = dayFormatter.format(new Date());
			
			Format timeformatter = new SimpleDateFormat("HH:mm:ss");
			String time = timeformatter.format(new Date());
			
			String message = day + " " + time + "\t " + playername + ": \t" + ChatColor.stripColor(msg) + newLine;
			
			// By Date
			String fileName = getDataFolder() + "/" + "Logs" + "/ByDate/" + day + ".txt";
			PrintWriter printWriter = null;
			File file = new File(fileName);
			file.getParentFile().mkdirs();
			try {
				if (!file.exists())
					file.createNewFile();
				printWriter = new PrintWriter(new FileOutputStream(fileName, true));
				printWriter.write(message);
			} catch (IOException ioex) {
				ioex.printStackTrace();
			} finally {
				if (printWriter != null) {
					printWriter.flush();
					printWriter.close();
				}
			}
			
			// By Player
			fileName = getDataFolder() + "/" + "Logs" + "/ByPlayer/" + playername + "_" + uuid + ".txt";
			printWriter = null;
			file = new File(fileName);
			file.getParentFile().mkdirs();
			try {
				if (!file.exists())
					file.createNewFile();
				printWriter = new PrintWriter(new FileOutputStream(fileName, true));
				printWriter.write(message);
			} catch (IOException ioex) {
				ioex.printStackTrace();
			} finally {
				if (printWriter != null) {
					printWriter.flush();
					printWriter.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/*
	 * Getter & Setter
	 */

	public void permissionWarning(CommandSender cs) {
		this.sendMessageToPlayer(cs, text.get(text.PermissionWarning), EMessageTyp.WARNING);
	}

	public CustomCategoryController getCustomCategoryController() {
		return this.customCategoryController;
	}

	public ItemController getItemController() {
		return itemController;
	}

	public PlayerController getPlayerController() {
		return playerController;
	}

	public UUID getAdminShopUUID() {
		return adminShopUUID;
	}

	public MainConfig getMainConfig() {
		return mainConfig;
	}

	public static GlobalChestShop getPlugin() {
		return plugin;
	}

	public MySqlConnector getMysql() {
		return mysql;
	}

	public static LanguageController getText() {
		return text;
	}

	public CustomCategoryController getCategoryVerwaltung() {
		return customCategoryController;
	}

	public boolean isCitezensEnabled() {
		return citezensEnabled;
	}

	public boolean isHolographicDisplaysEnabled() {
		return holographicDisplaysEnabled;
	}

	public GuiCore getGuiCore() {
		return this.guiCore;
	}

	public Economy getEconomy() {
		return this.econ;
	}

	public NPC_Listener getnPC_Listener() {
		return nPC_Listener;
	}

	public PriceEngine getPriceEngine() {
		return priceEngine;
	}

	public ShopController getShopVerwaltung() {
		return shopController;
	}

	public AuctionLimitController getAuctionLimitController() {
		return auctionLimitController;
	}

	public Map<Integer, AuctionController> getHashMapAuctionController() {
		return hashMapAuctionController;
	}

	public ShopEventListener getShopEventListener() {
		return shopEventListener;
	}

}