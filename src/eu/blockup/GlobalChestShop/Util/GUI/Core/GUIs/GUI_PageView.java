package eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;



class Chest_Page<T> {
  public List<T> listOfItems;
  static int amount_of_items_fitting__in_one_page = 27;

  Chest_Page(List<T> listOfItems, int index_of_this_page, int max_count_of_pages) {
    super();
    this.listOfItems = listOfItems;
    
  }
}

public abstract class GUI_PageView<T> extends InventoryGUI {

  public int pagenumber;
  protected List<T> objectList;
  
  public GUI_PageView(String title, ItemStack displayItem, int seitenNummer, InventoryGUI parentGUI) {
    super(title, 6, displayItem, parentGUI);
    this.pagenumber = seitenNummer;
    if (this.pagenumber < 1)
      this.pagenumber = 1;
    this.objectList = null; // Will be initialised on first load;
  }

  public abstract List<T> getRefreshedObjectList();
  public abstract boolean shouldObjectListBeRefreshedAutomatically();
  public abstract void drawAditionalButtons(Player player);
  
  public abstract Button convertListObjectToButton(T obj, Player player);
  
  public void reloadObjectList() {
    this.objectList = getRefreshedObjectList();
  }
  
  public int get_PageCount_of_List(List<T> list) {
    int listsize = list.size();
    if (listsize > 0) {
      double exact_number_of_pages = (double) listsize / Chest_Page.amount_of_items_fitting__in_one_page;
      double doublePageCount = (double) exact_number_of_pages % Chest_Page.amount_of_items_fitting__in_one_page;
      	if (doublePageCount % 1.0D == 0.0D) return (int) exact_number_of_pages;
        return ((int) exact_number_of_pages) + 1;
    } else {
      return 1;
    }
  }
  
  


  @Override
  public void onPlayerReturnsToThisGUI(Player player, InventoryGUI predecessorGUI) {
    if (this.shouldObjectListBeRefreshedAutomatically()) {
      this.reloadObjectList();
      this.redrawAllButtons(player);
    }
  }

  public Chest_Page<T> get_Page(List<T> list, int pagenumber) {

    int validePagenumber = Math.min(this.get_PageCount_of_List(objectList), pagenumber);
    int list_Size = list.size();
    int page_Size = Chest_Page.amount_of_items_fitting__in_one_page;

    List<T> resultList = new ArrayList<T>();

    for (int i = 0; i < page_Size; i++) {
      int position_in_List = i + (validePagenumber - 1) * page_Size;

      if (position_in_List < list_Size) {
    	  try {
    		  resultList.add(list.get(position_in_List));
    	  } catch (IndexOutOfBoundsException e) {
    		  // TODO find out why this can happen sometimes!
    	  }
      }
    }
    return new Chest_Page<T>(resultList, validePagenumber, get_PageCount_of_List(list));
  }
  

  @Override
  protected void drawButtons(Player player) {
    if (this.objectList == null) {
      this.reloadObjectList();
    }
    draw_ButtonList(player);

    draw_NavigationButtons();

    drawAditionalButtons(player);
  }
  

  
  public void draw_ButtonList(Player player) {
    Chest_Page<T> page = get_Page(this.objectList, this.pagenumber);
    int amount_of_items = page.listOfItems.size();
    int itemsAddedItems = 0;

    for (int y = 2; y < 5; y++) {
      for (int x = 0; x < 9; x++) {

        this.eraseButton(x, y);
        if (itemsAddedItems < amount_of_items) {
          if (page.listOfItems.get(itemsAddedItems) != null)
            this.drawButton(x, y, this.convertListObjectToButton(page.listOfItems.get(itemsAddedItems), player));
          itemsAddedItems++;
        }
      }
    }
  }

  
  protected Button_Bare getCurrentPageButton(int pageNumber) {
    return new Button_Bare(new ItemStack(Material.BOOK, pageNumber), GlobalChestShop.text.get(GlobalChestShop.text.PageView_CurrentPage, String.valueOf(pageNumber)));
  }


