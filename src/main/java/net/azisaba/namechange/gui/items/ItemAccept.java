package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageAcceptChange;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemAccept extends GuiItem {

    private final WaitingAcceptData data;

    public ItemAccept(InventoryGui gui, WaitingAcceptData data) {
        super(gui, new ItemStack(Material.GREEN_TERRACOTTA));
        this.data = data;
        this.setDisplayName(Chat.f("&a許可する"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',"&7これを押すと作成者は即座に交換/使用可能になります")));
        this.setLore(lore);
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        gui.player.closeInventory();

        if (data.isCompleted()) {
            gui.player.sendMessage(Chat.f("&aこの武器はすでに他の運営によって処理されました。"));
            gui.player.playSound(gui.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            return;
        }

        data.setAbleToDelete(true);
        gui.player.sendMessage(Chat.f("&a銃データの使用を許可しました"));
        data.setCompleted();

        Player namedplayer = Bukkit.getPlayer(data.getAuthorUUID());
        if(namedplayer != null){
            namedplayer.sendMessage("ネームドの申請が許可されました");
        }

        Bukkit.getScheduler().runTaskLater(NameChangeAutomation.INSTANCE, () -> {
            WaitingAcceptData data2 = NameChangeAutomation.INSTANCE.getAcceptQueueWeapons().nextAcceptWeapon();
            if (data2 == null) {
                return;
            }
            gui.openPage(new PageAcceptChange(gui, data2));
        }, 5);
    }
}
