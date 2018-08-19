package eu.blockup.GlobalChestShop.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.management.RuntimeErrorException;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import eu.blockup.GlobalChestShop.GlobalChestShop;

public class ItemController {

	class ItemContainer {
		private int			itemID;
		private ItemStack	itemStack;
		private String		serialized;

		public ItemContainer(int itemID, ItemStack itemStack, String serialized) {
			super();
			if (itemStack == null) {
				throw new NullPointerException();
			}
			this.itemID = itemID;
			this.itemStack = itemStack;
			this.serialized = serialized;
		}

		public synchronized int getItemID() {
			return itemID;
		}

		public synchronized ItemStack getItemStack() {
			return itemStack;
		}

		public synchronized String getSerialized() {
			return serialized;
		}

	}

	class BanneState {
		int		worldGrou;
		boolean	bannedInGlobalShop;
		boolean	bannedInLocalShop;

		public BanneState(int worldGrou, boolean bannedInGlobalShop, boolean bannedInLocalShopp) {
			super();
			this.worldGrou = worldGrou;
			this.bannedInGlobalShop = bannedInGlobalShop;
			this.bannedInLocalShop = bannedInLocalShopp;
		}

		public boolean isBannedInGlobalShop() {
			return bannedInGlobalShop;
		}

		public void setBannedInGlobalShop(boolean bannedInGlobalShop) {
			this.bannedInGlobalShop = bannedInGlobalShop;
		}

		public boolean isBannedInLocalShop() {
			return bannedInLocalShop;
		}

		public void setBannedInLocalShopp(boolean bannedInLocalShopp) {
			this.bannedInLocalShop = bannedInLocalShopp;
		}

		public int getWorldGrou() {
			return worldGrou;
		}

	}

	private List<ItemContainer>						itemContainerList;
	private Map<Integer, Map<Integer, BanneState>>	bannChache;

	public ItemController() {
		this.itemContainerList = Collections.synchronizedList(new ArrayList<ItemContainer>(50));
		this.bannChache = Collections.synchronizedMap(new HashMap<Integer, Map<Integer, BanneState>>());
		GlobalChestShop.plugin.getServer().getScheduler().runTaskAsynchronously(GlobalChestShop.plugin, new Runnable() {
			@Override
			public void run() {
				cacheAllItemStacksFromDatabase();
				cacheAllBannStatesFromDatabase();
			}
		});
	}

	public ItemContainer getItemContainer(Integer itemID) {
		synchronized (itemContainerList) {
			for (ItemContainer c : this.itemContainerList) {
				if (c.getItemID() == itemID) {
					return c;
				}
			}
		}
		return null;
	}

	public ItemContainer getItemContainer(ItemStack itemStack) {
		if (itemStack == null) {
			throw new NullPointerException();
		}
		synchronized (itemContainerList) {
			for (ItemContainer c : this.itemContainerList) {

					if (c.getItemStack().isSimilar(itemStack)) {
						return c;
					}
			}
		}
		return null;
	}

	public ItemContainer getItemContainer(String serialized) {
		synchronized (itemContainerList) {

			for (ItemContainer c : this.itemContainerList) {
					if (c.getSerialized().compareTo(serialized) == 0) {
						return c;
					}
			}
		}
		return null;
	}

	private void cacheNewItemStack(ItemStack itemStack, int itemID, String serialized) {
		ItemContainer cachedItem;
		try {
			cachedItem = new ItemContainer(itemID, itemStack, serialized);
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
			return;
		}
		this.itemContainerList.add(cachedItem);
	}

