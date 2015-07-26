package eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Exceptions.PriceNotInitializedException;
import eu.blockup.GlobalChestShop.Util.Experimental.PricingEngine.PriceCalculation.ItemAmountRelation;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_IngedientList extends GUI_PageView<ItemAmountRelation>{

	private ItemStack itemStack;
	private int worldGroup;
		
	public GUI_IngedientList(ItemStack itemStack, InventoryGUI parentGUI, int worldGroup) {
		super("IngedientList of " + GlobalChestShop.plugin.getItemStackDisplayName(itemStack), itemStack, 1, parentGUI);
		this.itemStack = itemStack;
		this.worldGroup = worldGroup;
	}

	@Override
	public List<ItemAmountRelation> getRefreshedObjectList() {
		return GlobalChestShop.plugin.getPriceEngine().getLeaveListOfItem(itemStack);
	}

	@Override
	public boolean shouldObjectListBeRefreshedAutomatically() {
		return false;
	}

	@Override
	public void drawAditionalButtons(Player player) {
		this.addButton(4, 0, new Button(this.itemStack) {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				this.clearDesacription();
				String buyPrice;
				String sellPrice;
				try {
					buyPrice = GlobalChestShop.plugin.formatPrice(GlobalChestShop.plugin.getPriceEngine().getBuyPriceOfItem(itemStack, 1, worldGroup), true);
					sellPrice = GlobalChestShop.plugin.formatPrice(GlobalChestShop.plugin.getPriceEngine().getSellPriceOfItem(itemStack, 1, worldGroup), true);	
				} catch (PriceNotInitializedException e) {
					buyPrice = ChatColor.RED + "Not defined"; // TODO
					sellPrice = ChatColor.RED + "Not defined"; // TODO
				}
					this.addDescriptionLine(ChatColor.GRAY + buyPrice); // TODO
					this.addDescriptionLine(ChatColor.GRAY + sellPrice); // TODO
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				
			}
		});
	}

	@Override
	public Button convertListObjectToButton(ItemAmountRelation obj, Player player) {
		return new Button_PriceChangerAdminShop(obj.itemStack, worldGroup);
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
