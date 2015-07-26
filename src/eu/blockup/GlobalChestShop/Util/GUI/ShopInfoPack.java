package eu.blockup.GlobalChestShop.Util.GUI;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.CustomCategory;
import eu.blockup.GlobalChestShop.Util.Statements.EShopTyp;



public class ShopInfoPack {
  public Integer worldGroup;
  public Location playerLocatioin;
  public Location signLocation;
  public Location chestLocation;
  public Location itemFrameLocation;
  private CustomCategory customCategory;
  private ItemStack itemStack;
  private Integer npcID;
  private Boolean adminShopOnly = false;
  private EShopTyp eShopTyp;
  private Boolean holo = false;
  private boolean newAuctions = false;
  private boolean sellAll = false;
  private int appearance = 0;



  public ShopInfoPack(EShopTyp eShopTyp, Integer worldGroup, Location playerLocatioin, Location signLocation, Location chestLocation, Location itemFrameLocation, Integer npcID) {
    super();
    this.eShopTyp = eShopTyp;
    this.playerLocatioin = playerLocatioin;
    this.signLocation = signLocation;
    this.chestLocation = chestLocation;
    this.itemFrameLocation = itemFrameLocation;
    this.worldGroup = worldGroup;
    this.npcID = npcID;
  }




  public ShopInfoPack(Integer worldGroup, Location playerLocatioin, Location signLocation, Location chestLocation, Location itemFrameLocation, CustomCategory customCategory, ItemStack itemStack, Integer npcID, Boolean adminShopOnly, EShopTyp eShopTyp, Boolean holo, boolean newAuctions, boolean sellAll, int appearance) {
    super();
    this.worldGroup = worldGroup;
    this.playerLocatioin = playerLocatioin;
    this.signLocation = signLocation;
    this.chestLocation = chestLocation;
    this.itemFrameLocation = itemFrameLocation;
    this.customCategory = customCategory;
    this.itemStack = itemStack;
    this.npcID = npcID;
    this.adminShopOnly = adminShopOnly;
    this.eShopTyp = eShopTyp;
    this.holo = holo;
    this.newAuctions = newAuctions;
    this.sellAll = sellAll;
    this.appearance = appearance;
  }


  
  public int getAppearance() {
    return appearance;
  }


  public void setAppearance(int appearance) {
    this.appearance = appearance;
  }


  public boolean isNewAuctions() {
    return newAuctions;
  }


  public void setNewAuctions(boolean newAuctions) {
    this.newAuctions = newAuctions;
  }

  public ShopInfoPack clone() {
    return new ShopInfoPack(worldGroup, playerLocatioin, signLocation, chestLocation, itemFrameLocation, customCategory, itemStack, npcID,
        adminShopOnly, eShopTyp, holo, newAuctions, sellAll, appearance);
  }

  
  
  public boolean isSellAll() {
    return sellAll;
  }


  public void setSellAll(boolean sellAll) {
    this.sellAll = sellAll;
  }


  public Boolean getHolo() {
    return holo;
  }
  
  public void setHolo(Boolean holo) {
    this.holo = holo;
  }
  


  public Integer getNpcID() {
    return npcID;
  }

  public void setNpcID(Integer npcID) {
    this.npcID = npcID;
  }

  public EShopTyp getShopTyp() {
    return eShopTyp;
  }

  public void setShopTyp(EShopTyp eShopTyp) {
    this.eShopTyp = eShopTyp;
  }

  public Integer getworldGroup() {
    return worldGroup;
  }

  public void setworldGroup(Integer worldGroup) {
    this.worldGroup = worldGroup;
  }

  public Location getPlayerLocatioin() {
    return playerLocatioin;
  }

  public void setPlayerLocatioin(Location playerLocatioin) {
    this.playerLocatioin = playerLocatioin;
  }

  public Location getSignLocation() {
    return signLocation;
  }

  public void setSignLocation(Location signLocation) {
    this.signLocation = signLocation;
  }

  public Location getChestLocation() {
    return chestLocation;
  }

  public void setChestLocation(Location chestLocation) {
    this.chestLocation = chestLocation;
  }

  public Location getItemFrameLocation() {
    return itemFrameLocation;
  }

  public void setItemFrameLocation(Location itemFrameLocation) {
    this.itemFrameLocation = itemFrameLocation;
  }

  public CustomCategory getCategory() {
    return customCategory;
  }

  public Integer getCategoryID() {
    if (getCategory() == null)
      return null;
    return getCategory().getCategoryID();
  }

  public void setCategory(CustomCategory customCategory) {
    this.customCategory = customCategory;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public void setItemStack(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  public Boolean getAdminShopOnly() {
    return adminShopOnly;
  }

  public void setAdminShopOnly(Boolean adminShopOnly) {
    this.adminShopOnly = adminShopOnly;
  }



}
