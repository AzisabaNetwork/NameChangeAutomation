package net.azisaba.namechange.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiListener implements Listener {

    private final GuiPage page;

    public GuiListener(GuiPage page){
        this.page = page;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getClickedInventory() != page.inventory) return;

        GuiItem item = page.getItem(e.getSlot());
        if(item != null){
            item.onClick(e);
            if(e.getClick() == ClickType.RIGHT){
                item.onRightClick(e);
            }else if(e.getClick() == ClickType.LEFT){
                item.onLeftClick(e);
            }
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(e.getInventory() != page.inventory) return;
        page.close();
    }

}
