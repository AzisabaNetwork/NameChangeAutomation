package net.azisaba.namechange.gui.custom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class NameChange implements InventoryHolder {

    private final Inventory inventory;
    private final String inventoryName;

    public NameChange(String inventoryName) {
        this.inventoryName = inventoryName;
        this.inventory = Bukkit.createInventory(this, 9 * 6, inventoryName);
    }
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public String getInventoryName() {
        return this.inventoryName;
    }

    public void openInventory(Player player) {
        player.openInventory(this.inventory);
    }
}
