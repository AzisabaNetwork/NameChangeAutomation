package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemMoveToDown extends GuiItem {
    public ItemMoveToDown (InventoryGui gui){
        super (gui, new ItemStack(Material.REDSTONE_TORCH));
        this.setDisplayName(Chat.f("&eこの行を下へ移動する"));
    }
    @Override
    public void onClick(){

    }
}
