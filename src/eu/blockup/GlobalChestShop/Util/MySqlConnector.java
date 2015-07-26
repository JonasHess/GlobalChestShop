package eu.blockup.GlobalChestShop.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Statements.MainConfig;

public class MySqlConnector {
	private String			host;
	private int				port;
	private String			user;
	private String			password;
	private String			database;
	private String			tablePrefix;

	private int				in								= 0;
	private int				out								= 0;
	private int				simultaneousOpenedConnections	= GlobalChestShop.plugin.getMainConfig().simultaneousOpenedConnections;
	private Semaphore		stock							= new Semaphore(0, true);
	private Semaphore		space							= new Semaphore(simultaneousOpenedConnections, true);
	private Connection[]	connectionPool					= new Connection[simultaneousOpenedConnections];

	public void returnConnection(Connection con) {
		try {
			space.tryAcquire(80, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			System.out.println("Aquire Skipped");
			GlobalChestShop.plugin.handleFatalException(e);
		}
		synchronized (connectionPool) {
			connectionPool[in] = con;
			in = (in + 1) % simultaneousOpenedConnections;
		}
		stock.release();
	}

	public Connection getConnection() throws Exception {
		Connection result;
		try {
			stock.tryAcquire(80, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			System.out.println("Aquire Skipped");
			GlobalChestShop.plugin.handleFatalException(e);
		}
		synchronized (connectionPool) {
			result = connectionPool[out];
			out = (out + 1) % simultaneousOpenedConnections;
		}
		space.release();

		// <-------------- DEBUG ----------->
		boolean debug = false; // TODO
		if (debug) {
			debugCounter++;
			try {
				System.out.println("MySql requestt #: " + this.debugCounter);
				Thread.sleep(10);
			} catch (InterruptedException e) {
				GlobalChestShop.plugin.handleFatalException(e);
			}
		}
		// <---------------------------------->

		if (!MySqlConnector.hasConnection(result)) {
			try {
				result.close();
			} catch (Exception e) {
				// Dont care
			}
			result = openConnection();
		}

		return result;

	}

	private int				debugCounter		= 0;

	public static String	table_itemstacks	= "GCS_itemstacks";
	public static String	table_auctions		= "GCS_auctions";
	public static String	table_shops			= "GCS_shops";
	public static String	table_categories	= "GCS_categories";
	public static String	table_categoryItems	= "GCS_categoryItems";
	public static String	table_players		= "GCS_players";
	public static String	table_bannedItems	= "GCS_bannedItems";

	public void createTabels() {
		this.queryUpdate("CREATE TABLE IF NOT EXISTS `" + MySqlConnector.table_itemstacks + "` (itemStackID INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY, serializedItemStack text NOT NULL, sortNumber double NOT NULL) DEFAULT CHARSET=latin1;");
		this.queryUpdate("CREATE TABLE IF NOT EXISTS `"
				+ MySqlConnector.table_auctions
				+ "` (auctionID int(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, itemStackID int(11) NOT NULL, amount int(11) NOT NULL, playerToShopPriceEach double NOT NULL, shopToPlayerPriceEach double NOT NULL,playerStarter int(11) NOT NULL, playerEnder int(11), ended tinyint(1) NOT NULL, startDate date NOT NULL, startTime time NOT NULL, endDate date, endTime time, adminshop tinyint(1) NOT NULL, worldGroup int(11) NOT NULL)DEFAULT CHARSET=utf8;");
		this.queryUpdate("CREATE TABLE IF NOT EXISTS `"
				+ MySqlConnector.table_shops
				+ "` (shopID int(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, ownerID int(11) NOT NULL, signLocation text NOT NULL, location2 text, adminshopOnly tinyint(1) NOT NULL, newAuctions tinyint(1) NOT NULL, sellAll tinyint(1) NOT NULL, itemFrame tinyint(1) NOT NULL, itemStack int(11), npcID int(11), holo tinyint(1) NOT NULL, categoryID int(11), worldGroup int(11) NOT NULL)DEFAULT CHARSET=utf8;");
		this.queryUpdate("CREATE TABLE IF NOT EXISTS `" + MySqlConnector.table_categories + "` (categoryID int(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, iconID int(11) NOT NULL, name text NOT NULL, adminshopOnly tinyint(1) NOT NULL)DEFAULT CHARSET=utf8;");
		this.queryUpdate("CREATE TABLE IF NOT EXISTS `" + MySqlConnector.table_categoryItems + "` (categoryID int(11) NOT NULL, itemID int(11) NOT NULL, sortPriority int(11) NOT NULL)DEFAULT CHARSET=utf8;");
		this.queryUpdate("CREATE TABLE IF NOT EXISTS `" + MySqlConnector.table_players + "` (playerID int(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, uuid text NOT NULL)DEFAULT CHARSET=utf8;");

		this.addColumToTable("ALTER TABLE `" + MySqlConnector.table_shops + "` ADD `appearance` int(11) NOT NULL DEFAULT 0;");

		this.queryUpdate("CREATE TABLE IF NOT EXISTS `" + MySqlConnector.table_bannedItems + "` (bannID int(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY, itemID int(11) NOT NULL, worldGroup int(11) NOT NULL)DEFAULT CHARSET=utf8;");

		this.addColumToTable("ALTER TABLE `" + MySqlConnector.table_bannedItems + "` ADD `inLocalShop` tinyint(1) NOT NULL DEFAULT 1;");

		this.addColumToTable("ALTER TABLE `" + MySqlConnector.table_categories + "` ADD `showInCreativeMenu` INT NOT NULL DEFAULT '0' ;");
	}

	public MySqlConnector() throws ClassNotFoundException, SQLException {
		MainConfig mainConfig = GlobalChestShop.plugin.getMainConfig();
		host = mainConfig.host;
		port = mainConfig.port;
		user = mainConfig.user;
		password = mainConfig.password;
		database = mainConfig.database;
		tablePrefix = mainConfig.tablePrefix;

		MySqlConnector.table_itemstacks = tablePrefix + "itemstacks";
		MySqlConnector.table_auctions = tablePrefix + "auctions";
		MySqlConnector.table_shops = tablePrefix + "shops";
		MySqlConnector.table_categories = tablePrefix + "categories";
		MySqlConnector.table_categoryItems = tablePrefix + "categoryItems";
		MySqlConnector.table_players = tablePrefix + "players";
		MySqlConnector.table_bannedItems = tablePrefix + "bannedItems";

		for (int i = 0; i < simultaneousOpenedConnections; i++) {
			this.returnConnection(openConnection());
		}

		this.createTabels();
	}

	private synchronized Connection openConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
		return conn;
	}

	int	conCount	= 0;

	private static boolean hasConnection(Connection conn) {
		boolean result = false;
		if (conn == null) {
			result = false;
		} else {
			try {
				result = conn.isValid(2);
			} catch (SQLException e) {
				return false;
			}
		}
		return result;
	}

	public void queryUpdate(String query) {
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.executeUpdate();
		} catch (SQLException e) {
			GlobalChestShop.plugin.handleFatalException(e);
		} finally {
			this.closeRessources(conn, null, st);
			this.returnConnection(conn);
		}
	}

	public void addColumToTable(String query) {
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (Exception e) {
			GlobalChestShop.plugin.handleFatalException(e);
		}
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.executeUpdate();
		} catch (SQLException e) {
		} finally {
			this.closeRessources(conn, null, st);
			this.returnConnection(conn);
		}
	}

	public boolean closeRessources(Connection conn, ResultSet rs, PreparedStatement st) {
		boolean result = true;
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				result = false;
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				result = false;
			}
		}
		return result;
	}

	public void closeAllConnections() {
		for (int i = 0; i < simultaneousOpenedConnections; i++) {
			try {
				this.connectionPool[i].close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
