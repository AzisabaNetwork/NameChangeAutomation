package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemMoveToUp extends GuiItem {
    public ItemMoveToUp (InventoryGui gui){
        super (gui, new ItemStack(Material.TORCH));
        this.setDisplayName(Chat.f("&eこの行を上へ移動する"));
    }
    @Override
    public void onClick(){

    }
}
