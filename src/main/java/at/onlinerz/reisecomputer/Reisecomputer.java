package at.onlinerz.reisecomputer;

import at.onlinerz.reisecomputer.commands.CMDplaneten;
import at.onlinerz.reisecomputer.configuration.ConfigFiles;
import at.onlinerz.reisecomputer.configuration.PlanetData;
import at.onlinerz.reisecomputer.listeners.NPCInteractListener;
import at.onlinerz.reisecomputer.playerdata.MySQL;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Reisecomputer extends JavaPlugin {

    public static Plugin pl;
    public static HashMap<String, PlanetData> planets = new HashMap<>();
    public static DataSource dataSource;
    public static Component guititle = MiniMessage.miniMessage().deserialize("<color:#5765EC>Reisecomputer");
    public static String thisServer;
    public static String npcname;

    private static Economy econ = null;

    @Override
    public void onEnable() {
        pl = this;

        // register
        this.getServer().getPluginManager().registerEvents(new NPCInteractListener(), this);
        this.getCommand("planeten").setExecutor(new CMDplaneten());

        // create and load config
        ConfigFiles.create();
        ConfigFiles.loadConfig();

        // MySQL
        dataSource = ConfigFiles.getFedDatasource();

        try {
            MySQL.setupDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Vault
        if (!setupEconomy() ) {
            pl.getLogger().severe(String.format("[Vault] Es wurde kein Economy-Plugin gefunden!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Bungeecord PMC
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

    }

    @Override
    public void onDisable() {
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static void sendPlayerTo(Player player, String servername) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(servername);

        player.sendPluginMessage(pl, "BungeeCord", out.toByteArray());

    }
}
