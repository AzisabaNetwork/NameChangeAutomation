package net.azisaba.namechange.gui.items;

import com.shampaggon.crackshot.CSUtility;
import me.rayzr522.jsonmessage.JSONMessage;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.config.NameChangeInfoIO;
import net.azisaba.namechange.data.NameChangeInfoData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemNIAuthor extends GuiItem {
    NameChangeInfoData data;
    public ItemNIAuthor(InventoryGui gui, String weaponNode, NameChangeInfoData data) {
        super(gui, new ItemStack(Material.PLAYER_HEAD));
        this.data = data;
        SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(data.getAuthorUUID()));
        this.itemStack.setItemMeta(skullMeta);
        this.setDisplayName(Chat.f("&6作成者"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(Chat.f("PlayerName:" + data.getAuthorName())));
        lore.add(Component.text(Chat.f("PlayerUUID:" + data.getAuthorUUID())));
        lore.add(Component.text(Chat.f("&6クリックすることで変更ができます")));
        this.setLore(lore);
    }

    @Override
    public void onClick(InventoryClickEvent e){
        NameChangeAutomation.INSTANCE.getChatReader().registerNextChat(gui.player, ChatContentType.AUTHOR_PLAYER_NAME);
        gui.player.closeInventory();
        JSONMessage.create(Chat.f("&aチャットに変更したい名前を入力してください")).title(0, 100, 20, gui.player);
        JSONMessage msg = JSONMessage.create(Chat.f("&e⇓&aプレイヤー名を打ち込んで下さい！&e⇓  "));
        gui.player.getName();
        msg.suggestCommand("")
                .then(Chat.f("&b[クリックで自分の名前を入力]"))
                .suggestCommand(gui.player.getName())
                .then(Chat.f("&b[クリックで登録済みの名前を入力]"))
                .suggestCommand(data.getAuthorName());
        msg.send(gui.player);
    }
}
