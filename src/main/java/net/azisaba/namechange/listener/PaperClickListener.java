package net.azisaba.namechange.listener;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.task.CSPReloadTask;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

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
        if(!NameChangeAutomation.INSTANCE.getPluginConfig().isLobby()){
            p.sendMessage(Chat.f("&cロビーでのみ利用可能です"));
            return;
        }
        String id = displayName.substring(displayName.indexOf(ChatColor.AQUA + "") + 2);
        cooldown.put(p.getUniqueId(), System.currentTimeMillis() + 5000L);

        if (plugin.getDenyWeapons().isDenied(id)) {
            p.sendMessage(Chat.f("&cこの武器データは却下されました。アイテムを戻します。"));
            if (getEmptySlots(p) < 2) {
                p.sendMessage(Chat.f("&cインベントリに十分な空きがありません。銃が消える可能性があります。"));
                p.sendMessage(Chat.f("&aインベントリを空けて再度実行してください。"));
                return;
            }

            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
            p.getInventory().setItemInMainHand(null);

            WaitingAcceptData data = plugin.getAcceptQueueWeapons().getWaitingData(id);

            ItemStack previousItem = new CSUtility().generateWeapon(data.getPreviousID());
            ItemStack nameChangeItem = new CSUtility().generateWeapon("NAME");
            ItemMeta meta = previousItem.getItemMeta();
            meta.setCustomModelData(data.getCustomModelData());
            previousItem.setItemMeta(meta);
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
        WaitingAcceptData data = plugin.getAcceptQueueWeapons().getWaitingData(id);
        ItemMeta meta = weapon.getItemMeta();
        if(data != null) {
            meta.setCustomModelData(data.getCustomModelData());
        }
        weapon.setItemMeta(meta);
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


        new CSPReloadTask().runTask(plugin);

        p.getInventory().setItemInMainHand(weapon);
        p.sendMessage(Chat.f("&a武器を交換しました！"));
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, "syncomma cspreload");
    }

    private int getEmptySlots(Player p) {
        Inventory inv = p.getInventory();
        int count = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                count++;
            }
        }

        return count;
    }
}
