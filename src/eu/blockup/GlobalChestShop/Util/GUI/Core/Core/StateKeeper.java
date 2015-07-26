package eu.blockup.GlobalChestShop.Util.GUI.Core.Core;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Interfaces.StateObserver;

public abstract class StateKeeper<T> {

  private LinkedList<StateObserver> observerList;

  public StateKeeper() {
    this.observerList = new LinkedList<StateObserver>();
  }


  protected abstract void onMofiyState(T offsetValue, Player player);

  protected abstract void onSetState(T absoluteValue, Player player);

  public abstract T getCurrentState(Player player);

  public abstract Button_Bare formatToDisplayButton(Player player, int amount);
  
  public abstract List<String> stateToString ( int amount);

  public void mofiyState(T offsetValue, Player player) {
    this.onMofiyState(offsetValue, player);
    this.informGUIsAboutStateChange(this.getCurrentState(player), player);
  }

  public void setState(T absoluteValue, Player player) {
    this.onSetState(absoluteValue, player);
    this.informGUIsAboutStateChange(this.getCurrentState(player), player);
  }

  public void informGUIsAboutStateChange(T newState, Player player) {
    for (StateObserver s : this.observerList) {
      s.updateOnStateChange(player);
    }
  }

  public void registerforStateChangeNotification(StateObserver s) {
    this.observerList.add(s);
  }

  public void unRegisterGUIforStateChangeNotification(StateObserver s) {
    this.observerList.remove(s);
  }

}
