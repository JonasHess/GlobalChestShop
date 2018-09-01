package eu.blockup.GlobalChestShop.Util.Statements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.ItemStackNotFoundException;

public class MainConfig {

	public String		host;
	public int			port;
	public String		database;
	public String		user;
	public String		password;
	public String		tablePrefix;
	public int			simultaneousOpenedConnections;

	public String		dateFormat;
	public String		timeFormat;
	public String		firstLineLocalChestShop;
	public String		firstLineGlobalChestShop;
	public String		firstLineAdminShop;
	public String		secondLineAllItems;
	private String		displayItemAllItems;
	public String		secondLineAllLocalShops;
	private String		displayItemAllLocalShops;
	public String		seconLineColor;
	private String		buyButton;
	private String		sellButton;
	private String		sellAllButton;
	private String		amountButton;
	private String		closeButton;
	private String		background;
	public int			pricePickerMultiplier;
	public boolean		broadcastSells;
	public boolean		broadcastCreationOfNewAuctions;
	public boolean		hideCategoryItemsNotContainingAuctions;
	
	public boolean		disableWorldGroups;

	public boolean		indicateAuctionAmount;
	public double		defaultInitialAuctionPrice;
	public double		maximalPriceAnAuctionCanSellFor;
	public BigDecimal	taxDecimal;
	public String		taxString;
	public boolean		showAdminshopsInsideGlobalShops;
	public int			amountOfLocalChestShopsPerPlayer;
	// public int amountOfMaximumAuctionsPerPlayer;

	public boolean		buyCommandShowsOnlyAdminShops;
	public boolean		whenSearchFindsNoItemsShowAllItems;
	public boolean		escButtonLeadsToPreviousWindow;
	public boolean		playersCanDeleteShopThatStillContainAuctions;
	public int			defaultShopTypForTheGlobalShopCommand;
	public boolean		deleteLocalShopsWhenUnableToFindChest;
	public boolean		logTransactionsToFile;
	public int			auctionExpirationOffsetInDays;

	public boolean		allowShiftClicksForQuickBuy;

	public MainConfig() throws FileNotFoundException, IOException, InvalidConfigurationException {
		this.readFile();
	}

	public ItemStack getDisplayItemAllItems() {
		try {
			return GlobalChestShop.convertRandomStringToItemStack(this.displayItemAllItems, null);
		} catch (ItemStackNotFoundException e) {
			return new ItemStack(Material.DIAMOND);
		}
	}

	public ItemStack getDisplayItemAllLocalShops() {
		try {
			return GlobalChestShop.convertRandomStringToItemStack(this.displayItemAllLocalShops, null);
		} catch (ItemStackNotFoundException e) {
			return new ItemStack(Material.DIAMOND);
		}
	}

	public ItemStack getBuyButton() {
		try {
			return GlobalChestShop.convertRandomStringToItemStack(this.buyButton, null);
		} catch (ItemStackNotFoundException e) {
			return new ItemStack(Material.SIGN);
		}
	}

	public ItemStack getSellButton() {
		try {
			return GlobalChestShop.convertRandomStringToItemStack(this.sellButton, null);
		} catch (ItemStackNotFoundException e) {
			return new ItemStack(Material.SIGN);
		}
	}

	public ItemStack getSellAllButton() {
		try {
			return GlobalChestShop.convertRandomStringToItemStack(this.sellAllButton, null);
		} catch (ItemStackNotFoundException e) {
			return new ItemStack(Material.SIGN);
		}
	}

	public ItemStack getAmountButton() {
		try {
			return GlobalChestShop.convertRandomStringToItemStack(this.amountButton, null);
		} catch (ItemStackNotFoundException e) {
			return new ItemStack(Material.ANVIL);
		}
	}

	public ItemStack getCloseButton() {
		try {
			return GlobalChestShop.convertRandomStringToItemStack(this.closeButton, null);
		} catch (ItemStackNotFoundException e) {
			return new ItemStack(XMaterial.RED_STAINED_GLASS.parseMaterial(), 1, (short) 14);
		}
	}

	public ItemStack getBackground() {
		if (background.length() == 0 || background == " " || background == "0" || background == "0:0" || background.equalsIgnoreCase("none") || background.equalsIgnoreCase("disabled") || background.equalsIgnoreCase("null") || background.equalsIgnoreCase("air")) {
			return new ItemStack(Material.AIR);
		}
		try {
			return GlobalChestShop.convertRandomStringToItemStack(this.background, null);
		} catch (ItemStackNotFoundException e) {
			return new ItemStack(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 15);
		}
	}

