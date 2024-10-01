package net.azisaba.namechange.gui.items;

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

public class ItemCancel extends GuiItem {
    public ItemCancel(InventoryGui gui) {
        super(gui, new ItemStack(Material.BARRIER));
        this.setDisplayName(Chat.f("&cやり直す"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',"&7押すと前の画面に戻ります")));
        this.setLore(lore);
    }

    @Override
    public void onClick(InventoryClickEvent e){
        gui.currentPage.back();
    }
}
