package net.azisaba.namechange.gui.items;

import me.rayzr522.jsonmessage.JSONMessage;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.data.NameChangeData;
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

public class ItemEditLoreCS extends GuiItem {
    public ItemEditLoreCS(InventoryGui gui) {
        super(gui, new ItemStack(Material.WRITTEN_BOOK));
        this.setDisplayName("CSの書式で一括入力する");
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',"&6 | で改行します")));
        lore.add(Component.text(ChatColor.translateAlternateColorCodes('&', "入力方法はShift+￥")));
        this.setLore(lore);
    }

    @Override
    public void onClick(InventoryClickEvent e){
        NameChangeAutomation.INSTANCE.getChatReader().registerNextChat(gui.player, ChatContentType.LOREEX);
        gui.player.closeInventory();
        JSONMessage msg = JSONMessage.create(Chat.f("&e⇓&a説明文を打ち込んで下さい！&e⇓  "));
        msg.send(gui.player);
    }
}