	private void readFile() throws FileNotFoundException, IOException, InvalidConfigurationException {
		File file = new File("plugins/GlobalChestShop/", "config.yml");
		FileConfiguration cfg = new YamlConfiguration();
		cfg.load(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));

		// ---------------------------------------
		// Writing

		// MySQL
		cfg.addDefault("MySQL.host", "localhost");
		cfg.addDefault("MySQL.port", 3306);
		cfg.addDefault("MySQL.database", "GlobalChestShop");
		cfg.addDefault("MySQL.user", "root");
		cfg.addDefault("MySQL.password", "");
		cfg.addDefault("MySQL.tablePrefix", "");
		cfg.addDefault("MySQL.simultaneousOpenedConnections", 4);

		// Appearance
		// cfg.addDefault("Appearance.dateFormat", "dd.MM.yyyy");
		// cfg.addDefault("Appearance.timeFormat", "hh:mm:ss");
		cfg.addDefault("Appearance.Sign.localChestShop", "&a[LocalShop]");
		cfg.addDefault("Appearance.Sign.globalChestShop", "&a[GlobalShop]");
		cfg.addDefault("Appearance.Sign.adminShop", "&a[AdminShop]");
		cfg.addDefault("Appearance.Sign.seconLineColor", "&2");
		cfg.addDefault("Appearance.Sign.AllItems.signFooter", "all items");
		cfg.addDefault("Appearance.Sign.AllItems.displayItem", "COMPASS");
		cfg.addDefault("Appearance.Sign.AllLocalShops.signFooter", "all chest-shops");
		cfg.addDefault("Appearance.Sign.AllLocalShops.displayItem", "COMPASS");
		cfg.addDefault("Appearance.buyButton", "323:0");
		cfg.addDefault("Appearance.sellButton", "SIGN");
		cfg.addDefault("Appearance.sellAllButton", "323:0");
		cfg.addDefault("Appearance.amountButton", "145:0");
		cfg.addDefault("Appearance.closeButton", "160:14");
		cfg.addDefault("Appearance.background", "160:15");
		cfg.addDefault("Appearance.showAuctionCountByAmount", false);
		cfg.addDefault("Appearance.hideCategoryItemsNotContainingAuctions", false);
		cfg.addDefault("Appearance.defaultShopTypForTheGlobalShopCommand", 1);

		// Other
		cfg.addDefault("Other.deleteLocalShopsWhenUnableToFindChest", false);
		cfg.addDefault("Other.showAdminshopsInsideGlobalShops", true);
		cfg.addDefault("Other.amountOfLocalChestShopsPerPlayer", 1);
		// cfg.addDefault("Other.amountOfMaximumAuctionsPerPlayer", 60);
		cfg.addDefault("Other.buyCommandShowsOnlyAdminShops", false);
		cfg.addDefault("Other.whenSearchFindsNoItemsShowAllItems", true);
		cfg.addDefault("Other.escButtonLeadsToPreviousWindow", true);
		cfg.addDefault("Other.playersCanDeleteShopThatStillContainAuctions", false);
		cfg.addDefault("Other.pricePickerMultiplier", 1);
		cfg.addDefault("Other.broadcastSells", true);
		cfg.addDefault("Other.broadcastCreationOfNewAuctions", false);
		cfg.addDefault("Other.allowShiftClicksForQuickBuy", false);
		cfg.addDefault("Other.logTransactionsToFile", true);
		cfg.addDefault("Other.auctionExpirationOffsetInDays", 365);
		cfg.addDefault("Other.disableWorldGroups", false);

		// Money
		cfg.addDefault("Money.tax", "5.0%");
		cfg.addDefault("Money.defaultInitialAuctionPrice", 100.0);
		cfg.addDefault("Money.maximalPriceAnAuctionCanSellFor", 5000000.0);

		// Speichere Defaults
		cfg.options().copyDefaults(true);
		Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
		fileWriter.write(cfg.saveToString());
		fileWriter.close();

		// ---------------------------------------
		// Reading

		// MySQL
		this.host = cfg.getString("MySQL.host");
		this.port = cfg.getInt("MySQL.port");
		this.database = cfg.getString("MySQL.database");
		this.user = cfg.getString("MySQL.user");
		this.password = cfg.getString("MySQL.password");
		this.tablePrefix = cfg.getString("MySQL.tablePrefix");
		this.simultaneousOpenedConnections = cfg.getInt("MySQL.simultaneousOpenedConnections");

