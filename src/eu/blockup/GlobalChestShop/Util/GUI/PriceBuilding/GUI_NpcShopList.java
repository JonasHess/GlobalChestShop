package eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_NpcShopList extends GUI_PageView<Shop>{

	private List<Shop> shopList;
	public GUI_NpcShopList(String title,  List<Shop> shopList, InventoryGUI parentGUI) {
		super(title, new ItemStack(Material.MONSTER_EGG), 1, parentGUI);
		this.shopList = shopList;
	}

	@Override
	public List<Shop> getRefreshedObjectList() {
		return this.shopList;
	}

	@Override
	public boolean shouldObjectListBeRefreshedAutomatically() {
		return true;
	}

	@Override
	public void drawAditionalButtons(Player player) {
		this.drawButton(4, 0, new Button_Bare(this.getDisplayIcon(), this.getTitle()));
	}

	@Override
	public Button convertListObjectToButton(final Shop obj, Player player) {
		return new Button(obj.getShopEntityIcon(), obj.getSecondSignLine(true)) {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				if (type == ClickType.RIGHT) { 
					obj.onInteractLeftClick(player, inventoryGUI);
				} else {
					obj.onInteractRightClick(player, inventoryGUI);
				}
				
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
