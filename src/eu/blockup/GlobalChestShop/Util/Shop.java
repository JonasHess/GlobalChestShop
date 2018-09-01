package eu.blockup.GlobalChestShop.Util;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;





import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.SignNotFoundException;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_CustomCategoryPage;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_DefaultCategoryCollection;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_DefaultCategoryPage;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_GlobalShopByAuctions;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_GlobalShopByPlayers;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_ShopDelete;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_LocalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.ShopInfoPack;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.SoftDependecies.HologramHolder;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;
import eu.blockup.GlobalChestShop.Util.Statements.MainConfig;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;


public class Shop {
	private ShopInfoPack shopInfo;
	private Integer			shopID = null;
	private Integer			owner;
	private UUID			ownerUUID;
	private final Location	signLocation;
	private Location		signHolder;
	private Location		itemFrameHolder;
	private Location		location2;
	private Integer			worldGorup;
	private ItemStack		itemStack;
	private Boolean			adminShopOnly;
	private boolean			holo;
	private Boolean			itemFrame;
	private Integer			npcID;
	private Integer			categoryID;
	private int				appearance;

	private EShopTyp		eShopTyp;
	private boolean			shopIsOnThisServer		= true; // TODo
	private ShopController	verwaltung;

	private Location		skullEntityLocation;
	private Entity			skullEntity;
	private ItemFrame		itemFrameEntity;
	private HologramHolder	hologramHolder;
	private boolean			newAuctions;
	private boolean			sellAll;
	private int				countWarningWasShown	= 0;
	private double			multiplier;
	private int defaultCategory;



	public Boolean getHolo() {
		return holo;
	}

	public Shop(Integer shopID, Integer owner, Location signLocation, Location location2, Integer worldGroup, ItemStack itemStack, Boolean adminShopOnly, Boolean itemFrame, Integer npcID, Integer categoryID, boolean holo, boolean newAuctions, boolean sellAll, int appearance, double multiplier, int defaultCategorie, ShopController controller) {
		super();
		if (shopID == null || shopID <= 0) {
			throw new InvalidParameterException("ShopId was not set!!");
		}
		this.shopID = shopID;
		this.verwaltung = controller;
		this.owner = owner;
		this.ownerUUID = GlobalChestShop.plugin.getPlayerController().getUuidFromPlayerID(owner);
		this.signLocation = signLocation;
		this.location2 = location2;
		this.worldGorup = worldGroup;
		this.itemStack = itemStack;
		this.adminShopOnly = adminShopOnly;
		this.itemFrame = itemFrame;
		this.npcID = npcID;
		this.holo = holo;
		this.newAuctions = newAuctions;
		this.categoryID = categoryID;
		this.appearance = appearance;
		this.signHolder = this.getSchildHalter(signLocation);
		this.sellAll = sellAll;
		this.multiplier = multiplier;
		this.defaultCategory = defaultCategorie;
		
		
		this.eShopTyp = this.getShopTyp();
		if (this.eShopTyp == EShopTyp.LocalChestShop && multiplier != 1.0) {
			throw new RuntimeException("Multiplier of LocalShop is not 1.0");
		}

	}
	public Shop init(){
		GlobalChestShop.plugin.getShopEventListener().registerShopToChunkManager(this);
		return this;
	}

