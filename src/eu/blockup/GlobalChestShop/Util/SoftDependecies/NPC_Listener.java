package eu.blockup.GlobalChestShop.Util.SoftDependecies;

import java.util.Collection;
import java.util.List;

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
import eu.blockup.GlobalChestShop.Util.GUI.PriceBuilding.GUI_NpcShopList;

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

	private List<Shop> getNpcShops(Integer npcID, Player player) throws WorldHasNoWorldGroupException {
		Location loc = player.getLocation();
		String worldName = loc.getWorld().getName();
		Integer worldGroup = GlobalChestShop.plugin.getworldGroup(loc);
		return GlobalChestShop.plugin.getShopVerwaltung().getNPCShops(npcID, worldGroup, worldName);
	}

	@EventHandler
	public void onNPCLeftClickEvent(NPCLeftClickEvent event) {
		Integer npcID = event.getNPC().getId();
		List<Shop> s;
		try {
			s = this.getNpcShops(npcID, event.getClicker());
		} catch (WorldHasNoWorldGroupException e) {
			return;
		}
		if (s == null || s.isEmpty()) {
			return;
		}
		event.getNPC().setProtected(true);
		event.setCancelled(true);
		Player player = event.getClicker();
		
		if (s.size() > 1) {
			new GUI_NpcShopList(event.getNPC().getFullName(), s, null).open(player);
		} else {
			s.get(0).onInteractLeftClick(player, null);
		}
		
		event.getNPC().faceLocation(event.getClicker().getLocation());
	}

	@EventHandler
	public void onNPCRightClickEvent(NPCRightClickEvent event) {
		Integer npcID = event.getNPC().getId();
		List<Shop> s;
		try {
			s = this.getNpcShops(npcID, event.getClicker());
		} catch (WorldHasNoWorldGroupException e) {
			return;
		}
		if (s == null || s.isEmpty()) {
			return;
		}
		event.getNPC().setProtected(true);
		Player player = event.getClicker();
		
		if (s.size() >= 2) {
			String npcName = "Shop";
			try {
				npcName = event.getNPC().getFullName();
			} catch (Exception e) {
			}
			new GUI_NpcShopList(npcName, s, null).open(player);
		} else if (s.size() == 1){
			if (player.isSneaking()) {
				s.get(0).onInteractLeftClick(player, null);
			} else {
				s.get(0).onInteractRightClick(player, null);
			}
		} else {
			return;
		}
		
		
		event.getNPC().faceLocation(event.getClicker().getLocation());
	}
}
