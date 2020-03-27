package net.azisaba.namechange.listener;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ChatListener implements Listener {

    private final NameChangeAutomation plugin;

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();

        if (!plugin.getChatReader().isRegistered(p)) {
            return;
        }

        e.setCancelled(true);
        plugin.getChatReader().onChat(p, msg);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        plugin.getChatReader().unregisterNextChat(p);
    }
}
