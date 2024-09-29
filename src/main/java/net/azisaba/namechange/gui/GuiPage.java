package net.azisaba.namechange.gui;

import net.azisaba.namechange.NameChangeAutomation;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class GuiPage {

    public InventoryGui gui;
    public Inventory inventory;
    public GuiListener listener;
    private HashMap<Integer, GuiItem> items = new HashMap<>();

    public GuiPage(InventoryGui gui, String name, int size) {
        inventory = Bukkit.createInventory(null, size, name);
        this.gui = gui;
        listener = new GuiListener(this);
        Bukkit.getPluginManager().registerEvents(listener, NameChangeAutomation.INSTANCE);
    }

    public abstract void setUp();

    public abstract void back();

    public void setItem(int slot, GuiItem item) {
        items.put(slot, item);
        inventory.setItem(slot, item.itemStack);
    }

    public void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public GuiItem getItem(int slot) {
        return items.get(slot);
    }

    public ItemStack getItemStack(int slot){
        return items.get(slot).itemStack;
    }

    public void close(){
        HandlerList.unregisterAll(listener);
    }
}
