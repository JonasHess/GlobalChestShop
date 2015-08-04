package eu.blockup.GlobalChestShop.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;

public class CustomCategoryController {

	public synchronized void createNewCategory(String name, ItemStack displayItem, boolean adminShopOnly) {
		String query = "INSERT INTO `" + MySqlConnector.table_categories + "` (`iconID`, `name`, `adminshopOnly`) " + "VALUES (?, ?, ?);";

		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
			return;
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.setInt(1, GlobalChestShop.plugin.itemController.getInteralIdOfItemStack(displayItem));
			st.setString(2, name);
			st.setBoolean(3, adminShopOnly);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}

		// // Find Categorie ID;
		// Integer result = -1;
		// try {
		// conn = GlobalChestShop.plugin.getMysql().getConnection();
		// } catch (Exception e) {
		// GlobalChestShop.plugin.handleSQLExceptions(e);
		// }
		// ResultSet rs = null;
		// PreparedStatement st = null;
		// try { // ___________
		// st =
		// conn.prepareStatement("SELECT categoryID FROM categories WHERE name = ? AND iconID = ?");
		// st.setString(1, name);
		// st.setInt(2,
		// GlobalChestShop.plugin.itemVerwaltung.getInteralIdOfItemStack(displayItem));
		// rs = st.executeQuery();
		// rs.last();
		// if (rs.getRow() != 0) {
		// rs.first();
		// result = rs.getInt(1); // <---------
		// }
		// } catch (SQLException e) {
		// GlobalChestShop.plugin.handleSQLExceptions(e);
		// } finally {
		// GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
		// }
		// return new CustomCategory(result);
	}

	public synchronized List<CustomCategory> getAllCategories() {
		List<CustomCategory> resultList = new LinkedList<CustomCategory>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT categoryID FROM " + MySqlConnector.table_categories + "");
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(new CustomCategory(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return resultList;
	}

	public synchronized List<CustomCategory> getAllCategoriesShownInCreativeMenu() {
		List<CustomCategory> resultList = new LinkedList<CustomCategory>();
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT categoryID FROM " + MySqlConnector.table_categories + " WHERE showInCreativeMenu > 0");
			rs = st.executeQuery();
			while (rs.next()) {
				resultList.add(new CustomCategory(rs.getInt(1)));
			}
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
		return resultList;
	}

	public synchronized CustomCategory getCategoryByID(Integer categoryID) {
		if (categoryID == null)
			return null;
		return new CustomCategory(categoryID);
	}

}
