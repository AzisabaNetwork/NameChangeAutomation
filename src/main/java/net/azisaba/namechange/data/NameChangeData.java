package net.azisaba.namechange.data;

import com.shampaggon.crackshot.CSUtility;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.util.NameChangeProgress;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class NameChangeData {

    private final UUID uuid;
    private final String name;

    private String previousWeaponID;

    private String displayName;
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
        String newID = previousWeaponID + "_" + name;
        int i = 1;
        while (new CSUtility().generateWeapon(newID) != null) {
            i++;
            newID = previousWeaponID + "_" + name + "_" + i;
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

        String newID = new CSUtility().getWeaponTitle(item);
        if (newID == null) {
            return true;
        }
        return newID.equals(previousWeaponID);
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
        if (lore != null && !lore.isEmpty()) {
            List<String> translatedLore = lore.stream()
                    .map(lore -> ChatColor.translateAlternateColorCodes('&', lore))
                    .collect(Collectors.toList());
            meta.setLore(translatedLore);
        }
        item.setItemMeta(meta);
        return item;
    }
}


