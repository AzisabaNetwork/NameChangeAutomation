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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemNIBaseWeapon extends GuiItem {
    NameChangeInfoData data;
    public ItemNIBaseWeapon(InventoryGui gui, String weaponNode, NameChangeInfoData data) {
        super(gui, new ItemStack(Material.BARRIER));
        this.data = data;
        ItemStack baseWeapon = new CSUtility().generateWeapon(data.getBaseWeapon());
        this.itemStack = baseWeapon;
        List<Component> lore = new ArrayList<>();
        lore.add(baseWeapon.getItemMeta().displayName());
        lore.add(Component.text(Chat.f("&6クリックで変更")));
        this.setLore(lore);
        this.setDisplayName(Chat.f("&6元となった武器"));
    }

    @Override
    public void onClick(InventoryClickEvent e){
        NameChangeAutomation.INSTANCE.getChatReader().registerNextChat(gui.player, ChatContentType.BASE_WEAPON);
        gui.player.closeInventory();
        JSONMessage.create(Chat.f("&aチャットに変更したいベース武器のIDを入力してください")).title(0, 100, 20, gui.player);
        JSONMessage msg = JSONMessage.create(Chat.f("&e⇓&a武器IDを打ち込んで下さい！&e⇓  "));
        gui.player.getName();
        msg.suggestCommand("")
                .then(Chat.f("&b[クリックで登録済みのIDを入力]"))
                .suggestCommand(data.getBaseWeapon());
        msg.send(gui.player);
    }

}
