package eu.blockup.GlobalChestShop.Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.blockup.GlobalChestShop.GlobalChestShop;

public class WorldGroupController {
  
  private Map<String, Integer> hashMap_WorldGroup;
  
  public WorldGroupController (){
    this.hashMap_WorldGroup = java.util.Collections.synchronizedMap(new HashMap<String, Integer>());
    this.readFile();
  }

  private synchronized void readFile() {
    File file = new File(GlobalChestShop.plugin.getDataFolder(),  "worldGroups.yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    
    for (String floorID : cfg.getConfigurationSection("WorldGroup").getKeys(false)) {
      List<String> worldList = cfg.getStringList("WorldGroup" + "." + floorID);
      for (String world : worldList) {
        this.hashMap_WorldGroup.put(world, Integer.parseInt(floorID));
      }
    }
}
  
  public synchronized Integer getworldGroup(String worldName) {
	  if (GlobalChestShop.plugin.mainConfig.disableWorldGroups) {
		  return 1;
	  }
    if (worldName == null) return null;
    return this.hashMap_WorldGroup.get(worldName);
    
  }
  
}
