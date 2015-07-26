package eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation.DefaultPrices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.DefaultCategory;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation.ItemAmountRelation;

public class PriceWriter {
	public PriceWriter () {
		try {
			this.readFile();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace(); // TODO
		}
	}

	
	private void readFile() throws FileNotFoundException, IOException, InvalidConfigurationException {
		File file = new File("plugins/GlobalChestShop/", "defaultPrices.yml");
		FileConfiguration cfg = new YamlConfiguration();
		cfg.load(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));

		Map<ItemStack, Integer> map = new HashMap<ItemStack, Integer>();
		// ---------------------------------------
		List<DefaultCategory> list = GlobalChestShop.plugin.getDefaultCategoryController(1).getAllCategories();
	    for (int i = 0; i <= 9; i++) {
	    	for (ItemStack item : list.get(i).getItemList(false, 1)) {
	    		for (ItemAmountRelation ing : GlobalChestShop.plugin.getPriceEngine().getLeaveListOfItem(item)) {
	    			map.put(ing.itemStack, 1);
	    		}
	    	}
	    }
	    int t = 0;
	    for (ItemStack key : map.keySet()) {
    		t++;
    		cfg.set("" + t + ".ItemName", GlobalChestShop.plugin.getItemStackDisplayName(key));
    		cfg.set("" + t + ".Item", key);
    		cfg.set("" + t + ".Price" , 100.0);
	    }


		// Speichere Defaults
		cfg.options().copyDefaults(true);
		Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
		fileWriter.write(cfg.saveToString());
		fileWriter.close();

		// ---------------------------------------
		// Reading

		// MySQL

	}
}
