package net.azisaba.namechange.gui;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
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

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
public class NameChangeGUI extends ClickableGUI {

    private final NameChangeAutomation plugin;

    private boolean itemsInitialized = false;

    private ItemStack closeItem;
    private ItemStack changeDisplayNameItem;
    private ItemStack changeLoreItem;
    private ItemStack middleSign;
    private ItemStack completeItem;

    private HashMap<UUID, Inventory> inventoryMap = new HashMap<>();

    @Override
    public Inventory getInventory(Player p) {
        if (inventoryMap.containsKey(p.getUniqueId())) {
            return inventoryMap.get(p.getUniqueId());
        }

        initializeItems();

        Inventory inv = Bukkit.createInventory(null, 9 * 6, Chat.f("&eName Change GUI"));

        ItemStack backGroundItem = ItemHelper.createItem(Material.WHITE_STAINED_GLASS_PANE, 15, " ");
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, backGroundItem);
        }

        ItemStack beforeItem = null, afterItem = null;
        NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
        if (data != null) {
            CSUtility utility = new CSUtility();
            beforeItem = utility.generateWeapon(data.getPreviousWeaponID());

            if (beforeItem != null) {
                afterItem = beforeItem.clone();
                ItemMeta meta = afterItem.getItemMeta();

                if (data.getDisplayName() != null) {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', data.getDisplayName()));
                }
                if (data.getLore() != null) {
                    meta.setLore(data.getLore());
                }
                afterItem.setItemMeta(meta);
            }
        }

        inv.setItem(11, beforeItem);
        inv.setItem(13, middleSign);
        inv.setItem(15, afterItem);

        inv.setItem(29, changeDisplayNameItem);
        inv.setItem(31, changeLoreItem);
        inv.setItem(33, completeItem);

        inv.setItem(inv.getSize() - 5, closeItem);
        inventoryMap.put(p.getUniqueId(), inv);
        return inv;
    }

    @Override
    public void onClickInventory(Player p, InventoryClickEvent e) {
        initializeItems();
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item.getType() == Material.WHITE_STAINED_GLASS_PANE) {
            return;
        }
        if (item.isSimilar(closeItem)) {
            p.closeInventory();
            return;
        }

        Inventory inv = e.getClickedInventory();
        CSUtility csUtility = new CSUtility();
        String id = csUtility.getWeaponTitle(item);

        if (p.getInventory().equals(inv)) {
            if (e.getInventory().getItem(11) != null && e.getInventory().getItem(11).getType() != Material.AIR) {
                return;
            }

            CSDirector director = (CSDirector) Bukkit.getPluginManager().getPlugin("CrackShot");
            String inventoryControl = director.getString(id + ".Item_Information.Inventory_Control");

            if (inventoryControl == null) {
                return;
            }
            if (!plugin.getPluginConfig().getNameChangeable().contains(inventoryControl)) {
                p.sendMessage(Chat.f("&cこのアイテムは名前変更できません！"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                return;
            }
            if (item.getAmount() > 1) {
                p.sendMessage(Chat.f("&c1つにしてからクリックしてください！"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                return;
            }

            e.getInventory().setItem(11, item);
            e.getInventory().setItem(15, item);
            e.getClickedInventory().setItem(e.getSlot(), null);
            playSound(p);

            NameChangeData data = new NameChangeData(p.getUniqueId(), p.getName());
            data.setPreviousWeaponID(id);

            plugin.getDataContainer().registerNewNameChangeData(p, data);
            return;
        }

        NameChangeData data = plugin.getDataContainer().getNameChangeData(p);

        if (data == null) {
            return;
        }
        if (item.isSimilar(changeDisplayNameItem)) {
            plugin.getChatReader().registerNextChat(p, ChatContentType.DISPLAY_NAME);
            p.closeInventory();

            JSONMessage.create(Chat.f("&aチャットにアイテム名を打ち込んでください！")).title(0, 100, 20, p);

            JSONMessage msg = JSONMessage.create(Chat.f("&e⇓&aアイテム名を打ち込んで下さい！&e⇓  "));
            if (data.getDisplayName() != null) {
                msg.suggestCommand("")
                        .then(Chat.f("&b[クリックで補完]"))
                        .suggestCommand(data.getDisplayName());
            }
            msg.send(p);
            return;
        }
        if (item.isSimilar(changeLoreItem)) {
            p.openInventory(plugin.getGuiDistributor().getGUI(EditLoreGUI.class).getInventory(p));
            return;
        }
        if (item.isSimilar(completeItem)) {
            if (!data.canUseThisData()) {
                p.sendMessage(Chat.f("&aこのデータでは、他の銃と見分けがつきません！"));
                p.sendMessage(Chat.f("&eアイテム名を変更してください。"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                p.closeInventory();
                return;
            }
            Inventory inventory = plugin.getGuiDistributor().getGUI(LastConfirmationGUI.class).getInventory(p);
            if (inventory != null) {
                p.openInventory(inventory);
            }
            return;
        }

        if (e.getSlot() == 11) {
            boolean success = returnItem(p, item);
            if (success) {
                inv.setItem(e.getSlot(), null);
                inv.setItem(e.getSlot() + 4, null);
                playSound(p);
                plugin.getDataContainer().unregisterNameChangeData(p);
                plugin.getDataContainer().removeFile(p);
            }
        }
    }

    @Override
    public boolean isSameInventory(Inventory inv) {

        if (inv.getViewers().getFirst() == null) {
            return false;
        }

        return inv.getViewers().getFirst().getOpenInventory().getTitle().equals(Chat.f("&eName Change GUI")) && inv.getSize() == 9 * 6;
    }

    public void deleteInventoryCache(Player p) {
        inventoryMap.remove(p.getUniqueId());
    }

    private void initializeItems() {
        if (itemsInitialized) {
            return;
        }

        closeItem = ItemHelper.create(Material.BARRIER, Chat.f("&c閉じる"));
        changeDisplayNameItem = ItemHelper.create(Material.NAME_TAG, Chat.f("&aアイテム名を変える"));
        changeLoreItem = ItemHelper.create(Material.WRITABLE_BOOK, Chat.f("&a説明文を変える"));
        middleSign = ItemHelper.create(Material.OAK_SIGN, Chat.f("&e« &a元のアイテム"), Chat.f("&a完成後のアイテム &e»"));
        completeItem = ItemHelper.createItem(Material.TERRACOTTA, 5, Chat.f("&a次へ進む &7(最終確認)"));

        itemsInitialized = true;
    }

    private boolean returnItem(Player p, ItemStack item) {
        for (int i = 0; i < 36; i++) {
            ItemStack item2 = p.getInventory().getItem(i);
            if (item2 == null || item2.getType() == Material.AIR) {
                p.getInventory().setItem(i, item);
                return true;
            }
        }

        return false;
    }

    private void playSound(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
    }
}
