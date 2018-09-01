package eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding;

import java.util.List;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.GUI.GUI_CreateShop2_0;
import eu.blockup.GlobalChestShop.Util.GUI.ShopInfoPack;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;

public class GUI_NpcShopList extends GUI_PageView<Shop>{

	private int npcId;
	private int worldGroup;
	private String worldName;
	public GUI_NpcShopList(String title, int npcId, int worldGroup, String worldName, InventoryGUI parentGUI) {
		super(title, new ItemStack(XMaterial.GHAST_SPAWN_EGG.parseItem()), 1, parentGUI);
		this.npcId = npcId;
		this.worldGroup = worldGroup;
		this.worldName = worldName;
	}

	@Override
	public List<Shop> getRefreshedObjectList() {
		return GlobalChestShop.plugin.getShopVerwaltung().getNPCShops(npcId, worldGroup, worldName);
	}

	@Override
	public boolean shouldObjectListBeRefreshedAutomatically() {
		return true;
	}

	@Override
	public void drawAditionalButtons(Player player) {
		this.drawButton(4, 0, new Button_Bare(this.getDisplayIcon(), this.getTitle()));
		
		this.drawButton(this.getWidth() -1, this.getHeight() -1, new Button(new ItemStack(XMaterial.LIME_STAINED_GLASS_PANE.parseItem()), "Add shop to NPC") {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				ShopInfoPack infoPack = new ShopInfoPack(EShopTyp.GlobalNpcShop, worldGroup, player.getLocation(), player.getLocation(), null, null, npcId);
				new GUI_CreateShop2_0(infoPack, inventoryGUI).open(player);;
				
			}
		});
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
