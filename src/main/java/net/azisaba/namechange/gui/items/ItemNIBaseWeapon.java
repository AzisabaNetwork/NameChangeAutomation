package net.azisaba.namechange.gui.items;

import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.data.NameChangeInfoData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
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
        if(!NameChangeAutomation.INSTANCE.getPluginConfig().isLobby()){
            e.getWhoClicked().sendMessage(Chat.f("&cロビーでのみ使用可能です"));
            return;
        }
        NameChangeAutomation.INSTANCE.getChatReader().registerNextChat(gui.player, ChatContentType.BASE_WEAPON);
        gui.player.closeInventory();
        e.getWhoClicked().showTitle(Title.title(Component.text(Chat.f("&aチャットに変更したいベース武器のIDを入力してください")), Component.empty(), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(5), Duration.ofSeconds(1))));
        gui.player.sendMessage(Component.text(Chat.f("&e⇓&a武器IDを打ち込んで下さい！&e⇓  ")));
        gui.player.sendMessage(Component.text(Chat.f("&b[クリックで登録済みのIDを入力]")).clickEvent(ClickEvent.suggestCommand(data.getBaseWeapon())));
    }

}
