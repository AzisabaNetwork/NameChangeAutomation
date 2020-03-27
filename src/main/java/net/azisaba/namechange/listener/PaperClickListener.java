package net.azisaba.namechange.listener;

import com.shampaggon.crackshot.CSUtility;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
public class PaperClickListener implements Listener {

    private HashMap<UUID, Long> cooldown = new HashMap<>();

    private final NameChangeAutomation plugin;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!e.getAction().toString().startsWith("RIGHT_CLICK_")) {
            return;
        }
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        if (!item.hasItemMeta()) {
            return;
        }
        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }
        if (cooldown.getOrDefault(p.getUniqueId(), 0L) > System.currentTimeMillis()) {
            return;
        }

        String displayName = item.getItemMeta().getDisplayName();
        if (!displayName.startsWith(ChatColor.YELLOW + "名前変更引換券: " + ChatColor.AQUA)) {
            return;
        }
        String id = displayName.substring(displayName.indexOf(ChatColor.AQUA + "") + 2);
        cooldown.put(p.getUniqueId(), System.currentTimeMillis() + 5000L);

        if (plugin.getDenyWeapons().isDenied(id)) {
            p.sendMessage(Chat.f("&cこの武器データは却下されました。アイテムを戻します。"));
            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
            p.getInventory().setItemInMainHand(null);

            WaitingAcceptData data = plugin.getAcceptQueueWeapons().getWaitingData(id);

            ItemStack previousItem = new CSUtility().generateWeapon(data.getPreviousID());
            ItemStack nameChangeItem = new CSUtility().generateWeapon("NAME");
            p.getInventory().addItem(previousItem);
            p.getInventory().addItem(nameChangeItem);

            data.setAbleToDelete(true);
            plugin.getDenyWeapons().deleteDenyInformation(id);
            return;
        }

        if (!plugin.getAcceptQueueWeapons().isCompleted(id)) {
            p.sendMessage(ChatColor.RED + "この武器はまだ審査がされていません。しばらくお待ちください。");
            return;
        }

        ItemStack weapon = new CSUtility().generateWeapon(id);
        if (weapon == null) {
            p.sendMessage(ChatColor.RED + "エラーが発生しました (武器が存在しません)");
            p.sendMessage(ChatColor.RED + "武器ID: " + ChatColor.YELLOW + id);
            p.sendMessage(ChatColor.RED + "運営に報告してください。");
            return;
        }

        if (!p.getInventory().getItemInMainHand().equals(item)) {
            p.sendMessage(ChatColor.RED + "利き手に紙を持って右クリックしてください。");
            return;
        }

        p.getInventory().setItemInMainHand(weapon);
        p.sendMessage(Chat.f("&a武器を交換しました！"));
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
