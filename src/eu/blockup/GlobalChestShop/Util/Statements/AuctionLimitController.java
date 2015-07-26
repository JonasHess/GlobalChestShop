package eu.blockup.GlobalChestShop.Util.Statements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.blockup.GlobalChestShop.GlobalChestShop;

public class AuctionLimitController {
	
	class EffectifeMaxChest {
		private int maxAuctions;
		private List<VipGroup>list;
		public EffectifeMaxChest(int maxAuctions) {
			super();
			this.maxAuctions = maxAuctions;
			
			this.list = java.util.Collections.synchronizedList(new ArrayList<VipGroup>());
		}
		public int getMaxAuctions() {
			return maxAuctions;
		}
		public List<VipGroup> getList() {
			return list;
		}
	}

	private Map<Integer, EffectifeMaxChest>maxAuctionCollection;


	public AuctionLimitController() {
		this.maxAuctionCollection = java.util.Collections.synchronizedMap(new HashMap<Integer, EffectifeMaxChest>());
		this.readFile();
	}

	private synchronized void readFile() {
		File file = new File(GlobalChestShop.plugin.getDataFolder(), "auctionLimits.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for (String worldGroup : cfg.getConfigurationSection("WorldGroup").getKeys(false)) {
			int maxAuctions = cfg.getInt("WorldGroup" + "." + worldGroup + "." + "MaximumAuctionsPerPlayerInThisWorldGroup");
			for (String groupName : cfg.getConfigurationSection("WorldGroup" + "." + worldGroup + "." + "VIP_Groups").getKeys(false)) {
				int exceedingLimit = cfg.getInt("WorldGroup" + "." + worldGroup + "." + "VIP_Groups" + "." + groupName + "." + "Players_in_this_VIP_group_can_exceed_maximum_by");
				EffectifeMaxChest e = maxAuctionCollection.get(Integer.valueOf(worldGroup));
				if (e == null) {
					e = new EffectifeMaxChest(maxAuctions);
					this.maxAuctionCollection.put(Integer.valueOf(worldGroup), e);
				}
				e.getList().add(new VipGroup(Integer.valueOf(worldGroup), groupName, exceedingLimit));
			}
		}
	}
	
	public int getMaxAmountOfRunningAuctions (Player player, Integer worldGroup) {
		
		try {
			int defaults = this.maxAuctionCollection.get(worldGroup).getMaxAuctions();
			int maxExceede = 0;
			for (VipGroup v : this.maxAuctionCollection.get(worldGroup).getList()) {
				if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.VIP_SELL_EXCEED_ADMOUNT_OF_AUCTION + "." + v.getPermissionGroup() + "." + worldGroup.toString())) {
					if (v.getExceedingLimit() > maxExceede) {
						maxExceede = v.getExceedingLimit();
					}
				}
			}
			return maxExceede + defaults;
			
			
		} catch (Exception e) {
			return 0;
		}
	}

}