	public synchronized void spawn() {
		if (this.eShopTyp != EShopTyp.GlobalNpcShop && this.eShopTyp != EShopTyp.GlobalHoloShop) {
			this.signLocation.getBlock().setMetadata(ShopEventListener.META_SHOP_KEY, new FixedMetadataValue(GlobalChestShop.plugin, this));
			this.signHolder.getBlock().setMetadata(ShopEventListener.META_SHOP_KEY, new FixedMetadataValue(GlobalChestShop.plugin, this));
			try {
				this.updateSignText(true);
			} catch (SignNotFoundException e) {
				this.inconsitenceWarning(2);
				return;
			}
		}

		if (this.eShopTyp == EShopTyp.GlobalHoloShop || this.eShopTyp == EShopTyp.GlobalNpcShop) {
			final Shop s = this;
			GlobalChestShop.plugin.executeTaskSynchronous(new Runnable() {
				public void run() {
					try {
						Sign sign = (Sign) s.getSignLocation().getBlock().getState();
						sign.getBlock().breakNaturally();
					} catch (ClassCastException e) {
					}
				}
			});

			if (this.eShopTyp == EShopTyp.GlobalHoloShop) {
				this.spawnLocalShopIcon(signLocation);
			}
		}

		if (this.eShopTyp == EShopTyp.LocalChestShop || this.eShopTyp == EShopTyp.GlobalChestShop) {
			if (!(location2.getBlock().getType().compareTo(Material.CHEST) == 0 || location2.getBlock().getType().compareTo(Material.ENDER_CHEST) == 0)) {
				this.inconsitenceWarning(3);
				return;
			}
			location2.getBlock().setMetadata(ShopEventListener.META_SHOP_KEY, new FixedMetadataValue(GlobalChestShop.plugin, this));
			this.spawnLocalShopIcon(location2);
			// this.updateSignText(true);
		}
		if (this.itemFrame) {
			this.itemFrameEntity = GlobalChestShop.plugin.getNearestItemFrameOnSameYAxis(location2, true);
			if (itemFrameEntity == null) {
				this.inconsitenceWarning(1);
				return;
			}
			this.itemFrameEntity.setMetadata(ShopEventListener.META_SHOP_KEY, new FixedMetadataValue(GlobalChestShop.plugin, this));
			this.itemFrameEntity.setItem(getShopEntityIcon());
			itemFrameHolder = itemFrameEntity.getLocation().getBlock().getRelative(itemFrameEntity.getAttachedFace()).getLocation();
			itemFrameHolder.getBlock().setMetadata(ShopEventListener.META_SHOP_KEY, new FixedMetadataValue(GlobalChestShop.plugin, this));
			try {
				this.updateSignText(true);
			} catch (SignNotFoundException e) {
				this.inconsitenceWarning(2);
				return;
			}
		}
	}

