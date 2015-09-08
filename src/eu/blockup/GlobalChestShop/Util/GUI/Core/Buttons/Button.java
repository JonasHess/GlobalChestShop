package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects.ButtonEffect;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public abstract class Button {

	ItemStack				displayIcon	= null;
	private String			title		= "";
	private List<String>	description;
	private ButtonEffect	buttonEffect;
	

	/**
	 * @param displayItem
	 *            visual appearance of the Button
	 * @param title
	 *            | if you want to obtain the original title of the ItemStack,
	 *            pass an empty String ""
	 * @param description
	 *            (lore) use : String[] or arg1, arg2, arg3...
	 */
	public Button(ItemStack displayItem, String title, String... description) {
		if (displayItem == null) {
			this.setTitle("NULL - Error");
			this.setDisplayIcon(new ItemStack(Material.DIRT));
		} else {
			this.setTitle(title);
			this.setDisplayIcon(displayItem);
		}
		this.setDescription(description);
	}

	public Button(Button_Bare button) {
		this(button.getDisplayIcon(), button.getTitle(), button.getDescription());
	}

	public Button(Button button) {
		this(button.getDisplayIcon(), button.getTitle(), button.getDescription());
	}

	public Button(ItemStack itemStack) {
		this(itemStack, "");
	}

	public Button() {
		this(new ItemStack(Material.STONE_PICKAXE), "Not defined", "Use the Button's constructor to change this!");
	}


	
	public synchronized void onPreButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType clickType, InventoryClickEvent event) {
			Sound clickSound = this.getClickSound(clickType);
			if (clickSound != null) {
				player.getLocation().getWorld().playSound(player.getLocation(), this.getClickSound(clickType), 1, 1);
			}
			this.onButtonClick(inventoryGUI, player, cursor, current, clickType, event);

	}
	
	protected Sound getClickSound(ClickType type) {
		return Sound.WOOD_CLICK;
	}

	public abstract void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType clickType, InventoryClickEvent event);

	public abstract void onRefresh(InventoryGUI inventoryGUI, Player player);

	public Button setButtonEffect(ButtonEffect effect) {
		this.buttonEffect = effect;
		return this;
	}

	public boolean hasButtonEffect() {
		return this.buttonEffect != null;
	}

	public void removeButtonEffect() {
		this.buttonEffect = null;
	}

	/**
	 * Changes the total appearance (the way the Button gets presented to the
	 * player). The functional aspects of the Button stay untouched. If you want
	 * the changes to be visible, don't forget to refresh() the InventoryGUI.
	 * 
	 * @param Button_Bare
	 */
	public void setAppearance(Button bareButton) {
		this.setDisplayIcon(bareButton.getDisplayIcon());
		this.setTitle(bareButton.getTitle());
		this.setDescription(bareButton.getDescription());
	}

	public ItemStack getDisplayIcon() {
		return displayIcon;
	}

	public Button setDisplayIcon(ItemStack displayIcon) {
		this.displayIcon = displayIcon;
		return this;
	}

	public int getAmount() {
		return this.getDisplayIcon().getAmount();
	}

	public Button setAmount(int amount) {
		this.getDisplayIcon().setAmount(amount);
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public boolean hasTitle() {
		return this.getTitle().length() != 0;
	}

	public Button setTitle(String name) {
		if (name == null) {
			this.title = "null";
		} else {
			this.title = name;
		}
		return this;
	}

	public Button setDescription(String... desc) {
		this.description = null;
		for (String s : desc) {
			this.addDescriptionLine(s);
		}

		return this;
	}

	public void clearDesacription() {
		this.description = null;
	}

	public Button setDescription(List<String> desc) {
		if (this.description == null) {
			this.description = new ArrayList<String>(desc.size());
		}
		for (String s : desc) {
			List<String> list = this.insertLineCreaks(s);
			for (String line : list) {
				this.description.add(line);
			}
		}
		return this;
	}

	public Button addDescriptionLine(String line) {
		List<String> list = this.insertLineCreaks(line);
		if (this.description == null) {
			this.description = list;
		} else {
			for (String s : list) {
				this.description.add(s);
			}
		}
		return this;
	}
	
	public Button addDescriptionLine(List<String> lineList) {
		for (String s: lineList) {
			this.addDescriptionLine(s);
		}
		return this;
	}

	private List<String> insertLineCreaks(String originalString) {
		int breakAt = 30;
		List<String> resultList = new ArrayList<String>();
		StringBuilder builder = new StringBuilder(originalString);
		StringBuilder tmpBuilder = new StringBuilder();
		String color;
		int t = 0;
		for (int i = 0; i < builder.length(); i++) {
			t++;
			if (t > breakAt && builder.charAt(i) == ' ' || t - breakAt > 15) {
				t = 0;
				color = ChatColor.getLastColors(tmpBuilder.toString());
				resultList.add(tmpBuilder.toString());
				tmpBuilder.setLength(0);
				tmpBuilder.append(color);
			} else {
				tmpBuilder.append(builder.charAt(i));
			}
		}
		resultList.add(tmpBuilder.toString());
		tmpBuilder.setLength(0);
		tmpBuilder = null;
		return resultList;
	}

	public String[] getDescription() {
		if (this.description == null)
			return new String[0];
		return this.description.toArray(new String[this.description.size()]);
	}

	private boolean hasDescription() {
		return this.getDescription() != null;
	}

	public ItemStack toItemStack(int tickCount) {
		if (this.hasButtonEffect()) {
			this.setAppearance(this.buttonEffect.getAnimatedButton(tickCount, this));
		}

		ItemStack item = this.getDisplayIcon().clone();
//		if (this.hasTitle() || this.hasDescription()) {
			ItemMeta meta;
			if (item.hasItemMeta()) {
				meta = item.getItemMeta();
			} else {
				meta = Bukkit.getItemFactory().getItemMeta(item.getType());
			}
			if (meta != null) {
				
				boolean titleToLong = false;
				List<String> titleList = this.insertLineCreaks(title);
				if (this.hasTitle()) {
					meta.setDisplayName(titleList.get(0));
					if (titleList.size() > 1) {
						titleToLong = true;
					}
				}
				List<String> lore = new ArrayList<String>();
				if (meta.hasLore()) {
					for (String loreLine : meta.getLore()) {
						lore.add(loreLine);
					}
				}
				if (titleToLong) {
					for (String s : titleList) {
						if (!titleToLong) {
							lore.add(s);
						}
						titleToLong = false;
					}
				}
				if (this.hasDescription()) {
					for (String line : this.getDescription()) {
						lore.add(line);
					}
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
//		}
		return item;
	}

	public enum ClickType {

		RIGHT(true), LEFT(false), SHIFT_RIGHT(true, true), SHIFT_LEFT(false, true), UNKNOWN(false, false);

		private boolean	isLeftClick;
		private boolean	isShiftClick	= false;

		ClickType(boolean isLeftClick) {
			this.isLeftClick = isLeftClick;
		}

		ClickType(boolean isRightClick, boolean isShiftClick) {
			this(isRightClick);
			this.isShiftClick = isShiftClick;
		}

		public boolean isRightClick() {
			return !this.isLeftClick;
		}

		public boolean isLeftClick() {
			return this.isLeftClick;
		}

		public boolean isShiftClick() {
			return this.isShiftClick;
		}
	}
}