	public Integer getInteralIdOfItemStack(ItemStack item) {
		if (item == null)
			return null;
		ItemContainer cache = this.getItemContainer(item);
		if (cache != null) {
			return cache.getItemID();
		}
		String serialized = this.serializeItemStack(item);
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		int attempts = 0;
		int result = -1;
		while (attempts < 2) {
			attempts++;
			ResultSet rs = null;
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement("SELECT itemStackID FROM " + MySqlConnector.table_itemstacks + " WHERE serializedItemStack = ? ORDER BY itemStackID ASC");
				st.setString(1, serialized);
				rs = st.executeQuery();
				rs.last();
				if (rs.getRow() != 0) {
					// Wenn nur einer, dann
					rs.first();
					result = rs.getInt(1);
				} else {
					// es gibt keinen!
					this.addItemStackToDatabase(serialized, this.getSortNumberOfItemStack(item));
				}
			} catch (SQLException e) {
				GlobalChestShop.plugin.handleFatalException(e);
			} finally {
				GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
				GlobalChestShop.plugin.getMysql().returnConnection(conn);
			}
			if (result != -1) {
				this.cacheNewItemStack(item, result, this.serializeItemStack(item));
				return result;
			}
		}
		throw new RuntimeException("Was not able to add ItemStack to database");
	}

	public ItemStack formatInternalItemIdToItemStack(Integer id) {
		if (id == null)
			return null;
		ItemContainer cache = this.getItemContainer(id);
		if (cache != null) {
			return cache.getItemStack();
		}
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		String result = "";
		try {
			st = conn.prepareStatement("SELECT * FROM " + MySqlConnector.table_itemstacks + " WHERE itemStackID = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				result = rs.getString("serializedItemStack");
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		try {
			ItemStack itemStack = this.deserializeItemStack(result);
			this.cacheNewItemStack(itemStack, id, result);
			return itemStack;
		} catch (IOException e) {
			GlobalChestShop.plugin.handleFatalException(e);
			return null;
		}
	}

	private void addItemStackToDatabase(String serialized, Double sortNumber) {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO `" + MySqlConnector.table_itemstacks + "` (`serializedItemStack`, `sortNumber`) VALUES ('" + serialized + "', ?);");
			st.setDouble(1, sortNumber);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	private synchronized void cacheAllItemStacksFromDatabase() {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT * FROM `" + MySqlConnector.table_itemstacks + "`");
			rs = st.executeQuery();
			while (rs.next()) {
				int itemID = rs.getInt(1);
				String serialized = rs.getString(2);
				try {
					this.cacheNewItemStack(this.deserializeItemStack(serialized), itemID, serialized);
				} catch (IOException e) {
				}
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return;
	}

	private Double getSortNumberOfItemStack(ItemStack item) {
		double result = 0;
		if (item == null) {
			return result;
		}
		try {
			@SuppressWarnings("deprecation")
			int id = item.getTypeId();
			int durability = item.getDurability();

			result = id;
			result += (Double) 0.00001 * durability;
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "SortNumberOfItemStack was not found for " + item.toString());
			Bukkit.getLogger().log(Level.WARNING, "Please send an email to the developer at edman@blockup.eu and tell him about this incidence");
		}
		return result;
	}

	public String serializeItemStack(ItemStack itemOriginal) throws IllegalStateException {
		if (itemOriginal == null)
			return null;
		ItemContainer cache = this.getItemContainer(itemOriginal);
		if (cache != null) {
			return cache.getSerialized();
		}
		ItemStack item = itemOriginal.clone();
		item.setAmount(1);
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			dataOutput.writeObject(item);

			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Item: " + itemOriginal.toString());
			Bukkit.getLogger().log(Level.WARNING, "Meta: " + itemOriginal.getItemMeta().toString());
			Class<?> enclosingClass = itemOriginal.getClass().getEnclosingClass();
			if (enclosingClass != null) {
				  System.out.println(enclosingClass.getName());
				} else {
				  System.out.println(itemOriginal.getClass().getName());
				}
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public ItemStack deserializeItemStack(String data) throws IOException {
		if (data == "")
			return null;
		ItemContainer cache = this.getItemContainer(data);
		if (cache != null) {
			return cache.getItemStack();
		}
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack item;

			item = (ItemStack) dataInput.readObject();

			dataInput.close();
			return item;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	private Map<Integer, BanneState> getBannChaheOfWorldGroup(Integer worldGroup) {
		if (!bannChache.containsKey(worldGroup)) {
			this.bannChache.put(worldGroup, Collections.synchronizedMap(new HashMap<Integer, BanneState>()));

		}
		return bannChache.get(worldGroup);
	}

	private BanneState getChachedBannStateOfItem(Integer worldGroup, Integer itemID) {
		BanneState result = this.getBannChaheOfWorldGroup(worldGroup).get(itemID);
		if (result == null) {
			// boolean bannedInGlobalShop =
			// isItemBannedFromShops_ignoreCache(itemID, worldGroup, false);
			// boolean bannedInLocalShop =
			// isItemBannedFromShops_ignoreCache(itemID, worldGroup, true);
			result = new BanneState(worldGroup, false, false);
			this.getBannChaheOfWorldGroup(worldGroup).put(itemID, result);
		}
		return result;
	}

	public synchronized void bannItemFromShops(ItemStack item, int worldGroup, boolean inLocalShop) {

		int itemID = this.getInteralIdOfItemStack(item);
		if (inLocalShop) {
			this.getChachedBannStateOfItem(worldGroup, itemID).setBannedInLocalShopp(true);
		} else {
			this.getChachedBannStateOfItem(worldGroup, itemID).setBannedInGlobalShop(true);
		}

		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO `" + MySqlConnector.table_bannedItems + "` (`itemID`, `worldGroup`, `inLocalShop`) VALUES (?, ?, ?);");
			st.setInt(1, itemID);
			st.setInt(2, worldGroup);
			st.setBoolean(3, inLocalShop);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public synchronized void unBannItemFromShops(ItemStack item, int worldGroup, boolean inLocalShop) {

		int itemID = this.getInteralIdOfItemStack(item);
		if (inLocalShop) {
			this.getChachedBannStateOfItem(worldGroup, itemID).setBannedInLocalShopp(false);
		} else {
			this.getChachedBannStateOfItem(worldGroup, itemID).setBannedInGlobalShop(false);
		}

		String query = "DELETE FROM `" + MySqlConnector.table_bannedItems + "` WHERE itemID = " + itemID + " AND worldGroup = " + worldGroup + " AND `inLocalShop` = " + ((inLocalShop) ? "1" : "0") + ";";
		GlobalChestShop.plugin.getMysql().queryUpdate(query);
	}

	public boolean isItemBannedFromShops(ItemStack item, int worldGroup, boolean inLocalShop) { // <---------

		
		int itemID = this.getInteralIdOfItemStack(item);
		if (inLocalShop) {
			return this.getChachedBannStateOfItem(worldGroup, itemID).isBannedInLocalShop();
		} else {
			return this.getChachedBannStateOfItem(worldGroup, itemID).isBannedInGlobalShop();
		}
	}

	private synchronized void cacheAllBannStatesFromDatabase() {
		class TmpBannStruct {
			public int		worldGroup;
			public int		itemID;
			public boolean	inLocalShop;

			public TmpBannStruct(int worldGroup, int itemID, boolean inLocalShop) {
				super();
				this.worldGroup = worldGroup;
				this.itemID = itemID;
				this.inLocalShop = inLocalShop;
			}

		}
		List<TmpBannStruct> resultList = new ArrayList<TmpBannStruct>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT * FROM " + MySqlConnector.table_bannedItems + ";");
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(new TmpBannStruct(rs.getInt("worldGroup"), rs.getInt("itemID"), rs.getBoolean("inLocalShop")));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		for (TmpBannStruct tbs : resultList) {
			try {
				BanneState allreadyChachedState = this.getBannChaheOfWorldGroup(tbs.worldGroup).get(tbs.itemID);
				if (tbs.inLocalShop) {
					allreadyChachedState.bannedInLocalShop = true;
				} else {
					allreadyChachedState.bannedInGlobalShop = true;
				}
			} catch (NullPointerException e) {
				this.getBannChaheOfWorldGroup(tbs.worldGroup).put(tbs.itemID, new BanneState(tbs.worldGroup, !tbs.inLocalShop, tbs.inLocalShop));
			}
		}
	}

	// // MySql getInfo
	// private boolean isItemBannedFromShops_ignoreCache(int itemID, int
	// worldGroup, boolean inLocalShop) { // <---------
	// Connection conn = null;
	// try {
	// conn = GlobalChestShop.plugin.getMysql().getConnection();
	// } catch (Exception e) {
	// GlobalChestShop.plugin.handleFatalException(e);
	// }
	// ResultSet rs = null;
	// PreparedStatement st = null;
	// try { // ___________
	// st = conn.prepareStatement("SELECT bannID FROM " +
	// MySqlConnector.table_bannedItems +
	// " WHERE itemID = ? AND worldGroup = ? AND `inLocalShop` = ?");
	// st.setInt(1, itemID);
	// st.setInt(2, worldGroup);
	// st.setBoolean(3, inLocalShop);
	// rs = st.executeQuery();
	// rs.last();
	// if (rs.getRow() != 0) {
	// return true;
	// } else {
	// return false;
	// }
	// } catch (SQLException e) {
	// GlobalChestShop.plugin.handleFatalException(e);
	// } finally {
	// GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
	// }
	// return true;
	// }

}
