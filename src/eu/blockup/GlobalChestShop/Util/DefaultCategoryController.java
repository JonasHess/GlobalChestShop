package eu.blockup.GlobalChestShop.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.RecipeTypeNotFound;

public class DefaultCategoryController {
	private Map<Integer, DefaultCategory>	hashMap_Categories;
	private List<ItemStack>					allItemList;
	private int								worldGroup;

	public DefaultCategoryController(int worldGroup) {
		this.worldGroup = worldGroup;
		this.hashMap_Categories = java.util.Collections.synchronizedMap(new HashMap<Integer, DefaultCategory>());
		this.allItemList = java.util.Collections.synchronizedList(new LinkedList<ItemStack>());

		this.hashMap_Categories.put(1, new DefaultCategory(new ItemStack(Material.BRICK), GlobalChestShop.text.get(GlobalChestShop.text.Category1), false));
		this.hashMap_Categories.put(2, new DefaultCategory(new ItemStack(Material.DOUBLE_PLANT, 1, (short) 5), GlobalChestShop.text.get(GlobalChestShop.text.Category2), false));
		this.hashMap_Categories.put(3, new DefaultCategory(new ItemStack(Material.REDSTONE), GlobalChestShop.text.get(GlobalChestShop.text.Category3), false));
		this.hashMap_Categories.put(4, new DefaultCategory(new ItemStack(Material.RAILS), GlobalChestShop.text.get(GlobalChestShop.text.Category4), false));
		this.hashMap_Categories.put(5, new DefaultCategory(new ItemStack(Material.LAVA_BUCKET), GlobalChestShop.text.get(GlobalChestShop.text.Category5), false));
		this.hashMap_Categories.put(6, new DefaultCategory(new ItemStack(Material.APPLE), GlobalChestShop.text.get(GlobalChestShop.text.Category6), false));
		this.hashMap_Categories.put(7, new DefaultCategory(new ItemStack(Material.IRON_AXE), GlobalChestShop.text.get(GlobalChestShop.text.Category7), false));
		this.hashMap_Categories.put(8, new DefaultCategory(new ItemStack(Material.GOLD_SWORD), GlobalChestShop.text.get(GlobalChestShop.text.Category8), false));
		this.hashMap_Categories.put(9, new DefaultCategory(new ItemStack(Material.POTION), GlobalChestShop.text.get(GlobalChestShop.text.Category9), false));
		this.hashMap_Categories.put(10, new DefaultCategory(new ItemStack(Material.STICK), GlobalChestShop.text.get(GlobalChestShop.text.Category10), false));
		this.hashMap_Categories.put(-1, new DefaultCategory(new ItemStack(Material.COAL), "DEBUG", true));
		this.hashMap_Categories.put(-2, new DefaultCategory(new ItemStack(Material.COAL), "HIDDEN", true));
		this.generateAllItemList();
		fillCustomCategories();
	}

	public synchronized List<DefaultCategory> getAllCategories() {
		List<DefaultCategory> resultList = new ArrayList<DefaultCategory>(11);
		for (Entry<Integer, DefaultCategory> entry : this.hashMap_Categories.entrySet()) {
			if (entry.getValue() == this.hashMap_Categories.get(-1))
				continue;
			if (entry.getValue() == this.hashMap_Categories.get(-2))
				continue;
			resultList.add(entry.getValue());
		}
		return resultList;
	}

	private void fillCustomCategories() {
		for (ItemStack item : this.allItemList) {
			this.getResponsibleCategory(item).addItem(item);
		}
	}

	public void addItemToProtfolio(ItemStack item) {
		ItemStack clonedItem = item.clone();
		clonedItem.setAmount(1);
		this.addItem(clonedItem);
		this.getResponsibleCategory(clonedItem).addItem(clonedItem);
	}

	private void addItem(ItemStack item) {
		if (item != null && item.getType() != Material.AIR) {
			if (item.getData().getItemType().name().matches("SULPHUR") && item.getDurability() == 5)
				return;
			// if (item.getDurability() == 32767 || item.getDurability() == -1)
			// return;
			if (item.getDurability() == 32767)
				return;
			ItemStack clonedItem = item.clone();
			clonedItem.setAmount(1);
			if (!this.allItemList.contains(clonedItem)) {
				this.allItemList.add(clonedItem);
			}
		}
	}

