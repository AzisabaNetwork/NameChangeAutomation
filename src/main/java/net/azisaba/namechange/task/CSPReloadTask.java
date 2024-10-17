package net.azisaba.namechange.task;

import com.shampaggon.crackshot.CSDirector;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CSPReloadTask extends BukkitRunnable {
    @Override
    public void run(){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, "crackshotplus reload");
        Bukkit.dispatchCommand(console, "aug reload");

        Plugin cs = Bukkit.getPluginManager().getPlugin("CrackShot");
        if (cs == null) {
            return;
        }
        CSDirector director = (CSDirector) cs;
        NameChangeAutomation.INSTANCE.loadNameChangeWeapons(director);

    }
}
