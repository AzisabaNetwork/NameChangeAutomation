package net.azisaba.namechange.data;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.util.NameChangeProgress;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class NameChangeData {

    private final UUID uuid;
    private final String name;

    private String previousWeaponID;

    @Getter
    private String displayName;
    private int customModelData;
    private List<String> lore = new ArrayList<>();

    @Getter(value = AccessLevel.PRIVATE)
    private int loreInput;

    private NameChangeProgress progress = NameChangeProgress.NONE;

    @Getter(value = AccessLevel.PRIVATE)
    private String lockedNewWeaponID;

    public String getNewWeaponID() {
        if (lockedNewWeaponID != null) {
            return lockedNewWeaponID;
        }
        String newID = "NC_" + previousWeaponID + "_" + name;
        int i = 1;
        while (new CSUtility().generateWeapon(newID) != null) {
            i++;
            newID = "NC_" + previousWeaponID + "_" + name + "_" + i;
        }

        return newID;
    }

    public void lockNewWeaponID() {
        lockedNewWeaponID = getNewWeaponID();
    }

    public void unlockNewWeaponID() {
        lockedNewWeaponID = null;
    }

    public void setLore(List<String> lore) {
        if (lore == null) {
            this.lore = new ArrayList<>();
        } else {
            this.lore = new ArrayList<>(lore);
        }
    }

    public void receiveLoreChat(String msg) {
        if (loreInput < 0) {
            return;
        }

        if (lore.size() <= loreInput) {
            lore.add(msg);
        } else {
            lore.set(loreInput, msg);
        }

        loreInput = -1;
    }

    public boolean canUseThisData() {
        ItemStack item = getNewItemStack();
        Component newDisplayName = item.getItemMeta().displayName();
        CSUtility util = new CSUtility();
        ItemStack prevItem = util.generateWeapon(getPreviousWeaponID());
        CSDirector CSD = (CSDirector) Bukkit.getPluginManager().getPlugin("CrackShot");
        if(CSD != null) {

            if (util.getHandle().parentlist.containsKey(CSD.getPureName(item.getItemMeta().getDisplayName()))) {
                return true;
            }

            if(NameChangeAutomation.getNamedWeaponDisplayName().contains(getDisplayName())){
                return true;
            }

        }
        if(newDisplayName == null || prevItem.getItemMeta().displayName() == null){
            return true;
        }
        return newDisplayName.equals(prevItem.getItemMeta().displayName());
    }

    public ItemStack getNewItemStack() {
        CSUtility util = new CSUtility();
        ItemStack item = util.generateWeapon(previousWeaponID);
        if (item == null) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (displayName != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName) + " ▪ «?»");
        }
        //CMD設定
        meta.setCustomModelData(customModelData);

        if (lore != null && !lore.isEmpty()) {
            List<Component> translatedLore = lore.stream()
                    .map(lore -> ChatColor.translateAlternateColorCodes('&', lore))
                    .map(Component::text)
                    .collect(Collectors.toList());
            meta.lore(translatedLore);
        }
        item.setItemMeta(meta);
        return item;
    }
    public List<Component> getComponentLore(){
        return lore.stream()
                .map(lore -> ChatColor.translateAlternateColorCodes('&', lore))
                .map(Component::text)
                .collect(Collectors.toList());
    }

}


