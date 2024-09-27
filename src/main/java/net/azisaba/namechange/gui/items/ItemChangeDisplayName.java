package net.azisaba.namechange.gui.items;

import me.rayzr522.jsonmessage.JSONMessage;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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

        JSONMessage.create(Chat.f("&aチャットにアイテム名を打ち込んでください！")).title(0, 100, 20, gui.player);

        JSONMessage msg = JSONMessage.create(Chat.f("&e⇓&aアイテム名を打ち込んで下さい！&e⇓  "));
        if (data.getDisplayName() != null) {
            msg.suggestCommand("")
                    .then(Chat.f("&b[クリックで補完]"))
                    .suggestCommand(data.getDisplayName());
        }
        msg.send(gui.player);
    }
}
