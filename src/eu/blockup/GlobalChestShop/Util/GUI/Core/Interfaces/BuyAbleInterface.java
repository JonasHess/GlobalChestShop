package eu.blockup.GlobalChestShop.Util.GUI.Core.Interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.blockup.GlobalChestShop.Util.GUI.Core.Buttons.Button_Bare;
import net.milkbowl.vault.economy.Economy;

public interface BuyAbleInterface {
	public void buy(Player player, Economy econ, int amount);
	public void sell(Player player, Economy econ, int amount);
	public void sellAll(Player player, Economy econ, int amount);
	public Button_Bare getItemStackAsButton();
	public ItemStack getItemStack(int amount);
	public int getMaxAmount();
	public double getShopToPlayerPrice(int amount);
	public double getPlayerToShopPrice(int amount);
	public Button_Bare getBuyButton(int amount, Player player);
	public Button_Bare getSellButton(int amount, Player player);
	public Button_Bare getSellAllButton(Player player);
	public int getAmountOfItemInInventory(Player player);
}
