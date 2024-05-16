package at.onlinerz.reisecomputer.listeners;

import at.onlinerz.reisecomputer.Reisecomputer;
import at.onlinerz.reisecomputer.frontend.BuyGUI;
import at.onlinerz.reisecomputer.frontend.CantBuyGUI;
import at.onlinerz.reisecomputer.frontend.MainGUI;
import at.onlinerz.reisecomputer.playerdata.PlanetPermissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NPCInteractListener implements Listener {

    @EventHandler
    public void onGUIClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem() != null) {
            if (e.getView().title().equals(Reisecomputer.guititle)) {
                e.setCancelled(true);

                if (e.getCurrentItem().getType() != Material.GRAY_STAINED_GLASS_PANE) {
                    ItemStack planet = e.getCurrentItem();
                    ItemMeta iplanet = planet.getItemMeta();

                    if (ifIsOwning(e.getCurrentItem())) {
                        NamespacedKey servernameN = new NamespacedKey(Reisecomputer.pl, "servername");
                        String servername = iplanet.getPersistentDataContainer().get(servernameN, PersistentDataType.STRING);
                        Reisecomputer.sendPlayerTo(p, servername);

                    } else {
                        NamespacedKey costN = new NamespacedKey(Reisecomputer.pl, "cost");
                        if (iplanet.getPersistentDataContainer().has(costN)) {
                            int cost = iplanet.getPersistentDataContainer().get(costN, PersistentDataType.INTEGER);
                            if (getBalanceOf(p) >= cost) {
                                BuyGUI.createFor(p, planet);
                            } else {
                                CantBuyGUI.createFor(p, planet);
                            }
                        }
                    }
                }
            } else {
                if (e.getView().getTitle().contains("Reisecomputer")) {
                    e.setCancelled(true);
                    ItemStack clicked = e.getCurrentItem();
                    Material clickedmat = clicked.getType();
                    if (clickedmat == Material.SPRUCE_DOOR || clickedmat == Material.RED_STAINED_GLASS_PANE) {
                        e.getInventory().close();
                        MainGUI.createFor(p);
                    } else if (clickedmat == Material.LIME_STAINED_GLASS_PANE) {
                        NamespacedKey costN = new NamespacedKey(Reisecomputer.pl, "cost");
                        NamespacedKey keyN = new NamespacedKey(Reisecomputer.pl, "key");
                        ItemStack mainitem = e.getInventory().getItem(13);
                        ItemMeta imainitem = mainitem.getItemMeta();

                        if (!imainitem.getPersistentDataContainer().isEmpty()) {
                            if (imainitem.getPersistentDataContainer().has(costN) && imainitem.getPersistentDataContainer().has(keyN))
                                ;
                            int cost = imainitem.getPersistentDataContainer().get(costN, PersistentDataType.INTEGER);
                            String key = imainitem.getPersistentDataContainer().get(keyN, PersistentDataType.STRING);

                            removeMoney(p, cost);
                            PlanetPermissions.addPlanetPermission(p.getUniqueId(), key);
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                            e.getInventory().close();
                            MainGUI.createFor(p);
                        }
                    }
                }
            }

        }
    }


    public static Boolean ifIsOwning(ItemStack planeti) {
        ItemMeta im = planeti.getItemMeta();
        if(! im.getPersistentDataContainer().isEmpty()) {
            NamespacedKey owningN = new NamespacedKey(Reisecomputer.pl, "owning");
            if(im.getPersistentDataContainer().has(owningN)) {
                return im.getPersistentDataContainer().get(owningN, PersistentDataType.BOOLEAN);
            }
        }
        return null;
    }

    public static Double getBalanceOf(Player p) {
        OfflinePlayer offpl = Bukkit.getOfflinePlayer(p.getUniqueId());
        return Reisecomputer.getEconomy().getBalance(offpl);
    }

    public static void removeMoney(Player p, double amount) {
        OfflinePlayer offpl = Bukkit.getPlayer(p.getUniqueId());
        Economy econ = Reisecomputer.getEconomy();

        if (econ != null) {
            EconomyResponse recon = econ.withdrawPlayer(offpl, amount);

            if (!recon.transactionSuccess()) {
                Reisecomputer.pl.getLogger().info("Der Kontostand von " + offpl.getName() + " konnte nicht geändert werden! [Problem mit Vault]");
            }
        } else {
            Reisecomputer.pl.getLogger().info("Der Kontostand von " + offpl.getName() + " konnte nicht geändert werden! [Problem mit Vault]");
        }

    }

}
