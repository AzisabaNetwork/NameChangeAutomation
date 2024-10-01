package net.azisaba.namechange.gui.items;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.util.NameChangeProgress;
import net.azisaba.namechange.utils.Chat;
import net.azisaba.namechange.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemConfirm extends GuiItem {
    public ItemConfirm(InventoryGui gui) {
        super(gui, new ItemStack(Material.GREEN_TERRACOTTA));
        this.setDisplayName(Chat.f("&cこれで申請する"));
    }

    @Override
    public void onClick(InventoryClickEvent e){
        gui.player.closeInventory();
        CSUtility util = new CSUtility();
        if (Objects.equals(util.getWeaponTitle(gui.player.getInventory().getItemInMainHand()), "NAME")) {
            if (gui.player.getInventory().getItemInMainHand().getAmount() > 1) {
                gui.player.sendMessage(Chat.f("&c重ねずに持ってから実行してください！"));
                return;
            }

            gui.player.getInventory().setItemInMainHand(null);
        } else {
            gui.player.sendMessage(Chat.f("&c手に名前変更コインをもって実行してください！"));
            return;
        }
        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        gui.player.sendMessage(Chat.f("&e処理しています..."));

        Bukkit.getScheduler().runTaskAsynchronously(NameChangeAutomation.INSTANCE, () -> {
            WaitingAcceptData waitingData = NameChangeAutomation.INSTANCE.getDataContainer().generateWeaponFile(data);

            if (data.getProgress() == NameChangeProgress.SUCCESS) {
                gui.player.sendMessage(Chat.f("&a成功しました。 &e名前変更引換券 &aを付与しました。 &c運営のチェックが終わり次第、右クリックで交換できます。"));
                ItemStack paper = ItemHelper.create(Material.PAPER, Chat.f("&e名前変更引換券: &b{0}", data.getNewWeaponID()), Chat.f("&c手に持って右クリックで変換！"));
                gui.player.getInventory().addItem(paper);

                NameChangeAutomation.INSTANCE.getAcceptQueueWeapons().addWaitingData(waitingData);

                CSDirector crackshot = (CSDirector) Bukkit.getPluginManager().getPlugin("CrackShot");
                crackshot.fillHashMaps(YamlConfiguration.loadConfiguration(waitingData.getFiles().getCrackShotFile()));

                NameChangeAutomation.INSTANCE.getDataContainer().unregisterNameChangeData(gui.player);
                NameChangeAutomation.INSTANCE.getDataContainer().removeFile(gui.player);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    // 運営にネームド追加を通知する (試合鯖にいる人にも通知出来たらしたい)
                    if (player.hasPermission("namechangeautomation.command.namechange")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a[Admin Message]: " + gui.player.getName() + "のネームド申請が追加されました"));
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 1f);
                    }
                }
            } else if (data.getProgress() == NameChangeProgress.FAIL) {
                gui.player.sendMessage(Chat.f("&c失敗しました。"));
                gui.player.getInventory().addItem(util.generateWeapon("NAME"));
            }
        });
    }
}
