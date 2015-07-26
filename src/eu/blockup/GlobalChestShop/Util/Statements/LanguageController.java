package eu.blockup.GlobalChestShop.Util.Statements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.bukkit.configuration.InvalidConfigurationException;

public class LanguageController {

	public String					GUI_WarningGuiTitle											= "GuiTitles.Warning";
	public String					Message_ItemNotAllowed										= "Messages.ItemNotAllowed";

	public String					Message_noChestFound										= "Messages.NoChestFound";
	public String					Message_noChestOwner										= "Messages.NoChestOwner";
	public String					Message_tooManyShops										= "Messages.TooManyShops";
	public String					Message_BuyCommandWrongUsage								= "Messages.BuyCommandWrongUsage";
	public String					Message_TooManyAuctions										= "Messages.TooManyAuctions";
	public String					Message_WarningDeleteShopStillHaveAuctions					= "Messages.CanNotDeleteShopTooManyAuctions";

	public String					Message_withdrawn											= "Messages.MoneyWithdrawn";
	public String					Message_deposit_Adminshop									= "Messages.MoneyDeposit";
	public String					Message_deposit												= "Messages.MoneyDepositTax";
	public String					Message_worldNotFound										= "Messages.WorldNotFound";
	public String					Message_informAboutBuy										= "Messages.InformAboutBuy";
	public String					Message_broadcastSell										= "Messages.BroadcastSell";
	public String					Message_BroadcastCreationOfNewAuction								= "Messages.BroadcastCreationOfNewAuction";
	public String					Button_Free													= "Buttons.Free";
	public String					Button_Next													= "Buttons.Next";
	public String					Button_SellSingle											= "Buttons.SellSingle";
	public String					Button_DeleteShop										= "Buttons.DeleteShop";
	public String					Button_SellAll												= "Buttons.SellAll	";
	public String					Button_Finish												= "Buttons.Finish";
	public String					GUI_AuctionSellItem											= "GuiTitles.AuctionSellItem";

