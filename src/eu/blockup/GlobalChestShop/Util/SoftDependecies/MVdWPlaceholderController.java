package eu.blockup.GlobalChestShop.Util.SoftDependecies;
import org.bukkit.entity.Player;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;


public class MVdWPlaceholderController {

	final String error = " - ERROR: Player not online! - ";
	
	public MVdWPlaceholderController() {
		PlaceholderAPI.registerPlaceholder(GlobalChestShop.plugin, "gcsActiveAuctionCount", new PlaceholderReplacer() {

			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				if (!event.isOnline()) {
					return error;
				}

				Player player = event.getPlayer();

				try {
					return "" + GlobalChestShop.plugin.getAuctionController(GlobalChestShop.plugin.getworldGroup(player.getLocation())).getCountOfActiveAuctionsFromPlayer(player.getUniqueId());
				} catch (WorldHasNoWorldGroupException e) {
					return "" + 0;
				}
			}

		});
		
		PlaceholderAPI.registerPlaceholder(GlobalChestShop.plugin, "gcsSoldAuctionCount", new PlaceholderReplacer() {

			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				if (!event.isOnline()) {
					return error;
				}

				Player player = event.getPlayer();

				try {
					return "" + GlobalChestShop.plugin.getAuctionController(GlobalChestShop.plugin.getworldGroup(player.getLocation())).getAllSoldAuctionsByPlayer(player.getUniqueId()).size();
				} catch (WorldHasNoWorldGroupException e) {
					return "" + 0;
				}
			}

		});
		
		
		
		PlaceholderAPI.registerPlaceholder(GlobalChestShop.plugin, "gcsBoughtAuctionCount", new PlaceholderReplacer() {

			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				if (!event.isOnline()) {
					return error;
				}

				Player player = event.getPlayer();

				try {
					return "" + GlobalChestShop.plugin.getAuctionController(GlobalChestShop.plugin.getworldGroup(player.getLocation())).getAllBoughtAuctionFromPlayer(player.getUniqueId()).size();
				} catch (WorldHasNoWorldGroupException e) {
					return "" + 0;
				}
			}

		});
		
		
		PlaceholderAPI.registerPlaceholder(GlobalChestShop.plugin, "gcsCanceledAuctionCount", new PlaceholderReplacer() {

			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				if (!event.isOnline()) {
					return error;
				}

				Player player = event.getPlayer();

				try {
					return "" + GlobalChestShop.plugin.getAuctionController(GlobalChestShop.plugin.getworldGroup(player.getLocation())).getAllCanceledAuctions(player.getUniqueId()).size();
				} catch (WorldHasNoWorldGroupException e) {
					return "" + 0;
				}
			}

		});
	}
}
