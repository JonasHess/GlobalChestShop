package eu.blockup.GlobalChestShop.Util.GUI;


import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PolarQuestion;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;

public class GUI_DeleteAllPlayerShops extends SimpleIInventoryGUI{

	public GUI_DeleteAllPlayerShops(InventoryGUI parentInventoryGUI) {
		super(ChatColor.RED + "Delete all player-shops", 6, new ItemStack(Material.REDSTONE_BLOCK), parentInventoryGUI);
	}

	@Override
	protected void drawButtons(Player player) {
		this.drawFrame(4,3,new ItemStack(XMaterial.RED_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 14));
		this.drawButton(4, 3, new Button(new ItemStack(Material.REDSTONE_BLOCK), ChatColor.RED + "Delete all player-shops") {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				
			}
			
			@Override
			public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
				InventoryGUI polarQuestion = new GUI_PolarQuestion("Delete all player-shops", inventoryGUI) {
					
					@Override
					public boolean shouldReturnButtonBeDrawn() {
						return true;
					}
					
					@Override
					public boolean shouldEscKeyLeadsToPreviousGUI() {
						return false;
					}
					
					@Override
					public boolean shouldEscKeyClosesTheGUI() {
						return true;
					}
					
					@Override
					public boolean shouldCloseButtonBeDrawn() {
						return true;
					}
					
					@Override
					public boolean shouldBackgroundBeDrawn() {
						return false;
					}
					
					@Override
					public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
						
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
					public ButtonTypEnum whichButtonShouldBeClickedOnTimeout() {
						return ButtonTypEnum.NoButton;
					}
					
					@Override
					protected void onYesButtonClick(InventoryGUI inventoryGUI, Player player) {
						GlobalChestShop.plugin.deleteAllPlayerShops();
						this.close(player);
					}
					
					@Override
					protected void onNoButtonClick(InventoryGUI inventoryGUI, Player player) {
						this.close(player);
					}
					
					@Override
					public Button_Bare get_the_Question_Button() {
						return new Button_Bare(new ItemStack(Material.PAPER));
					}
					
					@Override
					public Button_Bare get_YesButton() {
						return new Button_Bare(new ItemStack(XMaterial.LIME_WOOL.parseItem()), ChatColor.RED + " DELETE ALL");
					}
					
					@Override
					public Button_Bare get_NoButton() {
						return new Button_Bare(new ItemStack(XMaterial.RED_WOOL.parseItem()), ChatColor.GREEN + "Cancel");
					}
					
					@Override
					public String get_GUI_Title() {
						return "Delete all player-shops?";
					}
					
					@Override
					public long getTimeoutInTicks() {
						return 0;
					}
					
					@Override
					protected void drawAdditionalButtons(Player player) {
						
					}
				};
				polarQuestion.open(player);
			}
		});
	}

}
