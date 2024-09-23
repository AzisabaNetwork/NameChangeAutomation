package net.azisaba.namechange.gui;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GuiItem {
    public InventoryGui gui;
    public ItemStack itemStack;

    public GuiItem(InventoryGui gui, ItemStack itemStack){
        this.gui = gui;
        this.itemStack = itemStack;
    }

    public void onClick(){}
    public void onRightClick(){}
    public void onLeftClick(){}

    public void setDisplayName(String name){
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
    }
}
