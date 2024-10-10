package net.azisaba.namechange.listener;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.AcceptQueueWeapons;
import net.azisaba.namechange.data.NameChangeDataContainer;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
        WaitingAcceptData data = new AcceptQueueWeapons(NameChangeAutomation.INSTANCE).nextAcceptWeapon();
        if(data != null){
            for (Player player : Bukkit.getOnlinePlayers()) {
                // 運営にネームド追加を通知する (試合鯖にいる人にも通知出来たらしたい)
                if (player.hasPermission("namechangeautomation.command.namechange")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a[Admin Message]:&6ネームド申請が保留中です"));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 1f);
                }
            }
        }
    }
}
