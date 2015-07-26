package eu.blockup.GlobalChestShop.Util.Metrics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;

import eu.blockup.GlobalChestShop.GlobalChestShop;

public class CollectStatistics {

	private static int	maxPlayerCount	= 0;

	public static void startCollectionsTask() {
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(GlobalChestShop.plugin, new Runnable() {
			@Override
			public void run() {
				
				GlobalChestShop.plugin.executeTaskAsynchronous(new Runnable() {
					
					@Override
					public void run() {
						boolean DEBUG = false;
						try {
							int onlinePlayers = Bukkit.getOnlinePlayers().size();
							if (onlinePlayers > maxPlayerCount) {
								maxPlayerCount = onlinePlayers;
							}
							URL url = new URL("http://blockup.eu/GCS_STATS/index.php?players=" + maxPlayerCount);
							BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
							String strTemp = "";
							while (null != (strTemp = br.readLine())) {

								if (DEBUG) {
									System.out.println(strTemp);
								}
							}
						} catch (Exception ex) {
							if (DEBUG) {
								ex.printStackTrace();
							}
						}
					}
				});
			}
		}, 1, 20 * 60 * 20);

	}
}
