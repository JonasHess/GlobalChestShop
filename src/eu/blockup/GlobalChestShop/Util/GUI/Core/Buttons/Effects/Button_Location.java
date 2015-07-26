package eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Effects;

import java.awt.Point;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;

public class Button_Location {

	private Point position;
	private Button button;
	
	
	
	
	
	public Button_Location(Point position, Button button) {
		super();
		this.position = position;
		this.button = button;
	}
	
	
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public Button getButton() {
		return button;
	}
	public void setButton(Button button) {
		this.button = button;
	}
	
	
	
}
