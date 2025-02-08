package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.data.NameChangeData;
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

public class ItemChangeDisplayName extends GuiItem {
    public ItemChangeDisplayName(InventoryGui gui) {
        super(gui, new ItemStack(Material.NAME_TAG));
        this.setDisplayName(Chat.f("&aアイテム名を変える"));
    }

    @Override
    public void onClick(InventoryClickEvent e){
        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        if (data == null) return;

        NameChangeAutomation.INSTANCE.getChatReader().registerNextChat(gui.player, ChatContentType.DISPLAY_NAME);
        gui.player.closeInventory();

        e.getWhoClicked().showTitle(Title.title(Component.text(Chat.f("&aチャットにアイテム名を打ち込んでください！")), Component.empty(), Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(5), Duration.ofSeconds(1))));
        Component msg = Component.text(Chat.f("&e⇓&aアイテム名を打ち込んで下さい！&e⇓  "));
        if (data.getDisplayName() != null) {
            gui.player.sendMessage(msg.append(Component.text(Chat.f("&b[クリックで補完]")).clickEvent(ClickEvent.suggestCommand(data.getDisplayName()))));
        }else {
            gui.player.sendMessage(msg);
        }
    }
}
