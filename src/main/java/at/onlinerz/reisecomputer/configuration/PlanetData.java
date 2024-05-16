package at.onlinerz.reisecomputer.configuration;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

public class PlanetData {

    int slot;
    Material material;
    String mojangUrl;
    Component displayName;
    List<Component> lore;
    int cost;
    String servername;

    public PlanetData(int slot, Material material, String mojangUrl, Component displayName, List<Component> lore, int cost, String servername) {
        this.slot = slot;
        this.material = material;
        this.mojangUrl = mojangUrl;
        this.displayName = displayName;
        this.lore = lore;
        this.cost = cost;
        this.servername = servername;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getMojangUrl() {
        return mojangUrl;
    }

    public void setMojangUrl(String mojangUrl) {
        this.mojangUrl = mojangUrl;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Component displayName) {
        this.displayName = displayName;
    }

    public List<Component> getLore() {
        return lore;
    }

    public void setLore(List<Component> lore) {
        this.lore = lore;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }
}
