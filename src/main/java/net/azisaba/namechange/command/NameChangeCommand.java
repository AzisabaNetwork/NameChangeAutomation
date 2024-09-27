package net.azisaba.namechange.command;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageAcceptChange;
import net.azisaba.namechange.gui.pages.PageNameChange;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class NameChangeCommand implements CommandExecutor {

    private final NameChangeAutomation plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Chat.f("&cプレイヤーのみ実行可能です"));
            return true;
        }

        Player p = (Player) sender;

        WaitingAcceptData data = plugin.getAcceptQueueWeapons().nextAcceptWeapon();
        if (data == null) {
            p.sendMessage(Chat.f("&a現在待機中の通常名前変更はありません。"));
            return true;
        }

        InventoryGui gui = new InventoryGui(p);
        gui.openPage(new PageAcceptChange(gui,data));
        p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 2);
        return true;
    }
}
