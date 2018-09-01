package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GuiCore;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_CloseGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Return;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects.AnimatedButton;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects.ButtonEffect;

public abstract class InventoryGUI {
	private static final int		MAX_HEIGHT				= 6;
	private int						height;
	private final int				width					= 9;
	private Button[][]				buttons;
	private List<AnimatedButton>	animatedButtons;
	private String					title;
	private InventoryGUI			parentGUI;
	private boolean					closeButtonWasPressed	= false;
	private int						tickCount;
	private ItemStack				displayIcon;

	private boolean					wasInitialized			= false;

	public boolean					isOpened				= false;

	// Konstructor
	public InventoryGUI(String title, ItemStack displayItem, InventoryGUI parentInventoryGUI) {
		this(title, 6, displayItem, parentInventoryGUI);
	}

	// Konstructor
	public InventoryGUI(String title, int lines, ItemStack displayIcon, InventoryGUI parentInventoryGUI) {
		this.buttons = new Button[9][lines];
		this.setTitle(title);
		this.displayIcon = displayIcon;
		this.setParentGUI(parentInventoryGUI);
		if (lines > InventoryGUI.MAX_HEIGHT) {
			throw new RuntimeException("Too many lines. Maximum is " + InventoryGUI.MAX_HEIGHT + " lines!");
		}
		this.height = lines;
		this.animatedButtons = java.util.Collections.synchronizedList(new ArrayList<AnimatedButton>());
	}

	public void addAnimatedButton(ButtonEffect effect, Button button) {
		this.animatedButtons.add(new AnimatedButton(button, effect));
	}

	public void removeAllAnimatedButtons() {
		synchronized (animatedButtons) {
			this.animatedButtons.clear();
		}
	}

	public abstract boolean shouldBackgroundBeDrawn();

	public abstract boolean shouldEscKeyClosesTheGUI();

	public abstract boolean shouldEscKeyLeadsToPreviousGUI();

	public abstract boolean shouldReturnButtonBeDrawn();

	public abstract boolean shouldCloseButtonBeDrawn();

	protected abstract void drawButtons(Player player);

	public abstract void onPlayerOpensTheGUI(Player player);

	public abstract void onPlayerLeavesTheGUI(Player player);

