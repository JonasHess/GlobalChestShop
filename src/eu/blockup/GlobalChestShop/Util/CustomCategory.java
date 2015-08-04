package eu.blockup.GlobalChestShop.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;

public class CustomCategory {

	private Integer	categoryID;
	private String	name		= null;
	private Integer	displayItem	= null;

	public Integer getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}

	public CustomCategory(Integer categoryID) {
		super();
		this.categoryID = categoryID;
	}

	public int isShownInCreativeMenu() {
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
			st = conn.prepareStatement("SELECT showInCreativeMenu FROM " + MySqlConnector.table_categories + " WHERE categoryID = ?");
			st.setInt(1, this.categoryID);
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

	public void setShownInCreativeMenu(int data) {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
			st = conn.prepareStatement("UPDATE " + MySqlConnector.table_categories + " SET showInCreativeMenu = ? WHERE categoryID = ?;");
			st.setInt(1, data);
			st.setInt(2, this.categoryID);
			st.executeUpdate();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public String getName() {
		if (this.name != null) {
			return this.name;
		}
		String result = "";
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT name FROM " + MySqlConnector.table_categories + " WHERE categoryID = ?");
			st.setInt(1, this.categoryID);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		this.name = result;
		return result;
	}

	public void setName(String data) {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
			st = conn.prepareStatement("UPDATE " + MySqlConnector.table_categories + " SET name = ? WHERE categoryID = ?;"); // <---------
			st.setString(1, data);
			st.setInt(2, this.categoryID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		this.name = data;
	}

	public Boolean isAdminOnlyShop() {
		Boolean result = null;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT adminshopOnly FROM " + MySqlConnector.table_categories + " WHERE categoryID = ?");
			st.setInt(1, this.categoryID);
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
		return result;
	}

	public ItemStack getIconItemStack() {
		return GlobalChestShop.plugin.itemController.formatInternalItemIdToItemStack(getIconID());
	}

	public Integer getIconID() {
		if (this.displayItem != null) {
			return this.displayItem;
		}
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
			st = conn.prepareStatement("SELECT iconID FROM " + MySqlConnector.table_categories + " WHERE categoryID = ?");
			st.setInt(1, this.categoryID);
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
		this.displayItem = result;
		return result;
	}

	public void addItem(Integer itemID, int sortPriority) {
		if (itemID == null) {
			throw new NullPointerException();
		}
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
			return;
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO `" + MySqlConnector.table_categoryItems + "` (`categoryID`, `itemID`, `sortPriority`) VALUES (?, ?, ?);");
			st.setInt(1, this.getCategoryID());
			st.setInt(2, itemID);
			st.setInt(3, sortPriority);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public void changeItemOrder(Integer itemID, int newSortPriority) {
		this.removeItem(itemID);
		this.addItem(itemID, newSortPriority);
	}

	public void replacePriorityOrder(int oldOrder, int newOrder) {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
			st = conn.prepareStatement("UPDATE " + MySqlConnector.table_categoryItems + " SET sortPriority = ? WHERE categoryID = ? AND sortPriority = ?;");
			st.setInt(1, newOrder);
			st.setInt(2, this.categoryID);
			st.setInt(3, oldOrder);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public void swapLeft(Integer itemID) {
		Integer itemSortPriority = this.getSortPriority(itemID);
		if (itemSortPriority == null || itemSortPriority <= 0) {
			return;
		}
		Integer left = this.getHighestSortPriority(itemSortPriority);
		this.replacePriorityOrder(left, itemSortPriority);
		this.changeItemOrder(itemID, left);
	}

	public void swapRight(Integer itemID) {
		Integer itemSortPriority = this.getSortPriority(itemID);
		if (itemSortPriority == null || itemSortPriority <= 0) {
			return;
		}
		Integer right = this.getLowsetSortPriority(itemSortPriority);
		this.replacePriorityOrder(right, itemSortPriority);
		this.changeItemOrder(itemID, right);
	}

	public void removeItem(Integer itemID) {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
			st = conn.prepareStatement("DELETE FROM " + MySqlConnector.table_categoryItems + " WHERE categoryID = ? AND itemID = ?;");
			st.setInt(1, this.getCategoryID());
			st.setInt(2, itemID);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public void removeAllItems() {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
			st = conn.prepareStatement("DELETE FROM " + MySqlConnector.table_categoryItems + " WHERE categoryID = ?;");
			st.setInt(1, this.getCategoryID());
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public void delete() {
		this.removeAllItems();
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
			st = conn.prepareStatement("DELETE FROM " + MySqlConnector.table_categories + " WHERE categoryID = ?;");
			st.setInt(1, this.getCategoryID());
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

	public List<Integer> getAllItems(boolean filterItemsWithNoAuctions, int worldGroup, boolean onlyAdminShops) {
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
			st = conn.prepareStatement("SELECT itemID FROM " + MySqlConnector.table_categoryItems + " WHERE categoryID = ?  ORDER BY `sortPriority` ASC");
			st.setInt(1, getCategoryID());
			rs = st.executeQuery();
			while (rs.next()) {
				if (filterItemsWithNoAuctions && GlobalChestShop.plugin.getAuctionController(worldGroup).getAllActiveAuctionForItemStack(GlobalChestShop.plugin.getItemController().formatInternalItemIdToItemStack(rs.getInt(1)), onlyAdminShops).size() == 0) {
					continue;
				}
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

	public Integer getHighestSortPriority(int maximum) {
		Integer result = 0;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT MAX(sortPriority) FROM " + MySqlConnector.table_categoryItems + " WHERE categoryID = ? AND sortPriority < ?;");
			st.setInt(1, getCategoryID());
			st.setInt(2, maximum);
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

	public Integer getLowsetSortPriority(int minimum) {
		Integer result = 0;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT MIN(sortPriority) FROM " + MySqlConnector.table_categoryItems + " WHERE categoryID = ? AND sortPriority > ?;");
			st.setInt(1, getCategoryID());
			st.setInt(2, minimum);
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

	public Integer getSortPriority(Integer itemID) {
		if (itemID == null)
			return null;
		Integer result = 0;
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT sortPriority FROM " + MySqlConnector.table_categoryItems + " WHERE categoryID = ? AND itemID = ?;");
			st.setInt(1, getCategoryID());
			st.setInt(2, itemID);
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

	public void addItem(Integer itemID) {
		this.addItem(itemID, this.getHighestSortPriority(9999) + 1);
	}
}