	private void addItem(EvaluatedRecipe recipe) {
		this.addItem(recipe.getResultedItem());
		for (ItemStack i : recipe.getImputItemList()) {
			this.addItem(i);
		}
	}

	private void addItem(Recipe recipe) throws RecipeTypeNotFound {
		this.addItem(this.evaluateRecipe(recipe));
	}

	private void generateAllItemList() {
		List<Integer> auctionItemList = GlobalChestShop.plugin.getAuctionController(worldGroup).getAllActiveItems();
		for (int i : auctionItemList) {
			this.addItem(GlobalChestShop.plugin.itemController.formatInternalItemIdToItemStack(i));
		}
		Iterator<Recipe> iterator = GlobalChestShop.plugin.getServer().recipeIterator();
		while (iterator.hasNext()) {
			try {
				this.addItem(iterator.next());
			} catch (RecipeTypeNotFound e) {
			}
		}
		for (Material material : Material.values()) {
			this.addItem(new ItemStack(material));
		}
	}

	private EvaluatedRecipe evaluateRecipe(Recipe recipe) throws RecipeTypeNotFound {
		EvaluatedRecipe tempEvaluatedRecepie;

		// ShapedRecipe
		if (recipe instanceof ShapedRecipe) {
			tempEvaluatedRecepie = (new EvaluatedRecipe(recipe.getResult()));
			Map<Character, ItemStack> itemMap = ((ShapedRecipe) recipe).getIngredientMap();
			for (Character key : itemMap.keySet()) {
				if (itemMap.get(key) != null) {
					tempEvaluatedRecepie.addImputItem(itemMap.get(key));
				}
			}
			itemMap.clear();
			return tempEvaluatedRecepie;
		}

		// FurnaceRecipe
		if (recipe instanceof FurnaceRecipe) {
			tempEvaluatedRecepie = (new EvaluatedRecipe(recipe.getResult()));
			tempEvaluatedRecepie.addImputItem(((FurnaceRecipe) recipe).getInput());
			return tempEvaluatedRecepie;
		}

		// ShapelessRecipe
		if (recipe instanceof ShapelessRecipe) {
			tempEvaluatedRecepie = (new EvaluatedRecipe(recipe.getResult()));
			List<ItemStack> itemlist = ((ShapelessRecipe) recipe).getIngredientList();
			for (ItemStack listelement : itemlist) {
				if (listelement.clone() != null) {
					tempEvaluatedRecepie.addImputItem(listelement.clone());
				}
			}
			return tempEvaluatedRecepie;
		}
		throw new RecipeTypeNotFound();
	}

	class EvaluatedRecipe {
		private ItemStack		resultedItem;
		private List<ItemStack>	imputItemList;

		public EvaluatedRecipe(ItemStack resultedItem) {
			this.imputItemList = new ArrayList<ItemStack>();
			this.resultedItem = resultedItem;
		}

		public void addImputItem(ItemStack itemStack) {
			for (ItemStack listElement : this.imputItemList) {
				if (listElement.getData().equals(itemStack.getData())) {
					listElement.setAmount((listElement.getAmount()) + (itemStack.getAmount()));
					return;
				}
			}
			this.imputItemList.add(itemStack);
		}

		public List<ItemStack> getImputItemList() {
			return imputItemList;
		}

		public ItemStack getResultedItem() {
			return resultedItem;
		}
	}

