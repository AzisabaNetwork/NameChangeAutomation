package net.azisaba.namechange.listener;

import net.azisaba.crackshot.CSUtility;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageNameChange;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ItemClickListener implements Listener {

    private final NameChangeAutomation plugin;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND
                || (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }
        CSUtility util = new CSUtility();
        String id = util.getWeaponTitle(item);
        if (id == null) {
            return;
        }
        if (!id.equalsIgnoreCase("NAME")) {
            return;
        }

        e.setCancelled(true);
        if (!plugin.isLobbyServer()) {
            p.sendMessage(Chat.f("&cロビーでのみ使用可能です"));
            return;
        }

        if (item.getAmount() > 1) {
            p.sendMessage(Chat.f("&c重ねず、一つの状態でクリックしてください、"));
            return;
        }

        // GUIを開く
        InventoryGui gui = new InventoryGui(p);
        gui.openPage(new PageNameChange(gui));


        plugin.getChatReader().unregisterNextChat(p);
    }
}
