package eu.blockup.GlobalChestShop.Util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_SubmitBuy;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_SubmitSell;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Interfaces.BuyAbleInterface;

public class Auction implements BuyAbleInterface, Comparable<Auction> {

	private int		auctionID;
	private int		itemIdCache			= -1;
	private int		amountCache			= 0;
	private Date	startDateCache		= null;
	private Time	startTimeCache		= null;
	private Boolean	adminShopCache		= null;
	private int		playerStarterCache	= -1;
	private int		worldGroupCache		= -1;

	public Auction(int auctionID) {
		super();
		this.auctionID = auctionID;
	}

	public Auction(int auctionID, int itemIdCache, int amountCache, Date startDateCache, Time startTimeCache, Boolean adminShopCache, int playerStarterCache, int worldGroup) {
		super();
		this.auctionID = auctionID;
		this.itemIdCache = itemIdCache;
		this.amountCache = amountCache;
		this.startDateCache = startDateCache;
		this.startTimeCache = startTimeCache;
		this.adminShopCache = adminShopCache;
		this.playerStarterCache = playerStarterCache;
		this.worldGroupCache = worldGroup;
	}

	public int getworldGroup() {
		this.preChacheAttributes();
		if (this.worldGroupCache != -1) {
			return this.worldGroupCache;
		}
		int result = 1;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT worldGroup FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
			st.setInt(1, this.auctionID);
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
		this.worldGroupCache = result;
		return result;
	}

	public void preChacheAttributes() {
		if (this.startDateCache != null) {
			return;
		}
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try { // ___________
			st = conn.prepareStatement("SELECT * FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
			st.setInt(1, this.auctionID);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				this.itemIdCache = rs.getInt("itemStackID");
				this.amountCache = rs.getInt("amount");
				this.startDateCache = rs.getDate("startDate");
				this.startTimeCache = rs.getTime("startTime");
				this.adminShopCache = rs.getBoolean("adminshop");
				this.playerStarterCache = rs.getInt("playerStarter");
				this.worldGroupCache = rs.getInt("worldGroup");
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);

		}
	}

	// //////////////////////////////////////

	// // MySql setInfo
	// public void setInfo(String data) { // <---------
	// Connection conn = null;
	// PreparedStatement st = null;
	// try {
	// conn = GlobalChestShop.plugin.getMysql().getConnection();
	// st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_auctions +
	// " SET itemStackID = ? WHERE auctionID = ?;"); //
	// <---------
	// st.setString(1, data); // <---------
	// st.setInt(2, this.auctionID);
	// st.executeUpdate();
	// } catch (SQLException e) {
	// GlobalChestShop.plugin.handleSQLExceptions(e);
	// GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
	// } catch (Exception e) {
	// GlobalChestShop.plugin.handleSQLExceptions(e);
	// }
	// }
	//
	// // MySql getInfo
	// public String getInfo() { // <---------
	// String result = ""; // <---------
	// Connection conn = null;
	// try {
	// conn = GlobalChestShop.plugin.getMysql().getConnection();
	// } catch (Exception e) {
	// GlobalChestShop.plugin.handleSQLExceptions(e);
	// }
	// ResultSet rs = null;
	// PreparedStatement st = null;
	// try { // ___________
	// st = conn.prepareStatement("SELECT serializedItemStack FROM " +
	// MySqlConnector.table_auctions + " WHERE auctionID = ?");
	// st.setInt(1, this.auctionID);
	// rs = st.executeQuery();
	// rs.last();
	// if (rs.getRow() != 0) {
	// rs.first();
	// result = rs.getString(1); // <---------
	//
	// }
	// } catch (SQLException e) {
	// GlobalChestShop.plugin.handleSQLExceptions(e);
	// } finally {
	// GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
	// }
	// return result;
	// }

	// //////////////////////////////////

