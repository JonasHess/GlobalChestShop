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



	@EventHandler
	public void onNPCLeftClickEvent(NPCLeftClickEvent event) {
		Player player = event.getClicker();
		Integer npcID = event.getNPC().getId();
		String npcName = "Shop";
		try {
			npcName = event.getNPC().getFullName();
		} catch (Exception e) {
		}
		Integer worldGroup;
		try {
			worldGroup = GlobalChestShop.plugin.getworldGroup(player.getLocation());
		} catch (WorldHasNoWorldGroupException e) {
			return;
		}
		String worldName = player.getLocation().getWorld().getName();
		
		List<Shop> shopList;
		try {
			shopList = GlobalChestShop.plugin.getShopVerwaltung().getNpcShops(npcID, event.getClicker());
		} catch (WorldHasNoWorldGroupException e) {
			return;
		}
		if (shopList == null || shopList.isEmpty()) {
			return;
		}
		event.getNPC().setProtected(true);
		event.setCancelled(true);
		
		
		
		if (shopList.size() > 1) {
			new GUI_NpcShopList(npcName, npcID, worldGroup, worldName, null).open(player);
		} else {
			shopList.get(0).onInteractLeftClick(player, null);
		}
		
		event.getNPC().faceLocation(event.getClicker().getLocation());
	}

	@EventHandler
	public void onNPCRightClickEvent(NPCRightClickEvent event) {
		Player player = event.getClicker();
		Integer npcID = event.getNPC().getId();
		String npcName = "Shop";
		try {
			npcName = event.getNPC().getFullName();
		} catch (Exception e) {
		}
		Integer worldGroup;
		try {
			worldGroup = GlobalChestShop.plugin.getworldGroup(player.getLocation());
		} catch (WorldHasNoWorldGroupException e) {
			return;
		}
		String worldName = player.getLocation().getWorld().getName();
		
		List<Shop> shopList;
		try {
			shopList = GlobalChestShop.plugin.getShopVerwaltung().getNpcShops(npcID, event.getClicker());
		} catch (WorldHasNoWorldGroupException e) {
			return;
		}
		if (shopList == null || shopList.isEmpty()) {
			return;
		}
		event.getNPC().setProtected(true);
		event.setCancelled(true);
		
		if (shopList.size() >= 2) {
			
			new GUI_NpcShopList(npcName, npcID, worldGroup, worldName, null).open(player);
		} else if (shopList.size() == 1){
			if (player.isSneaking()) {
				shopList.get(0).onInteractLeftClick(player, null);
			} else {
				shopList.get(0).onInteractRightClick(player, null);
			}
		} else {
			return;
		}
		
		
		event.getNPC().faceLocation(event.getClicker().getLocation());
	}
}
