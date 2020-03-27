package net.azisaba.namechange.gui;

import com.shampaggon.crackshot.CSUtility;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.core.ClickableGUI;
import net.azisaba.namechange.utils.Chat;
import net.azisaba.namechange.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class AcceptNameChangeGUI extends ClickableGUI {

    private final NameChangeAutomation plugin;

    private ItemStack sign;
    private ItemStack accept, deny;
    private boolean itemsInitialized = false;

    @Override
    public Inventory getInventory(Player p) {
        return null;
    }

    public Inventory getInventory(WaitingAcceptData data) {
        initializeItems();
        Inventory inv = Bukkit.createInventory(null, 9 * 3, Chat.f("&aAccept NameChange &7- &e" + data.getNewID()));

        CSUtility util = new CSUtility();
        ItemStack beforeItem = util.generateWeapon(data.getPreviousID());
        ItemStack afterItem = util.generateWeapon(data.getNewID());

        ItemStack editedSign = sign.clone();
        ItemHelper.addLore(editedSign, "");
        ItemHelper.addLore(editedSign, Chat.f("&7作成者: &e{0}", data.getAuthorName()));

        inv.setItem(10, beforeItem);
        inv.setItem(11, editedSign);
        inv.setItem(12, afterItem);
        inv.setItem(14, accept);
        inv.setItem(16, deny);

        return inv;
    }

    @Override
    public void onClickInventory(Player p, InventoryClickEvent e) {
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null) {
            return;
        }
        if (e.getClickedInventory() != e.getInventory()) {
            return;
        }
        Inventory inv = e.getClickedInventory();
        String id = inv.getTitle().substring(inv.getTitle().indexOf(ChatColor.YELLOW + "") + 2);

        WaitingAcceptData data = plugin.getAcceptQueueWeapons().getWaitingData(id);

        if (data == null) {
            p.closeInventory();
            p.sendMessage(Chat.f("&aこの武器はすでに他の運営によって許可されました。"));
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
            return;
        }

        if (item.getType() == Material.STAINED_CLAY) {
            p.closeInventory();
            data.setAbleToDelete(true);
            p.sendMessage(Chat.f("&a銃データの使用を許可しました"));
            data.setCompleted();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                WaitingAcceptData data2 = plugin.getAcceptQueueWeapons().nextAcceptWeapon();
                if (data2 == null) {
                    return;
                }
                Inventory inv2 = ((AcceptNameChangeGUI) plugin.getGuiDistributor().getGUI(AcceptNameChangeGUI.class)).getInventory(data2);
                p.openInventory(inv2);
            }, 5);
        } else if (item.getType() == Material.BARRIER) {
            p.closeInventory();
            // deny
            plugin.getDenyWeapons().executeDenyProcess(data);
            p.sendMessage(Chat.f("&c銃データの使用を却下しました"));
            p.sendMessage(Chat.f("&7作者には名前変更アイテムと元武器が返却されます"));
            data.setCompleted();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                WaitingAcceptData data2 = plugin.getAcceptQueueWeapons().nextAcceptWeapon();
                if (data2 == null) {
                    return;
                }
                Inventory inv2 = ((AcceptNameChangeGUI) plugin.getGuiDistributor().getGUI(AcceptNameChangeGUI.class)).getInventory(data2);
                p.openInventory(inv2);
            }, 5);
        }
    }

    @Override
    public boolean isSameInventory(Inventory inv) {
        return inv.getTitle().startsWith(Chat.f("&aAccept NameChange &7- &e")) && inv.getSize() == 27;
    }

    public void initializeItems() {
        if (itemsInitialized) {
            return;
        }

        sign = ItemHelper.create(Material.SIGN, Chat.f("&e« &a元のアイテム"), Chat.f("&a完成後のアイテム &e»"));
        accept = ItemHelper.createItem(Material.STAINED_CLAY, 5, Chat.f("&a許可する"), Chat.f("&7これを押すと作成者は即座に交換/使用可能になります"));
        deny = ItemHelper.create(Material.BARRIER, Chat.f("&c却下する"), Chat.f("&7これを押すとデータは削除され、復旧できません！"));

        itemsInitialized = true;
    }
}
