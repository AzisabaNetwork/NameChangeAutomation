package net.azisaba.namechange.gui.items;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.config.NameChangeInfoIO;
import net.azisaba.namechange.data.NameChangeInfoData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemNIBaseWeapon extends GuiItem {
    NameChangeInfoData data;
    public ItemNIBaseWeapon(InventoryGui gui, String weaponNode, NameChangeInfoData data) {
        super(gui, new ItemStack(Material.BARRIER));
        this.data = data;
        ItemStack baseWeapon = new CSUtility().generateWeapon(data.getBaseWeapon());
        this.itemStack = baseWeapon;
        this.setDisplayName(Chat.f("&6元となった武器"));
        List<Component> lore = new ArrayList<>();
        lore.add(baseWeapon.getItemMeta().displayName());
        this.setLore(lore);
    }
}
