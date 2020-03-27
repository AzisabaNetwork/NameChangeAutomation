package net.azisaba.namechange.listener;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.data.NameChangeDataContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class JoinListener implements Listener {

    private final NameChangeDataContainer container;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        container.loadData(p);
    }
}
