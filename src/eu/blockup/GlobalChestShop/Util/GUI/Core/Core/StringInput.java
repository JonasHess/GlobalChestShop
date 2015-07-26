package eu.blockup.GlobalChestShop.Util.GUI.Core.Core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.GUI.Core.GUIs.InventoryGUI;

public abstract class StringInput {


  private int inputId = 0;
  private InventoryGUI prevInventoryGUI;


  public void start(final Player player) {
    // if (Epicraft.API.is_Player_inFight(player)) {
    // player.sendMessage(ChatColor.RED + "Can not be opened while fighting");
    // return;
    // }
    if (this.shouldPlayerBeBlindWhileWriting())
      Bukkit.getScheduler().runTask(GlobalChestShop.plugin, new Runnable() {

        @Override
        public void run() {
          player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) getTimeoutInTicks(), 1));
        }
      });

    GlobalChestShop.plugin.getGuiCore().hashMap_String_Input_Awaiting.put(player.getUniqueId(), this);

    this.prevInventoryGUI = GlobalChestShop.plugin.getGuiCore().getPlayersOpenedInventoryGui(player);
    if (prevInventoryGUI != null)
      prevInventoryGUI.close(player);
    for (int i = 0; i < 10; i++)
      player.sendMessage("");
    String[] promtArray = this.getPlayersInstructions(player);
    for (String message : promtArray) {
      player.sendMessage(message);
    }

    // Start Timeout
    final Player playerFinal = player;
    final StringInput button = this;
    final int inputIdFinal = ++inputId;
    GlobalChestShop.plugin.getServer().getScheduler().scheduleSyncDelayedTask(GlobalChestShop.plugin, new Runnable() {
      public void run() {
        button.onTimeout(inputIdFinal, playerFinal);
      }
    }, this.getTimeoutInTicks());
  }

  public void onTimeout(int inputId, final Player player) {
    if (inputId != this.inputId)
      return; // Timeout was initialized by previous call and will be ignored
    GlobalChestShop.plugin.getGuiCore().hashMap_String_Input_Awaiting.remove(player.getUniqueId());
    if (this.shouldPlayerBeBlindWhileWriting())
      Bukkit.getScheduler().runTask(GlobalChestShop.plugin, new Runnable() {

        @Override
        public void run() {
          player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
      });

    onTimeout(this.prevInventoryGUI, player);
  }


  public void onPlayerMove(PlayerMoveEvent e) {
    if (this.shouldPlayerBeUnableToMoveWhileWriting()) {
      e.setCancelled(true);
      e.getPlayer().teleport(e.getFrom());
      e.getPlayer().sendMessage(this.getPlayersInstructions(e.getPlayer()));
    }
  }

  public void onPlayerChatEvent(AsyncPlayerChatEvent e) {
    GlobalChestShop.plugin.getGuiCore().hashMap_String_Input_Awaiting.remove(e.getPlayer().getUniqueId());
    this.inputId++; // Disables Timeout;
    if (this.shouldPlayerBeBlindWhileWriting())
      e.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
    this.onPlayerTypedString(prevInventoryGUI, e.getPlayer(), e.getMessage());
    e.setCancelled(true);
  }



  protected abstract void onPlayerTypedString(InventoryGUI parentGUI, Player player, String value);

  protected abstract void onTimeout(InventoryGUI prevInventoryGUI, Player player);

  public abstract String[] getPlayersInstructions(Player p);

  public abstract long getTimeoutInTicks();

  public abstract boolean shouldPlayerBeBlindWhileWriting();

  public abstract boolean shouldPlayerBeUnableToMoveWhileWriting();

}
