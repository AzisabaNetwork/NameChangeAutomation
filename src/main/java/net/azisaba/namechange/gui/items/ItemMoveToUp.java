package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageEditLore;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class ItemMoveToUp extends GuiItem {
    int line;
    public ItemMoveToUp (InventoryGui gui,int line){
        super (gui, new ItemStack(Material.TORCH));
        this.setDisplayName(Chat.f("&eこの行を上へ移動する"));
        this.line = line;
    }
    @Override
    public void onClick(InventoryClickEvent e){
        line = ((e.getSlot() + 6) / 9);
        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        Collections.swap(data.getLore(), line - 1, line - 2);
        gui.openPage(new PageEditLore(gui));
    }
}
