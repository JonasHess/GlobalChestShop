package eu.blockup.GlobalChestShop.Util.SoftDependecies;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class HologramHolder {

	public Hologram	hologram;
	public Shop		shop;

	public void spawnFloatingEntiy(final Location signLocation, final Shop shop) {
		GlobalChestShop.plugin.getServer().getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new Runnable() {
			@Override
			public void run() {
				hologram = HologramsAPI.createHologram(GlobalChestShop.plugin, new Location(signLocation.getWorld(), 0.5 + signLocation.getBlockX(), signLocation.getBlockY() + 1.7, 0.5 + signLocation.getBlockZ()));
				hologram.insertItemLine(0, shop.getShopEntityIcon()).setTouchHandler(new TouchHandler() {
					@Override
					public void onTouch(Player player) {
						if (player == null) {
							return;
						}
						if (player.isSneaking()) {
							shop.onInteractLeftClick(player, null);
						} else {

							shop.onInteractRightClick(player, null);
						}
					}
				});
			}
		});
	}

	public void spawnHoloShop(final Location loc, final Shop shop) {
		GlobalChestShop.plugin.getServer().getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new Runnable() {
			@Override
			public void run() {
				hologram = HologramsAPI.createHologram(GlobalChestShop.plugin, new Location(loc.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ()));
				hologram.appendTextLine(shop.getFirstSignLine(true));
				hologram.appendTextLine(shop.getSecondSignLine(true));
				hologram.insertItemLine(0, shop.getShopEntityIcon()).setTouchHandler(new TouchHandler() {

					@Override
					public void onTouch(Player player) {
						if (player == null) {
							return;
						}
						if (player.isSneaking()) {
							shop.onInteractLeftClick(player, null);
						} else {
							if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
								player.sendMessage(ChatColor.AQUA + "[GlobalChestShop] To remove a HoloShop, click while sneaking.");
							}
							shop.onInteractRightClick(player, null);
						}
					}
				});
			}
		});
	}

	public void despawn() {
		if (this.hologram != null) {
			this.hologram.delete();
		}
	}

}
