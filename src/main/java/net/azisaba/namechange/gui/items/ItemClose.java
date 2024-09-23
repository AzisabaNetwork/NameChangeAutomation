package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemClose extends GuiItem {

    public ItemClose(InventoryGui gui){
        super(gui, new ItemStack(Material.BARRIER));
        this.setDisplayName(Chat.f("&c閉じる"));
    }

    @Override
    public void onClick(){
        gui.currentPage.close();
    }

}
