package eu.blockup.GlobalChestShop.Util;


import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_AuctionCreate;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_DebugAllShops;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_DefaultCategoryCollection;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_DeleteAllPlayerShops;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_GlobalShopByAuctions;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_GlobalShopByPlayers;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_Search;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class ShopCommandExecutor implements CommandExecutor {

	public ShopCommandExecutor() {
		super();
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

		if (args.length > 0) {

			// Argument && CONSOLE

			// RELOAD
			if (args[0].equalsIgnoreCase("reload")) {
				if (GlobalChestShop.plugin.validatePermissionCheck(cs, Permissions.ADMIN)) {
					final CommandSender commandSender = cs;
					GlobalChestShop.plugin.getServer().getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new Runnable() {
						public void run() {
							GlobalChestShop.plugin.reload();
							commandSender.sendMessage( ChatColor.GREEN + "GlobalChestShop reloaded");
						}
					}, 1L);
				} else {
					GlobalChestShop.plugin.permissionWarning(cs);
				}

				return true;
			}

			if (!(cs instanceof Player)) {
				cs.sendMessage("You are not a player");
				return true;
			}
			final Player player = (Player) cs;

			// Argument && NO CONSOLE

			// BUY
			if (args[0].equalsIgnoreCase("buy")) {
				int worldGroup;
				try {
					worldGroup = GlobalChestShop.plugin.getworldGroup(player.getLocation());
				} catch (WorldHasNoWorldGroupException e) {
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_worldNotFound));
					if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
						player.sendMessage("You have to configure /plugins/GlobalChestShop/worldGroups.yml");
					}
					return true;
				}
				if (!GlobalChestShop.plugin.validatePermissionCheck(cs, Permissions.VIP_SELL_COMMAND + "." + worldGroup)) {
					GlobalChestShop.plugin.permissionWarning(player);
					return true;
				}
				if (!(args.length > 1)) {
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_BuyCommandWrongUsage));
					return true;
				}
				String searched = args[1];
				if (searched.equalsIgnoreCase("hand")) {
					searched = GlobalChestShop.plugin.getItemStackDisplayName(player.getItemInHand());
				}

				new GUI_Search(searched, null, worldGroup, GlobalChestShop.plugin.getMainConfig().buyCommandShowsOnlyAdminShops, GlobalChestShop.plugin.validatePermissionCheck(cs, Permissions.VIP_CREATE_NEW_AUCTIONS_INSIDE_BUY_COMMAND)).open(player);
				return true;
			}

			// Sell
			if (args[0].equalsIgnoreCase("sell")) {
				int worldGroup;
				try {
					worldGroup = GlobalChestShop.plugin.getworldGroup(player.getLocation());
				} catch (WorldHasNoWorldGroupException e) {
					player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_worldNotFound));
					if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
						player.sendMessage("You have to configure /plugins/GlobalChestShop/worldGroups.yml");
					}
					return true;
				}
				if (!GlobalChestShop.plugin.validatePermissionCheck(cs, Permissions.VIP_SELL_COMMAND + "." + worldGroup)) {
					GlobalChestShop.plugin.permissionWarning(player);
					return true;
				}
				ItemStack preset = null;
				if (args.length > 1 && args[1].equalsIgnoreCase("hand")) {
					preset = player.getItemInHand();
				}
				new GUI_AuctionCreate(null, preset, worldGroup, player).open(player);
				return true;
			}

			// DEBUG
			if (args[0].equalsIgnoreCase("debug")) {
				if (!GlobalChestShop.plugin.validatePermissionCheck(cs, Permissions.ADMIN)) {
					player.sendMessage(ChatColor.RED + "No Permission");
					return true;
				}
				if (!(args.length > 1)) {
					player.sendMessage(ChatColor.GREEN + "/GlobalCHestShop debug " + ChatColor.RED + "<ShopID>");
					player.sendMessage(ChatColor.GREEN + "/GlobalCHestShop debug " + ChatColor.RED + "next");
					
					new GUI_DebugAllShops(null).open(player);
					return true;
				}
				
				if (args[1].equalsIgnoreCase("next")) {
					
					if (GlobalChestShop.plugin.getShopVerwaltung().getBrokenShopList().isEmpty()) {
						player.sendMessage(ChatColor.GREEN + "There are no more broken shops :)");
						return true;
					}
					Shop s = GlobalChestShop.plugin.getShopVerwaltung().getBrokenShopList().get(0);
					if (s == null) {
						return true;
					}
					player.teleport(s.getSignLocation());
					s.onInteractLeftClick(player, null);
					return true;
				}
				if (!(GlobalChestShop.isStringNumeric(args[1]))) {
					player.sendMessage(ChatColor.GREEN + "/GlobalCHestShop debug " + ChatColor.RED + "<ShopID>");
					return true;
				}
				int i = Integer.valueOf(args[1]);
				Shop s = null;
				List<Shop> brokenShopList = GlobalChestShop.plugin.getShopVerwaltung().getBrokenShopList();
				synchronized (brokenShopList) {
					for (Shop tmp : brokenShopList) {
						if (tmp.getShopID() == i) {
							s = tmp;
						}
					}
				}
				if (s == null) {
					player.sendMessage(ChatColor.RED + "Shop " + i + " not found :(");
					return true;
				}
				player.teleport(s.getSignLocation());
				s.onInteractLeftClick(player, null);
				return true;
			}

			// Test
			if (args[0].equalsIgnoreCase("test")) {
				if (GlobalChestShop.plugin.validatePermissionCheck(cs, Permissions.ADMIN)) {
					// player.sendMessage(" May Auctions: " + new
					// AuctionLimitController().getMaxAmountOfRunningAuctions(player,
					// 1));
					// GlobalChestShop.plugin.getPriceEngine().openPriceChangeMenuForItem(player,
					// null, player.getItemInHand(), 1);
					// new PriceWriter();
					// player.sendMessage(new
					// ItemStackUtil().toString(player.getItemInHand()));
					// player.sendMessage("DONE");
					return true;
				} else {
					player.sendMessage("No Permission");
					return true;
				}
			}

			// reset
			if (args[0].equalsIgnoreCase("reset")) {
				if (GlobalChestShop.plugin.validatePermissionCheck(cs, Permissions.ADMIN)) {
					new GUI_DeleteAllPlayerShops(null).open(player);
					return true;
				} else {
					player.sendMessage("No Permission");
					return true;
				}
			}

			player.sendMessage(ChatColor.RED + "Command not found");
			return true;
		}

		// !Argument && NO CONSOLE
		if (!(cs instanceof Player)) {
			cs.sendMessage("You are not a player");
			return true;
		}
		final Player player = (Player) cs;

		// !Argument && CONSOLE

		int worldGroup;
		try {
			worldGroup = GlobalChestShop.plugin.getworldGroup(player.getLocation());
		} catch (WorldHasNoWorldGroupException e) {
			player.sendMessage(GlobalChestShop.text.get(GlobalChestShop.text.Message_worldNotFound));
			if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
				player.sendMessage("You have to configure /plugins/GlobalChestShop/worldGroups.yml");
			}
			return true;
		}

		// OPEN GLOBAL SHOP
		if (GlobalChestShop.plugin.validatePermissionCheck(cs, Permissions.VIP_OPEN_GLOBALSHOP_BY_COMMAND + "." + worldGroup)) {
			int defaultShopTypForTheGlobalShopCommand = GlobalChestShop.plugin.getMainConfig().defaultShopTypForTheGlobalShopCommand;
			boolean canCreateNewAuctions = GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.VIP_CREATE_NEW_AUCTIONS_INSIDE_GLOBALSHOP_COMMAND + "." + worldGroup);
			if (defaultShopTypForTheGlobalShopCommand == 1) {
				new GUI_DefaultCategoryCollection(null, false, worldGroup, canCreateNewAuctions).open(player);
			} else if ( defaultShopTypForTheGlobalShopCommand == 2){
				new GUI_GlobalShopByPlayers(worldGroup, canCreateNewAuctions, null).open(player);
			} else {
				new GUI_GlobalShopByAuctions(worldGroup, false, canCreateNewAuctions, null).open(player);
			}
			
			return true;
		} else {
			GlobalChestShop.plugin.permissionWarning(cs);
		}
		return true;
	}
}
