package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageEditLore;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemChangeLore extends GuiItem {
    InventoryGui gui;
    public ItemChangeLore(InventoryGui gui) {
        super(gui, new ItemStack(Material.WRITABLE_BOOK));
        this.setDisplayName(Chat.f("&a説明文を変える"));
        this.gui = gui;
    }

    @Override
    public void onClick(){
        gui.openPage(new PageEditLore(this.gui));
    }
}