	private void inconsitenceWarning(int i) {
		countWarningWasShown++;

		if (GlobalChestShop.plugin.mainConfig.deleteLocalShopsWhenUnableToFindChest && this.eShopTyp == EShopTyp.LocalChestShop) {
			GlobalChestShop.plugin.getLogger().log(Level.INFO, "Was not able to find the Chest of a LocalShop");
			GlobalChestShop.plugin.getLogger().log(Level.INFO, "The ID of the damaged shop is : " + shopID + ", Owned by: " + GlobalChestShop.plugin.getPlayerController().getPlayersName(this.getOwner()));
			GlobalChestShop.plugin.getLogger().log(Level.INFO, "The Location of the damaged shop is : " + this.getSignLocationString());
			GlobalChestShop.plugin.getLogger().log(Level.INFO, "It will be deleted to suppress further warnings.");
			GlobalChestShop.plugin.getLogger().log(Level.INFO, "In case you don’t want to have shops be deleted automatically, change the config.yml file under Other.deleteLocalShopsWhenUnableToFindTheChest");
			this.delete();
			return;
		}
		if (countWarningWasShown > 1) {
			return;
		}
		GlobalChestShop.plugin.getLogger().log(Level.WARNING, "**********************************************************");
		GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "An error occurred while loading a shop from the database");
		if (i == 1) {
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "An error occurred because an ItemFrame was not found at expected location.");
		} else if (i == 2) {
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "An error occurred because a SIGN was not found at expected location.");
		} else if (i == 3) {
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "An error occurred because a CHEST was not found at expected location.");
		}
		GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "Your world data seems to be divergent to the shops in your database!");
		GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "If you have renamed your world, this was not a good idea! Contact the developer or rename it back");
		GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "If you are running BungeeCord, make sure all worlds in your network have different names!");
		GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "This error also appears when the server has crashed and your worlds were not saved correctly");
		if (this.shopID != null) {
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "The ID of the damaged shop is : " + shopID + ", Owned by: " + GlobalChestShop.plugin.getPlayerController().getPlayersName(this.getOwner()));
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "The Location of the damaged shop is : " + this.getSignLocationString());
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "If you want to get rid of this error, type: \"/GlobalChestShop debug " + shopID + "\" into the chat");
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "You can also use the command \"/GlobalChestShop debug next\" if you have multiple  errors.");
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "If this did not work, you have to edit the database manually and delete the shop with id: " + shopID);
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "[ERROR] " + "If you get hundreds of errors like this, contact the developer");
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, "**********************************************************");
		}
		List<Shop> brokenShopList = GlobalChestShop.plugin.getShopVerwaltung().getBrokenShopList();
		synchronized (brokenShopList) {
			if (!brokenShopList.contains(this)) {
				brokenShopList.add(this);
			}
		}

	}

	public Integer getCategoryID() {
		return categoryID;
	}

	public EShopTyp getShopTyp() {
		boolean npcShop = this.npcID != null;
		boolean itemframeShop = this.itemFrame;
		boolean adminShop = this.getOwnerUUID().compareTo(GlobalChestShop.plugin.adminShopUUID) == 0;
		boolean chestShop = this.location2 != null;

		if (this.holo) {
			return EShopTyp.GlobalHoloShop;
		}

		if (!itemframeShop && !adminShop && !npcShop) {
			return EShopTyp.LocalChestShop;
		} else if (!itemframeShop && adminShop && !npcShop && !chestShop) {
			return EShopTyp.GlobalSignShop;
		} else if (!itemframeShop && adminShop && !npcShop && chestShop) {
			return EShopTyp.GlobalChestShop;
		} else if (!itemframeShop && adminShop && npcShop && !chestShop) {
			return EShopTyp.GlobalNpcShop;
		} else if (itemframeShop && adminShop && !npcShop) {
			return EShopTyp.GlobalItemframeShop;
		}
		return null;
	}

	private void spawnLocalShopIcon(final Location loc) {
		if (hologramHolder != null) {
			hologramHolder.despawn();
		}
		
		if (this.eShopTyp == EShopTyp.GlobalHoloShop) {
			this.hologramHolder = new HologramHolder();
			this.hologramHolder.spawnHoloShop(loc, this);
			return;
		}

		if (GlobalChestShop.plugin.isHolographicDisplaysEnabled()) {
			this.hologramHolder = new HologramHolder();
			this.hologramHolder.spawnFloatingEntiy(loc, this);
			return;
		}

		// GlobalChestShop.plugin.getServer().getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin,
		// new Runnable() {
		// @Override
		// public void run() {
		// skullEntityLocation = new Location(loc.getWorld(), 0.5 +
		// loc.getBlockX(), loc.getBlockY() + 1, 0.5 + loc.getBlockZ());
		// skullEntity = loc.getWorld().dropItem(skullEntityLocation,
		// getShopEntityIcon());
		// skullEntity.setMetadata(ShopEventListener.META_SHOP_KEY, new
		// FixedMetadataValue(GlobalChestShop.plugin, this));
		// }
		// });

	}

	public synchronized void deSpawn() {
		try {
			updateSignText(false);
		} catch (SignNotFoundException e) {
			// Don't care as we are about to shut down.
		}

		if (hologramHolder != null) {
			hologramHolder.despawn();
		}
		if (signHolder != null) {
			signHolder.getBlock().removeMetadata(ShopEventListener.META_SHOP_KEY, GlobalChestShop.plugin);
		}
		if (signLocation != null) {
			signLocation.getBlock().removeMetadata(ShopEventListener.META_SHOP_KEY, GlobalChestShop.plugin);
		}
		if (location2 != null) {
			location2.getBlock().removeMetadata(ShopEventListener.META_SHOP_KEY, GlobalChestShop.plugin);
		}
		if (skullEntity != null) {
			skullEntity.removeMetadata(ShopEventListener.META_SHOP_KEY, GlobalChestShop.plugin);
			skullEntity.remove();
		}
		if (itemFrameEntity != null) {
			itemFrameEntity.removeMetadata(ShopEventListener.META_SHOP_KEY, GlobalChestShop.plugin);
			itemFrameEntity.setItem(new ItemStack(Material.AIR));
		}

		if (itemFrameHolder != null) {
			itemFrameHolder.getBlock().removeMetadata(ShopEventListener.META_SHOP_KEY, GlobalChestShop.plugin);
		}

		this.hologramHolder = null;
		this.skullEntity = null;
	}

	public void breakSign() {
		try {
			getSignLocation().getBlock().breakNaturally();
		} catch (Exception e) {
		}
	}

	public void breakBlocks() {
		final Location signLoc = this.signLocation;
		GlobalChestShop.plugin.executeTaskSynchronous(new Runnable() {
			@Override
			public void run() {
				try {
					signLoc.getBlock().breakNaturally();
				} catch (NullPointerException e) {
				}
				breakSign();

				deSpawn();
			}
		});
	}

	public void delete() {
		GlobalChestShop.plugin.getShopEventListener().deleteShopFromChunkManager(this);
		List<Shop> brokenShopList = GlobalChestShop.plugin.getShopVerwaltung().getBrokenShopList();
		synchronized (brokenShopList) {
			brokenShopList.remove(this);
		}
		this.verwaltung.deleteShop(this);
		this.breakBlocks();
	}

	public void reTeleportSkullEntity() {
		if (this.eShopTyp == EShopTyp.GlobalChestShop || this.eShopTyp == EShopTyp.LocalChestShop) {
			if (this.skullEntity != null) {
				this.skullEntity.teleport(this.skullEntityLocation);
			}
		}
	}

	public void onInteractLeftClick(Player player, InventoryGUI previousGUI) {
		if (player.getUniqueId().compareTo(getOwnerUUID()) == 0 || GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
			GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_ShopDelete(this, previousGUI));
		} else {
			this.onInteractRightClick(player, previousGUI);
		}
	}

	private void playOpenShopSound(final Player player, final Sound sound) {
		GlobalChestShop.plugin.executeTaskSynchronous(new Runnable() {

			@Override
			public void run() {
				if (sound != null && player != null) {
					player.getLocation().getWorld().playSound(player.getLocation(), sound, 1, 1);
				}
			}
		});

	}

	public void onInteractRightClick(final Player player, final InventoryGUI previousGUI) {

		final Shop self = this;
		GlobalChestShop.plugin.executeTaskAsynchronous(new Runnable() {
			@Override
			public void run() {
				synchronized (self) {
					int worldGroup;
					try {
						worldGroup = GlobalChestShop.plugin.getworldGroup(player.getLocation());
					} catch (WorldHasNoWorldGroupException e) {
						InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Message_worldNotFound), false, player, null);
						if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
							player.sendMessage("You have to configure '/plugins/GlobalChestShop/worldGroups.yml' first");
						}
						return;
					}
					if (worldGroup != worldGorup) {
						return;
					}
					if (getShopTyp() == EShopTyp.LocalChestShop) {
						if (!(player.getUniqueId().compareTo(ownerUUID) == 0)) {
							// Player is not the Owner!
							if (!GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.PLAYER_VISIT_OTHER_PLAYERS_SHOPS + "." + worldGroup)) {
								GlobalChestShop.plugin.permissionWarning(player);
								return;
							}
						} else {
							// Player == OWNER
							if (!GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.PLAYER_CREATE_CHEST_SHOP + "." + worldGroup)) {
								GlobalChestShop.plugin.permissionWarning(player);
								return;
							}
						}
						playOpenShopSound(player, Sounds.ENDERMAN_TELEPORT.bukkitSound());
						GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_LocalChestShop(getOwnerUUID(), previousGUI, worldGroup));
					} else {
						if (adminShopOnly) {
							// AMDINSHOP
							if (!GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.PLAYER_OPEN_ADMIN_SHOP + "." + worldGroup)) {
								GlobalChestShop.plugin.permissionWarning(player);
								return;
							}
						} else {
							// GLOBALSHOP
							if (!GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.PLAYER_OPEN_GLOBAL_SHOPS + "." + worldGroup)) {
								GlobalChestShop.plugin.permissionWarning(player);
								return;
							}
						}
						playOpenShopSound(player, Sounds.ENDERMAN_TELEPORT.bukkitSound());
						if (getItemStack() != null) {
							if (adminShopOnly) {
								GlobalChestShop.plugin.openAdminShopOnlyGUI(previousGUI, player, itemStack, worldGroup, multiplier);
							} else {
								GlobalChestShop.plugin.openNormalAuctionGUI(previousGUI, player, itemStack, worldGroup, newAuctions, adminShopOnly, multiplier);
							}
						} else if (categoryID != null) {
							GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CustomCategoryPage(new CustomCategory(categoryID), previousGUI, adminShopOnly, worldGroup, newAuctions, multiplier, player));
							
						}else if (getDefaultCategory() != -1) {
							
							new GUI_DefaultCategoryPage(getDefaultCategory(getDefaultCategory(), worldGroup), previousGUI, adminShopOnly, worldGroup, newAuctions, worldGroup).open(player);;
						
						} else {
							if (appearance == 1) {
								new GUI_GlobalShopByPlayers(worldGroup, newAuctions, previousGUI, multiplier).open(player);
							} else if (appearance == 2) {
								new GUI_GlobalShopByAuctions(worldGroup, adminShopOnly, newAuctions, multiplier, previousGUI).open(player);
							} else {
								GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_DefaultCategoryCollection(previousGUI, adminShopOnly, worldGroup, newAuctions, multiplier));
							}
						}
					}
				}
			}
		});

	}
	private DefaultCategory getDefaultCategory(int categortieId, int worldGroup) {
		return GlobalChestShop.plugin.getDefaultCategoryController(worldGroup).getAllCategories().get(categortieId -1);
	}

	public ItemStack getShopEntityIcon() {
		ItemStack result;

		
		if (this.eShopTyp == EShopTyp.LocalChestShop) {
			result = GlobalChestShop.plugin.getPlayerHead(owner, false);
		}else if (this.defaultCategory != -1) {
			return this.getDefaultCategory(this.defaultCategory, this.getworldGroup()).getDisplayItem();
		}
		else if (this.categoryID != null) {
			result = new CustomCategory(categoryID).getIconItemStack();
		} else if (this.itemStack != null) {
			result = this.getItemStack().clone();
		} else if (this.appearance == 2) {
			result = GlobalChestShop.plugin.getMainConfig().getDisplayItemAllLocalShops();
		} else {
			result = GlobalChestShop.plugin.mainConfig.getDisplayItemAllItems();
		}
		return result;
	}

	public Integer getShopID() {
		return shopID;
	}

	// public CustomCategory getCategory() {
	// return
	// GlobalChestShop.plugin.getCustomCategoryVerwaltung().getCategoryByID(categoryID);
	// }

	public void updateSignText(final boolean enabled) throws SignNotFoundException {

		try {
			final Sign sign = (Sign) signLocation.getBlock().getState();

			GlobalChestShop.plugin.executeTaskSynchronous(new Runnable() {
				@Override
				public void run() {
					if (eShopTyp != EShopTyp.GlobalNpcShop) {
						sign.setLine(0, getFirstSignLine(enabled));
						sign.setLine(1, getSecondSignLine(enabled));
						sign.update();
					}
				}
			});
		} catch (Exception e) {
			if (getShopTyp() != EShopTyp.GlobalItemframeShop && enabled) {
				throw new SignNotFoundException();
			}
		}
	}

	public String getFirstSignLine(boolean enabled) {
		MainConfig mainConfig = GlobalChestShop.plugin.getMainConfig();
		if (!enabled) {
			return ChatColor.RED + "[Shop]";
		}
		if (eShopTyp == EShopTyp.LocalChestShop) {
			return mainConfig.firstLineLocalChestShop;
		}
		if (this.adminShopOnly) {
			return mainConfig.firstLineAdminShop;
		}
		if (getShopTyp() != EShopTyp.GlobalNpcShop) {
			return mainConfig.firstLineGlobalChestShop;
		}
		return "ERROR";

	}

	public String getSecondSignLine(boolean enabled) {
		String color = GlobalChestShop.plugin.getMainConfig().seconLineColor;
		if (!enabled) {
			color = ChatColor.RED + "";
			return color + "DISABLED";
		}
		if (defaultCategory != -1) {
				return this.getDefaultCategory(this.defaultCategory, this.getworldGroup()).getTitle();
		}
		if (eShopTyp == EShopTyp.LocalChestShop) {
			return color + GlobalChestShop.plugin.getNameOfPlayer(owner);
		}
		if (getItemStack() != null) {
			return color + GlobalChestShop.plugin.getItemStackDisplayName(this.itemStack);
		}
		CustomCategory customCategory = GlobalChestShop.plugin.getCustomCategoryController().getCategoryByID(categoryID);
		if (customCategory != null) {
			return color + customCategory.getName();
		}
		if (this.appearance == 1) {
			return color + GlobalChestShop.plugin.getMainConfig().secondLineAllLocalShops;
		}
		return color + GlobalChestShop.plugin.getMainConfig().secondLineAllItems;
	}

	public double getMultiplier() {
		if (multiplier <= 0) {
			throw new RuntimeException("multiplier is null");
		}
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		if (this.eShopTyp == EShopTyp.LocalChestShop) {
			throw new RuntimeException("LocalShops can not have multipliers");
		}
		this.multiplier = multiplier;

		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_shops + " SET `multiplier` = ? WHERE `shopID` = ?");
			st.setDouble(1, this.multiplier);
			st.setInt(2, shopID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public Integer getOwner() {
		return owner;
	}

	void setShopID(Integer shopID) {
		this.shopID = shopID;
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public Location getSignLocation() {
		return signLocation;
	}

	public Location getLocation2() {
		return location2;
	}

	public String getSignLocationString() {
		return verwaltung.locationToString(signLocation);
	}

	public String getLocation2String() {
		return verwaltung.locationToString(location2);
	}

	public Integer getworldGroup() {
		return worldGorup;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public Integer getItemStackID() {
		return GlobalChestShop.plugin.getItemController().getInteralIdOfItemStack(itemStack);
	}

	public Boolean getAdminShopOnly() {
		return adminShopOnly;
	}

	public Boolean getItemFrame() {
		return itemFrame;
	}

	public boolean isShopIsOnThisServer() {
		return shopIsOnThisServer;
	}

	public Location getSkullEntityLocation() {
		return skullEntityLocation;
	}

	public Entity getSkullEntity() {
		return skullEntity;
	}

	public Integer getNpcID() {
		return this.npcID;
	}

	@SuppressWarnings("deprecation")
	public Location getSchildHalter(Location schildLoc) {
		Location halter = schildLoc.clone();
		if (schildLoc.getBlock().getType().equals(XMaterial.SIGN.parseMaterial())) {
			halter.setY(halter.getY() - 1);
			return halter;
		}
		byte bdata = schildLoc.getBlock().getData();
		switch (bdata) {
			case 2: {
				halter.setZ(halter.getZ() + 1);
				break;
			}
			case 3: {
				halter.setZ(halter.getZ() - 1);
				break;
			}
			case 4: {
				halter.setX(halter.getX() + 1);
				break;
			}
			case 5: {
				halter.setX(halter.getX() - 1);
				break;
			}
		}
		return halter;
	}

	public boolean getNewAuctions() {
		return this.newAuctions;
	}

	public boolean isSellAll() {
		return this.sellAll;
	}

	public int getAppearance() {
		return appearance;
	}

	public boolean isGlobalShop() {
		return this.eShopTyp != EShopTyp.LocalChestShop;
	}
	
	public int getDefaultCategory() {
		return defaultCategory;
	}


}
