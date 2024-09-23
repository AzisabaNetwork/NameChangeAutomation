package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.AcceptNameChangeGUI;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageAcceptChange;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemDeny extends GuiItem {

    private final WaitingAcceptData data;

    public ItemDeny(InventoryGui gui, WaitingAcceptData data) {
        super(gui, new ItemStack(Material.BARRIER));
        this.data = data;
    }

    @Override
    public void onClick(){

        gui.player.closeInventory();

        if(data.isCompleted()){
            gui.player.sendMessage(Chat.f("&aこの武器はすでに他の運営によって処理されました。"));
            gui.player.playSound(gui.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            return;
        }

        // deny
        NameChangeAutomation.INSTANCE.getDenyWeapons().executeDenyProcess(data);
        gui.player.sendMessage(Chat.f("&c銃データの使用を却下しました"));
        gui.player.sendMessage(Chat.f("&7作者には名前変更アイテムと元武器が返却されます"));
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
