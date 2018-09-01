package eu.blockup.GlobalChestShop.Util.GUI;

import eu.blockup.GlobalChestShop.Util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.SimpleIInventoryGUI;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;

public class GUI_CreateShop1 extends SimpleIInventoryGUI{
  public ShopInfoPack infoPack;
  public int worldGroup;
  
  public GUI_CreateShop1(ShopInfoPack infoPack, InventoryGUI parentInventoryGUI, int worldGroup) {
    super(ChatColor.DARK_GREEN + "Choose a shop type", 4, new ItemStack(Material.ENDER_CHEST), parentInventoryGUI);
    this.infoPack = infoPack;
    this.worldGroup = worldGroup;
    
  }

  @Override
  protected void drawButtons(Player player) {
    
    
    
    // Sign Shop
    this.drawButton(2, 2, new Button(new ItemStack(Material.SIGN), "SignShop") {
      
      private boolean check(InventoryGUI inventoryGUI) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        ShopInfoPack infoPacks = gui.infoPack;
        return (infoPacks.getSignLocation()!= null);
      }
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        if (check(inventoryGUI)) {
          this.setAmount(1);
        } else {
          this.setAmount(0);
        }
      }
      
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        if (!check(inventoryGUI)) {
          player.sendMessage("No Sign found");
          return;
        }
        ShopInfoPack infoPack = gui.infoPack.clone();
        infoPack.setShopTyp(EShopTyp.GlobalSignShop);
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop2_0(infoPack, inventoryGUI));
      }
    });
    // Chest Shop
    this.drawButton(3, 2, new Button(new ItemStack(Material.ENDER_CHEST), "EnderChest Shop") {
      
      private boolean check(InventoryGUI inventoryGUI) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        ShopInfoPack infoPacks = gui.infoPack;
        return (infoPacks.getSignLocation()!= null && infoPacks.getChestLocation() != null);
      }
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        if (check(inventoryGUI)) {
          this.setAmount(1);
        } else {
          this.setAmount(0);
        }
      }
      
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        if (!check(inventoryGUI)) {
          player.sendMessage(ChatColor.RED + "This requires an EnderChest 1 block away from the sign");
          return;
        }
        ShopInfoPack infoPack = gui.infoPack.clone();
        infoPack.setShopTyp(EShopTyp.GlobalChestShop);
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop2_0(infoPack, inventoryGUI));
        gui.returnToParentGUI(player);
      }
    });
    // ItemFrame Shop
    this.drawButton(4, 2, new Button(new ItemStack(Material.ITEM_FRAME), "ItemFrame Shop") {
      
      private boolean check(InventoryGUI inventoryGUI) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        ShopInfoPack infoPacks = gui.infoPack;
        return (infoPacks.getSignLocation()!= null && gui.infoPack.getItemFrameLocation() != null);
      }
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        if (check(inventoryGUI)) {
          this.setAmount(1);
        } else {
          this.setAmount(0);
        }
      }
      
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        if (!check(inventoryGUI)) {
          player.sendMessage(ChatColor.RED +"This requires an ItemFram above or below the sign");
          return;
        }
        ShopInfoPack infoPack = gui.infoPack.clone();
        infoPack.setShopTyp(EShopTyp.GlobalItemframeShop);
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop2_0(infoPack, inventoryGUI));
        gui.returnToParentGUI(player);
      }
    });
 // Holo Shop
    this.drawButton(5, 2, new Button(new ItemStack(Material.BEACON), "Holo Shop") {
      
      private boolean check(InventoryGUI inventoryGUI) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        ShopInfoPack infoPacks = gui.infoPack;
        return (GlobalChestShop.plugin.isHolographicDisplaysEnabled() && infoPacks.getSignLocation()!= null);
      }
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        if (check(inventoryGUI)) {
          this.setAmount(1);
        } else {
          this.setAmount(0);
        }
      }
      
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        if (!check(inventoryGUI)) {
          player.sendMessage(ChatColor.RED +"This requires an installation of the HolographicDisplays plugin!");
          return;
        }
        ShopInfoPack infoPack = gui.infoPack.clone();
        infoPack.setShopTyp(EShopTyp.GlobalHoloShop);
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop2_0(infoPack, inventoryGUI));
        gui.returnToParentGUI(player);
      }
    });
    
    // NPC Shop
    this.drawButton(6, 2, new Button(new ItemStack(XMaterial.GHAST_SPAWN_EGG.parseMaterial()), "NPC Shop") {
      
      private boolean check(InventoryGUI inventoryGUI) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        ShopInfoPack infoPacks = gui.infoPack;
        return (GlobalChestShop.plugin.isCitezensEnabled() && infoPacks.getSignLocation()!= null && infoPacks.getNpcID() != null);
      }
      
      @Override
      public void onRefresh(InventoryGUI inventoryGUI, Player player) {
        if (check(inventoryGUI)) {
          this.setAmount(1);
        } else {
          this.setAmount(0);
        }
      }
      
      @Override
      public void onButtonClick(InventoryGUI inventoryGUI, Player player, ItemStack cursor, ItemStack current, ClickType type, InventoryClickEvent event) {
        GUI_CreateShop1 gui = (GUI_CreateShop1)inventoryGUI;
        if (!check(inventoryGUI)) {
          player.sendMessage(ChatColor.GOLD + "__________________________________________");
          player.sendMessage(ChatColor.RED +"This requires an installation of the Citizens plugin!");
          player.sendMessage(ChatColor.BLUE +"If you have installed this dependency, try again but this time spawn a NPC next to the sign first");
          player.sendMessage(ChatColor.BLUE +"Example: " + ChatColor.AQUA + "/npc create myNPC --type villager");
          player.sendMessage(ChatColor.GOLD + "__________________________________________");
          return;
        }
        ShopInfoPack infoPack = gui.infoPack.clone();
        infoPack.setShopTyp(EShopTyp.GlobalNpcShop);
        GlobalChestShop.plugin.getGuiCore().open_InventoyGUI(player, new GUI_CreateShop2_0(infoPack, inventoryGUI));
        gui.returnToParentGUI(player);
      }
    });

  }

}
