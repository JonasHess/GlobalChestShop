package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.entity.Player;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_StateChangerBoolean;



public class Button_BanIItem extends Button_StateChangerBoolean{

	public Button_BanIItem(Player player, BannState bannState) {
		super(bannState, player);
	}

}
