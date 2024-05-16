package at.onlinerz.reisecomputer.frontend;

import at.onlinerz.reisecomputer.Reisecomputer;
import at.onlinerz.reisecomputer.configuration.PlanetData;
import at.onlinerz.reisecomputer.listeners.NPCInteractListener;
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

public class BuyGUI {

    public static void createFor(Player p, ItemStack clickedi) {
        NamespacedKey keyN = new NamespacedKey(Reisecomputer.pl, "key");
        String key = clickedi.getItemMeta().getPersistentDataContainer().get(keyN, PersistentDataType.STRING);
        PlanetData pd = Reisecomputer.planets.get(key);
        int cost = pd.getCost();
        Component title = Reisecomputer.guititle.append(MiniMessage.miniMessage().deserialize("<gray> » ")).append(pd.getDisplayName());
        Inventory gui = Bukkit.createInventory(null, 3*9, title);

        ItemStack planeti = clickedi.clone();
        ItemMeta planetim = planeti.getItemMeta();
        // displayname
        Component extend = MiniMessage.miniMessage().deserialize("<green>Kaufe ");
        Component displayname = extend.append(pd.getDisplayName());
        planetim.displayName(displayname);
        //lore
        List<Component> lore = new ArrayList<>(pd.getLore());
        lore.add(MiniMessage.miniMessage().deserialize("<gray>» <yellow>Preis: <green>"+cost+ " Credits"));
        lore.add(MiniMessage.miniMessage().deserialize("<gray>Kontostand nach dem Kauf: <green>"+(NPCInteractListener.getBalanceOf(p)-cost)+ " Credits"));
        planetim.lore(lore);
        // cost key & finalize
        NamespacedKey costN = new NamespacedKey(Reisecomputer.pl, "cost");
        planetim.getPersistentDataContainer().set(costN, PersistentDataType.INTEGER, cost);
        planeti.setItemMeta(planetim);
        gui.setItem(13, planeti);

        ItemStack yes = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta iyes = yes.getItemMeta();
        iyes.displayName(MiniMessage.miniMessage().deserialize("<green>[✔] Ja"));
        yes.setItemMeta(iyes);
        gui.setItem(11, yes);

        ItemStack no = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta ino = no.getItemMeta();
        ino.displayName(MiniMessage.miniMessage().deserialize("<red>[X] Nein"));
        no.setItemMeta(ino);
        gui.setItem(15, no);

        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta iback = background.getItemMeta();
        iback.displayName(MiniMessage.miniMessage().deserialize(""));
        background.setItemMeta(iback);

        while(gui.firstEmpty() > -1) {
            gui.setItem(gui.firstEmpty(), background);
        }

        p.openInventory(gui);

    }
}
