package eu.blockup.GlobalChestShop.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mysql.jdbc.Statement;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.RequiredPluginNotFoundException;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.ShopInfoPack;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;

class SkullTeleporter implements Runnable {
	private ShopController	shopController;
	private boolean			enabled	= true;

	public SkullTeleporter(ShopController shopController) {
		this.shopController = shopController;
	}

	@Override
	public void run() {
		if (this.enabled) {
			Map<Integer, List<Shop>> map = this.shopController.getHashMap_SkullLocations();
			synchronized (map) {
				for (Map.Entry<Integer, List<Shop>> entry : map.entrySet()) {
					List<Shop> shopList = entry.getValue();
					synchronized (shopList) {
						for (Shop s : shopList) {
							s.reTeleportSkullEntity();
						}
					}
				}
			}
		}
	}

	public void disable() {
		this.enabled = false;
		this.shopController = null;
	}

}

public class ShopController {

	private Map<Integer, List<Shop>>	hashMap_AllShops;
	private SkullTeleporter				executable;
	private int							adminShopPlayerID	= GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(GlobalChestShop.plugin.adminShopUUID);
	private List<Shop>					brokenShopList;
	
	public Shop getBrokenPlayerShop(UUID ownerUUID) {
		for (Shop s : this.brokenShopList) {
			if (s.getOwnerUUID().compareTo(ownerUUID) == 0) {
				return s;
			}
		}
		return null;
	}

	public ShopController() {
		this.hashMap_AllShops = java.util.Collections.synchronizedMap(new HashMap<Integer, List<Shop>>());
		this.brokenShopList = java.util.Collections.synchronizedList(new LinkedList<Shop>());
				ladeAlleShops();
		this.executable = new SkullTeleporter(this);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(GlobalChestShop.plugin, this.executable, 15, 15);
	}

	public void deleteAllPlayerShops() {
		int counter = 0;
		synchronized (hashMap_AllShops) {
			for (Map.Entry<Integer, List<Shop>> entry : this.hashMap_AllShops.entrySet()) {
				for (Shop s : entry.getValue()) {
					if (s.getOwnerUUID().compareTo(GlobalChestShop.plugin.adminShopUUID) != 0) {
						counter++;
						s.breakBlocks();
					}
				}
			}
		}
		String query = "DELETE FROM `" + MySqlConnector.table_shops + "` WHERE ownerID != 1";
		GlobalChestShop.plugin.getMysql().queryUpdate(query);
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "Removed " + counter + " PlayerShops");

