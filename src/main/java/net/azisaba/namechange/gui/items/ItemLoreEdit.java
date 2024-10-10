package net.azisaba.namechange.gui.items;

import me.rayzr522.jsonmessage.JSONMessage;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemLoreEdit extends GuiItem {
    int line;
    public ItemLoreEdit(InventoryGui gui,int line) {
        super(gui, new ItemStack(Material.WRITABLE_BOOK));
        this.setDisplayName(Chat.f("&cこの行を編集する"));
        this.line = line;
    }
    @Override
    public void onClick(InventoryClickEvent e){
        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        NameChangeAutomation.INSTANCE.getChatReader().registerNextChat(gui.player, ChatContentType.LORE);
        data.setLoreInput(line);
        gui.player.closeInventory();
        JSONMessage msg = JSONMessage.create(Chat.f("&e⇓&a説明文を打ち込んで下さい！&e⇓  "));
        if (data.getLore().size() >= line && data.getLore().get(line) != null) {
            msg.suggestCommand("")
                    .then(Chat.f("&b[クリックで補完]"))
                    .suggestCommand(LegacyComponentSerializer.legacyAmpersand().serialize(data.getLore().get(line)));
        }
        msg.send(gui.player);
    }
}
