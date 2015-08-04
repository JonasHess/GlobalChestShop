package eu.blockup.GlobalChestShop.Util.GUI;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Core.StringInput;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class Button_SearchAuctions extends Button {

	class SeachInput extends StringInput {

		private int	worldGroup;
		private double multiplier;

		public SeachInput(int worldGroup, double multiplier) {
			super();
			this.worldGroup = worldGroup;
			this.multiplier = multiplier;
		}

		@Override
		protected void onPlayerTypedString(InventoryGUI parentGUI, Player player, String value) {
			try {
				if (worldGroup != GlobalChestShop.plugin.getworldGroup(player.getLocation())) {
					throw new WorldHasNoWorldGroupException();
				}
			} catch (WorldHasNoWorldGroupException e) {
				return;
			}
			new GUI_Search(value, parentGUI, this.worldGroup, GlobalChestShop.plugin.getMainConfig().buyCommandShowsOnlyAdminShops, GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.VIP_CREATE_NEW_AUCTIONS_INSIDE_BUY_COMMAND), multiplier).open(player);
		}

		@Override
		protected void onTimeout(InventoryGUI prevInventoryGUI, Player player) {
			try {
				if (worldGroup != GlobalChestShop.plugin.getworldGroup(player.getLocation())) {
					throw new WorldHasNoWorldGroupException();
				}
			} catch (WorldHasNoWorldGroupException e) {
				return;
			}
			prevInventoryGUI.open(player);
		}

		@Override
		public String[] getPlayersInstructions(Player p) {
			String[] a = new String[1];
			a[0] = GlobalChestShop.text.get(GlobalChestShop.text.Chat_SearchInstructions);
			return a;
		}

		@Override
		public long getTimeoutInTicks() {
			return 20 * 40;
		}

		@Override
		public boolean shouldPlayerBeBlindWhileWriting() {
			return true;
		}

		@Override
		public boolean shouldPlayerBeUnableToMoveWhileWriting() {
			return true;
		}

	}

	private int worldGroup;
	private double multiplier;
	public Button_SearchAuctions(int worldGroup, double multiplier) {
		super(new ItemStack(Material.HOPPER), GlobalChestShop.text.get(GlobalChestShop.text.SearchButton_Title), GlobalChestShop.text.get(GlobalChestShop.text.SearchButton_DESC));
		this.worldGroup = worldGroup;
		this.multiplier = multiplier;
	}

	@Override
	public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {

		new SeachInput(worldGroup, multiplier).start(player);
	}

	@Override
	public void onRefresh(InventoryGUI inventoryGUI, Player player) {

	}

}