		// Appearance
		// this.dateFormat = cfg.getString("Appearance.dateFormat");
		// this.timeFormat = cfg.getString("Appearance.timeFormat");
		this.firstLineLocalChestShop = ChatColor.translateAlternateColorCodes('&', cfg.getString("Appearance.Sign.localChestShop"));
		this.firstLineGlobalChestShop = ChatColor.translateAlternateColorCodes('&', cfg.getString("Appearance.Sign.globalChestShop"));
		this.firstLineAdminShop = ChatColor.translateAlternateColorCodes('&', cfg.getString("Appearance.Sign.adminShop"));
		this.seconLineColor = ChatColor.translateAlternateColorCodes('&', cfg.getString("Appearance.Sign.seconLineColor"));
		this.secondLineAllItems = ChatColor.translateAlternateColorCodes('&', cfg.getString("Appearance.Sign.AllItems.signFooter"));
		this.displayItemAllItems = cfg.getString("Appearance.Sign.AllItems.displayItem");
		this.secondLineAllLocalShops = ChatColor.translateAlternateColorCodes('&', cfg.getString("Appearance.Sign.AllLocalShops.signFooter"));
		this.displayItemAllLocalShops = cfg.getString("Appearance.Sign.AllLocalShops.displayItem");
		this.buyButton = cfg.getString("Appearance.buyButton");
		this.sellButton = cfg.getString("Appearance.sellButton");
		this.sellAllButton = cfg.getString("Appearance.sellAllButton");
		this.amountButton = cfg.getString("Appearance.amountButton");
		this.closeButton = cfg.getString("Appearance.closeButton");
		this.background = cfg.getString("Appearance.background");
		this.indicateAuctionAmount = cfg.getBoolean("Appearance.showAuctionCountByAmount");
		this.hideCategoryItemsNotContainingAuctions = cfg.getBoolean("Appearance.hideCategoryItemsNotContainingAuctions");
		this.defaultShopTypForTheGlobalShopCommand = cfg.getInt("Appearance.defaultShopTypForTheGlobalShopCommand");

		// Other
		this.showAdminshopsInsideGlobalShops = cfg.getBoolean("Other.showAdminshopsInsideGlobalShops");
		this.amountOfLocalChestShopsPerPlayer = cfg.getInt("Other.amountOfLocalChestShopsPerPlayer");
		// this.amountOfMaximumAuctionsPerPlayer =
		// cfg.getInt("Other.amountOfMaximumAuctionsPerPlayer");
		this.buyCommandShowsOnlyAdminShops = cfg.getBoolean("Other.buyCommandShowsOnlyAdminShops");
		this.whenSearchFindsNoItemsShowAllItems = cfg.getBoolean("Other.whenSearchFindsNoItemsShowAllItems");
		this.escButtonLeadsToPreviousWindow = cfg.getBoolean("Other.escButtonLeadsToPreviousWindow");
		this.playersCanDeleteShopThatStillContainAuctions = cfg.getBoolean("Other.playersCanDeleteShopThatStillContainAuctions");
		this.pricePickerMultiplier = cfg.getInt("Other.pricePickerMultiplier");
		this.broadcastSells = cfg.getBoolean("Other.broadcastSells");
		this.broadcastCreationOfNewAuctions = cfg.getBoolean("Other.broadcastCreationOfNewAuctions");
		this.allowShiftClicksForQuickBuy = cfg.getBoolean("Other.allowShiftClicksForQuickBuy");
		this.deleteLocalShopsWhenUnableToFindChest = cfg.getBoolean("Other.deleteLocalShopsWhenUnableToFindChest");
		this.logTransactionsToFile = cfg.getBoolean("Other.logTransactionsToFile");
		this.auctionExpirationOffsetInDays = cfg.getInt("Other.auctionExpirationOffsetInDays");
		this.disableWorldGroups = cfg.getBoolean("Other.disableWorldGroups");

		// Money
		this.taxDecimal = new BigDecimal(cfg.getString("Money.tax").trim().replace("%", "")).divide(BigDecimal.valueOf(100));
		this.taxString = cfg.getString("Money.tax");
		this.defaultInitialAuctionPrice = cfg.getDouble("Money.defaultInitialAuctionPrice");
		this.maximalPriceAnAuctionCanSellFor = cfg.getDouble("Money.maximalPriceAnAuctionCanSellFor");

		this.validateConfigs();
	}

	private void validateConfigs() {
		if (this.pricePickerMultiplier <= 0) {
			pricePickerMultiplier = 1;
		}
		if (this.pricePickerMultiplier % 10 > 1) {
			pricePickerMultiplier = 1;
		}

	}

}
