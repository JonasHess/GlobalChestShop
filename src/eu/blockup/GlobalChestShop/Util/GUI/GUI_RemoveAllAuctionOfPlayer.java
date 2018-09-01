package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.UUID;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_GetStringInput;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.Permissions;

public class GUI_RemoveAllAuctionOfPlayer extends SimpleIInventoryGUI{

	int worldGroup;
	public GUI_RemoveAllAuctionOfPlayer(InventoryGUI parentInventoryGUI, int worldGroup) {
		super("Remove all auctions of player", 3, new ItemStack(Material.ANVIL), parentInventoryGUI);
		this.worldGroup = worldGroup;
	}

	@Override
	protected void drawButtons(Player player) {
		if (! GlobalChestShop.plugin.validatePermissionCheck(player, Permissions.MODERATOR_DELETE_AUCTIONS)) {
			return;
		}
		
		this.drawButton(4, 1, new Button_GetStringInput(new ItemStack(XMaterial.SKELETON_SKULL.parseMaterial()), "Find Player") {
			
			@Override
			public void onRefresh(InventoryGUI inventoryGUI, Player player) {
				
			}
			
			@Override
			protected boolean shouldPlayerBeUnableToMoveWhileWriting() {
				return true;
			}
			
			@Override
			public boolean shouldPlayerBeBlindWhileWriting() {
				return true;
			}
			
			@Override
			public void onTimeout(InventoryGUI prevInventoryGUI, Player player) {
				prevInventoryGUI.open(player);
			}
			
			@SuppressWarnings("deprecation")
			@Override
			public void onPlayerTypedString(InventoryGUI prevInventoryGUI, Player player, String value) {
				UUID uuid = null;
				try {
					uuid = UUID.fromString(value);
				} catch(Exception e) {
					uuid = null;
				}
				if (uuid == null) {
					OfflinePlayer p = GlobalChestShop.plugin.getServer().getOfflinePlayer(value);
					if (p != null && p.hasPlayedBefore()) {
						uuid = p.getUniqueId();
					}
					
				}
				
				if (uuid == null) {
					warning(ChatColor.RED + "Player: \"" + value + "\" not found!", true, player, prevInventoryGUI);
				} else {
					new GUI_RemoveAllAuctionsOfPlayerPolarQuestion(prevInventoryGUI, uuid, worldGroup).open(player);
				}
			}
			
			@Override
			public long getTimeoutInTicks() {
				return 60 * 20;
			}
			
			@Override
			public String[] getPlayersInstructions(Player p) {
				String[] s = new String[1];
				s[0] = "Type the name or the uuid of the player into the chat";
 				return s;
			}
		});
	}

}