		// GlobalChestShop.plugin.reload();
	}

	public List<Shop> getBrokenShopList() {
		return brokenShopList;
	}

	public Map<Integer, List<Shop>> getHashMap_SkullLocations() {
		return hashMap_AllShops;
	}

	// private void addSkull(Entity e, Location l) {
	// this.hashMap_SkullLocations.put(e, l);
	// }

	// private void removeSkull(int entiyID) {
	// Entity e = null;
	// for (Map.Entry<Entity, Location> entry :
	// getHashMap_SkullLocations().entrySet()) {
	// if (entry.getKey().getEntityId() == entiyID) {
	// e = entry.getKey();
	// }
	// }
	// if (e != null) {
	// hashMap_AllShops.remove(e);
	//
	// }
	// }
	//
	public void disable() {
		this.executable.disable();
		for (Map.Entry<Integer, List<Shop>> entry : getHashMap_SkullLocations().entrySet()) {
			for (Shop s : entry.getValue()) {
				s.deSpawn();
			}
		}
		this.hashMap_AllShops.clear();
	}

	public String locationToString(Location loc) {
		if (loc == null)
			return null;
		String returnString = loc.getWorld().getName() + "," + ((int) loc.getX()) + "," + ((int) loc.getY()) + "," + ((int) loc.getZ());
		return returnString;
	}

	class LocationNotFoundException extends Exception {
		private static final long	serialVersionUID	= 1L;
	}

	public Location stringToLocation(String loc) throws LocationNotFoundException {
		if (loc == null)
			return null;
		String[] splited = loc.split(",");
		String world = splited[0];
		int x = Integer.parseInt(splited[1]);
		int y = Integer.parseInt(splited[2]);
		int z = Integer.parseInt(splited[3]);

		Location returnLocation;
		try {
			returnLocation = new Location(GlobalChestShop.plugin.getServer().getWorld(world), x, y, z);
			returnLocation.getBlock();
		} catch (Exception e) {
			throw new LocationNotFoundException();
		}
		return returnLocation;
	}

	private synchronized Shop wirteShopToDatabase(Shop s) {
		String query = "INSERT INTO `" + MySqlConnector.table_shops + "` (`ownerID`, `signLocation`, `location2`, `adminshopOnly`, `itemFrame`, `itemStack`, `worldGroup`, `npcID`, `categoryID`, `holo`, `newAuctions`, `sellAll`, `appearance`, `defaultCategory`) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		int shopId = -1;

		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
			return null;
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, s.getOwner());
			st.setString(2, s.getSignLocationString());
			st.setString(3, s.getLocation2String());
			st.setBoolean(4, s.getAdminShopOnly());
			st.setBoolean(5, s.getItemFrame());
			Integer itemStackID = s.getItemStackID();
			if (itemStackID == null) {
				st.setNull(6, 0);
			} else {
				st.setInt(6, itemStackID);
			}
			st.setInt(7, s.getworldGroup());
			Integer npcID = s.getNpcID();
			if (npcID == null) {
				st.setNull(8, 0);
			} else {
				st.setInt(8, npcID);
			}
			Integer categoryID = s.getCategoryID();
			if (categoryID == null) {
				st.setNull(9, 0);
			} else {
				st.setInt(9, categoryID);
			}
			st.setBoolean(10, s.getHolo());
			st.setBoolean(11, s.getNewAuctions());
			st.setBoolean(12, s.isSellAll());
			st.setInt(13, s.getAppearance());
			st.setInt(14, s.getDefaultCategory());
			shopId =  st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		
		Shop shop = addShop(shopId, s.getOwner(), s.getSignLocation(), s.getLocation2(), s.getworldGroup(), s.getItemStack(), s.getAdminShopOnly(), s.getItemFrame(), s.getNpcID(), s.getCategoryID(), s.getHolo(), s.getNewAuctions(), s.isSellAll(), s.getAppearance(), s.getMultiplier(), s.getDefaultCategory());
		return shop;

	}

	public synchronized Integer getAmountOfShopPlayerOwnsInThisWorldGroup(Integer playerID, Integer worldGroup) { // <---------
		if (playerID == null || worldGroup == null) {
			return 100;
		}
		Integer result = 100;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT COUNT(shopID) FROM " + MySqlConnector.table_shops + " WHERE ownerID = ? AND worldGroup = ?;");
			st.setInt(1, playerID);
			st.setInt(2, worldGroup);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				result = rs.getInt(1);

			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return result;
	}


	
	public Shop createNewGlobalShop(ShopInfoPack infoPack) {

		if (infoPack.getShopTyp() == EShopTyp.GlobalChestShop) {
			return this.createNewShop(this.adminShopPlayerID, infoPack.getSignLocation(), infoPack.getChestLocation(), infoPack.getworldGroup(), infoPack.getItemStack(), infoPack.getAdminShopOnly(), false, null, infoPack.getCategoryID(), false, infoPack.isNewAuctions(), infoPack.isSellAll(), infoPack.getAppearance(), infoPack.getMultiplier(), infoPack.getDefaultShop());

		} else if (infoPack.getShopTyp() == EShopTyp.GlobalSignShop) {
			return this.createNewShop(this.adminShopPlayerID, infoPack.getSignLocation(), null, infoPack.getworldGroup(), infoPack.getItemStack(), infoPack.getAdminShopOnly(), false, null, infoPack.getCategoryID(), false, infoPack.isNewAuctions(), infoPack.isSellAll(), infoPack.getAppearance(), infoPack.getMultiplier(), infoPack.getDefaultShop());

		} else if (infoPack.getShopTyp() == EShopTyp.GlobalItemframeShop) {
			return this.createNewShop(this.adminShopPlayerID, infoPack.getSignLocation(), infoPack.getItemFrameLocation(), infoPack.getworldGroup(), infoPack.getItemStack(), infoPack.getAdminShopOnly(), true, null, infoPack.getCategoryID(), false, infoPack.isNewAuctions(), infoPack.isSellAll(), infoPack.getAppearance(), infoPack.getMultiplier(), infoPack.getDefaultShop());
		} else if (infoPack.getShopTyp() == EShopTyp.GlobalHoloShop) {
			Location l = infoPack.getSignLocation();
			l.add(0.5, 0, 0.5);
			return this.createNewShop(this.adminShopPlayerID, l, infoPack.getItemFrameLocation(), infoPack.getworldGroup(), infoPack.getItemStack(), infoPack.getAdminShopOnly(), false, null, infoPack.getCategoryID(), true, infoPack.isNewAuctions(), infoPack.isSellAll(), infoPack.getAppearance(), infoPack.getMultiplier(), infoPack.getDefaultShop());

		} else if (infoPack.getShopTyp() == EShopTyp.GlobalNpcShop) {
			return this.createNewShop(this.adminShopPlayerID, infoPack.getSignLocation(), infoPack.getItemFrameLocation(), infoPack.getworldGroup(), infoPack.getItemStack(), infoPack.getAdminShopOnly(), false, infoPack.getNpcID(), infoPack.getCategoryID(), false, infoPack.isNewAuctions(), infoPack.isSellAll(), infoPack.getAppearance(), infoPack.getMultiplier(), infoPack.getDefaultShop());
		}
		System.out.println("Shop Verwaltung ERROR");
		return null;
	}

	// public void createNewGlobalChestShop(Location signLocation, Location
	// chestLocation, Integer
	// worldGroup) {
	// this.createNewShop(this.adminShopPlayerID, signLocation, chestLocation,
	// worldGroup, null,
	// false,
	// false, null);
	// }
	//
	// public void createNewGlobalItemFrameShop(Location signLocation, Location
	// itemFrameLocation,
	// Integer worldGroup) {
	// this.createNewShop(this.adminShopPlayerID, signLocation,
	// itemFrameLocation, worldGroup, null,
	// false,
	// true, null);
	// }
	//
	// public void createNewGlobalNPCShop(Location playerLocation, Integer
	// npcID, Integer worldGroup)
	// {
	// this.createNewShop(this.adminShopPlayerID, playerLocation, null,
	// worldGroup, null, false,
	// false,
	// npcID);
	// }

	public synchronized Shop createNewLocalChestShop(Player player, Location signLocation, Location chestLocation, Integer worldGroup) {
		return this.createNewShop(GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(player.getUniqueId()), signLocation, chestLocation, worldGroup, null, false, false, null, null, false, false, false, 0, 1.0, -1);
	}

	private synchronized Shop createNewShop(Integer playerID, Location signLocation, Location location2, Integer worldGroup, ItemStack itemStack, Boolean adminShopOnly, Boolean itemFrame, Integer npcID, Integer categoryID, boolean holo, boolean newAuctions, boolean sellAll, int appearance, double multiplier, int defaultShop) {
		Shop shop = addShop(1, playerID, signLocation, location2, worldGroup, itemStack, adminShopOnly, itemFrame, npcID, categoryID, holo, newAuctions, sellAll, appearance, multiplier, defaultShop);
		this.wirteShopToDatabase(shop);
		return shop;
	}

	private synchronized Shop loadShopFromDB(Integer shopID, Integer owner, String signLocation, String location2, Integer worldGroup, Integer itemStack, Boolean adminShopOnly, Boolean itemFrame, Integer npcID, Integer categoryID, boolean holo, boolean newAuctions, boolean sellAll, int appearance, double multiplier, int defaultShop) throws LocationNotFoundException, WorldHasNoWorldGroupException, RequiredPluginNotFoundException {
		Location L_signLocation = this.stringToLocation(signLocation);
		Location L_location2 = this.stringToLocation(location2);
		Integer worldGoupTmp = null;
		if (L_signLocation != null) {
			worldGoupTmp = GlobalChestShop.plugin.getworldGroup(L_signLocation);
		}
		if (L_location2 != null) {
			worldGoupTmp = GlobalChestShop.plugin.getworldGroup(L_location2);
		}
		if (worldGoupTmp != worldGroup) {
			throw new WorldHasNoWorldGroupException();
		}

		if (holo && !GlobalChestShop.plugin.isHolographicDisplaysEnabled()) {
			throw new RequiredPluginNotFoundException();
		}
		if (npcID != null && !GlobalChestShop.plugin.isCitezensEnabled()) {
			throw new RequiredPluginNotFoundException();
		}

		return this.addShop(shopID, owner, L_signLocation, L_location2, worldGroup, GlobalChestShop.plugin.itemController.formatInternalItemIdToItemStack(itemStack), adminShopOnly, itemFrame, npcID, categoryID, holo, newAuctions, sellAll, appearance, multiplier, defaultShop);
	}


	private Shop addShop(Integer shopID, Integer owner, Location signLocation, Location location2, Integer worldGroup, ItemStack itemStack, Boolean adminShopOnly, Boolean itemFrame, Integer npcID, Integer categoryID, boolean holo, boolean newAuctions, boolean sellAll, int appearance, double multiplier, int defaultShop) {
		Shop s = new Shop(shopID, owner, signLocation, location2, worldGroup, itemStack, adminShopOnly, itemFrame, npcID, categoryID, holo, newAuctions, sellAll, appearance, multiplier, defaultShop, this);
		synchronized (hashMap_AllShops) {
			if (!this.hashMap_AllShops.containsKey(s.getOwner())) {
				this.hashMap_AllShops.put(s.getOwner(), java.util.Collections.synchronizedList(new LinkedList<Shop>()));
			}
			List<Shop> liste = this.hashMap_AllShops.get(s.getOwner());
			synchronized (liste) {
				liste.add(s); 
				System.out.println("Shop added");
			}
		}
		return s;
	}

	public void deleteShop(Shop shop) {

		if (shop == null) {
			Bukkit.getLogger().log(Level.WARNING, "Can't delete unloaded Shop (null)");
			return;
		}
		try {
			synchronized (hashMap_AllShops) {
				this.hashMap_AllShops.get(shop.getOwner()).remove(shop);
			}
		} catch (Exception e1) {
			Bukkit.getLogger().log(Level.WARNING, "Can't delete Shop from internal List (not in found)");
		}
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
			st = conn.prepareStatement("DELETE FROM `" + MySqlConnector.table_shops + "` WHERE `shopID` = ?"); // <---------
			st.setInt(1, shop.getShopID());
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
			
		}

		synchronized (brokenShopList) {
			brokenShopList.remove(shop);
		}
	}

	public Shop getShop(Integer shopID) {
		if (shopID == null || shopID < 0) {
			return null;
		}
		synchronized (hashMap_AllShops) {
			for (Map.Entry<Integer, List<Shop>> entry : this.hashMap_AllShops.entrySet()) {
				synchronized (entry.getValue()) {
					for (Shop s : entry.getValue()) {
						if (s.getShopID().equals(shopID)) {
							return s;
						}
					}
				}
			}
		}
		System.out.println("Shop ID not found!!" + shopID);
		
		return null;
	}

	public List<Shop> getNpcShops(Integer npcId, Player player) throws WorldHasNoWorldGroupException {
		Location loc = player.getLocation();
		String worldName = loc.getWorld().getName();
		Integer worldGroup = GlobalChestShop.plugin.getworldGroup(loc);
		return this.getNPCShops(npcId, worldGroup, worldName);
	}
	
	public List<Shop> getNPCShops(Integer npcId, Integer worldGroup, String worldName) { // <---------
		if (npcId == null) {
			return null;
		}
		List<Shop> resultList = null;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try { // ___________
			st = conn.prepareStatement("SELECT shopID FROM " + MySqlConnector.table_shops + " WHERE npcID = ? AND worldGroup = ? AND signLocation like ? order by shopID asc");
			st.setInt(1, (int)npcId);
			st.setInt(2, worldGroup);
			st.setString(3, worldName + ",%");
			rs = st.executeQuery();
			
			rs = st.executeQuery();
			resultList = new ArrayList<Shop>();
			while (rs.next()) {
				resultList.add(this.getShop(rs.getInt("shopID")));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return resultList;
	}

	public void setShopID(Shop s) { // <---------
		Integer result = -1;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try { // ___________
			st = conn.prepareStatement("SELECT shopID FROM " + MySqlConnector.table_shops + " WHERE ownerID = ? AND signLocation = ? AND worldGroup = ?");
			st.setInt(1, s.getOwner());
			st.setString(2, s.getSignLocationString());
			st.setInt(3, s.getworldGroup());
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				result = rs.getInt(1); // <---------
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		s.setShopID(result);
	}

	public void ladeAlleShops() {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT * FROM " + MySqlConnector.table_shops + "");
			rs = st.executeQuery();
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
			while (rs.next()) {
				Integer npcID = rs.getInt("npcID");
				if (rs.wasNull()) {
					npcID = null;
				}

				Integer categoryID = rs.getInt("categoryID");
				if (rs.wasNull()) {
					categoryID = null;
				}

				Integer itemStack = rs.getInt("itemStack");
				if (rs.wasNull()) {
					itemStack = null;
				}
				try {
					this.loadShopFromDB(rs.getInt("shopID"), rs.getInt("ownerID"), rs.getString("signLocation"), rs.getString("location2"), rs.getInt("worldGroup"), itemStack, rs.getBoolean("adminshopOnly"), rs.getBoolean("itemFrame"), npcID, categoryID, rs.getBoolean("holo"), rs.getBoolean("newAuctions"), rs.getBoolean("sellAll"), rs.getInt("appearance"), rs.getDouble("multiplier"), rs.getInt("defaultCategory"));

				
				} catch (LocationNotFoundException e) {
					continue;
				} catch (WorldHasNoWorldGroupException e) {
					continue;
				} catch (RequiredPluginNotFoundException e) {
					continue;
				}
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			
		}
	}

	public Shop openLocalShopOfPlayer(UUID uuid, Player player, InventoryGUI prev) {
		if (uuid.compareTo(GlobalChestShop.plugin.adminShopUUID) == 0) {
			return null;
		}
		Shop s;
		synchronized (hashMap_AllShops) {
			List<Shop> list = this.hashMap_AllShops.get(GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(uuid));
			if (list.size() == 0) {
				return null;
			}
			s = list.get(0);
		}
		s.onInteractRightClick(player, prev);
		return s;
	}

	public synchronized List<UUID> getAllActiveLocalShops(int worldGroup) {
		List<UUID> resultList = new LinkedList<UUID>();
		// resultList.add(GlobalChestShop.plugin.getAdminShopUUID());
		for (Map.Entry<Integer, List<Shop>> entry : getHashMap_SkullLocations().entrySet()) {
			boolean allreadyListed = false;
			for (Shop s : entry.getValue()) {
				if (s.getShopTyp() != EShopTyp.LocalChestShop) {
					continue;
				}
				for (UUID u : resultList) {
					if (u.compareTo(s.getOwnerUUID()) == 0) {
						allreadyListed = true;
					}
				}
				if (!allreadyListed) {
					if (GlobalChestShop.plugin.getAuctionController(worldGroup).getAllActiveAuctionsFromPlayer(s.getOwnerUUID()).size() > 0) {
						resultList.add(s.getOwnerUUID());
					}
				}
			}
		}
		Collections.sort(resultList, new Comparator<UUID>() {
			@Override
			public int compare(UUID arg0, UUID arg1) {

				try {
					if (arg0.compareTo(GlobalChestShop.plugin.getAdminShopUUID()) == 0) {
						return -1;
					}
					OfflinePlayer p0 = GlobalChestShop.plugin.getServer().getOfflinePlayer(arg0);
					OfflinePlayer p1 = GlobalChestShop.plugin.getServer().getOfflinePlayer(arg1);
					return p0.getName().compareToIgnoreCase(p1.getName());
				} catch (Exception e) {
					return 1;
				}
			}
		});
		return resultList;
	}



}
