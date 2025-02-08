package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.chat.ChatContentType;
import net.azisaba.namechange.chat.ChatReader;
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
    public ItemEditLoreCS(InventoryGui gui,int slot) {
        super(gui, new ItemStack(Material.WRITTEN_BOOK));
        if(slot == 0){
            this.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&6CSの書式で一括入力する(上書き)"));
        } else if (slot == 9) {
            this.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&6CSの書式で一括入力する(追加)"));
        }else if (slot == 18) {
            this.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&6指定した行を削除する(1~)"));
        }

        List<Component> lore = new ArrayList<>();
        if(slot == 18){
            lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',"&6数字以外の文字は無視されます")));
        }else{
            lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',"&6 | で改行します")));
            lore.add(Component.text(ChatColor.translateAlternateColorCodes('&', "入力方法はShift+￥")));
        }

        this.setLore(lore);
    }

    @Override
    public void onClick(InventoryClickEvent e){
        int slot = e.getSlot();
        ChatReader chatReader = NameChangeAutomation.INSTANCE.getChatReader();
        if(slot == 0) {
            //Lore上書き
            chatReader.registerNextChat(gui.player, ChatContentType.LOREEX);
        } else if (slot == 9) {
            //Lore追加
            chatReader.registerNextChat(gui.player, ChatContentType.LOREEXADD);
        }else if (slot == 18) {
            //Lore指定行削除
            chatReader.registerNextChat(gui.player, ChatContentType.LOREEXREMOVE);
        }
        gui.player.closeInventory();
        if(slot == 18){
            //指定行削除用
            gui.player.sendMessage(Component.text(Chat.f("&e⇓&a削除したい行数を数字(半角)で打ち込んで下さい！&e⇓  ")));
        }else {
            gui.player.sendMessage(Component.text(Chat.f("&e⇓&a説明文を打ち込んで下さい！&e⇓  ")));
        }
    }
}
