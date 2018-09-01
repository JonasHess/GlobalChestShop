package eu.blockup.GlobalChestShop.Util.GUI.Core.Core;

import java.util.ArrayList;
import java.util.List;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public abstract class TextInput {

  private String name;
  private List<String> pages = new ArrayList<String>();
  
  private String startMessage = null;
  
  public TextInput(String name) {
      this.name = name;
  }
  
  public TextInput(String name, String... pages) {
      this(name);
      this.setPages(pages);
  }
  
  public TextInput setName(String name) {
      this.name = name;
      return this;
  }
  
  public String getName() {
      return this.name;
  }
  
  public TextInput clearPages() {
      this.pages.clear();
      return this;
  }
  
  public TextInput setPages(String... pages) {
      this.clearPages();
      for (String page : pages) {
          this.pages.add(page);
      }
      return this;
  }
  
  public TextInput setPage(int index, String page) {
      while (this.pages.size() <= index) {
          this.addPage("");
      }
      
      return this;
  }
  
  public TextInput addPage(String page) {
      this.pages.add(page);
      return this;
  }
  
  public String[] getPages() {
      return this.pages.toArray(new String[this.pages.size()]);
  }
  
  public TextInput setStartMessage(String startMessage) {
      this.startMessage = startMessage;
      return this;
  }
  
  public String getStartMessage() {
      return this.startMessage;
  }
  
  public boolean hasStartMessage() {
      return this.getStartMessage() != null;
  }
  
  public ItemStack toItemStack() {
      ItemStack item = new ItemStack(XMaterial.WRITABLE_BOOK.parseItem());
      BookMeta meta = (BookMeta) item.getItemMeta();
      meta.setDisplayName(this.getName());
      if (this.pages.isEmpty()) {
          meta.setPages("");
      } else {
          meta.setPages(this.pages);
      }
      item.setItemMeta(meta);
      return item;
  }
  
  public abstract void onSend(Player player, String title, String[] content);
}
