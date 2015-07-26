package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects;

import java.awt.Point;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;

public class AnimatedButton {
	private Button button;
	private ButtonEffect effect;
	
	
	public AnimatedButton(Button button, ButtonEffect effect) {
		super();
		this.button = button;
		this.effect = effect;
	}
	
	public Button getAnimatedButton( int tickCount) {
		return this.effect.getAnimatedButton(tickCount, this.button);
	}
	
	public Point getPosition(int tickCount) {
		return this.effect.getButtonLocation(tickCount);
	}
	public void setButton(Button button) {
		this.button = button;
	}
	public ButtonEffect getEffect() {
		return effect;
	}
	public void setEffect(ButtonEffect effect) {
		this.effect = effect;
	}
	
	
	
	
	
	
}
