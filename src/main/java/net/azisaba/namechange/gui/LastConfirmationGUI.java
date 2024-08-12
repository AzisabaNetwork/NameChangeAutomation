package net.azisaba.namechange.gui;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.core.ClickableGUI;
import net.azisaba.namechange.util.NameChangeProgress;
import net.azisaba.namechange.utils.Chat;
import net.azisaba.namechange.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@RequiredArgsConstructor
public class LastConfirmationGUI extends ClickableGUI {

    private final NameChangeAutomation plugin;

    private ItemStack confirm, cancel;
    private boolean itemsInitialized = false;

    @Override
    public Inventory getInventory(Player p) {
        initializeItems();

        Inventory inv = Bukkit.createInventory(null, 27, Chat.f("&eName Change &7- &cLast Confirm"));
        NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
        if (data == null) {
            return null;
        }

        inv.setItem(11, data.getNewItemStack());
        inv.setItem(13, ItemHelper.create(Material.OAK_SIGN, Chat.f("&eこの内容で申請しますか？"), Chat.f("&7この操作は取り消せません！")));
        inv.setItem(15, confirm);
        inv.setItem(16, cancel);

        return inv;
    }

    @Override
    public void onClickInventory(Player p, InventoryClickEvent e) {
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null) {
            return;
        }

        if (item.isSimilar(confirm)) {
            p.closeInventory();
            CSUtility util = new CSUtility();
            if (Objects.equals(util.getWeaponTitle(p.getInventory().getItemInMainHand()), "NAME")) {
                if (p.getInventory().getItemInMainHand().getAmount() > 1) {
                    p.sendMessage(Chat.f("&c重ねずに持ってから実行してください！"));
                    return;
                }

                p.getInventory().setItemInMainHand(null);
            } else {
                p.sendMessage(Chat.f("&c手に名前変更コインをもって実行してください！"));
                return;
            }
            NameChangeData data = plugin.getDataContainer().getNameChangeData(p);
            p.sendMessage(Chat.f("&e処理しています..."));

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                WaitingAcceptData waitingData = plugin.getDataContainer().generateWeaponFile(data);

                if (data.getProgress() == NameChangeProgress.SUCCESS) {
                    p.sendMessage(Chat.f("&a成功しました。 &e名前変更引換券 &aを付与しました。 &c運営のチェックが終わり次第、右クリックで交換できます。"));
                    ItemStack paper = ItemHelper.create(Material.PAPER, Chat.f("&e名前変更引換券: &b{0}", data.getNewWeaponID()), Chat.f("&c手に持って右クリックで変換！"));
                    p.getInventory().addItem(paper);

                    plugin.getAcceptQueueWeapons().addWaitingData(waitingData);

                    CSDirector crackshot = (CSDirector) Bukkit.getPluginManager().getPlugin("CrackShot");
                    crackshot.fillHashMaps(YamlConfiguration.loadConfiguration(waitingData.getFiles().getCrackShotFile()));

                    plugin.getDataContainer().unregisterNameChangeData(p);
                    plugin.getDataContainer().removeFile(p);
                } else if (data.getProgress() == NameChangeProgress.FAIL) {
                    p.sendMessage(Chat.f("&c失敗しました。"));
                    p.getInventory().addItem(util.generateWeapon("NAME"));
                }
            });
            return;
        }
        if (item.isSimilar(cancel)) {
            p.openInventory(plugin.getGuiDistributor().getGUI(NameChangeGUI.class).getInventory(p));
        }
    }

    @Override
    public boolean isSameInventory(Inventory inv) {
        return inv.getViewers().getFirst().getOpenInventory().getTitle().equals(Chat.f("&eName Change &7- &cLast Confirm")) && inv.getSize() == 27;
    }

    private void initializeItems() {
        if (itemsInitialized) {
            return;
        }

        confirm = ItemHelper.createItem(Material.TERRACOTTA, 5, Chat.f("&a申請する"), Chat.f("&7この操作は取り消せません！"));
        cancel = ItemHelper.create(Material.BARRIER, Chat.f("&cキャンセルする"));

        itemsInitialized = true;
    }
}