	public String					Message_ItemDamaged											= "Messages.ItemIsDamaged";
	public String					Message_AdminShop_under_Construction						= "Messages.AdminShopIsDisabled";
	public String					Button_Money												= "Buttons.MoneyButtons.Title";
	public String					Category1													= "DefaultCategories.BuildingBlocks";
	public String					Category2													= "DefaultCategories.DecorationBlocks";
	public String					Category3													= "DefaultCategories.Redstone";
	public String					Category4													= "DefaultCategories.Transportation";
	public String					Category5													= "DefaultCategories.Miscellaneous";
	public String					Category6													= "DefaultCategories.Foodstuff";
	public String					Category7													= "DefaultCategories.Tools";
	public String					Category8													= "DefaultCategories.Combat";
	public String					Category9													= "DefaultCategories.Brewing";
	public String					Category10													= "DefaultCategories.Materials";
	public String					PermissionWarning											= "Messages.PermissionWarning";
	public String					PolarQuestion_YES											= "Buttons.PolarQuestion.YES";
	public String					PolarQuestion_NO											= "Buttons.PolarQuestion.NO";
	public String					PolarQuestion_NO_Leave_NO_SAVE								= "Buttons.PolarQuestion.NoWithoutSave";
	public String					Inventory_NoSpace											= "Messages.Inventory.NoSpace";
	public String					Inventory_NoItem											= "Messages.Inventory.NoItem";
	public String					Inventory_NoMoney											= "Messages.Inventory.NoMoney";
	public String					GUI_AllPlayerHistory_Title									= "Buttons.AllPlayerHistory.Title";
	public String					GUI_AllPlayerHistory_Description							= "Buttons.AllPlayerHistory.Description";
	public String					Auction_Status_Active										= "Buttons.AuctionInfo.Status.Active";
	public String					Auction_Status_Canceled										= "Buttons.AuctionInfo.Status.Canceled";
	public String					Auction_Status_Ended										= "Buttons.AuctionInfo.Status.Ended";
	public String					Auction_Info_Seller											= "Buttons.AuctionInfo.Seller";
	public String					Auction_Info_Buyer											= "Buttons.AuctionInfo.Buyer";
	public String					Auction_Info_Amount											= "Buttons.AuctionInfo.Amount";
	public String					Auction_Info_PriceEach										= "Buttons.AuctionInfo.PriceEach";
	public String					Auction_Info_BuyPriceEach									= "Buttons.AuctionInfo.BuyPriceEach";
	public String					Auction_Info_SellPriceEach									= "Buttons.AuctionInfo.SellPriceEach";
	public String					Auction_Info_PriceTotal										= "Buttons.AuctionInfo.PriceTotal";
	public String					Auction_Info_Date_Started									= "Buttons.AuctionInfo.DateStarted";
	public String					Auction_Info_Date_Ended										= "Buttons.AuctionInfo.DateEnded";
	public String					Auction_Title_AdminShop										= "Buttons.AuctionInfo.Title.AdminShop";
	public String					Auction_Title_ChestShop										= "Buttons.AuctionInfo.Title.LocalShop";
	public String					Button_Auction_Create_Title									= "Buttons.AuctionCreate.Title";
	public String					Auction_Create_Description									= "Buttons.AuctionCreate.Description";
	public String					GUI_AdministrateAdminShop_Title								= "GuiTitles.EditAdminShop";
	public String					GUI_AdministrateAdminShop_Toggle_ON							= "Buttons.EditAdminShop.Toggle.ON.Title";
	public String					GUI_AdministrateAdminShop_Toggle_ON_DESC					= "Buttons.EditAdminShop.Toggle.ON.Description";
	public String					GUI_AdministrateAdminShop_Toggle_OFF						= "Buttons.EditAdminShop.Toggle.OFF.Title";
	public String					GUI_AdministrateAdminShop_Toggle_OFF_DESC					= "Buttons.EditAdminShop.Toggle.OFF.Description";
	public String					GUI_Button_BuyPrice_Title									= "Buttons.BuyPrice.Title";
	public String					GUI_Button_BuyPrice_DESC									= "Buttons.BuyPrice.Description";
	public String					GUI_Button_SellPrice_Title									= "Buttons.SellPrice.Title";
	public String					GUI_Button_SellPrice__DESC									= "Buttons.SellPrice.Description";
	public String					GUI_AdministrateAdminShop_ExitSave							= "Buttons.EditAdminShop.ExitSave.Title";
	public String					GUI_AdministrateAdminShop_ExitSave_DESC						= "Buttons.EditAdminShop.ExitSave.Description";
	public String					GUI_AdministrateAdminShop_Warning_BuyPriceGreaterSellPrice	= "Messages.WarningBuyPriceGreaterSellPrice";
	public String					GUI_AdministrateAdminShop_WQuestion_Save_Changes			= "Messages.QuestionSaveChanges";
	public String					GUI_AdministrateAuction_Title								= "GuiTitles.CancelAuction";
	public String					GUI_AdministrateAuction_Cancel_Auction						= "Buttons.CancelAuction";
	public String					GUI_AdministrateAuction_Cancel_Auction_Question				= "Buttons.PolarQuestion.CancelAuction";
	public String					GUI_AdministrateAuction_Auction_Allready_Ended				= "Messages.AuctionAllreadyEnded";
	// public String GUI_AdministrateAuction_Auction_Sucsessful_created =
	// "Messages.AuctionSucsessfulcreated";
	public String					GUI_Title_AdminShop											= "GuiTitles.AdminShop";
	public String					GUI_BuyAdminShop_Amount_Button								= "Buttons.AmountButtons.Title";
	public String					GUI_BuyAdminShop_Amount_Button_DESC							= "Buttons.AmountButtons.Description";
	public String					GUI_BuyAuction_Title										= "GuiTitles.BuyAuction";
	public String					GUI_MainWindos_GUI_Title									= "GuiTitles.AllItemsInCategories";
	public String					GUI_Title_AllAuctions										= "GuiTitles.AllAuctions";
	public String					GUI_ChooseCategory_MyAuction_Button							= "Buttons.AuctionHistory.Title";
	public String					GUI_ChooseCategory_MyAuction_Button_DESC					= "Buttons.AuctionHistory.Description";
	public String					GUI_CreateAdminShop_Title									= "GuiTitles.CreateAdminShop";
	public String					GUI_CreateAdminShop_SetSellPrice_Title						= "GuiTitles.SetSellPrice";
	public String					GUI_CreateAdminShop_SetBuyPrice_Title						= "Buttons.SetBuyPriceTitle";
	public String					GUI_CreateAdminShop_SubmitButton							= "Buttons.CreateAdminShop.Title";
	public String					GUI_CreateAdminShop_SubmitButton_DESC						= "Buttons.CreateAdminShop.Description";
	public String					GUI_CreateAuction_Holding_Item_Title						= "Buttons.ChooseItem.Title";
	public String					GUI_CreateAuction_Holding_Item_DESC							= "Buttons.ChooseItem.Description";
	public String					GUI_CreateAuction_Title										= "GuiTitles.CreateAuction";
	public String					GUI_CreateAuction_Holding_Item_ClickMessage					= "Messages.ChooseItem";
	public String					GUI_CreateAuction_PriceChange_Menu_Title					= "GuiTitles.AuctionChangePrice";
	public String					GUI_CreateAuction_Submit_Button								= "Buttons.CreateAuction.Title";
	public String					GUI_CreateAuction_Submit_Button_DESC						= "Buttons.CreateAuction.Description";
	public String					GUI_EndedAuction_Title										= "GuiTitles.EndedAuctionTitle";
	public String					GUI_History_RunningAuctions									= "GuiTitles.History.RunningAuctions";
	public String					GUI_History_BoughtAuctions									= "GuiTitles.History.BoughtAuctions";
	public String					GUI_History_SoldAuctions									= "GuiTitles.History.SoldAuctions";
	public String					GUI_History_CanceledAuctions								= "GuiTitles.History.CanceledAuctions";
	public String					GUI_SubmitBuy_Title											= "Buttons.PolarQuestion.SubmitBuy";
	public String					GUI_SubmitBuy_Sure_to_Buy									= "Buttons.PolarQuestion.SuretoBuy";
	public String					GUI_SubmitBuy_TooSlow_Auction_Ended							= "Messages.TooSlowAuctionEnded";
	public String					GUI_SubmitBuy_ItemBoughtSuccsses							= "Messages.ItemBought";
	public String					GUI_SubmitCreateAuction_Question							= "Buttons.PolarQuestion.CreateAuction";
	public String					GUI_SubmitCreateAuction_Succsess							= "Messages.CreatedAuction";
	public String					GUI_Message_RemovedAuction									= "Messages.RemovedAuction";
	public String					GUI_SubmitSell_Question										= "Buttons.PolarQuestion.SubmitSell";
	public String					GUI_SubmitSell_Item_Sold_SUccses							= "Messages.ItemSold";
	public String					StateKeeperPrice_PriceCantBeLessThanZero					= "Messages.PriceCantBeLessThanZero";
	// public String StateKeeperPrice_BuyPrice = "Buttons.BuyPrice";
	// public String StateKeeperPrice_SellPrice = "Buttons.SellPrice";
	public String					Auction_GetBuyButton_Title									= "Buttons.BuyButtons.Title";
	public String					Auction_GetSellButton_Title									= "Buttons.SellButtons.Title";
	public String					Auction_GetSellAllButton_Title								= "Buttons.SellAllButtons.Title";
	public String					BackButton_Title											= "Buttons.BackButtons.Title";
	public String					BackButton_DESC												= "Buttons.BackButtons.Description";
	public String					CloseButton_Title											= "Buttons.CloseButtons.Title";
	public String					CloseButton_DESC											= "Buttons.CloseButtons.Description";
	public String					PageView_SinglePage											= "Buttons.PageView.SinglePage";
	public String					PageView_FirstPage											= "Buttons.PageView.FirstPage";
	public String					PageView_LastPage											= "Buttons.PageView.LastPage";
	public String					PageView_CurrentPage										= "Buttons.PageView.CurrentPage";