	public static void createNewAuction(ItemStack itemStack, int amount, double playerToShopPriceEach, double shopToPlayerPriceEach, UUID playerStarter, UUID playerEnder, boolean endent, Date startDate, Time startTime, Date endDate, Time endTime, boolean adminshop, int worldGroup) {

		int itemIdInternal = GlobalChestShop.plugin.itemControler.getInteralIdOfItemStack(itemStack);
		String query = "INSERT INTO `" + MySqlConnector.table_auctions + "` ( `itemStackID`, `amount`, `playerToShopPriceEach`, `shopToPlayerPriceEach`, `playerStarter`, `playerEnder`, `ended`, `startDate`, `startTime`, `endDate`, `endTime`, `adminshop`, `worldGroup`) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		Integer uuidStarter = GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playerStarter);
		Integer uuidEnder = GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(playerEnder);
		if (uuidEnder == null)
			uuidEnder = -1;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, itemIdInternal);
			st.setInt(2, amount);
			st.setDouble(3, playerToShopPriceEach);
			st.setDouble(4, shopToPlayerPriceEach);
			st.setInt(5, uuidStarter);
			st.setInt(6, uuidEnder);
			st.setBoolean(7, endent);
			st.setDate(8, startDate);
			st.setTime(9, startTime);
			st.setDate(10, endDate);
			st.setTime(11, endTime);
			st.setBoolean(12, adminshop);
			st.setInt(13, worldGroup);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}

	}

	public Time getStartTime() {
		this.preChacheAttributes();
		Time result = null;
		if (this.startTimeCache == null) {
			Connection conn = null;
			try {
				conn = GlobalChestShop.plugin.getMysql().getConnection();
			} catch (Exception e) {
				GlobalChestShop.plugin.handleFatalException(e);
			}
			ResultSet rs = null;
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement("SELECT startTime FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
				st.setInt(1, this.auctionID);
				rs = st.executeQuery();
				rs.last();
				if (rs.getRow() != 0) {
					rs.first();
					result = rs.getTime(1);
				}
			} catch (SQLException e) {
				GlobalChestShop.plugin.handleFatalException(e);
			} finally {
				GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
				GlobalChestShop.plugin.getMysql().returnConnection(conn);
			}
		} else {
			result = this.startTimeCache;
		}
		this.startTimeCache = result;
		return result;
	}

	public Time getEndTime() {
		Time result = null;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT endTime FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
			st.setInt(1, this.auctionID);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				result = rs.getTime(1);

			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return result;
	}

	public void createAdminShopHistoryEntry(Auction adminShop, boolean buy, int amount, UUID player) {
		if (!adminShop.isAdminshop())
			throw new RuntimeException("This is not an Admin Shop!");
		double ShopToPlayerPriceEach = adminShop.getShopToPlayerPrice(1);
		double PlayerToShopPriceEach = adminShop.getPlayerToShopPrice(1);
		if (buy) {
			PlayerToShopPriceEach = -1;
		} else {
			ShopToPlayerPriceEach = -1;
		}
		if (buy) {
			Auction.createNewAuction(this.getItemStack(1), amount, PlayerToShopPriceEach, ShopToPlayerPriceEach, this.getPlayerStarter(), player, true, this.getStartDate(), this.getStartTime(), new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), true, this.getworldGroup());
		} else {
			Auction.createNewAuction(this.getItemStack(1), amount, ShopToPlayerPriceEach, PlayerToShopPriceEach, player, this.getPlayerStarter(), true, this.getStartDate(), this.getStartTime(), new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), true, this.getworldGroup());

		}
	}

	public void setPlayerToShopPriceEach(Double playerToShopPriceEach) {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_auctions + " SET playerToShopPriceEach = ? WHERE auctionID = ?;");
			st.setDouble(1, playerToShopPriceEach);
			st.setInt(2, this.auctionID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public void setShopToPlayerPriceEach(Double shopToPlayerPriceEach) {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_auctions + " SET shopToPlayerPriceEach = ? WHERE auctionID = ?;");
			st.setDouble(1, shopToPlayerPriceEach);
			st.setInt(2, this.auctionID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public void disableAdminShop() {
		if (!this.isAdminshop())
			throw new RuntimeException("This is not an Admin Shop!");
		this.markAsEnded(GlobalChestShop.plugin.adminShopUUID);
	}

	public Boolean isAdminshop() {
		this.preChacheAttributes();
		Boolean result = false;
		if (this.adminShopCache == null) {

			Connection conn = null;
			try {
				conn = GlobalChestShop.plugin.getMysql().getConnection();
			} catch (Exception e) {
				GlobalChestShop.plugin.handleFatalException(e);
			}
			ResultSet rs = null;
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement("SELECT adminshop FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
				st.setInt(1, this.auctionID);
				rs = st.executeQuery();
				rs.last();
				if (rs.getRow() != 0) {
					rs.first();
					result = rs.getBoolean(1);
				}
			} catch (SQLException e) {
				GlobalChestShop.plugin.handleFatalException(e);
			} finally {
				GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
				GlobalChestShop.plugin.getMysql().returnConnection(conn);
			}
			this.adminShopCache = result;
		} else {
			result = this.adminShopCache;
		}
		return result;
	}

	public void setEnded(boolean data) {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_auctions + " SET ended = ? WHERE auctionID = ?;");
			st.setBoolean(1, data);
			st.setInt(2, this.auctionID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public void enableAdminShop() {
		if (!this.isAdminshop())
			throw new RuntimeException("This is not an Admin Shop!");
		this.setEnded(false);
		this.setEndDate(null);
		this.setEndTime(null);
		this.setPlayerEnder(null);
	}

	private void setEndTime(Time data) {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_auctions + " SET endTime = ? WHERE auctionID = ?;");
			st.setTime(1, data);
			st.setInt(2, this.auctionID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public void markAsEnded(UUID uuid) {
		this.setEndDate(new Date(System.currentTimeMillis()));
		this.setEndTime(new Time(System.currentTimeMillis()));
		this.setPlayerEnder(uuid);
		this.setEnded(true);
	}

	public void canceleAuction() {
		this.markAsEnded(getPlayerStarter());
	}

	/**
	 * BuyAble Interface
	 */

	@Override
	public void buy(Player player, final Economy econ, int amount) {
		InventoryGUI parent = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedInventoryGui(player);
		new GUI_SubmitBuy(this, amount, parent).open(player);
	}

	@Override
	public void sell(Player player, Economy econ, int amount) {
		if (!this.isAdminshop()) {
			throw new RuntimeException("You can not sell to a normal auction!");
		}
		InventoryGUI parent = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedInventoryGui(player);
		new GUI_SubmitSell(this, amount, false, parent).open(player);

	}

	@Override
	public void sellAll(Player player, Economy econ, int amount) {

		if (!this.isAdminshop()) {
			throw new RuntimeException("You can not sellAll to a normal auction!");
		}
		InventoryGUI parent = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedInventoryGui(player);
		new GUI_SubmitSell(this, amount, true, parent).open(player);

	}

	@Override
	public Button_Bare getBuyButton(int amountTmp, Player player) {
		if (player == null) {
			throw new NullPointerException("Player was null");
		}
		return new Button_Bare(GlobalChestShop.plugin.getMainConfig().getBuyButton(), GlobalChestShop.text.get(GlobalChestShop.text.Auction_GetBuyButton_Title), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Amount, String.valueOf(amountTmp)), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceEach, GlobalChestShop.plugin.formatPrice(this.getShopToPlayerPrice(1), true)),
				GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceTotal, GlobalChestShop.plugin.formatPrice(this.getShopToPlayerPrice(amountTmp), true)));
	}

	@Override
	public Button_Bare getSellButton(int amountTmp, Player player) {
		if (player == null) {
			throw new NullPointerException("Player was null");
		}
		return new Button_Bare(GlobalChestShop.plugin.getMainConfig().getSellButton(), GlobalChestShop.text.get(GlobalChestShop.text.Auction_GetSellButton_Title), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Amount, String.valueOf(amountTmp)),
				GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceEach, GlobalChestShop.plugin.formatPrice(this.getPlayerToShopPrice(1), true)), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceTotal, GlobalChestShop.plugin.formatPrice(this.getPlayerToShopPrice(amountTmp), false)));
	}

	@Override
	public Button_Bare getSellAllButton(Player player) {
		if (player == null) {
			throw new NullPointerException("Player was null");
		}
		int amount = this.getAmountOfItemInInventory(player);
		return new Button_Bare(GlobalChestShop.plugin.getMainConfig().getSellButton(), GlobalChestShop.text.get(GlobalChestShop.text.Auction_GetSellAllButton_Title), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_Amount, String.valueOf(amount)),
				GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceEach, GlobalChestShop.plugin.formatPrice(this.getPlayerToShopPrice(1), true)), GlobalChestShop.text.get(GlobalChestShop.text.Auction_Info_PriceTotal, GlobalChestShop.plugin.formatPrice(this.getPlayerToShopPrice(amount), false)));
	}

	@Override
	public int getAmountOfItemInInventory(Player player) {
		int amountOfItemsInInventory = 0;
		synchronized (player.getInventory()) {
			for (ItemStack playersItem : player.getInventory()) {
				if (playersItem == null)
					continue;
				if (!(playersItem.isSimilar(this.getItemStack(1))))
					continue;
				amountOfItemsInInventory += playersItem.getAmount();
			}
		}
		return amountOfItemsInInventory;
	}

	@Override
	public Button_Bare getItemStackAsButton() {
		return new Button_Bare(this.getItemStack(this.getAmount()));
	}

	@Override
	public int getMaxAmount() {
		return this.getItemStack(1).getMaxStackSize();
	}

	public double getShopToPlayerPrice(int amount) {
		double result = 99999;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT shopToPlayerPriceEach FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
			st.setInt(1, this.auctionID);
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
		return result * amount;
	}

	@Override
	public double getPlayerToShopPrice(int amount) {
		double result = 0;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT playerToShopPriceEach FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
			st.setInt(1, this.auctionID);
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
		return result * amount;
	}

	/**
	 * Getter Setter
	 * 
	 */

	public boolean canceledByOwner() {
		if (!this.isEndent()) {
			return false;
		}
		return this.getPlayerStarter().compareTo(getPlayerEnder()) == 0;
	}

	@Override
	public ItemStack getItemStack(int amount) {
		this.preChacheAttributes();
		int result = -1; // <---------
		if (this.itemIdCache == -1) {
			Connection conn = null;
			try {
				conn = GlobalChestShop.plugin.getMysql().getConnection();
			} catch (Exception e) {
				GlobalChestShop.plugin.handleFatalException(e);
			}
			ResultSet rs = null;
			PreparedStatement st = null;
			try { // ___________
				st = conn.prepareStatement("SELECT itemStackID FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
				st.setInt(1, this.auctionID);
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
			this.itemIdCache = result;
		} else {
			result = itemIdCache;
		}
		ItemStack resultItem = GlobalChestShop.plugin.itemControler.formatInternalItemIdToItemStack(result);
		resultItem.setAmount(amount);
		return resultItem;
	}

	public int getAmount() {
		this.preChacheAttributes();
		int result = 0;
		if (this.amountCache == 0) {
			Connection conn = null;
			try {
				conn = GlobalChestShop.plugin.getMysql().getConnection();
			} catch (Exception e) {
				GlobalChestShop.plugin.handleFatalException(e);
			}
			ResultSet rs = null;
			PreparedStatement st = null;
			try { // ___________
				st = conn.prepareStatement("SELECT amount FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
				st.setInt(1, this.auctionID);
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
			this.amountCache = result;
		} else {
			result = amountCache;
		}
		return result;
	}

	public int getAuctionID() {
		return auctionID;
	}

	public UUID getPlayerStarter() {
		this.preChacheAttributes();
		Integer result = -1;
		if (this.playerStarterCache == -1) {

			Connection conn = null;
			try {
				conn = GlobalChestShop.plugin.getMysql().getConnection();
			} catch (Exception e) {
				GlobalChestShop.plugin.handleFatalException(e);
			}
			ResultSet rs = null;
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement("SELECT playerStarter FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
				st.setInt(1, this.auctionID);
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
			this.playerStarterCache = result;
		} else {
			result = this.playerStarterCache;
		}
		return GlobalChestShop.plugin.getPlayerController().getUuidFromPlayerID(result);
	}

	public UUID getPlayerEnder() {
		Integer result = null;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT playerEnder FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
			st.setInt(1, this.auctionID);
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
		if (result == null)
			return null;
		return GlobalChestShop.plugin.getPlayerController().getUuidFromPlayerID(result);
	}

	public void setPlayerEnder(UUID uuid) {
		Integer data = GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(uuid);
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_auctions + " SET playerEnder = ? WHERE auctionID = ?;");
			if (data == null) {
				st.setNull(1, 0);
			} else {
				st.setInt(1, data);
			}
			st.setInt(2, this.auctionID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public boolean isEndent() {
		return this.getPlayerEnder() != null;
	}

	public Date getStartDate() {
		this.preChacheAttributes();
		Date result = null;
		if (this.startDateCache == null) {

			Connection conn = null;
			try {
				conn = GlobalChestShop.plugin.getMysql().getConnection();
			} catch (Exception e) {
				GlobalChestShop.plugin.handleFatalException(e);
			}
			ResultSet rs = null;
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement("SELECT startDate FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
				st.setInt(1, this.auctionID);
				rs = st.executeQuery();
				rs.last();
				if (rs.getRow() != 0) {
					rs.first();
					result = rs.getDate(1);
				}
			} catch (SQLException e) {
				GlobalChestShop.plugin.handleFatalException(e);
			} finally {
				GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
				GlobalChestShop.plugin.getMysql().returnConnection(conn);
			}
			this.startDateCache = result;
		} else {
			result = this.startDateCache;
		}
		return result;
	}

	public void setStartDate(Date data) {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_auctions + " SET startDate = ? WHERE auctionID = ?;"); // <---------
			st.setDate(1, data);
			st.setInt(2, this.auctionID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public Date getEndDate() {
		Date result = null;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT endDate FROM " + MySqlConnector.table_auctions + " WHERE auctionID = ?");
			st.setInt(1, this.auctionID);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				result = rs.getDate(1);
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return result;
	}

	public void setEndDate(Date data) {
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  " + MySqlConnector.table_auctions + " SET endDate = ? WHERE auctionID = ?;");
			st.setDate(1, data);
			st.setInt(2, this.auctionID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	// public static Comparator<Auction> getComparator(SortTyp sortTyp) {
	// Comparator<Auction> result;
	// if (sortTyp == SortTyp.BEST_SINGLE_PRICE) {
	// result = new Comparator<Auction>() {
	//
	// @Override
	// public int compare(Auction arg0, Auction arg1) {
	// double p0 = arg0.getShopToPlayerPrice(1);
	// double p1 = arg1.getShopToPlayerPrice(1);
	// if (p0 == p1) {
	// return 0;
	// } else if (p0 < p1) {
	// return -1;
	// } else {
	// return 1;
	// }
	// }
	// };
	// } else if (sortTyp == sortTyp.DATE_CREATED) {
	// result = new Comparator<Auction>() {
	//
	// @Override
	// public int compare(Auction arg0, Auction arg1) {
	// return arg0.getStartDate().compareTo(arg1.getStartDate());
	// }
	//
	// };
	// } else {
	// result = new Comparator<Auction>() {
	//
	// @Override
	// public int compare(Auction arg0, Auction arg1) {
	// return arg0.getEndDate().compareTo(arg1.getEndDate());
	// }
	//
	// };
	// }
	// return result;
	// }

	@Override
	public int compareTo(Auction arg0) {
		return 0;
		// return this.getComparator().compare(this, arg0);
	}

}
