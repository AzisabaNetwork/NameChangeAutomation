package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemLoreComplete extends GuiItem {
    public ItemLoreComplete(InventoryGui gui){
        super (gui, new ItemStack(Material.GREEN_TERRACOTTA));
        this.setDisplayName(Chat.f("&c完了"));
    }
    @Override
    public void onClick(){
        gui.currentPage.back();
    }
}
