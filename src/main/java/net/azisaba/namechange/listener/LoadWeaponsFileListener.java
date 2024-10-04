package net.azisaba.namechange.listener;

import com.shampaggon.crackshot.CSDirector;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class LoadWeaponsFileListener implements Listener {

    private final NameChangeAutomation plugin;

    @EventHandler
    public void onLoadPlugin(PluginEnableEvent e) {
        if (!e.getPlugin().getName().equalsIgnoreCase("CrackShot")) {
            return;
        }
        CSDirector director = (CSDirector) e.getPlugin();

        plugin.loadNameChangeWeapons(director);
    }

    @EventHandler
    public void onCommandExecute(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        if (cmd.split(" ")[0].contains(":")) {
            cmd = "/" + cmd.substring(cmd.indexOf(":") + 1);
        }
        if (cmd.equalsIgnoreCase("/crackshot config reload")) {
        } else if (cmd.equalsIgnoreCase("/cs config reload")) {
        } else if (cmd.equalsIgnoreCase("/shot config reload")) {
        } else if (cmd.equalsIgnoreCase("/cra config reload")) {
        }else {
            return;
        }

        if (!e.getPlayer().hasPermission("crackshot.reloadplugin")) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Plugin cs = Bukkit.getPluginManager().getPlugin("CrackShot");
            if (cs == null) {
                return;
            }
            CSDirector director = (CSDirector) cs;
            plugin.loadNameChangeWeapons(director);

            e.getPlayer().sendMessage(Chat.f("&e子ディレクトリの読み込みを完了しました"));
        }, 0L);
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent e) {
        String cmd = e.getCommand();
        if (cmd.split(" ")[0].contains(":")) {
            cmd = cmd.substring(cmd.indexOf(":") + 1);
        }
        if (e.getCommand().equalsIgnoreCase("crackshot config reload")) {
        } else if (e.getCommand().equalsIgnoreCase("cs config reload")) {
        } else if (e.getCommand().equalsIgnoreCase("shot config reload")) {
        } else if (e.getCommand().equalsIgnoreCase("cra config reload")) {
        }else {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Plugin cs = Bukkit.getPluginManager().getPlugin("CrackShot");
            if (cs == null) {
                return;
            }
            CSDirector director = (CSDirector) cs;
            plugin.loadNameChangeWeapons(director);

            e.getSender().sendMessage(Chat.f("&e子ディレクトリの読み込みを完了しました"));
        }, 0L);
    }
}
