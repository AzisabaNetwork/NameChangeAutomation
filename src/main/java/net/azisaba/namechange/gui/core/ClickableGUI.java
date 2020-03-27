package net.azisaba.namechange.gui.core;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

abstract public class ClickableGUI {

    abstract public Inventory getInventory(Player p);

    abstract public void onClickInventory(Player p, InventoryClickEvent e);

    abstract public boolean isSameInventory(Inventory inv);
}
