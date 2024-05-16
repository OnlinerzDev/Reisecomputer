package at.onlinerz.reisecomputer.frontend;

import at.onlinerz.reisecomputer.Reisecomputer;
import at.onlinerz.reisecomputer.configuration.PlanetData;
import at.onlinerz.reisecomputer.lib.SkullCreator;
import at.onlinerz.reisecomputer.playerdata.PlanetPermissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainGUI {

    public static void createFor(Player p) {
        Inventory gui = Bukkit.createInventory(null, 6*9, Reisecomputer.guititle);

        for(Map.Entry<String, PlanetData> entry : Reisecomputer.planets.entrySet()) {
            String key = entry.getKey();

            // crawling
            PlanetData planet = entry.getValue();
            int slot = planet.getSlot();
            int cost = planet.getCost();
            Material material = planet.getMaterial();
            Component displayName = planet.getDisplayName();
            List<Component> unspecifiedlore = new ArrayList<>(planet.getLore());
            unspecifiedlore.add(MiniMessage.miniMessage().deserialize(""));
            String servername = planet.getServername();
            String mojangUrl = planet.getMojangUrl();
            Boolean hasPerm = hasPermForPlanet(p.getUniqueId(), key, cost);

            // buiding the item
            ItemStack i;
            if(material == Material.PLAYER_HEAD) {
                i = SkullCreator.itemFromTexture(mojangUrl);
            } else {
                i = new ItemStack(material);
            }
            ItemMeta im = i.getItemMeta();
            im.displayName(displayName);
            List<Component> lore = getPlayerPermSpecifiedLore(unspecifiedlore, hasPerm, cost);
            im.lore(lore);

            NamespacedKey servernameN = new NamespacedKey(Reisecomputer.pl, "servername");
            NamespacedKey costN = new NamespacedKey(Reisecomputer.pl, "cost");
            NamespacedKey owningN = new NamespacedKey(Reisecomputer.pl, "owning");
            NamespacedKey keyN = new NamespacedKey(Reisecomputer.pl, "key");
            im.getPersistentDataContainer().set(servernameN, PersistentDataType.STRING, servername);
            im.getPersistentDataContainer().set(costN, PersistentDataType.INTEGER, cost);
            im.getPersistentDataContainer().set(owningN, PersistentDataType.BOOLEAN, hasPerm);
            im.getPersistentDataContainer().set(keyN, PersistentDataType.STRING, key);
            i.setItemMeta(im);

            gui.setItem(slot, i);

        }

        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta bim = background.getItemMeta();
        bim.displayName(MiniMessage.miniMessage().deserialize(""));
        background.setItemMeta(bim);

        while(gui.firstEmpty() > -1) {
            gui.setItem(gui.firstEmpty(), background);
        }

        p.openInventory(gui);
    }

    public static List<Component> getPlayerPermSpecifiedLore(List<Component> lore, Boolean hasPerm, int cost) {
        if (! hasPerm) {
            lore.add(MiniMessage.miniMessage().deserialize("<color:#ff2e51>Nicht verfügbar</color>"));
            lore.add(MiniMessage.miniMessage().deserialize("<color:#fff700>Kaufen für <gold>" + cost + " Credits</gold></color><dark_gray> (Linksklick)"));

        } else {
            lore.add(MiniMessage.miniMessage().deserialize("<color:#03ff3d>Verfügbar</color>"));
        }

        return lore;
    }

    public static Boolean hasPermForPlanet(UUID uuid, String key, int cost) {
        if (cost > 0) {
            if (! PlanetPermissions.hasPlanetPermission(uuid, key)) {
                return false;
            }
        }
        return true;
    }
}
