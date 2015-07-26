package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class Button_AllPlayers extends Button {

	private final Integer	worldGroup;

	public Button_AllPlayers(Integer worldGroup) {
		super(new ItemStack(Material.IRON_FENCE, 1, (short) 3), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AllPlayerHistory_Title), GlobalChestShop.text.get(GlobalChestShop.text.GUI_AllPlayerHistory_Description));
		this.worldGroup = worldGroup;
	}

	@Override
	public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
		GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_PageView<UUID>(this.getTitle(), this.getDisplayIcon(), 1, inventoryGUI) {

			@Override
			public void drawAditionalButtons(Player player) {

			}

			@Override
			public Button convertListObjectToButton(final UUID obj, Player player) {
				ItemStack item;
				if (obj.compareTo(GlobalChestShop.plugin.adminShopUUID) == 0) {
					item = new ItemStack(Material.ENDER_CHEST);
				} else {
					item = GlobalChestShop.plugin.getPlayerHead(GlobalChestShop.plugin.getPlayerController().getPlayerIdFromUUID(obj), false);
				}
				return new Button(item, GlobalChestShop.plugin.getNameOfPlayer(obj)) {
					@Override
					public void onRefresh(InventoryGUI inventoryGUI, Player player) {

					}

					@Override
					public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
						GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_AuctionHistory(inventoryGUI, obj, Visibility.AllActive, true, false, worldGroup));
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

			@Override
			public List<UUID> getRefreshedObjectList() {
				return GlobalChestShop.plugin.getAuctionController(worldGroup).getAllPlayers();
			}

			@Override
			public boolean shouldObjectListBeRefreshedAutomatically() {
				return true;
			}

		});
	}

	@Override
	public void onRefresh(InventoryGUI inventoryGUI, Player player) {

	}

}
