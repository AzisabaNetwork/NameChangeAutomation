package net.azisaba.namechange.gui;


import lombok.RequiredArgsConstructor;
import me.rayzr522.jsonmessage.JSONMessage;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.data.NameChangeData;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

@RequiredArgsConstructor
public class EditLoreGUI extends ClickableGUI {

    private final NameChangeAutomation plugin;

    private boolean itemsInitialized = false;
    private ItemStack deleteLoreItem;
    private ItemStack editItem;
    private ItemStack moveToUp;
    private ItemStack moveToDown;
    private ItemStack lockedSlotItem;
    private ItemStack defaultSign;
    private ItemStack completeItem;

    @Override
    public Inventory getInventory(Player p) {
        initializeItems();

        /**
         * TODO: ここHashMapとかでInventory保存するなど最適化いるかも。時間無いからスキップ
         */

        Inventory inv = Bukkit.createInventory(null, 9 * 6, Chat.f("&eLore Edit GUI"));
        NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
        boolean hasLine = true;
        for (int line = 0; line < 6; line++) {
            ItemStack sign2 = defaultSign.clone();
            sign2.setAmount(line + 1);
            if (data != null) {
                String lore = null;
                if (data.getLore() != null && data.getLore().size() > line) {
                    lore = data.getLore().get(line);
                }
                if (lore != null) {
                    ItemMeta meta = sign2.getItemMeta();
                    meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + ChatColor.translateAlternateColorCodes('&', lore));
                    sign2.setItemMeta(meta);
                    hasLine = true;
                } else {
                    hasLine = false;
                }
            }

            inv.setItem((line * 9) + 1, sign2);
            inv.setItem((line * 9) + 2, editItem);
            if (hasLine) {
                if (line != 0)
                    inv.setItem((line * 9) + 3, moveToUp);
                if (line != 5)
                    inv.setItem((line * 9) + 4, moveToDown);
                inv.setItem((line * 9) + 5, deleteLoreItem);
            } else {
                int deleteItemSlot = (line * 9) + 4 - 9;
                if (deleteItemSlot >= 0)
                    inv.setItem(deleteItemSlot, null);
            }
        }

        if (data != null) {
            ItemStack newItem = data.getNewItemStack();
            if (newItem != null) {
                inv.setItem(16, newItem);
            }
        }

        inv.setItem(43, completeItem);

        return inv;
    }

    @Override
    public void onClickInventory(Player p, InventoryClickEvent e) {
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (item.isSimilar(completeItem)) {
            p.openInventory(plugin.getGuiDistributor().getGUI(NameChangeGUI.class).getInventory(p));
            return;
        }

        int slot = e.getSlot();
        if ((slot + 7) % 9 == 0) {
            int line = ((slot + 7) / 9);
            NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
            if (data == null) {
                p.sendMessage(ChatColor.RED + "先に銃をセットしてください！");
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                p.openInventory(plugin.getGuiDistributor().getGUI(NameChangeGUI.class).getInventory(p));
                return;
            }

            plugin.getChatReader().registerNextChat(p, ChatContentType.LORE);
            data.setLoreInput(line - 1);
            p.closeInventory();
            JSONMessage msg = JSONMessage.create(Chat.f("&e⇓&a説明文を打ち込んで下さい！&e⇓  "));
            if (data.getLore().size() >= line && data.getLore().get(line - 1) != null) {
                msg.suggestCommand("")
                        .then(Chat.f("&b[クリックで補完]"))
                        .suggestCommand(data.getLore().get(line - 1));
            }
            msg.send(p);
        } else if ((slot + 6) % 9 == 0) {
            int line = ((slot + 6) / 9);
            NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
            Collections.swap(data.getLore(), line - 1, line - 2);
            p.openInventory(getInventory(p));
        } else if ((slot + 5) % 9 == 0) {
            int line = ((slot + 5) / 9);
            NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
            Collections.swap(data.getLore(), line - 1, line);
            p.openInventory(getInventory(p));
        } else if ((slot + 4) % 9 == 0) {
            int line = ((slot + 4) / 9);
            NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
            if (data.getLore().size() >= line) {
                data.getLore().remove(line - 1);
            }

            p.openInventory(getInventory(p));
        }
    }

    @Override
    public boolean isSameInventory(Inventory inv) {
        return inv.getViewers().getFirst().getOpenInventory().getTitle().equals(Chat.f("&eLore Edit GUI")) && inv.getSize() == 9 * 6;
    }

    private void initializeItems() {
        if (itemsInitialized) {
            return;
        }

        deleteLoreItem = ItemHelper.create(Material.BARRIER, Chat.f("&cこの行を削除する"));
        editItem = ItemHelper.create(Material.WRITABLE_BOOK, Chat.f("&aこの行を編集する"));
        moveToUp = ItemHelper.create(Material.TORCH, Chat.f("&eこの行を上へ移動する"));
        moveToDown = ItemHelper.create(Material.REDSTONE_TORCH, Chat.f("&eこの行を下へ移動する"));
        lockedSlotItem = ItemHelper.create(Material.BARRIER, Chat.f("&cこの行はロックされています！"));
        defaultSign = ItemHelper.create(Material.OAK_SIGN, " ");
        completeItem = ItemHelper.createItem(Material.TERRACOTTA, 5, Chat.f("&a完了 &7(最初のGUIに戻る)"));

        itemsInitialized = true;
    }
}
