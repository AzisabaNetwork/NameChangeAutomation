package net.azisaba.namechange.gui.items;

import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.config.NameChangeInfoIO;
import net.azisaba.namechange.data.NameChangeInfoData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemNIApprover extends GuiItem {
    NameChangeInfoData data;
    public ItemNIApprover(InventoryGui gui, String weaponNode,NameChangeInfoData data) {
        super(gui, new ItemStack(Material.PLAYER_HEAD));
        this.data = data;
        if(!data.getApproverUUID().equals("N/A")){
            SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(data.getApproverUUID())));
            this.itemStack.setItemMeta(skullMeta);
        }
        this.setDisplayName(Chat.f("&6承認者"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(Chat.f("PlayerName:" + data.getApproverName())));
        lore.add(Component.text(Chat.f("PlayerUUID:" + data.getApproverUUID())));
        lore.add(Component.text(Chat.f("&6クリックすることで変更ができます")));
        this.setLore(lore);
    }

    @Override
    public void onClick(InventoryClickEvent e){
        if(!NameChangeAutomation.INSTANCE.getPluginConfig().isLobby()){
            e.getWhoClicked().sendMessage(Chat.f("&cロビーでのみ使用可能です"));
            return;
        }
        NameChangeAutomation.INSTANCE.getChatReader().registerNextChat(gui.player, ChatContentType.APPROVER_PLAYER_NAME);
        gui.player.closeInventory();
        e.getWhoClicked().showTitle(Title.title(Component.text(Chat.f("&aチャットに変更したい名前を入力してください")), Component.empty(), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(5), Duration.ofSeconds(1))));
        gui.player.sendMessage(Component.text(Chat.f("&e⇓&aプレイヤー名を打ち込んで下さい！&e⇓  ")));
        gui.player.sendMessage(Component.text(Chat.f("&b[クリックで自分の名前を入力]")).clickEvent(ClickEvent.suggestCommand(gui.player.getName())));
        gui.player.sendMessage(Component.text(Chat.f("&b[クリックで登録済みの名前を入力]")).clickEvent(ClickEvent.suggestCommand(data.getApproverName())));
    }
}
