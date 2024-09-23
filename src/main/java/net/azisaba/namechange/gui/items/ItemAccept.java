package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageAcceptChange;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class ItemAccept extends GuiItem {

    private final WaitingAcceptData data;

    public ItemAccept(InventoryGui gui, WaitingAcceptData data) {
        super(gui, new ItemStack(Material.GREEN_TERRACOTTA));
        this.data = data;
    }

    @Override
    public void onClick() {

        gui.player.closeInventory();

        if (data.isCompleted()) {
            gui.player.sendMessage(Chat.f("&aこの武器はすでに他の運営によって処理されました。"));
            gui.player.playSound(gui.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            return;
        }

        data.setAbleToDelete(true);
        gui.player.sendMessage(Chat.f("&a銃データの使用を許可しました"));
        data.setCompleted();

        Bukkit.getScheduler().runTaskLater(NameChangeAutomation.INSTANCE, () -> {
            WaitingAcceptData data2 = NameChangeAutomation.INSTANCE.getAcceptQueueWeapons().nextAcceptWeapon();
            if (data2 == null) {
                return;
            }
            gui.openPage(new PageAcceptChange(gui, data2));
        }, 5);
    }
}
