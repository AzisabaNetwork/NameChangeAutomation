package net.azisaba.namechange.listener;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.gui.core.ClickableGUI;
import net.azisaba.namechange.gui.core.ClickableGUIDistributor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ClickableGUIListener implements Listener {

    private final ClickableGUIDistributor distributor;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player p = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        ClickableGUI gui = distributor.getClickableGUI(inv);
        if (gui == null) {
            return;
        }
        gui.onClickInventory(p, event);
    }
}
