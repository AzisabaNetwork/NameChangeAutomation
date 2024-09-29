package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemEditItem extends GuiItem {
    public ItemEditItem(InventoryGui gui) {
        super(gui, new ItemStack(Material.WRITABLE_BOOK));
    }
}