	public abstract void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI);

	public void onPreTickEvent(Player player, int tickCount) {
		synchronized (animatedButtons) {

			boolean refreshAnimatedButtons = false;
			for (AnimatedButton a : this.animatedButtons) {
				if (a.getEffect().doesApperienceChangeWithThisTick(tickCount)) {
					refreshAnimatedButtons = true;
					break;
				}
			}
			this.onTick(player, tickCount);
			if (refreshAnimatedButtons) {
				this.refresh(player);
			}
		}
	}

	public abstract void onTick(Player player, int tickCount);

	abstract public  void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event);

	public void open(Player player) {
		GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, this);
	}

	public InventoryGUI drawButton(int x, int y, Button button) {
		try {
			this.buttons[x][y] = null;
			this.buttons[x][y] = button;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new RuntimeException("You tried to add / remove a Button outside the grid! MaxWidth = " + (this.width - 1) + " |  Max Height = " + (this.height - 1));
		}
		return this;
	}

	public void drawFrame(int X, int Y, ItemStack itemStack) {
		for (int x = X - 1; x <= X + 1; x++) {
			for (int y = Y - 1; y <= Y + 1; y++) {
				if (x >= 0 && x <= this.getWidth() - 1) {
					if (y >= 0 && y <= this.getHeight() - 1) {
						if (!(x == X && y == Y))
							this.drawButton(x, y, new Button_Bare(itemStack, " "));
					}
				}
			}
		}
	}

	public InventoryGUI addButton(int x, int y, Button button) {
		this.drawButton(x, y, button);
		return this;
	}

	public InventoryGUI eraseButton(int x, int y) {
		return this.drawButton(x, y, null);
	}

	public InventoryGUI removeButton(int x, int y) {
		eraseButton(x, y);
		return this;
	}

	public InventoryGUI deleteButton(int x, int y) {
		eraseButton(x, y);
		return this;
	}

	public Button getButton(int x, int y) {
		synchronized (this.animatedButtons) {
			for (AnimatedButton a : this.animatedButtons) {
				if (a.getPosition(tickCount).distance(new Point(x, y)) == 0) {
					return a.getAnimatedButton(tickCount);
				}
			}
		}
		return this.buttons[x][y];
	}

	public Button getButton(int slot) {
		if (slot < 0 || slot > this.getSlots()) {
			return null;
		}
		return this.getButton(slot % 9, slot / 9);
	}

	public Button[][] getButtonGrid() {
		return this.buttons;
	}

	public final int getSlots() {
		return this.getWidth() * this.getHeight();
	}

	public List<Button> getAllButtons() {
		List<Button> resultList = new LinkedList<Button>();
		for (int x = 0; x < this.buttons.length; x++) {
			for (int y = 0; y < this.buttons[x].length; y++) {
				Button button = this.getButton(x, y);
				if (button != null) {
					resultList.add(button);
				}
			}
		}
		return resultList;
	}

	public InventoryGUI removeAllButtons() {
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				this.eraseButton(x, y);
			}
		}
		return this;
	}

	public final int getWidth() {
		return this.width;
	}

	public final int getHeight() {
		return this.height;
	}

	public ItemStack getDisplayIcon() {
		return displayIcon;
	}

	public InventoryGUI setDisplayIcon(ItemStack displayIcon) {
		this.displayIcon = displayIcon;
		return this;
	}

	public InventoryGUI setTitle(String title) {
		if (title.length() > 32) {
			title = title.substring(0, 32);
		}
		this.title = title;
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public int getTickCount() {
		return tickCount;
	}

	public InventoryGUI setTickCount(int tickCount) {
		this.tickCount = tickCount;
		return this;
	}

	public InventoryGUI setParentGUI(InventoryGUI parentGUI) {
		this.parentGUI = parentGUI;
		return this;
	}

	public InventoryGUI getParentGUI() {
		return parentGUI;
	}

	public InventoryGUI getParentGUI(int hops) {
		int t = 0;
		InventoryGUI result = this;
		while (t < hops && result.hasParentGUI()) {
			t++;
			result = result.getParentGUI();
		}
		return result;
	}

	public boolean hasParentGUI() {
		return this.getParentGUI() != null;
	}

	public InventoryGUI getFirstGUI() {
		return this.getParentGUI(9999);
	}

	public int getGUIsStackPosition(InventoryGUI gui) {
		int t = 0;
		InventoryGUI result = this;
		while (result.hasParentGUI()) {
			t++;
			result = result.getParentGUI();
		}
		return t;
	}

	public InventoryGUI returnToInventoryGUI(InventoryGUI gui, Player player) {
		if (gui != null) {
			gui.onPlayerReturnsToThisGUI(player, this);
			gui.open(player);
		} else {
			this.close(player);
		}
		return gui;
	}

	public InventoryGUI returnToParentGUI(Player player) {
		return this.returnToInventoryGUI(getParentGUI(), player);
	}

	public InventoryGUI returnToParentGUI(Player player, int hops) {
		return this.returnToInventoryGUI(getParentGUI(hops), player);
	}

	public InventoryGUI returnToFirstGUI(Player player) {
		return this.returnToInventoryGUI(getFirstGUI(), player);
	}

	public InventoryGUI goBack(Player player) {
		return this.returnToParentGUI(player);
	}

	public InventoryGUI close(Player player) {
		GlobalChestShop.plugin.getGuiCore().close_InventoyGUI(player);
		return this;
	}

	/**
	 * Override this function to change the appearance of CloseButtons
	 * 
	 * @return
	 */
	protected Button_CloseGUI getCloseButton() {
		// return new Button_CloseGUI(new ItemStack(Material.STAINED_GLASS_PANE,
		// 1, (short) 14),
		// GlobalChestShop.text.get(GlobalChestShop.text.CloseButton_Title),
		// GlobalChestShop.text.get2(GlobalChestShop.text.CloseButton_DESC)) {
		return new Button_CloseGUI(GlobalChestShop.plugin.getMainConfig().getCloseButton(), GlobalChestShop.text.get(GlobalChestShop.text.CloseButton_Title), GlobalChestShop.text.get(GlobalChestShop.text.CloseButton_DESC)) {

			@Override
			protected void afterInventoryClose(InventoryGUI closedGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
			}
		};
	}

	/**
	 * Override this function to change the appearance of ReturnButton. By
	 * default the ReturnButton will look like the prevoidGUIs displayIcon
	 * 
	 * @return
	 */
	protected Button_Return getReturnButton() {
		return new Button_Return(this, GlobalChestShop.text.get(GlobalChestShop.text.BackButton_Title), GlobalChestShop.text.get(GlobalChestShop.text.BackButton_DESC));
	}

	protected InventoryGUI drawReturnBackButon() {
		if (this.shouldReturnButtonBeDrawn() && this.getParentGUI() != null) {
			this.drawButton(0, 0, this.getReturnButton());
		}
		return this;
	}

	protected InventoryGUI drawCloseButton() {
		if (this.shouldCloseButtonBeDrawn())
			this.drawButton(8, 0, this.getCloseButton());
		return this;
	}

	/**
	 * Override this function if you want to change the background.
	 * 
	 * @return ItemStack that will be converted into a button later on.
	 */
	public ItemStack getBackgroundIcon() {
		// return new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		return GlobalChestShop.plugin.getMainConfig().getBackground();

	}

	protected InventoryGUI drawBackground() {
		ItemStack bgItemStack = this.getBackgroundIcon();
		if (bgItemStack == null || !this.shouldBackgroundBeDrawn())
			return this;
		Button background = new Button_Bare(bgItemStack, " ");
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				this.drawButton(x, y, background);
			}
		}
		return this;
	}

	public InventoryGUI redrawAllButtons(Player player) {
		initialize(player);
		return this;
	}

	public InventoryGUI resetAllButtons(Player player) {
		initialize(player);
		return this;
	}

	public InventoryGUI initialize(Player player) {
		if (wasInitialized) {
			removeAllButtons();
		}
		this.wasInitialized = true;
		drawBackground();
		drawButtons(player);
		this.drawReturnBackButon();
		this.drawCloseButton();
		return this;
	}

	/**
	 * Call this function every time you change something inside the
	 * InventoryGUI and want the player to see the change.
	 * 
	 * @param player
	 * @return
	 */
	public void refresh(final Player player) {
		if (!GuiCore.isEnabled) {
			return;
		}
		if (player == null) {
			return;
		}
		if (!wasInitialized) {
			initialize(player);
		}

		Inventory inv = null;
		final UUID uuid = player.getUniqueId();
		boolean firstOpen = false;

		// Check if the BukkitInventory was created before.
		inv = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedBukkitInventory(player.getUniqueId());
		boolean newName = ((inv == null) ? false : !inv.getTitle().equals(getTitle()));
		if (inv == null || newName) {
			if (newName) {
				GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, this);
				return;
			}
			// if not, create a new Inventory
			inv = Bukkit.createInventory(null, getSlots(), getTitle());
			firstOpen = true;
		}

		// Transform Buttons to ItemStacks and place them inside the inventory.
		Button button;
		ItemStack item;
		for (int x = 0; x < buttons.length; x++) {
			for (int y = 0; y < buttons[x].length; y++) {
				button = getButton(x, y);
				item = null;
				if (button != null) {
					button.onRefresh(this, player);
					item = button.toItemStack(this.tickCount);
				}
				inv.setItem(x + y * 9, item);
			}
		}

		//
		synchronized (this.animatedButtons) {
			for (AnimatedButton animatedButton : this.animatedButtons) {
				button = animatedButton.getAnimatedButton(tickCount);
				Point pos = animatedButton.getPosition(tickCount);
				item = null;
				if (button != null) {
					button.onRefresh(this, player);
					item = button.toItemStack(this.tickCount);
				}
				inv.setItem((int) pos.getX() + (int) pos.getY() * 9, item);
			}
		}

		//
		final boolean fitstOpenFinal = firstOpen;
		final Inventory invFinal = inv;
		final InventoryGUI gui = this;

		Bukkit.getScheduler().runTask(GlobalChestShop.plugin, new Runnable() {
			@Override
			public void run() {
				if (!GuiCore.isEnabled) {
					return;
				}
				// Send the inventory to the player.
				if (fitstOpenFinal) {
					player.openInventory(invFinal);
				} else {
					player.updateInventory();
				}
				// GlobalChestShop.plugin.getGuiCore().hashMap_Player_BukkitInventory.put(uuid,
				// invFinal);
				// GlobalChestShop.plugin.getGuiCore().hashMap_Player_InventoryGUI.put(uuid,
				// gui);
				GlobalChestShop.plugin.getGuiCore().addGuiToHashMaps(gui, invFinal, uuid);
			}
		});

		// Store the session for later use.
		return;

		// ////////ORIGINAL
		// if (!this.wasInitialized) {
		// this.initialize(player);
		// }
		//
		// Inventory inv = null;
		// UUID uuid = player.getUniqueId();
		//
		//
		// // Check if the BukkitInventory was created before.
		// if
		// (GlobalChestShop.plugin.getGuiCore().hashMap_Player_BukkitInventory.containsKey(uuid))
		// {
		// inv =
		// GlobalChestShop.plugin.getGuiCore().hashMap_Player_BukkitInventory.get(uuid);
		// }
		// boolean newName = ((inv == null) ? false :
		// !inv.getTitle().equals(this.getTitle()));
		// if (inv == null || newName) {
		// if (newName) {
		// GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, this);
		// return this;
		// }
		// // if not, create a new Inventory
		// inv = Bukkit.createInventory(null, this.getSlots(), this.getTitle());
		// }
		//
		// // Transform Buttons to ItemStacks and place them inside the
		// inventory.
		// for (int x = 0; x < this.buttons.length; x++) {
		// for (int y = 0; y < this.buttons[x].length; y++) {
		// Button button = this.getButton(x, y);
		// ItemStack item = null;
		// if (button != null) {
		// button.onRefresh(this, player);
		// item = button.toItemStack();
		// }
		// inv.setItem(x + y * 9, item);
		// }
		// }
		//
		// // Send the inventory to the player.
		// if (inv !=
		// GlobalChestShop.plugin.getGuiCore().hashMap_Player_BukkitInventory.get(uuid))
		// {
		// player.openInventory(inv);
		// } else {
		// player.updateInventory();
		// }
		//
		// // Store the session for later use.
		// GlobalChestShop.plugin.getGuiCore().hashMap_Player_BukkitInventory.put(uuid,
		// inv);
		// GlobalChestShop.plugin.getGuiCore().hashMap_Player_InventoryGUI.put(uuid,
		// this);
		// return this;
		// //// ORIGINAL ENDE
	}

	public boolean isCloseButtonPressed() {
		return closeButtonWasPressed;
	}

	public void setCloseButtonAsPressed(boolean closeButtonWasPressed) {
		this.closeButtonWasPressed = closeButtonWasPressed;
	}

	public static void warning(final String message, final boolean important, Player player, InventoryGUI prevGUI) {
		GlobalChestShop.plugin.getGuiCore().close_InventoyGUI(player);
		GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new InventoryGUI(GlobalChestShop.text.get(GlobalChestShop.text.GUI_WarningGuiTitle), 6, new ItemStack(Material.PAPER), prevGUI) {

			@Override
			public void onTick(Player player, int tickCount) {

				if (important) {
					if (tickCount % 20 == 0) {
						this.drawFrame(4, 1, new ItemStack(XMaterial.RED_STAINED_GLASS_PANE.parseItem()));
						this.refresh(player);
					}
					if (tickCount % 20 == 10) {
						this.drawFrame(4, 1, new ItemStack(XMaterial.RED_STAINED_GLASS_PANE.parseItem()));
						this.refresh(player);
					}
				}

				// Countdown
				// if (tickCount > timeoutInTicks) {
				// this.returnToParentGUI(player);
				// } else {
				// if ((tickCount % 20) == 0 || tickCount == 1) {
				// long secLeft = (long) ((timeoutInTicks - tickCount) / 20);
				// if (tickCount == 1)
				// secLeft++;
				// this.drawButton(this.getWidth() - 1, 0, (new Button_Bare(new
				// ItemStack(Material.WATCH),
				// message).setAmount((int) secLeft % 64)));
				// this.refresh(player);
				// }
				// }
			}

			@Override
			protected void drawButtons(Player player) {
				this.drawButton(4, 1, new Button_Bare(new ItemStack(Material.PAPER), message));

				this.drawButton(4, 3, new Button(new ItemStack(Material.ARROW), "Ok") {

					@Override
					public void onRefresh(InventoryGUI inventoryGUI, Player player) {

					}

					@Override
					public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
						inventoryGUI.returnToParentGUI(player);
					}
				});
			}

			@Override
			public boolean shouldReturnButtonBeDrawn() {
				return false;
			}

			@Override
			public boolean shouldEscKeyLeadsToPreviousGUI() {
				return false;
			}

			@Override
			public boolean shouldEscKeyClosesTheGUI() {
				return false;
			}

			@Override
			public boolean shouldCloseButtonBeDrawn() {
				return false;
			}

			@Override
			public boolean shouldBackgroundBeDrawn() {
				return true;
			}

			@Override
			public void onPlayerOpensTheGUI(Player player) {

			}

			@Override
			public void onPlayerLeavesTheGUI(Player player) {
			}

			@Override
			public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
			}

			@Override
			public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
			}
		});
	}

}
