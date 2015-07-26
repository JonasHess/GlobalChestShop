package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects;

import java.awt.Point;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;

public class ButtonEffect_FlashRed extends ButtonEffect{

	public ButtonEffect_FlashRed(int x, int y) {
		super(x, y);
	}

	@Override
	public boolean doesApperienceChangeWithThisTick(int tickCount) {
		return (tickCount % 10) == 0;
	}

	@Override
	public Point getButtonLocation(int tickCount) {
		return this.finalPoint;
	}

	@Override
	public Button getAnimatedButton(int tickCount, Button button) {
		if ((tickCount % 20) == 0) {
			return new Button_Bare(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), button.getTitle(), button.getDescription());
		}
		return button;
	}

}