	public String					GUI_Title_LocalChestShop									= "GuiTitles.LocalChestShop";
	public String					GUI_Title_AllLocalChestShop									= "GuiTitles.AllLocalChestShop";
	public String					GUI_Title_GUI_AuctionItemPage								= "GuiTitles.AuctionPage";
	public String					GUI_Title_GUI_Search										= "GuiTitles.Search";
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private HashMap<String, String>	hashMap_Messages;

	public LanguageController() throws IOException, InvalidConfigurationException {
		this.hashMap_Messages = new HashMap<String, String>();
		this.fillHashMapWithDefaults();
		this.read_language_file();
	}

	private void read_language_file() throws IOException, InvalidConfigurationException {
		File file = new File("plugins/GlobalChestShop/", "language.yml");
		boolean debug1 = false;
		if (debug1) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		// // TEST

		FileInputStream fileinputstream = null;
		try {
			fileinputstream = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		FileConfiguration cfg = new YamlConfiguration();
		cfg.load(new InputStreamReader(fileinputstream, Charset.forName("UTF-8")));
		// //// / TESTG

		// Schreibe Defaults;
		for (Map.Entry<String, String> entry : this.hashMap_Messages.entrySet()) {
			cfg.addDefault(entry.getKey(), entry.getValue());
		}
		cfg.options().copyDefaults(true);
		// cfg.save(file);

		Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
		fileWriter.write(cfg.saveToString());
		fileWriter.close();

		// Read into HashMap
		HashMap<String, String> oldList = this.hashMap_Messages;
		HashMap<String, String> newList = new HashMap<String, String>();

		boolean debug = false;
		if (!debug) {
			for (Map.Entry<String, String> entry : oldList.entrySet()) {
				newList.put(entry.getKey(), cfg.getString(entry.getKey()));
			}
			this.hashMap_Messages = newList;
			oldList.clear();
		}

		return;
	}

	public String get(String key) {
		return this.get(key, "");
	}

	public String get(String key, String arg0) {
		return this.get(key, arg0, "");
	}

	public String get(String key, String arg0, String arg1) {
		return this.get(key, arg0, arg1, "");
	}

	public String get(String key, String arg0, String arg1, String arg2) {
		return this.get(key, arg0, arg1, arg2, "");
	}

	public String get(String key, String arg0, String arg1, String arg2, String arg3) {

		String result = hashMap_Messages.get(key);
		if (result == null) {
			return "&cError in language.yml at" + key;
		}
		result = result.replaceAll(Pattern.quote("%arg0"), Matcher.quoteReplacement(arg0));
		result = result.replaceAll(Pattern.quote("%arg1"), Matcher.quoteReplacement(arg1));
		result = result.replaceAll(Pattern.quote("%arg2"), Matcher.quoteReplacement(arg2));
		result = result.replaceAll(Pattern.quote("%arg3"), Matcher.quoteReplacement(arg3));
		result = ChatColor.translateAlternateColorCodes('&', result);

		boolean debug = false;
		if (debug) {
			System.out.println("Message Loaded: " + result);
			return key;
		}
		return result;
	}

	public void fillHashMapWithDefaults() {
		this.hashMap_Messages.put("Version_DONT_CHANGE_THIS!", "1.0");
		this.hashMap_Messages.put(this.PageView_SinglePage, "Page: %arg0");
		this.hashMap_Messages.put(this.PageView_FirstPage, "First Page");
		this.hashMap_Messages.put(this.PageView_LastPage, "Last Page");
		this.hashMap_Messages.put(this.PageView_CurrentPage, "Page: %arg0");
		this.hashMap_Messages.put(this.CloseButton_Title, "EXIT");
		this.hashMap_Messages.put(this.CloseButton_DESC, "close this inventory");
		this.hashMap_Messages.put(this.BackButton_Title, "RETURN");
		this.hashMap_Messages.put(this.BackButton_DESC, "go back to previous inventory");
		this.hashMap_Messages.put(this.Category1, "Building Blocks");
		this.hashMap_Messages.put(this.Category2, "Decoration Blocks");
		this.hashMap_Messages.put(this.Category3, "Redstone");
		this.hashMap_Messages.put(this.Category4, "Transportation");
		this.hashMap_Messages.put(this.Category5, "Miscellaneous");
		this.hashMap_Messages.put(this.Category6, "Foodstuff");
		this.hashMap_Messages.put(this.Category7, "Tools");
		this.hashMap_Messages.put(this.Category8, "Combat");
		this.hashMap_Messages.put(this.Category9, "Brewing");
		this.hashMap_Messages.put(this.Category10, "Materials");
		this.hashMap_Messages.put(this.PermissionWarning, "&cYou don't have the permission to do this!");
		this.hashMap_Messages.put(this.PolarQuestion_YES, "Yes");
		this.hashMap_Messages.put(this.PolarQuestion_NO, "No");
		this.hashMap_Messages.put(this.PolarQuestion_NO_Leave_NO_SAVE, "&cNo, leave WITHOUT saving!");
		this.hashMap_Messages.put(this.Inventory_NoSpace, "&cYou don't have enough space in your inventory");
		this.hashMap_Messages.put(this.Inventory_NoItem, "&cYou don't own this item!");
		this.hashMap_Messages.put(this.Inventory_NoMoney, "&cYou don't have enough money!");
		this.hashMap_Messages.put(this.GUI_AllPlayerHistory_Description, "global history - admins only");
		this.hashMap_Messages.put(this.GUI_AllPlayerHistory_Title, "Auction overview");
		this.hashMap_Messages.put(this.Auction_Status_Active, "&7Status: &2active");
		this.hashMap_Messages.put(this.Auction_Status_Canceled, "&7Status: &4canceled");
		this.hashMap_Messages.put(this.Auction_Status_Ended, "&7Status: &cended");
		this.hashMap_Messages.put(this.Auction_Info_Seller, "&7Seller:  &9%arg0");
		this.hashMap_Messages.put(this.Auction_Info_Buyer, "&7Buyer:  &9%arg0");
		this.hashMap_Messages.put(this.Auction_Info_Amount, "&7Amount: &6%arg0");
		this.hashMap_Messages.put(this.Auction_Info_PriceEach, "&7PriceEach:  &c%arg0");
		this.hashMap_Messages.put(this.Auction_Info_BuyPriceEach, "&7Buy price each:  &c%arg0");
		this.hashMap_Messages.put(this.Auction_Info_SellPriceEach, "&7Sell price each:  &c%arg0");
		this.hashMap_Messages.put(this.Auction_Info_PriceTotal, "&7PriceTotal:  &c%arg0");
		this.hashMap_Messages.put(this.Auction_Info_Date_Started, "&7started: %arg0");
		this.hashMap_Messages.put(this.Auction_Info_Date_Ended, "&7ended: %arg0");
		this.hashMap_Messages.put(this.Auction_Title_AdminShop, "AdminShop");
		this.hashMap_Messages.put(this.Auction_Title_ChestShop, "Player offer");
		this.hashMap_Messages.put(this.Button_Auction_Create_Title, "&2Create new auction");
		this.hashMap_Messages.put(this.Auction_Create_Description, "&7click here to sell your items");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_Title, "&6Edit AdminShop");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_Toggle_ON, "&7AdminShop is &aenabled");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_Toggle_ON_DESC, "&9click to &cdisable");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_Toggle_OFF, "&7AdminShop is &cdisabled");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_Toggle_OFF_DESC, "&9click to &aenable");
		this.hashMap_Messages.put(this.GUI_Button_BuyPrice_Title, "&6Buy Price");
		this.hashMap_Messages.put(this.GUI_Button_BuyPrice_DESC, "&7click to change the buy price");
		this.hashMap_Messages.put(this.GUI_Button_SellPrice_Title, "&6Sell Price");
		this.hashMap_Messages.put(this.GUI_Button_SellPrice__DESC, "&7click to change the sell price");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_ExitSave, "Save and exit");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_ExitSave_DESC, "&7click here to exit");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_Warning_BuyPriceGreaterSellPrice, "&cBuy price can't be greater sell price!");
		this.hashMap_Messages.put(this.GUI_AdministrateAdminShop_WQuestion_Save_Changes, "&cSave all changes?");
		this.hashMap_Messages.put(this.GUI_AdministrateAuction_Title, "&cCanceling auction");
		this.hashMap_Messages.put(this.GUI_AdministrateAuction_Cancel_Auction, "&cCancel this auction");
		this.hashMap_Messages.put(this.GUI_AdministrateAuction_Cancel_Auction_Question, "&cAre you sure you want to cancel?");
		this.hashMap_Messages.put(this.GUI_AdministrateAuction_Auction_Allready_Ended, "&cThis auction was already bought");
		this.hashMap_Messages.put(this.GUI_BuyAdminShop_Amount_Button, "&6Amount");
		this.hashMap_Messages.put(this.GUI_BuyAdminShop_Amount_Button_DESC, "&7Use shift to stack");
		this.hashMap_Messages.put(this.GUI_BuyAuction_Title, "Buying an auction");
		this.hashMap_Messages.put(this.GUI_MainWindos_GUI_Title, "GlobalChestShop");
		this.hashMap_Messages.put(this.GUI_ChooseCategory_MyAuction_Button, "Overview");
		this.hashMap_Messages.put(this.GUI_ChooseCategory_MyAuction_Button_DESC, "auction history");
		this.hashMap_Messages.put(this.GUI_CreateAdminShop_Title, "&2Create new Adminshop");
		this.hashMap_Messages.put(this.GUI_CreateAdminShop_SetSellPrice_Title, "&6Change sell price");
		this.hashMap_Messages.put(this.GUI_CreateAdminShop_SetBuyPrice_Title, "&6Change buy price");
		this.hashMap_Messages.put(this.GUI_CreateAdminShop_SubmitButton, "&2Create / Edit Adminshop");
		this.hashMap_Messages.put(this.GUI_CreateAdminShop_SubmitButton_DESC, "&7click to create / edit an AdminShop");
		this.hashMap_Messages.put(this.GUI_CreateAuction_Holding_Item_Title, "&aDrag an item here");
		this.hashMap_Messages.put(this.GUI_CreateAuction_Holding_Item_DESC, "&aor use SHIFT-Click");
		this.hashMap_Messages.put(this.GUI_CreateAuction_Holding_Item_ClickMessage, "&aDrag an item in the center slot to sell it");
		this.hashMap_Messages.put(this.GUI_CreateAuction_Title, "&2Creating new auction");
		this.hashMap_Messages.put(this.GUI_CreateAuction_PriceChange_Menu_Title, "&6Changing price");
		this.hashMap_Messages.put(this.GUI_CreateAuction_Submit_Button, "&2Create new auction");
		this.hashMap_Messages.put(this.GUI_CreateAuction_Submit_Button_DESC, "&7click here to sell your item");
		this.hashMap_Messages.put(this.GUI_History_RunningAuctions, "&aRunning Auctions");
		this.hashMap_Messages.put(this.GUI_History_BoughtAuctions, "&9Bought auction");
		this.hashMap_Messages.put(this.GUI_History_SoldAuctions, "&bSold auctions");
		this.hashMap_Messages.put(this.GUI_History_CanceledAuctions, "&6Canceled Auctions");
		this.hashMap_Messages.put(this.GUI_SubmitBuy_Title, "&cAre you sure to buy?");
		this.hashMap_Messages.put(this.GUI_SubmitBuy_Sure_to_Buy, "&cAre you sure to buy?");
		this.hashMap_Messages.put(this.GUI_SubmitBuy_TooSlow_Auction_Ended, "&cThis auction is no longer for sale!");
		this.hashMap_Messages.put(this.GUI_SubmitBuy_ItemBoughtSuccsses, "&aYou just bought an item &b%arg0");
		this.hashMap_Messages.put(this.GUI_SubmitCreateAuction_Question, "&cSure to create a new auction?");
		this.hashMap_Messages.put(this.GUI_SubmitCreateAuction_Succsess, "&aYou have successfully created a new auction");
		this.hashMap_Messages.put(this.GUI_SubmitSell_Question, "&cAre you sure to sell?");
		this.hashMap_Messages.put(this.GUI_SubmitSell_Item_Sold_SUccses, "&aYou just sold an item");
		this.hashMap_Messages.put(this.StateKeeperPrice_PriceCantBeLessThanZero, "&cPrice can't be nagative");
		this.hashMap_Messages.put(this.Auction_GetBuyButton_Title, "&9Buy");
		this.hashMap_Messages.put(this.Auction_GetSellButton_Title, "&9Sell");
		this.hashMap_Messages.put(this.Auction_GetSellAllButton_Title, "&9Sell all");
		this.hashMap_Messages.put(this.GUI_Title_GUI_AuctionItemPage, "&8Auctions");
		this.hashMap_Messages.put(this.GUI_Title_LocalChestShop, "&0LocalChestShop");
		this.hashMap_Messages.put(this.GUI_Title_AllLocalChestShop, "&6 All LocalChestShops");
		this.hashMap_Messages.put(this.GUI_Title_AllAuctions, "&6 All Auctions");
		this.hashMap_Messages.put(this.GUI_EndedAuction_Title, "&7 This auction has ended!");
		this.hashMap_Messages.put(this.GUI_Title_AdminShop, "&6 AdminShop");
		this.hashMap_Messages.put(this.Message_AdminShop_under_Construction, "&cThis AdminShop is not available!");
		this.hashMap_Messages.put(this.Button_Money, "&6Your money");
		this.hashMap_Messages.put(this.Message_ItemDamaged, "&cItem is damaged");
		this.hashMap_Messages.put(this.Message_withdrawn, "[Money] " + "%arg0" + " were withdrawn from your account.");
		this.hashMap_Messages.put(this.Message_deposit, "[BANK] You have received " + "%arg0" + "(" + "%arg1" + " are tax)");
		this.hashMap_Messages.put(this.Message_deposit_Adminshop, "[BANK] You have received " + "%arg0" + ".");
		this.hashMap_Messages.put(this.Message_informAboutBuy, "[SHOP] " + "%arg0" + " has purchased your auction: " + "%arg1" + " " + "%arg2");
		this.hashMap_Messages.put(this.Message_worldNotFound, "&c You cannot access the shop system in this world");
		this.hashMap_Messages.put(this.Button_Free, "&aFree");
		this.hashMap_Messages.put(this.Message_noChestFound, "&cYou can only create shops 1 block away from a chest");
		this.hashMap_Messages.put(this.Message_noChestOwner, "&cIt seems this chest is not yours. If it is yours, place the chest again!");
		this.hashMap_Messages.put(this.Message_tooManyShops, "&cYou already have too many shops ");
		this.hashMap_Messages.put(this.GUI_Title_GUI_Search, "&0Search results of: \"&2%arg0&0\"");
		this.hashMap_Messages.put(this.Message_BuyCommandWrongUsage, "&7/GlobalChestShop buy &c<itemname | hand>");
		this.hashMap_Messages.put(this.GUI_Message_RemovedAuction, "&cCanceled auction");
		this.hashMap_Messages.put(this.Button_Next, "&aNext");
		this.hashMap_Messages.put(this.Button_Finish, "&aFINISH - Sell this item");
		this.hashMap_Messages.put(this.GUI_AuctionSellItem, "&aSell this item");

		this.hashMap_Messages.put(this.Message_TooManyAuctions, "&cYou cant have more than " + "%arg0" + " running auctions");
		this.hashMap_Messages.put(this.GUI_WarningGuiTitle, "&cWarning");
		this.hashMap_Messages.put(this.Message_WarningDeleteShopStillHaveAuctions, "&cThis shop still contains running auctions. To delete a shop you have to cancel all your auctions first.");
		this.hashMap_Messages.put(this.Message_ItemNotAllowed, "&cYou cannot sell this item here.");

		this.hashMap_Messages.put(this.Message_broadcastSell, "&a[GlobalChestShop]  &6%arg0 &ahas purchased &6%arg1 &afor %arg2 &aeach from %arg3&a.");
		
		//this.hashMap_Messages.put(this.BroadcastCreationOfNewAuction, "&a[GlobalChestShop]  &6Player &ahas created a new global auction: &664 Stone &afor &6price &aeach.");
		this.hashMap_Messages.put(this.Message_BroadcastCreationOfNewAuction, "&a[GlobalChestShop]  &6%arg0 &ahas created a new global auction: &6%arg1 %arg2 &afor &6%arg3 &aeach.");
		
		
		
		this.hashMap_Messages.put(this.Button_SellSingle, "&aSell this item stack");
		this.hashMap_Messages.put(this.Button_DeleteShop, "&cDelete Shop");
		this.hashMap_Messages.put(this.Button_SellAll, "&aSell as many stacks I own");
	}
	// ////// CHINA
	//
	//
	//
	//
	//
	// private static FileConfiguration config;
	//
	//
	// public void loadConfig() {
	// String path =
	// GlobalChestShop.plugin.getDataFolder().getAbsolutePath()+File.separator+"lang.yml";
	// GlobalChestShop.plugin.getLogger().log(Level.INFO, "lang path = "+path);
	// File file = new File(path);
	// // Load configuration
	// try {
	// FileInputStream fileinputstream = new FileInputStream(file);
	// config = new YamlConfiguration();
	// config.load(new InputStreamReader(fileinputstream,
	// Charset.forName("UTF-16BE")));
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (InvalidConfigurationException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public String getMessage(short rank, int messageIt) {
	// return config.getString("messages.rank"+rank+".message"+messageIt,"");
	// }
	//
	// public String t(String label) {
	// return config.getString("lang."+label,label).replace("&", "\u00A7");
	// }
	// public String t(String label, Object... args) {
	// String format = config.getString("lang."+label,label);
	// return String.format(format, args).replace("&", "\u00A7");
	// }
	// }
}
