package eu.blockup.GlobalChestShop.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.logging.Level;

import eu.blockup.GlobalChestShop.GlobalChestShop;

public class PlayerController {

	private Map<UUID, Integer>		uuidMap;
	private Map<Integer, UUID>		playerID;
	private Map<Integer, String>	playerNames;

	public PlayerController() {
		this.uuidMap = java.util.Collections.synchronizedMap(new HashMap<UUID, Integer>());
		this.playerID = java.util.Collections.synchronizedMap(new HashMap<Integer, UUID>());
		this.playerNames = java.util.Collections.synchronizedMap(new HashMap<Integer, String>());
	}

	public synchronized String getPlayersName(Integer playerID) {
		String cache = this.playerNames.get(playerID);
		if (cache != null) {
			return cache;
		} else {
			String result = GlobalChestShop.plugin.getNameOfPlayer(this.getUuidFromPlayerID(playerID));
			this.playerNames.put(playerID, result);
			return result;
		}
	}

	public Integer getPlayerIdFromUUID(UUID uuid) {

		if (uuid == null)
			return null;

		Integer cache = this.uuidMap.get(uuid);
		if (cache != null) {
			return cache;
		}
		String uuidString = uuid.toString();
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
				st = conn.prepareStatement("SELECT `playerID` FROM " + MySqlConnector.table_players + " WHERE uuid = ? ORDER BY `playerID` ASC");
				st.setString(1, uuidString);
				rs = st.executeQuery();
				rs.last();
				if (rs.getRow() != 0) {
					// Es gibt mindestes einen
					// if (rs.getRow() > 1) {
					// throw new
					// RuntimeException("UUID was added multible times to Database!  Please inform the developer");
					// }
					// Wenn nur einer, dann
					rs.first();
					result = rs.getInt(1);
				} else {
					// es gibt keinen!
					this.addUuidToDatabase(uuid);
				}
			} catch (SQLException e) {
				GlobalChestShop.plugin.handleFatalException(e);
			} finally {
				GlobalChestShop.plugin.getMysql().closeRessources(conn, rs, st);
				GlobalChestShop.plugin.getMysql().returnConnection(conn);
			}
			if (result != -1) {
				this.uuidMap.put(uuid, Integer.valueOf(result));
				this.playerID.put(Integer.valueOf(result), uuid);
				return result;
			}
		}
		throw new RuntimeException("Was not able to add UUID to database");
	}

	public UUID getUuidFromPlayerID(Integer playerID) {
		if (playerID == null || playerID <= 0)
			return null;
		UUID cache = this.playerID.get(playerID);
		if (cache != null) {
			return cache;
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
			st = conn.prepareStatement("SELECT uuid FROM " + MySqlConnector.table_players + " WHERE playerID = ?");
			st.setInt(1, playerID);
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
		try {
			UUID resultUUID = UUID.fromString(result);
			this.uuidMap.put(resultUUID, playerID);
			this.playerID.put(playerID, resultUUID);
			return resultUUID;
		} catch (Exception e) {
			System.out.println("***********************************************************");
			GlobalChestShop.plugin.getLogger().log(Level.WARNING, " ERROR! Corrupted UUID in your database! PlayerID: " + playerID + "; InvalideID: " + result);
			System.out.println("To delete the corrupted data securely, please manually execute the following sql commands:");
			System.out.println("DELETE FROM gcs_players WHERE playerID = " + playerID + ";");
			System.out.println("DELETE FROM gcs_shops WHERE ownerID = " + playerID + ";");
			System.out.println("DELETE FROM gcs_auctions WHERE playerStarter = " + playerID + " OR playerEnder = " + playerID + ";");
			System.out.println("***********************************************************");
			GlobalChestShop.plugin.handleFatalException(e);
			return null;
		}
	}

	private void addUuidToDatabase(UUID uuid) {
		if (uuid == null) {
			throw new NullPointerException();
		}
		Connection conn = null;
		try {
			conn = GlobalChestShop.plugin.getMysql().getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO `" + MySqlConnector.table_players + "` (`uuid`) VALUES ('" + uuid.toString() + "');");
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			GlobalChestShop.plugin.getMysql().closeRessources(conn, null, st);
			GlobalChestShop.plugin.getMysql().returnConnection(conn);
		}
	}

}
