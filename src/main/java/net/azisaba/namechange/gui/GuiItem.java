package net.azisaba.namechange.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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
    public void setLore(List<Component> lore){
        ItemMeta meta = itemStack.getItemMeta();
        meta.lore(lore);
        itemStack.setItemMeta(meta);
    }
}