	public synchronized List<ItemStack> getAllItemsFromAllCategoriesFilteresByString(String s) {
		final String searched = s.toLowerCase();
		List<ItemStack> resultList = new ArrayList<ItemStack>();
		for (int i = 1; i <= 10; i++) {
			DefaultCategory c = this.hashMap_Categories.get(i);
			for (ItemStack item : c.getItemList(false, worldGroup)) {
				String itemName = GlobalChestShop.plugin.getItemStackDisplayName(item).toLowerCase();
				if (itemName.contains(searched) || Levenshtein.similarity(itemName, searched) > 0.8) {
					resultList.add(item);
				}
			}
		}
		if (resultList.size() == 0 && GlobalChestShop.plugin.getMainConfig().whenSearchFindsNoItemsShowAllItems) {
			for (int i = 1; i <= 10; i++) {
				DefaultCategory c = this.hashMap_Categories.get(i);
				for (ItemStack item : c.getItemList(false, worldGroup)) {
					resultList.add(item);
				}
			}
		}
		Collections.sort(resultList, new Comparator<ItemStack>() {

			@Override
			public int compare(ItemStack arg0, ItemStack arg1) {
				String sarg0 = GlobalChestShop.plugin.getItemStackDisplayName(arg0).toLowerCase();

				String sarg1 = GlobalChestShop.plugin.getItemStackDisplayName(arg1).toLowerCase();
				if (sarg0.equalsIgnoreCase(searched)) {
					return -1;
				}
				if (sarg0.contains(searched) && !sarg1.contains(searched)) {
					return -1;
				}

				double sim0 = Levenshtein.similarity(sarg0, searched);
				double sim1 = Levenshtein.similarity(sarg1, searched);

				if (sim0 > sim1) {
					return -1;
				}
				if (sim0 == sim1) {
					return 0;
				}
				return 1;
			}
		});
		return resultList;
	}

	public DefaultCategory getDebugCategory() {
		return this.hashMap_Categories.get(-1);
	}

	public DefaultCategory getHiddenItemsCategory() {
		return this.hashMap_Categories.get(-2);
	}

