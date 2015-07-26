package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects;

import java.awt.Point;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;

public abstract class ButtonEffect {


	protected Point finalPoint;
	
	
	public ButtonEffect(int x, int y) {
		super();
		this.finalPoint = new Point(x,y);
	}

	
	public abstract boolean doesApperienceChangeWithThisTick(int tickCount);
	
	public abstract Point getButtonLocation(int tickCount) ;

	public abstract Button getAnimatedButton(int tickCount, Button button) ;
	
	

	
	
}
