package eu.blockup.GlobalChestShop.Util.GUI;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button.ClickType;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.GUI_PageView;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public class GUI_Search extends GUI_PageView<ItemStack>{

  private String searchedItemString;
  private Integer worldGroup;
  private boolean adminShopsOnly;
  private boolean newAuctions;
  private double multiplier;
  
  public GUI_Search(String searchedItemString, InventoryGUI parentGUI, Integer worldGorup, boolean adminShopsOnly, boolean newAuctions, double multiplier) {
    super(GlobalChestShop.text.get(GlobalChestShop.text.GUI_Title_GUI_Search,searchedItemString), new ItemStack(Material.HOPPER), 1, parentGUI);
    this.worldGroup = worldGorup;
    this.searchedItemString = searchedItemString;
    this.adminShopsOnly = adminShopsOnly;
    this.newAuctions = newAuctions;
    this.multiplier = multiplier;
  }

  @Override
  public List<ItemStack> getRefreshedObjectList() {
    return GlobalChestShop.plugin.getDefaultCategoryController(worldGroup).getAllItemsFromAllCategoriesFilteresByString(searchedItemString);
  }

  @Override
  public boolean shouldObjectListBeRefreshedAutomatically() {
    return false;
  }

  @Override
  public void drawAditionalButtons(Player player) {
    
  }

  @Override
  public Button convertListObjectToButton(ItemStack obj, Player player) {
    return new Button_AuctionList(obj, this.adminShopsOnly, this.worldGroup, this.newAuctions, false, multiplier);
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
