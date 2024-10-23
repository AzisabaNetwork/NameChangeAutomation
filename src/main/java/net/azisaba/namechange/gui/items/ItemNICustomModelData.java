package net.azisaba.namechange.gui.items;

import com.shampaggon.crackshot.CSDirector;
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

public class ItemNICustomModelData extends GuiItem {
    NameChangeInfoData data;
    public ItemNICustomModelData(InventoryGui gui, String weaponNode, NameChangeInfoData data) {
        super(gui, new ItemStack(Material.BARRIER));
        this.data = data;
        this.itemStack = new CSUtility().generateWeapon(weaponNode);
        this.setCMD(data.getCustomModelData());
        this.setDisplayName(Chat.f("&6設定済みのCustomModelData:" + data.getCustomModelData()));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(Chat.f("&6クリックすることで変更ができます")));
        this.setLore(lore);
    }

    @Override
    public void onClick(InventoryClickEvent e){
        NameChangeAutomation.INSTANCE.getChatReader().registerNextChat(gui.player, ChatContentType.NUMBER);
        gui.player.closeInventory();
        JSONMessage.create(Chat.f("&aチャットに変更するCMDを入力してください")).title(0, 100, 20, gui.player);
        JSONMessage msg = JSONMessage.create(Chat.f("&e⇓&a数字を打ち込んで下さい！&e⇓  "));
        gui.player.getName();
        msg.suggestCommand("")
                .then(Chat.f("&b[クリックで登録済みのCMDを入力]"))
                .suggestCommand(String.valueOf(data.getCustomModelData()));
        msg.send(gui.player);
    }
}
