package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.CustomCategory;
import eu.blockup.GlobalChestShop.Util.DefaultCategory;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_DefaultCategoryCollection extends SimpleIInventoryGUI {
	private boolean	adminShopsOnly;
	private Integer	worldGroup;
	private boolean	newAuctions;

	public GUI_DefaultCategoryCollection(InventoryGUI parentInventoryGUI, boolean onlyAdminShops, Integer worldGroup, boolean newAuctions) {
		super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_MainWindos_GUI_Title), 6, GlobalChestShop.plugin.mainConfig.getDisplayItemAllItems(), parentInventoryGUI);
		this.adminShopsOnly = onlyAdminShops;
		this.worldGroup = worldGroup;
		this.newAuctions = newAuctions;
	}

	@Override
	protected void drawButtons(Player player) {

		class Button_openDefaultCategory extends Button {

			private DefaultCategory	category;

			public Button_openDefaultCategory(DefaultCategory category) {
				super(category.toDisplayButton());
				this.category = category;
			}

			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {

			}

			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				GUI_DefaultCategoryCollection gui = (GUI_DefaultCategoryCollection) inventoryGUI;
				GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_DefaultCategoryPage(category, inventoryGUI, gui.adminShopsOnly, gui.worldGroup, gui.newAuctions));
			}

		}

		// Display Item
		this.drawButton(4, 0, new Button_Bare(this.getDisplayIcon(), this.getTitle()));
		// this.addAnimatedButton(new ButtonEffect_FadeIn(4 , 0, 10), new
		// Button_Bare(this.getDisplayIcon(), this.getTitle()));

		// Create New Auction
		if (this.newAuctions) {
			this.drawButton(this.getWidth() - 1, this.getHeight() - 1, new Button_AuctionCreate(this.worldGroup));
			this.drawButton(0, this.getHeight() - 1, new Button_AuctionHistory(true, this.worldGroup, true));
		}

		// Categoties
		List<DefaultCategory> list = GlobalChestShop.plugin.getDefaultCategoryController(this.worldGroup).getAllCategories();
		int X = 2, Y = 2;
		int x = X, y = Y;

		for (DefaultCategory category : list) {
			this.drawButton(x, y, new Button_openDefaultCategory(category));
			x++;
			this.removeButton(x - 1, Y + 1);
			if (x >= X + 5 && y == Y) {
				x = X;
				y += 2;
			}
		}

		List<CustomCategory> customCategoryList = GlobalChestShop.plugin.getCustomCategoryController().getAllCategoriesShownInCreativeMenu();
		for (CustomCategory c : customCategoryList) {
			this.drawButton(c.isShownInCreativeMenu() + 1, 3, new Button_CustomCategory(c, worldGroup));
		}
		
		
		if (GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.ADMIN)) {
			this.addButton(8, 3, new Button_openDefaultCategory(GlobalChestShop.plugin.getDefaultCategoryController(worldGroup).getHiddenItemsCategory()));
			
			boolean DEBUG = false;  // TODO
			if (DEBUG) {
				this.addButton(7, 3, new Button_openDefaultCategory(GlobalChestShop.plugin.getDefaultCategoryController(worldGroup).getDebugCategory()));
			}
		}
	}

	public GUI_DefaultCategoryCollection(String title, int lines, ItemStack displayIcon, InventoryGUI parentInventoryGUI) {
		super(title, lines, displayIcon, parentInventoryGUI);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
		if (type == ClickType.SHIFT_LEFT) {
			if (this.adminShopsOnly) {
				GlobalChestShop.plugin.openAdminShopOnlyGUI(this, player, clicked, worldGroup);
			} else {
				GlobalChestShop.plugin.openNormalAuctionGUI(this, player, clicked, worldGroup, false, adminShopsOnly);
			}
		}
	}

}
