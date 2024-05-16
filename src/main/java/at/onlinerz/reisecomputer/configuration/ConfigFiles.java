package at.onlinerz.reisecomputer.configuration;

import at.onlinerz.reisecomputer.Reisecomputer;
import at.onlinerz.reisecomputer.playerdata.MySQL;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;

public class ConfigFiles {

    public static File file = new File(Reisecomputer.pl.getDataFolder()+"/config.yml");
    public static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static File mysqlfile = new File(Reisecomputer.pl.getDataFolder()+"/mysql.yml");
    public static YamlConfiguration mysqlconf = YamlConfiguration.loadConfiguration(mysqlfile);

    public static void create() {
        if(! file.exists()) {
            config.set("thisServername", "marsserver");
            config.set("npcName", "&e&lReise Computer");

            config.set("nethermond.slot", 20);
            config.set("nethermond.material", "PLAYER_HEAD");
            config.set("nethermond.mojangUrl", "1a7956801e921c6951ae5f965732d3d7e026fe41660e2c09ac7b1ac052d88be9");
            config.set("nethermond.displayname", "<dark_red>Nether-Mond");
            config.set("nethermond.lore", "<gray>Ein von Lava überzogener Mond, der:<gray>sich in unmittelbarer Nähe einer Sonne befindet,:<gray>beheimatet einer unbekannten Spezies.");
            config.set("nethermond.cost", 125000);
            config.set("nethermond.moveServername", "nethermondserver");

            config.set("mars.slot", 31);
            config.set("mars.material", "PLAYER_HEAD");
            config.set("mars.mojangUrl", "bf647a352321bdb90362726352d325b5db86f27e3941bb169f2aaf53b0e43eda");
            config.set("mars.displayname", "<dark_green>Mars");
            config.set("mars.lore", "<gray>Auf diesem Planeten herrscht eine:<gray>blühende Ära, auf der lebensfreundlichen:<gray>Oberfläche kann man existieren.");
            config.set("mars.cost", 0);
            config.set("mars.moveServername", "marsserver");

            config.set("erde.slot", 24);
            config.set("erde.material", "PLAYER_HEAD");
            config.set("erde.mojangUrl", "dade199d5b3a8b8c6fe2eeb628af38daada305f7c90d1bd1224cf1f071fcbe8b");
            config.set("erde.displayname", "<aqua>Erde");
            config.set("erde.lore", "<gray>Ein von Kälte und Hitzewellen:<gray>gezeichneter Planet kämpft mit den Folgen:<gray>einer vergangenen katastrophalen Zeit.");
            config.set("erde.cost", 200000);
            config.set("erde.moveServername", "erdeserver");

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(! mysqlfile.exists()) {
            mysqlconf.set("host", "your.host.net");
            mysqlconf.set("port", 3306);
            mysqlconf.set("database", "nameOfYourDatabase");
            mysqlconf.set("username", "yourUsername");
            mysqlconf.set("password", "123abc");

            try {
                mysqlconf.save(mysqlfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadConfig() {
        Reisecomputer.thisServer = config.getString("thisServername");
        Reisecomputer.npcname = config.getString("npcName").replace("&", "§");

        for(String key : config.getKeys(false)) {
            if(config.contains(key+".slot")) {
                int slot = config.getInt(key+".slot");
                String materialName = config.getString(key+".material");
                Material material;
                if(Material.getMaterial(materialName) == null) {
                    material = Material.BEDROCK;
                } else {
                    material = Material.valueOf(config.getString(key+".material"));
                }
                String mojangUrl = config.getString(key+".mojangUrl");
                Component displayname = MiniMessage.miniMessage().deserialize(config.getString(key+".displayname"));
                String[] unform = config.getString(key+".lore").split(":");
                List<Component> lore = new ArrayList<>();
                for(String s : unform) {
                    lore.add(MiniMessage.miniMessage().deserialize(s));
                }
                int cost = config.getInt(key+".cost");
                String servername = config.getString(key+".moveServername");

                Reisecomputer.planets.put(key, new PlanetData(slot, material, mojangUrl, displayname, lore, cost, servername));
            }
        }

        Reisecomputer.pl.getLogger().info("Es wurden "+Reisecomputer.planets.size()+" Planeten geladen.");
    }

    public static DataSource getFedDatasource() {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();

        if(mysqlconf.getString("database").equalsIgnoreCase("nameOfYourDatabase")) {
            Reisecomputer.pl.getLogger().info("Bitte trage deine MySQL-Daten ein!");
        } else {
            dataSource.setServerName(mysqlconf.getString("host"));
            dataSource.setPort(mysqlconf.getInt("port"));
            dataSource.setDatabaseName(mysqlconf.getString("database"));
            dataSource.setUser(mysqlconf.getString("username"));
            dataSource.setPassword(mysqlconf.getString("password"));

            try {
                MySQL.testDataSource(dataSource);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        return dataSource;
    }
}
