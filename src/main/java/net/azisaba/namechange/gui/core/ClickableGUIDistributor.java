package net.azisaba.namechange.gui.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ClickableGUIDistributor {

    private List<ClickableGUI> guiList = new ArrayList<>();

    public void registerClickableGUI(ClickableGUI gui) {
        if (getGUI(gui.getClass()) != null) {
            throw new IllegalArgumentException("GUI already registered");
        }
        guiList.add(gui);
    }

    public ClickableGUI getGUI(Class<? extends ClickableGUI> clazz) {
        for (ClickableGUI gui : guiList) {
            if (gui.getClass().equals(clazz)) {
                return gui;
            }
        }
        return null;
    }

    public ClickableGUI getClickableGUI(Inventory inv) {
        for (ClickableGUI gui : guiList) {
            if (gui.isSameInventory(inv)) {
                return gui;
            }
        }
        return null;
    }

    public void closeAllInventories() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory openingInventory = p.getOpenInventory().getTopInventory();
            if (openingInventory == null) {
                continue;
            }

            for (ClickableGUI gui : guiList) {
                if (gui.isSameInventory(openingInventory)) {
                    p.closeInventory();
                }
            }
        }
    }
}
