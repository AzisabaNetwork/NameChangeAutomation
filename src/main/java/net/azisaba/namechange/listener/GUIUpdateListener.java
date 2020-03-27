package net.azisaba.namechange.listener;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.NameChangeGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class GUIUpdateListener implements Listener {

    private final NameChangeAutomation plugin;

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();

        if (plugin.getGuiDistributor().getClickableGUI(inv) instanceof NameChangeGUI) {
            NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
            if (data != null) {
                inv.setItem(15, data.getNewItemStack());
            }
        }
    }
}
