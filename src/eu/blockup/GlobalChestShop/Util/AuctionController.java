package eu.blockup.GlobalChestShop.Util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.NoFreeSlotInInventoryException;
import eu.blockup.GlobalChestShop.Util.Exceptions.PlayerHasNotEnoughMoneyException;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class AuctionController {

	private int						worldGroup;
	private Map<Integer, Auction>	hashMap_Auctions;

	public AuctionController(int worldGroup) {
		super();
		this.worldGroup = worldGroup;
		this.hashMap_Auctions = java.util.Collections.synchronizedMap(new HashMap<Integer, Auction>());
		
		Bukkit.getScheduler().runTask(GlobalChestShop.plugin, new Runnable() {
			@Override
			public void run() {
				preChacheAllAuctions();
			}
		});
	}

	public synchronized void buyAuction(int amount, Auction	auction, double multiplier, InventoryGUI inventoryGUI, Player player ) {
		if (auction.isEndent() || auction.isExpired()) {
			player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitBuy_TooSlow_Auction_Ended));
			return;
		}
		
		try {
			Double priceBeforeTax = auction.getShopToPlayerPrice(amount, multiplier);
			Double priceTax = priceBeforeTax * GlobalChestShop.plugin.getMainConfig().taxDecimal.doubleValue();
			Double priceAfterTax = priceBeforeTax - priceTax;
			GlobalChestShop.plugin.validateBuyConditions(player, priceBeforeTax);
			player.getInventory().addItem(auction.getItemStack(amount));
			if (auction.isAdminshop()) {
				auction.createAdminShopHistoryEntry(auction, true, amount, multiplier, player.getUniqueId());
			} else {
				auction.markAsEnded(player.getUniqueId());
			}
			GlobalChestShop.plugin.getEconomy().withdrawPlayer(player, player.getLocation().getWorld().getName(), priceBeforeTax);
			if (priceAfterTax > 0) {
				player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_withdrawn, GlobalChestShop.plugin.formatPrice(priceBeforeTax, false)));
			}
			try {
				OfflinePlayer offlineOwner = GlobalChestShop.plugin.getServer().getOfflinePlayer(auction.getPlayerStarter());
				if (priceAfterTax > 0) {
					GlobalChestShop.plugin.getEconomy().depositPlayer(offlineOwner, player.getLocation().getWorld().getName(), priceAfterTax);
				}
				if (offlineOwner.isOnline()) {
					Player onlineOwner = (Player) offlineOwner;
					onlineOwner.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_informAboutBuy, player.getName(), String.valueOf(auction.getAmount()), GlobalChestShop.plugin.getItemStackDisplayName(auction.getItemStack(1))));
					if (priceAfterTax > 0) {
						onlineOwner.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_deposit, GlobalChestShop.plugin.formatPrice(priceAfterTax, false), GlobalChestShop.plugin.formatPrice(priceTax, false)));
					}
				}
			} catch (Exception e) {
				// Player was not found...
			}
			Double priceEachBeforeTax = 0.0;
			if (amount > 0) {
				priceEachBeforeTax = priceBeforeTax / amount;
			}
			String broadcastMessage = GlobalChestShop.text.get(GlobalChestShop.text.Message_broadcastSell, player.getName(), String.valueOf(amount) + " " + GlobalChestShop.plugin.getItemStackDisplayName(auction.getItemStack(1)), GlobalChestShop.plugin.formatPrice(priceEachBeforeTax, false), GlobalChestShop.plugin.getNameOfPlayer(auction.getPlayerStarter()));
			GlobalChestShop.plugin.logToTradeLogger(player.getName(), player.getUniqueId(),  broadcastMessage);
			GlobalChestShop.plugin.getLogger().log(Level.INFO, ChatColor.stripColor(broadcastMessage));
			if (GlobalChestShop.plugin.getMainConfig().broadcastSells) {
				for (Player p : GlobalChestShop.plugin.getServer().getOnlinePlayers()) {
					try {
						if (GlobalChestShop.plugin.getworldGroup(p.getLocation()) == auction.getworldGroup()) {
							if (p.getUniqueId().compareTo(player.getUniqueId()) == 0) {
								 continue; 
							}
							if (p.getUniqueId().compareTo(auction.getPlayerStarter()) == 0) {
								 continue; 
							}
							p.sendMessage(broadcastMessage);
						}
					} catch (WorldHasNoWorldGroupException e) {
						continue;
					}

				}
			}

			player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.GUI_SubmitBuy_ItemBoughtSuccsses));
			inventoryGUI.returnToParentGUI(player, 2);
			player.updateInventory();
		} catch (NoFreeSlotInInventoryException e) {
			InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoSpace), false, player, inventoryGUI.getParentGUI());
			player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoSpace));
			return;
		} catch (PlayerHasNotEnoughMoneyException e) {
			InventoryGUI.warning(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoMoney), false, player, inventoryGUI.getParentGUI());
			player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Inventory_NoMoney));
			return;
		}
	}
	
	
	public Auction getAuction(int id) {
		Auction result = null;
		if (hashMap_Auctions.containsKey(id)) {
			result = hashMap_Auctions.get(id);
		} else {
			result = new Auction(id);
			this.hashMap_Auctions.put(id, result);
		}
		return result;
	}

	public void preChacheAllAuctions() {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try { // ___________
			st = conn.prepareStatement("SELECT * FROM " + MySqlConnector.table_auctions + " WHERE worldGroup = ?");
			st.setInt(1, this.worldGroup);
			
			rs = st.executeQuery();
			while (rs.next()) {
				int auctionID = rs.getInt("auctionID");
				int itemIdCache = rs.getInt("itemStackID");
				int amountCache = rs.getInt("amount");
				Date startDateCache = rs.getDate("startDate");
				Time startTimeCache = rs.getTime("startTime");
				boolean adminShopCache = rs.getBoolean("adminshop");
				int playerStarterCache = rs.getInt("playerStarter");
				int worldGroupCache = rs.getInt("worldGroup");
				double multiplierCache = rs.getDouble("multiplier");
				Auction auction = new Auction(auctionID, itemIdCache, amountCache, startDateCache, startTimeCache, adminShopCache, playerStarterCache, worldGroupCache, multiplierCache);
				this.hashMap_Auctions.put(auctionID, auction);
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public Double getLastPriceForPlayersAuction(Player player, ItemStack item) {
		int itemID = GlobalChestShop.plugin.itemController.getInteralIdOfItemStack(item);
		int playerID = GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(player.getUniqueId());
		Double result = null;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT shopToPlayerPriceEach FROM " + MySqlConnector.table_auctions + "  WHERE auctionID = (SELECT MAX(auctionID) FROM " + MySqlConnector.table_auctions + " WHERE playerStarter = ? AND itemStackID = ? AND worldGroup = ?);");
			st.setInt(1, playerID);
			st.setInt(2, itemID);
			st.setInt(3, this.worldGroup);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				result = rs.getDouble(1);
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return result;
	}

	private List<Auction> filterExpiredAuctions(List<Auction> list) {
		for (int i = list.size() -1; i >= 0; i--) {
			if (list.get(i).isExpired()) {
				list.remove(i);
			}
		}
		return list;
	}
	private List<Auction> filterNoneExpiredAuctions(List<Auction> list) {
		for (int i = list.size() -1; i >= 0; i--) {
			if (! list.get(i).isExpired()) {
				list.remove(i);
			}
		}
		return list;
	}
	
	
	public List<Auction> getAllSoldAuctionsByPlayer(UUID playersUUID) {
		List<Auction> resultList = new ArrayList<Auction>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND playerStarter = ? AND playerEnder != ? AND worldGroup = ? ORDER BY endDate DESC, endTime DESC");
			st.setBoolean(1, true);
			st.setInt(2, GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playersUUID));
			st.setInt(3, GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playersUUID));
			st.setInt(4, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(getAuction(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return resultList;
	}

	public List<Auction> getAllCanceledAuctions(UUID playersUUID) {
		List<Auction> resultList = new ArrayList<Auction>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND playerStarter = ? AND playerEnder = ? AND worldGroup = ? ORDER BY endDate DESC, endTime DESC");
			st.setBoolean(1, true);
			st.setInt(2, GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playersUUID));
			st.setInt(3, GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playersUUID));
			st.setInt(4, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(getAuction(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return resultList;
	}

	public List<Auction> getAllActiveAuctionsFromPlayer(UUID playersUUID) {
		List<Auction> resultList = new ArrayList<Auction>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND playerStarter = ? AND worldGroup = ? ORDER BY startDate DESC, startTime DESC");
			st.setBoolean(1, false);
			st.setInt(2, GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playersUUID));
			st.setInt(3, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(getAuction(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return filterExpiredAuctions(resultList);
	}
	
	public List<Auction> getAllExpiredAuctionsFromPlayer(UUID playersUUID) {
		List<Auction> resultList = new ArrayList<Auction>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND playerStarter = ? AND worldGroup = ? ORDER BY startDate DESC, startTime DESC");
			st.setBoolean(1, false);
			st.setInt(2, GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playersUUID));
			st.setInt(3, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(getAuction(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return filterNoneExpiredAuctions(resultList);
	}
	
	

	public int getCountOfActiveAuctionsFromPlayer(UUID playersUUID) {
		int playerID = GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playersUUID);
		int result = 0;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT COUNT(*) FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND playerStarter = ? AND worldGroup = ? ORDER BY startDate DESC, startTime DESC;");
			st.setBoolean(1, false);
			st.setInt(2, playerID);
			st.setInt(3, this.worldGroup);
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

	public List<Auction> getAllBoughtAuctionFromPlayer(UUID playersUUID) {
		List<Auction> resultList = new ArrayList<Auction>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND playerEnder = ? AND playerEnder != playerStarter AND worldGroup = ? ORDER BY endDate DESC, endTime DESC");
			st.setBoolean(1, true);
			st.setInt(2, GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playersUUID));
			st.setInt(3, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(getAuction(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return resultList;
	}

	public List<UUID> getAllPlayers() {
		List<UUID> resultList = new LinkedList<UUID>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT * FROM " + MySqlConnector.table_auctions + " WHERE worldGroup = ?");
			st.setInt(1, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				UUID uuid;
				for (int i = 0; i < 2; i++) {
					try {
						if (i == 0) {
							uuid = GlobalChestShop.plugin.getPlayerController().getUuidFromPlayerID(rs.getInt("playerStarter"));
						} else {
							uuid = GlobalChestShop.plugin.getPlayerController().getUuidFromPlayerID(rs.getInt("playerEnder"));
						}
						if (uuid != null && !resultList.contains(uuid)) {
							resultList.add(uuid);
						}
					} catch (NullPointerException e) {
						// The Auction was not ended yet... playerEnder == null
					} catch (IllegalArgumentException e) {
						// The Auction was not ended yet... playerEnder == null
					}
				}
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
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

	public List<Auction> getAllActiveAuctionForItemStack(ItemStack item, boolean onlyAdminShops) {
		boolean includeAdminShop = GlobalChestShop.plugin.getMainConfig().showAdminshopsInsideGlobalShops;
		int itemID = GlobalChestShop.plugin.itemController.getInteralIdOfItemStack(item);
		List<Auction> resultList = new ArrayList<Auction>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			if (onlyAdminShops && includeAdminShop) {
				st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND itemStackID = ? AND worldGroup = ? AND adminshop = 1 ORDER BY shopToPlayerPriceEach ASC");
			} else if (!onlyAdminShops && !includeAdminShop) {
				st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND itemStackID = ? AND worldGroup = ? AND adminshop = 0 ORDER BY shopToPlayerPriceEach ASC");
			} else {
				st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND itemStackID = ? AND worldGroup = ? ORDER BY shopToPlayerPriceEach ASC");
			}
			st.setBoolean(1, false);
			st.setInt(2, itemID);
			st.setInt(3, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(this.getAuction(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return filterExpiredAuctions(resultList);
	}

	public List<Auction> getAllActiveAuction(boolean onlyAdminShops) {
		boolean includeAdminShop = GlobalChestShop.plugin.getMainConfig().showAdminshopsInsideGlobalShops;
		List<Auction> resultList = new ArrayList<Auction>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			if (onlyAdminShops && includeAdminShop) {
				st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND worldGroup = ? AND adminshop = 1 ORDER BY shopToPlayerPriceEach ASC");
			} else if (!onlyAdminShops && !includeAdminShop) {
				st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND worldGroup = ? AND adminshop = 0 ORDER BY shopToPlayerPriceEach ASC");
			} else {
				st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE ended = ?  AND worldGroup = ? ORDER BY shopToPlayerPriceEach ASC");
			}
			st.setBoolean(1, false);
			st.setInt(2, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(this.getAuction(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return filterExpiredAuctions(resultList);
	}

	public Auction getAdminShopFromItemStack(ItemStack item) {
		int itemID = GlobalChestShop.plugin.itemController.getInteralIdOfItemStack(item);
		Auction result = null;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT auctionID FROM " + MySqlConnector.table_auctions + " WHERE itemStackID = ? AND adminshop = ? AND (ended = ? OR (ended = ? AND playerStarter = playerEnder)) AND worldGroup = ?");
			st.setInt(1, itemID);
			st.setBoolean(2, true);
			st.setBoolean(3, false);
			st.setBoolean(4, true);
			st.setInt(5, this.worldGroup);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				result = getAuction(rs.getInt(1));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return result;
	}

	public void reenableAdminShop(Auction adminshop) {
		adminshop.enableAdminShop();
	}

	public List<Integer> getAllActiveItems() { //TODO filter expired auctions
		List<Integer> resultList = new LinkedList<Integer>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT itemStackID FROM " + MySqlConnector.table_auctions + " WHERE ended = ? AND worldGroup = ?");
			st.setBoolean(1, false);
			st.setInt(2, this.worldGroup);
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return resultList;

	}

	public void deleteAllPlayerAuctions() {
		String query = "DELETE FROM `" + MySqlConnector.table_auctions + "` WHERE playerStarter != 1 OR (playerStarter = 1 AND (playerEnder != 1 AND playerEnder IS NOT NULL AND playerEnder != -1))";
		GlobalChestShop.plugin.getMysql().queryUpdate(query);
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "Removed all Auction");
	}

}
