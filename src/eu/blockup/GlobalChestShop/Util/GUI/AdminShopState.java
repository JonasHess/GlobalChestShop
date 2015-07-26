package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Auction;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StateKeeper;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class AdminShopState extends StateKeeper<Boolean> {

	private ItemStack	itemStack;
	private int			worldGroup;
	private Auction		adminshop;

	public AdminShopState(ItemStack itemStack, int worldGroup) {
		super();
		this.itemStack = itemStack;
		this.worldGroup = worldGroup;
	}

	@Override
	protected void onMofiyState(Boolean offsetValue, Player player) {
		// TODO Auto-generated method stub

	}

	public Auction getAdminShop(Player player) {
		if (this.adminshop == null) {
			this.adminshop = GlobalChestShop.plugin.getAuctionController(this.worldGroup).getAdminShopFromItemStack(this.itemStack);
		}
		return adminshop;
	}

	@Override
	protected void onSetState(Boolean absoluteValue, Player player) {
		Auction adminShop = this.getAdminShop(player);
		InventoryGUI gui = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedInventoryGui(player).close(player);
		if (getCurrentState(player)) {
			GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AdminShopEdit(adminShop, gui, this.worldGroup));
		} else {
			if (adminShop == null) {
				GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AdminShopCreate(itemStack, gui, worldGroup));
			} else {
				GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AdminShopEdit(adminShop, gui, this.worldGroup));
			}
		}
	}

	@Override
	public Boolean getCurrentState(Player player) {
		Auction adminShop = this.getAdminShop(player);
		if (adminShop == null)
			return false;
		return !adminShop.isEndent();
	}

	@Override
	public Button_Bare formatToDisplayButton(Player player, int amount) {
		ItemStack item = new ItemStack(Material.ENDER_CHEST);
		// ItemStack item = new ItemStack(Material.WOOL, 1, (short) 5);
		// ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 14);
		if (this.getCurrentState(player)) {
			return new Button_Bare(item, GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Toggle_ON), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Toggle_ON_DESC));
		}
		return new Button_Bare(item, GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Toggle_OFF), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AdministrateAdminShop_Toggle_OFF_DESC));
	}

	@Override
	public List<String> stateToString(int amount) {
		return null;
	}

}
