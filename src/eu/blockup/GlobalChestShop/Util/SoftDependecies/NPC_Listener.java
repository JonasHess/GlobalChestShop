package eu.blockup.GlobalChestShop.Util.SoftDependecies;

import java.util.Collection;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import eu.blockup.GlobalChestShop.GlobalChestShop;
import eu.blockup.GlobalChestShop.Util.Shop;
import eu.blockup.GlobalChestShop.Util.Exceptions.WorldHasNoWorldGroupException;

public class NPC_Listener implements Listener {

	public Integer findNearestNPC(Location loc) {
		try {
			if (loc == null)
				return null;
			double radius = 15.0;
			Integer npcID = null;
			double smallesDistance = 99D;
			Collection<Entity> near = loc.getWorld().getEntities();
			for (Entity e : near) {
				double distance = e.getLocation().distance(loc);
				if (CitizensAPI.getNPCRegistry().isNPC(e) && distance <= radius) {
					if (distance < smallesDistance) {
						npcID = CitizensAPI.getNPCRegistry().getNPC(e).getId();
						smallesDistance = distance;
					}
				}
			}
			return npcID;
		} catch (Exception e) {
			return null;
		}
	}

	private Shop getNpcShop(Integer npcID, Player player) throws WorldHasNoWorldGroupException {
		Location loc = player.getLocation();
		String worldName = loc.getWorld().getName();
		Integer worldGroup = GlobalChestShop.plugin.getworldGroup(loc);
		return GlobalChestShop.plugin.getShopVerwaltung().getNPCShop(npcID, worldGroup, worldName);
	}

	@EventHandler
	public void onNPCLeftClickEvent(NPCLeftClickEvent event) {
		Integer npcID = event.getNPC().getId();
		Shop s;
		try {
			s = this.getNpcShop(npcID, event.getClicker());
		} catch (WorldHasNoWorldGroupException e) {
			return;
		}
		if (s == null) {
			return;
		}
		event.getNPC().setProtected(true);
		event.setCancelled(true);
		Player player = event.getClicker();
		s.onInteractLeftClick(player, null);
		event.getNPC().faceLocation(event.getClicker().getLocation());
	}

	@EventHandler
	public void onNPCRightClickEvent(NPCRightClickEvent event) {
		Integer npcID = event.getNPC().getId();
		Shop s;
		try {
			s = this.getNpcShop(npcID, event.getClicker());
		} catch (WorldHasNoWorldGroupException e) {
			return;
		}
		if (s == null) {
			return;
		}
		event.getNPC().setProtected(true);
		Player player = event.getClicker();
		if (player.isSneaking()) {
			s.onInteractLeftClick(player, null);
		} else {
			s.onInteractRightClick(player, null);
		}
		event.getNPC().faceLocation(event.getClicker().getLocation());
	}
}
