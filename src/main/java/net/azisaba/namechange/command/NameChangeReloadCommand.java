package net.azisaba.namechange.command;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageAcceptChange;
import net.azisaba.namechange.task.CSPReloadTask;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class NameChangeReloadCommand implements CommandExecutor {

    private final NameChangeAutomation plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new CSPReloadTask().runTask(plugin);
        return true;
    }
}
