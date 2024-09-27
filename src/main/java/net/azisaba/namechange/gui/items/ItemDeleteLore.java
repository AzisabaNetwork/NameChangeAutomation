package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDeleteLore extends GuiItem {
    public ItemDeleteLore(InventoryGui gui){
        super (gui, new ItemStack(Material.BARRIER));
        this.setDisplayName(Chat.f("&aこの行を削除する"));
    }
    @Override
    public void onClick(InventoryClickEvent e){

    }
}
