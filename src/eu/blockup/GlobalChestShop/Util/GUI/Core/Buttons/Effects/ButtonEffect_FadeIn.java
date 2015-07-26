package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects;

import java.awt.Point;



import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;

public class ButtonEffect_FadeIn extends ButtonEffect {
	
	private int timespanInTicks;
	private int lastX = -1;
	
	public ButtonEffect_FadeIn(int x, int y, int timespanInTicks) {
		super(x,y);
		this.timespanInTicks = timespanInTicks;
	}

	@Override
	public boolean doesApperienceChangeWithThisTick(int tickCount) {
		boolean result =  tickCount <= (timespanInTicks);
		int newX = -1;
		if (result) {
			newX = getXCoordinate(tickCount);
			result = lastX != newX;
		}
		if (result) {
			lastX = newX;
		}
		return result;
	}
	
	private int getXCoordinate(int tickCount) {
		int x;
		if (tickCount >= timespanInTicks) {
			return this.finalPoint.x;
		} else {
			try {
				x = (int) (tickCount / (int) (this.timespanInTicks / finalPoint.x));
			} catch (ArithmeticException e) {
				x = 0;
			} 
		}
		if (x >= finalPoint.x) {
			return finalPoint.x;
		}
		return x;
	}

	
	
	@Override
	public Point getButtonLocation(int tickCount) {
		return new Point (this.getXCoordinate(tickCount), finalPoint.y);
	}

	@Override
	public Button getAnimatedButton(int tickCount, Button button) {
		if (tickCount < timespanInTicks) {
			return new Button_Bare(button);
		}
		return button;
	}

}
