package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemCancel extends GuiItem {
    public ItemCancel(InventoryGui gui) {
        super(gui, new ItemStack(Material.BARRIER));
    }

    @Override
    public void onClick(){
        gui.currentPage.back();
    }
}
