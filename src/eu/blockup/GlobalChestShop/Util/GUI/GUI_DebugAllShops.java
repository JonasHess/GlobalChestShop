package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;

public class GUI_DebugAllShops extends GUI_PageView<Shop>{

	public GUI_DebugAllShops(InventoryGUI parentGUI) {
		super("Broken Shops", new ItemStack(Material.ANVIL), 1, parentGUI);
	}

	@Override
	public List<Shop> getRefreshedObjectList() {
		return GlobalChestShop.plugin.getShopVerwaltung().getBrokenShopList();
	}

	@Override
	public boolean shouldObjectListBeRefreshedAutomatically() {
		return true;
	}

	@Override
	public void drawAditionalButtons(Player player) {
		this.drawButton(4, 0, new Button_Bare(new ItemStack(Material.ANVIL), "Broken Shops"));
	}

	@Override
	public Button convertListObjectToButton(final Shop obj, Player player) {
		return new Button(new ItemStack(obj.getShopEntityIcon()), GlobalChestShop.plugin.getNameOfPlayer(obj.getOwner()), "ShopID: " + obj.getShopID(), obj.getShopTyp() == EShopTyp.LocalChestShop ? "Owner UUID: " + obj.getOwnerUUID() : "") {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				player.teleport(obj.getSignLocation());
				obj.onInteractLeftClick(player, inventoryGUI);
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
