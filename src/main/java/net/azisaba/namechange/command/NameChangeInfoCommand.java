package net.azisaba.namechange.command;

import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.config.NameChangeInfoIO;
import net.azisaba.namechange.data.NameChangeInfoData;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageEditNameInfo;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NameChangeInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Chat.f("&cプレイヤーのみ実行可能です"));
            return true;
        }
        Player p = (Player) sender;
        String weaponNode = new CSUtility().getWeaponTitle(p.getInventory().getItemInMainHand());
        if(weaponNode == null){
            sender.sendMessage(Chat.f("&6武器を手に持って実行してください"));
            return true;
        }

        NameChangeInfoData data = new NameChangeInfoIO().load(weaponNode);
        if(data == null){
            new NameChangeInfoIO().save(new NameChangeInfoData(weaponNode, "N/A", "N/A", "N/A", "N/A", 0),weaponNode);
        }

        InventoryGui gui = new InventoryGui(p);
        gui.openPage(new PageEditNameInfo(gui,weaponNode,data));

        return true;
    }
}
