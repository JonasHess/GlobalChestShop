package eu.blockup.GlobalChestShop.Util.Experimental;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemStackUtil {

	@SuppressWarnings("deprecation")
	public static ItemStack toItemStack(String s) {
		if (s.equalsIgnoreCase("null")) {
			return new ItemStack(Material.AIR);
		}
		if (isInt(s)) {
			return new ItemStack(Material.getMaterial(Integer.parseInt(s)));
		}
		byte meta = 0;
		int amount = 1;
		Material m;
		String imeta = "";

		if (s.indexOf(":") > -1) {
			m = Material.getMaterial(s.substring(0, s.indexOf(":")).toUpperCase());
			if (s.indexOf("@") > -1) {
				meta = Byte.parseByte(s.substring(s.indexOf(":") + 1, s.indexOf("@")));
				if (s.indexOf("$") > -1) {
					amount = Integer.parseInt(s.substring(s.indexOf("@") + 1), s.indexOf("$"));
					imeta = s.substring(s.indexOf("$") + 1);
				} else {
					amount = Integer.parseInt(s.substring(s.indexOf("@") + 1));
				}
			} else {
				if (s.indexOf("$") > s.indexOf(":")) {
					meta = Byte.parseByte(s.substring(s.indexOf(":") + 1, s.indexOf("$")));
					imeta = s.substring(s.indexOf("$") + 1);
				} else {
					meta = Byte.parseByte(s.substring(s.indexOf(":") + 1));
				}
			}
		} else {
			if (s.indexOf("@") > -1) {
				m = Material.getMaterial(s.substring(0, s.indexOf("@")).toUpperCase());
				if (s.indexOf("$") > -1) {
					amount = Integer.parseInt(s.substring(s.indexOf("@") + 1), s.indexOf("$"));
					imeta = s.substring(s.indexOf("$") + 1);
				} else {
					amount = Integer.parseInt(s.substring(s.indexOf("@") + 1));
				}
			} else {
				if (s.indexOf("$") > -1) {
					m = Material.getMaterial(s.substring(0, s.indexOf("$")).toUpperCase());
					imeta = s.substring(s.indexOf("$") + 1);
				} else {
					m = Material.getMaterial(s.toUpperCase());
				}
			}
		}

		ItemStack is = new ItemStack(m, amount);
		MaterialData md = new MaterialData(m);
		md.setData(meta);
		is.setData(md);
		is.setDurability((short) meta);

		if (!imeta.isEmpty()) {
			is.setItemMeta(ItemMetaUtil.toItemMeta(imeta, is.getItemMeta()));
		}

		return is;
	}

	@SuppressWarnings("deprecation")
	public static String toString(ItemStack is) {
		if (is == null) {
			return "null";
		}
		String s = "";
		int amount = is.getAmount();
		byte meta = is.getData().getData();
		Material m = is.getType();
		s = s + m.toString().toUpperCase();
		if (meta > 0) {
			s = s + ":" + String.valueOf(meta);
		}
		if (amount > 1) {
			s = s + "@" + String.valueOf(amount);
		}

		if (is.hasItemMeta()) {
			s = s + "$" + ItemMetaUtil.toString(is.getItemMeta());
		}

		return s;
	}

	public static class ItemMetaUtil {

		public static ItemMeta toItemMeta(String s, ItemMeta ime) {
			String m = s;
			if (s.startsWith("leather")) {
				m = m.substring(7);
				if (m.startsWith("~")) {
					String dl = m.substring(1, m.indexOf("°"));
					List<String> lore = new ArrayList<String>();
					if (!dl.isEmpty()) {
						if (dl.lastIndexOf("#") > -1) {
							boolean dnused = false;
							for (String l : dl.split("#")) {
								if (l.startsWith("?") && dnused == false) {
									dnused = true;
									ime.setDisplayName(l.substring(1));
								} else {
									lore.add(l);
								}
							}
							ime.setLore(lore);
						} else {
							if (dl.startsWith("?")) {
								ime.setDisplayName(dl.substring(1));
							} else {
								lore.add(dl);
								ime.setLore(lore);
							}
						}
					}
				}
				m = m.substring(m.indexOf("°") + 1);
				LeatherArmorMeta lam = (LeatherArmorMeta) ime;
				lam.setColor(Color.fromRGB(Integer.parseInt(m.split(",")[0]), Integer.parseInt(m.split(",")[1]), Integer.parseInt(m.split(",")[2])));
				return lam;
			} else if (s.startsWith("skull")) {
				m = m.substring(5);
				if (m.startsWith("~")) {
					String dl = m.substring(1, m.indexOf("°"));
					List<String> lore = new ArrayList<String>();
					if (!dl.isEmpty()) {
						if (dl.lastIndexOf("#") > -1) {
							boolean dnused = false;
							for (String l : dl.split("#")) {
								if (l.startsWith("?") && dnused == false) {
									dnused = true;
									ime.setDisplayName(l.substring(1));
								} else {
									lore.add(l);
								}
							}
							ime.setLore(lore);
						} else {
							if (dl.startsWith("?")) {
								ime.setDisplayName(dl.substring(1));
							} else {
								lore.add(dl);
								ime.setLore(lore);
							}
						}
					}
				}
				m = m.substring(m.indexOf("°") + 1);
				SkullMeta sm = (SkullMeta) ime;
				sm.setOwner(m);
				return sm;
			} else if (s.startsWith("book")) {
				m = m.substring(4);
				if (m.startsWith("~")) {
					String dl = m.substring(1, m.indexOf("°"));
					List<String> lore = new ArrayList<String>();
					if (!dl.isEmpty()) {
						if (dl.lastIndexOf("#") > -1) {
							boolean dnused = false;
							for (String l : dl.split("#")) {
								if (l.startsWith("?") && dnused == false) {
									dnused = true;
									ime.setDisplayName(l.substring(1));
								} else {
									lore.add(l);
								}
							}
							ime.setLore(lore);
						} else {
							if (dl.startsWith("?")) {
								ime.setDisplayName(dl.substring(1));
							} else {
								lore.add(dl);
								ime.setLore(lore);
							}
						}
					}
				}
				m = m.substring(m.indexOf("°") + 1);
				BookMeta bm = (BookMeta) ime;
				bm.setAuthor(m.split("%")[0]);
				bm.setTitle(m.split("%")[1]);
				for (int i = 2; i < m.split("%").length; i++) {
					bm.addPage(m.split("%")[i]);
				}
			} else if (s.startsWith("potion")) {
				m = m.substring(6);
				if (m.startsWith("~")) {
					String dl = m.substring(1, m.indexOf("°"));
					List<String> lore = new ArrayList<String>();
					if (!dl.isEmpty()) {
						if (dl.lastIndexOf("#") > -1) {
							boolean dnused = false;
							for (String l : dl.split("#")) {
								if (l.startsWith("?") && dnused == false) {
									dnused = true;
									ime.setDisplayName(l.substring(1));
								} else {
									lore.add(l);
								}
							}
							ime.setLore(lore);
						} else {
							if (dl.startsWith("?")) {
								ime.setDisplayName(dl.substring(1));
							} else {
								lore.add(dl);
								ime.setLore(lore);
							}
						}
					}
				}
				m = m.substring(m.indexOf("°") + 1);
				PotionMeta pm = (PotionMeta) ime;
				for (int i = 0; i < m.split("%").length; i++) {
					String pe = m.split("%")[i];
					pm.addCustomEffect(new PotionEffect(PotionEffectType.getByName(pe.split(",")[2]), Integer.parseInt(pe.split(",")[1]), Integer.parseInt(pe.split(",")[0])), false);
				}
			} else {
				if (m.startsWith("~")) {
					m = m.substring(1);
					List<String> lore = new ArrayList<String>();
					if (!m.isEmpty()) {
						if (m.lastIndexOf("#") > -1) {
							boolean dnused = false;
							for (String l : m.split("#")) {
								if (l.startsWith("?") && dnused == false) {
									dnused = true;
									ime.setDisplayName(l.substring(1));
								} else {
									lore.add(l);
								}
							}
							ime.setLore(lore);
						} else {
							if (m.startsWith("?")) {
								ime.setDisplayName(m.substring(1));
							} else {
								lore.add(m);
								ime.setLore(lore);
							}
						}
					}
				}
			}
			return ime;
		}

		public static String toString(ItemMeta im) {
			String s = toItemMetaString(im);
			if (im instanceof BookMeta) {
				s = "book" + s + "°";
				s = s + ((BookMeta) im).getAuthor();
				s = s + "%" + ((BookMeta) im).getTitle();
				for (String p : ((BookMeta) im).getPages()) {
					s = s + "%" + p;
				}
			} else if (im instanceof LeatherArmorMeta) {
				if (((LeatherArmorMeta) im).getColor() != null) {
					s = "leather" + s + "°" + ((LeatherArmorMeta) im).getColor().getRed() + "," + ((LeatherArmorMeta) im).getColor().getGreen() + "," + ((LeatherArmorMeta) im).getColor().getBlue();
				}
			} else if (im instanceof SkullMeta) {
				if (((SkullMeta) im).hasOwner()) {
					s = "skull" + s + "°" + ((SkullMeta) im).getOwner();
				}
			} else if (im instanceof PotionMeta) {
				s = "potion" + s + "°";
				String p = "";
				for (PotionEffect pe : ((PotionMeta) im).getCustomEffects()) {
					p = p + "%" + pe.getAmplifier() + "," + pe.getDuration() + "," + pe.getType().getName();
				}
				p = p.substring(1);
				s = s + p;
			}
			return s;
		}

		private static String toItemMetaString(ItemMeta im) {
			// Displayname und Lore, später Enchantments usw.
			String s = "";
			if (im.hasDisplayName()) {
				s = "~?" + im.getDisplayName();
			}
			if (im.hasLore()) {
				if (!s.startsWith("~")) {
					s = "~" + s;
				}
				String lore = "";
				for (String l : im.getLore()) {
					lore = lore + "#" + l;
				}
				lore = lore.substring(1);
				if (s.startsWith("~?")) {
					s = s + "#" + lore;
				} else {
					s = s + lore;
				}
			}
			return s;
		}

	}

	public static boolean isInt(String s) {
		@SuppressWarnings("unused")
		int is = 0;
		try {
			is = Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		is = 1;
		return true;
	}

}