  protected Button_Bare getSinglePageButton(int pageNumber, boolean firstPage,  boolean previousPage,  boolean nextPage, boolean lastPage) {
    String title = GlobalChestShop.text.get(GlobalChestShop.text.PageView_SinglePage, String.valueOf(pageNumber));
    if (firstPage) title = GlobalChestShop.text.get(GlobalChestShop.text.PageView_FirstPage);
    if (lastPage) title = GlobalChestShop.text.get(GlobalChestShop.text.PageView_LastPage);
    return new Button_Bare(new ItemStack(Material.PAPER, pageNumber), title);
  }
  
  private void draw_NavigationButtons() {

    final int maxPages = get_PageCount_of_List(this.objectList);

    if (maxPages > 1) {
      int y_Row_Page_Iterator = this.getHeight() - 1;


      // Current Page
      this.drawButton(4, this.getHeight() - 1,  this.getCurrentPageButton(Math.min(this.pagenumber, 64)));
//      this.drawButton(4, this.getHeight() - 1, new Button_Bare(new ItemStack(Material.BOOK, Math.min(this.pagenumber, 64)), "Page: " + this.pagenumber, ""));

      // Next Page
      if ((maxPages > pagenumber)) {
          this.drawButton(5, y_Row_Page_Iterator, new Button(this.getSinglePageButton(Math.min(pagenumber + 1, 64), false, false, true, false)) {

          @Override
          public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
            ((GUI_PageView<?>) inventoryGUI).pagenumber = pagenumber + 1;
            inventoryGUI.resetAllButtons(player);
            inventoryGUI.refresh(player);
          }

          @Override
          public void onRefresh(InventoryGUI inventoryGUI, Player player) {
            
          }

  		@Override
  		protected Sound getClickSound(ClickType type) {
  			return getNextPageSound();
  		}
        });

      }

      // Last Page
      if (maxPages > pagenumber + 1) {
          this.drawButton(6, y_Row_Page_Iterator, new Button(this.getSinglePageButton( Math.min(maxPages, 64), false, false, false, true)) {

          @Override
          public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
            ((GUI_PageView<?>) inventoryGUI).pagenumber = maxPages;
            inventoryGUI.resetAllButtons(player);
            inventoryGUI.refresh(player);
          }

          @Override
          public void onRefresh(InventoryGUI inventoryGUI, Player player) {
            
          }

  		@Override
  		protected Sound getClickSound(ClickType type) {
  			return getNextPageSound();
  		}
        });
      }


      // previous Page
      if (pagenumber > 1) {
        this.drawButton(3, y_Row_Page_Iterator, new Button(this.getSinglePageButton(Math.min(pagenumber - 1, 64), false, true, false, false)) {

          @Override
          public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
            ((GUI_PageView<?>) inventoryGUI).pagenumber = pagenumber - 1;
            inventoryGUI.resetAllButtons(player);
            inventoryGUI.refresh(player);
          }

          @Override
          public void onRefresh(InventoryGUI inventoryGUI, Player player) {
          }

  		@Override
  		protected Sound getClickSound(ClickType type) {
  			return getNextPageSound();
  		}
        });

      }


      // First Page
      if (pagenumber > 2) {
        this.drawButton(2, y_Row_Page_Iterator, new Button(this.getSinglePageButton( 1, true, false, false, false)) {

          @Override
          public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
            ((GUI_PageView<?>) inventoryGUI).pagenumber = 1;
            inventoryGUI.resetAllButtons(player);
            inventoryGUI.refresh(player);
          }

          @Override
          public void onRefresh(InventoryGUI inventoryGUI, Player player) {
            
          }

		@Override
		protected Sound getClickSound(ClickType type) {
			return getNextPageSound();
		}
          
          
        });
      }

    }

  }
  
  private Sound getNextPageSound() {
	  return Sound.ENTITY_ENDERDRAGON_FLAP;
  }

  
  public int getPagenumber() {
	return pagenumber;
}


public void setPagenumber(int pagenumber) {
	this.pagenumber = pagenumber;
}


public List<T> getObjectsList() {
	return objectList;
}





}
