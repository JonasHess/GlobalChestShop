package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.DefaultCategory;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_CreateShop2_3 extends GUI_PageView<DefaultCategory>{

	
	private ShopInfoPack infoPack;
	public GUI_CreateShop2_3(InventoryGUI parentGUI, ShopInfoPack infoPack) {
		super("Predefined Categories", new ItemStack(Material.BOOKSHELF), 1, parentGUI);
		this.infoPack = infoPack;
	}

	@Override
	public List<DefaultCategory> getRefreshedObjectList() {
		return GlobalChestShop.plugin.getDefaultCategoryController(infoPack.worldGroup).getAllCategories();
	}

	@Override
	public boolean shouldObjectListBeRefreshedAutomatically() {
		return false;
	}

	@Override
	public void drawAditionalButtons(Player player) {
		
	}

	@Override
	public Button convertListObjectToButton(final DefaultCategory obj, Player player) {
		return new Button(obj.getDisplayItem(), obj.getTitle()) {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				ShopInfoPack packClone = infoPack.clone();
				packClone.setDefaultShop(obj.getId());
				new GUI_CreateShop3(packClone, inventoryGUI).open(player);
			}
		};
	}

	@Override
	public boolean shouldBackgroundBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldEscKeyClosesTheGUI() {
		return false;
	}

	@Override
	public boolean shouldEscKeyLeadsToPreviousGUI() {
		return true;
	}

	@Override
	public boolean shouldReturnButtonBeDrawn() {
		return true;
	}

	@Override
	public boolean shouldCloseButtonBeDrawn() {
		return true;
	}

	@Override
	public void onPlayerOpensTheGUI(Player player) {
		
	}

	@Override
	public void onPlayerLeavesTheGUI(Player player) {
		
	}

	@Override
	public void onTick(Player player, int tickCount) {
		
	}

	@Override
	public void onClickInLowerInventory(Player player, ItemStack clicked, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
		
	}

}