	public DefaultCategory getResponsibleCategory(ItemStack item) {
		String itemText = item.getData().getItemType().name();
		int categoryID = 1;

		if (GlobalChestShop.plugin.getItemController().isItemBannedFromShops(item, worldGroup, false)) {
			return getHiddenItemsCategory();
		}

		// blabla get item controller is item disabled? return -2;

		// // 1

		// if (itemText.contains("ORE"))
		// categoryID = 1;
		// if (itemText.contains("BLOCK"))
		// categoryID = 1;
		// if (itemText.contains("STAIRS"))
		// categoryID = 1;
		// if (itemText.contains("WALL"))
		// categoryID = 1;
		// if (itemText.contains("CLAY"))
		// categoryID = 1;
		// if (itemText.contains("SLAP"))
		// categoryID = 1;
		// if (itemText.contains("SANDSTONE"))
		// categoryID = 1;
		// if (itemText.contains("STONE"))
		// categoryID = 1;
		// if (itemText.contains("GRAVEL"))
		// categoryID = 1;
		// if (itemText.contains("DIORITE"))
		// categoryID = 1;
		// if (itemText.contains("GLASS"))
		// categoryID = 1;
		// if (itemText.contains("PUMKIN"))
		// categoryID = 1;
		// if (itemText.contains("SNOW"))
		// categoryID = 1;
		// if (itemText.contains("BOOKSHELF"))
		// categoryID = 1;
		// if (itemText.contains("BRICKS"))
		// categoryID = 1;
		// if (itemText.contains("SAND"))
		// categoryID = 1;
		// if (itemText.contains("NETHER"))
		// categoryID = 1;
		// if (itemText.contains("MELON"))
		// categoryID = 1;
		// if (itemText.contains("WOOD"))
		// categoryID = 1;
		// if (itemText.contains("PRISMARINE"))
		// categoryID = 1;
		// if (itemText.contains("ICE"))
		// categoryID = 1;
		// if (itemText.contains("BEDROCK"))
		// categoryID = 1;
		// if (itemText.contains("SPONGE"))
		// categoryID = 1;
		// if (itemText.contains("WOOL"))
		// categoryID = 1;
		// if (itemText.contains("OBSIDIAN"))
		// categoryID = 1;
		// if (itemText.contains("GLOWSTONE"))
		// categoryID = 1;
		// if (itemText.contains("SNOW"))
		// categoryID = 1;
		// if (itemText.contains("MYCELIUM"))
		// categoryID = 1;
		// if (itemText.contains("QUARTZ"))
		// categoryID = 1;
		// if (itemText.contains("PRISMARINE"))
		// categoryID = 1;
		// if (itemText.contains("HAY"))
		// categoryID = 1;
		// if (itemText.contains("SANDSTONE"))
		// categoryID = 1;
		// if (itemText.contains("LOG"))
		// categoryID = 1;
		// if (itemText.contains("DIRT"))
		// categoryID = 1;
		// if (itemText.contains("STEP"))
		// categoryID = 1;
		// if (itemText.contains("BRICK"))
		// categoryID = 1;
		// if (itemText.contains("LANTERN"))
		// categoryID = 1;
		// if (itemText.contains("SANDSTONE"))
		// categoryID = 1;
		// if (itemText.contains("SANDSTONE"))
		// categoryID = 1;
		// if (itemText.contains("SANDSTONE"))
		// categoryID = 1;
		// if (itemText.contains("SANDSTONE"))
		// categoryID = 1;

		// 2
		if (itemText.contains("SAPLING"))
			categoryID = 2;
		if (itemText.contains("LADDER"))
			categoryID = 2;
		if (itemText.contains("ANVIL"))
			categoryID = 2;
		if (itemText.contains("WORK"))
			categoryID = 2;
		if (itemText.contains("FURNANCE"))
			categoryID = 2;
		if (itemText.contains("THIN_GLASS"))
			categoryID = 2;
		if (itemText.contains("LEAVES"))
			categoryID = 2;
		if (itemText.contains("CUBWEB"))
			categoryID = 2;
		if (itemText.contains("GRASS"))
			categoryID = 2;
		if (itemText.contains("FERN"))
			categoryID = 2;
		if (itemText.contains("BUSH"))
			categoryID = 2;
		if (itemText.contains("TORCH"))
			categoryID = 2;
		if (itemText.contains("CHEST"))
			categoryID = 2;
		if (itemText.contains("TABLE"))
			categoryID = 2;
		if (itemText.contains("FURNANCE"))
			categoryID = 2;
		if (itemText.contains("SNOW"))
			categoryID = 2;
		if (itemText.contains("CACTUS"))
			categoryID = 2;
		if (itemText.contains("JUKEBOX"))
			categoryID = 2;
		if (itemText.contains("FENCE"))
			categoryID = 2;
		if (itemText.contains("MONSTER EGG"))
			categoryID = 2;
		if (itemText.contains("BARS"))
			categoryID = 2;
		if (itemText.contains("PANE"))
			categoryID = 2;
		if (itemText.contains("VINES"))
			categoryID = 2;
		if (itemText.contains("LILY"))
			categoryID = 2;
		if (itemText.contains("SLIME"))
			categoryID = 2;
		if (itemText.contains("CARPET"))
			categoryID = 2;
		if (itemText.contains("PAINTING"))
			categoryID = 2;
		if (itemText.contains("SIGN"))
			categoryID = 2;
		if (itemText.contains("BED"))
			categoryID = 2;
		if (itemText.contains("FRAME"))
			categoryID = 2;
		if (itemText.contains("SKULL"))
			categoryID = 2;
		if (itemText.contains("HEAD"))
			categoryID = 2;
		if (itemText.contains("STAND"))
			categoryID = 2;
		if (itemText.contains("BANNER"))
			categoryID = 2;
		if (itemText.contains("ROSE"))
			categoryID = 2;
		if (itemText.contains("MUSHROOM"))
			categoryID = 2;
		if (itemText.contains("VINE"))
			categoryID = 2;
		if (itemText.contains("PUMKIN"))
			categoryID = 2;
		if (itemText.contains("JACK_O"))
			categoryID = 2;
		if (itemText.contains("BEACON"))
			categoryID = 2;
		if (itemText.contains("PLANT"))
			categoryID = 2;
		if (itemText.contains("FLOWER"))
			categoryID = 2;
		if (itemText.contains("BANNER"))
			categoryID = 2;
		if (itemText.matches("BEDROCK"))
			categoryID = 1;
		if (itemText.matches("GRASS"))
			categoryID = 1;

		// 3
		if (itemText.contains("DISPENSER"))
			categoryID = 3;
		if (itemText.contains("NOTE_BLOCK"))
			categoryID = 3;
		if (itemText.contains("PISTON"))
			categoryID = 3;
		if (itemText.contains("TNT"))
			categoryID = 3;
		if (itemText.contains("LEVER"))
			categoryID = 3;
		if (itemText.contains("REDSTONE_TORCH"))
			categoryID = 3;
		if (itemText.contains("DOOR"))
			categoryID = 3;
		if (itemText.contains("GATE"))
			categoryID = 3;
		if (itemText.contains("LAMP"))
			categoryID = 3;
		if (itemText.contains("HOOK"))
			categoryID = 3;
		if (itemText.contains("BUTTON"))
			categoryID = 3;
		if (itemText.contains("_PLATE"))
			categoryID = 3;
		if (itemText.contains("DETECTOR"))
			categoryID = 3;
		if (itemText.contains("REDSTONE_BLOCK"))
			categoryID = 3;
		if (itemText.contains("HOPPER"))
			categoryID = 3;
		if (itemText.contains("DROPPER"))
			categoryID = 3;
		if (itemText.contains("TRAPDOOR"))
			categoryID = 3;
		if (itemText.matches("REDSTONE"))
			categoryID = 3;
		if (itemText.contains("DIODE"))
			categoryID = 3;
		if (itemText.contains("COMPARATOR"))
			categoryID = 3;

		// 4
		if (itemText.contains("RAIL"))
			categoryID = 4;
		if (itemText.contains("MINECART"))
			categoryID = 4;
		if (itemText.contains("SADDLE"))
			categoryID = 4;
		if (itemText.contains("BOAT"))
			categoryID = 4;
		if (itemText.contains("CARROT_STICK"))
			categoryID = 4;

		// 5
		if (itemText.contains("BEACON"))
			categoryID = 5;
		if (itemText.contains("BUCKET"))
			categoryID = 5;
		if (itemText.contains("BALL"))
			categoryID = 5;
		if (itemText.contains("PAPER"))
			categoryID = 5;
		if (itemText.contains("BOOK"))
			categoryID = 5;
		if (itemText.contains("BONE"))
			categoryID = 5;
		if (itemText.contains("PEARL"))
			categoryID = 5;
		if (itemText.contains("EGG"))
			categoryID = 5;
		if (itemText.contains("EXP_BOTTLE"))
			categoryID = 5;
		if (itemText.contains("FIREBALL"))
			categoryID = 5;
		if (itemText.contains("MAP"))
			categoryID = 5;
		if (itemText.contains("FIREWORK"))
			categoryID = 5;
		if (itemText.contains("HORSE"))
			categoryID = 5;
		if (itemText.contains("RECORD"))
			categoryID = 5;

		// 6

		if (itemText.contains("APPLE"))
			categoryID = 6;
		if (itemText.contains("SOUP"))
			categoryID = 6;
		if (itemText.contains("BREAD"))
			categoryID = 6;
		if (itemText.contains("PORK"))
			categoryID = 6;
		if (itemText.contains("FISH"))
			categoryID = 6;
		if (itemText.contains("CAKE"))
			categoryID = 6;
		if (itemText.contains("COOKIE"))
			categoryID = 6;
		if (itemText.contains("MELON"))
			categoryID = 6;
		if (itemText.contains("BEEF"))
			categoryID = 6;
		if (itemText.contains("RAW_"))
			categoryID = 6;
		if (itemText.contains("COOKED_"))
			categoryID = 6;
		if (itemText.contains("EYE"))
			categoryID = 6;
		if (itemText.contains("CARROT_ITEM"))
			categoryID = 6;
		if (itemText.contains("POTATO"))
			categoryID = 6;
		if (itemText.contains("_PIE"))
			categoryID = 6;
		if (itemText.contains("RABBIT"))
			categoryID = 6;
		if (itemText.contains("MUTTON"))
			categoryID = 6;

		// 7
		if (itemText.contains("SPADE"))
			categoryID = 7;
		if (itemText.contains("PICKAXE"))
			categoryID = 7;
		if (itemText.contains("AXE"))
			categoryID = 7;
		if (itemText.contains("HOE"))
			categoryID = 7;
		if (itemText.contains("FLINT_AND"))
			categoryID = 7;
		if (itemText.contains("COMPASS"))
			categoryID = 7;
		if (itemText.contains("_ROD"))
			categoryID = 7;
		if (itemText.contains("WATCH"))
			categoryID = 7;
		if (itemText.contains("SHEARS"))
			categoryID = 7;
		if (itemText.contains("LEASH"))
			categoryID = 7;
		if (itemText.contains("NAME_TAG"))
			categoryID = 7;
		if (itemText.contains("ENCHANTED_BOOK"))
			categoryID = 7;

		// 8
		if (itemText.contains("BOW"))
			categoryID = 8;
		if (itemText.contains("ARROW"))
			categoryID = 8;
		if (itemText.contains("SWORD"))
			categoryID = 8;
		if (itemText.contains("HELMET"))
			categoryID = 8;
		if (itemText.contains("CHESTPLATE"))
			categoryID = 8;
		if (itemText.contains("LEGGINGS"))
			categoryID = 8;
		if (itemText.contains("BOOTS"))
			categoryID = 8;
		if (itemText.contains("ENCHANTED_BOOK"))
			categoryID = 8;
		if (itemText.contains("_BARDING"))
			categoryID = 8;

		// 9
		if (itemText.contains("TEAR"))
			categoryID = 9;
		if (itemText.contains("POTION"))
			categoryID = 9;
		if (itemText.contains("GLASS_BOTTLE"))
			categoryID = 9;
		if (itemText.contains("SPIDER_EYE"))
			categoryID = 9;
		if (itemText.contains("BLAZE_POWDER"))
			categoryID = 9;
		if (itemText.contains("MAGMA_CREAM"))
			categoryID = 9;
		if (itemText.contains("BREWING_STAND"))
			categoryID = 9;
		if (itemText.contains("CAULDRON_ITEM"))
			categoryID = 9;
		if (itemText.contains("SPECKLED_MELON"))
			categoryID = 9;
		if (itemText.contains("GOLDEN_CARROT"))
			categoryID = 9;
		if (itemText.contains("RABBIT_FOOT"))
			categoryID = 9;

		// 10
		if (itemText.matches("COAL"))
			categoryID = 10;
		if (itemText.matches("DIAMOND"))
			categoryID = 10;
		if (itemText.contains("INGOT"))
			categoryID = 10;
		if (itemText.matches("STICK"))
			categoryID = 10;
		if (itemText.matches("BOWL"))
			categoryID = 10;
		if (itemText.contains("STRING"))
			categoryID = 10;
		if (itemText.contains("FEATHER"))
			categoryID = 10;
		if (itemText.contains("SULPHUR"))
			categoryID = 10;
		if (itemText.contains("SEEDS"))
			categoryID = 10;
		if (itemText.contains("WHEAT"))
			categoryID = 10;
		if (itemText.matches("FLINT"))
			categoryID = 10;
		if (itemText.matches("LEATHER"))
			categoryID = 10;
		if (itemText.matches("CLAY_BRICK"))
			categoryID = 10;
		if (itemText.contains("CLAY_BALL"))
			categoryID = 10;
		if (itemText.matches("EGG"))
			categoryID = 10;
		if (itemText.contains("DUST"))
			categoryID = 10;
		if (itemText.contains("INK"))
			categoryID = 10;
		if (itemText.contains("SUGAR"))
			categoryID = 10;
		if (itemText.contains("BLAZE_ROD"))
			categoryID = 10;
		if (itemText.contains("NUGGET"))
			categoryID = 10;
		if (itemText.contains("_STALK"))
			categoryID = 10;
		if (itemText.matches("EMERALD"))
			categoryID = 10;
		if (itemText.contains("_STAR"))
			categoryID = 10;
		if (itemText.contains("BRICK_ITEM"))
			categoryID = 10;
		if (itemText.matches("QUARTZ"))
			categoryID = 10;
		if (itemText.contains("PRISMARINE_SHARD"))
			categoryID = 10;
		if (itemText.contains("PRISMARINE_CRY"))
			categoryID = 10;
		if (itemText.contains("RABBIT_HIDE"))
			categoryID = 10;
		if (itemText.contains("ROTTEN_FLESH"))
			categoryID = 10;

		// -1

		if (itemText.contains("AIR"))
			categoryID = -1;
		if (itemText.matches("WATER"))
			categoryID = -1;
		if (itemText.matches("TRIPWIRE"))
			categoryID = -1;
		if (itemText.contains("STATIONARY"))
			categoryID = -1;
		if (itemText.matches("DOUBLE_STONE_SLAB2"))
			categoryID = -1;
		if (itemText.matches("WOOD_DOUBLE_STEP"))
			categoryID = -1;
		if (itemText.matches("LAVA"))
			categoryID = -1;
		if (itemText.matches("DOUBLE_STEP"))
			categoryID = -1;
		if (itemText.matches("FIRE"))
			categoryID = -1;
		if (itemText.matches("REDSTONE_WIRE"))
			categoryID = -1;
		if (itemText.matches("GLOWING_REDSTONE_ORE"))
			categoryID = -1;
		if (itemText.matches("CROPS"))
			categoryID = -1;
		if (itemText.matches("PUMPKIN_STEM"))
			categoryID = -1;
		if (itemText.matches("NETHER_WARTS"))
			categoryID = -1;
		if (itemText.matches("CAULDRON"))
			categoryID = -1;
		if (itemText.matches("PRIPWIRE"))
			categoryID = -1;
		if (itemText.matches("COCOA"))
			categoryID = -1;
		if (itemText.matches("CARROT"))
			categoryID = -1;
		if (itemText.matches("SKULL"))
			categoryID = -1;
		if (itemText.matches("SOIL"))
			categoryID = -1;
		if (itemText.matches("BURNING_FURNACE"))
			categoryID = -1;
		if (itemText.matches("PORTAL"))
			categoryID = -1;
		if (itemText.matches("BREWING_STAND"))
			categoryID = -1;
		if (itemText.matches("SUGAR_CANE_BLOCK"))
			categoryID = -1;
		if (itemText.matches("PISTON_MOVING_PIECE"))
			categoryID = -1;
		if (itemText.matches("CAKE_BLOCK"))
			categoryID = -1;
		if (itemText.matches("MELON_STEM"))
			categoryID = -1;
		if (itemText.matches("POTATO"))
			categoryID = -1;
		if (itemText.matches("ENDER_PORTAL"))
			categoryID = -1;
		if (itemText.matches("BED_BLOCK"))
			categoryID = -1;
		if (itemText.matches("SIGN_POST"))
			categoryID = -1;
		if (itemText.matches("WALL_SIGN"))
			categoryID = -1;
		if (itemText.matches("WALL_BANNER"))
			categoryID = -1;
		if (itemText.matches("STANDING_BANNER"))
			categoryID = -1;
		if (itemText.matches("FLOWER_POT"))
			categoryID = -1;
		if (itemText.matches("REDSTONE_EXTENSION"))
			categoryID = -1;
		if (itemText.matches("PISTON_EXTENSION"))
			categoryID = -1;
		if (itemText.matches("WOODEN_DOOR"))
			categoryID = -1;
		if (itemText.contains("DOOR_BLOCK"))
			categoryID = -1;
		if (itemText.endsWith("_OFF"))
			categoryID = -1;
		if (itemText.endsWith("_ON"))
			categoryID = -1;
		if (itemText.endsWith("_INVERTED"))
			categoryID = -1;
		if (itemText.matches("SPRUCE_DOOR"))
			categoryID = -1;
		if (itemText.matches("BIRCH_DOOR"))
			categoryID = -1;
		if (itemText.matches("JUNGLE_DOOR"))
			categoryID = -1;
		if (itemText.matches("ACACIA_DOOR"))
			categoryID = -1;
		if (itemText.matches("DARK_OAK_DOOR"))
			categoryID = -1;
		if (itemText.endsWith("STAIRS"))
			categoryID = 1;
		if (itemText.matches("SULPHUR") && item.getDurability() == 5) {
			categoryID = -1;
		}
		// if (itemText.contains("DOUBLE_STEP")) categoryID = -1;
		return this.hashMap_Categories.get(categoryID);

	}

}
