package net.azisaba.namechange.chat;

import com.shampaggon.crackshot.CSUtility;
import lombok.RequiredArgsConstructor;
import me.rayzr522.jsonmessage.JSONMessage;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.config.NameChangeInfoIO;
import net.azisaba.namechange.data.NameChangeInfoData;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageEditLore;
import net.azisaba.namechange.gui.pages.PageEditNameInfo;
import net.azisaba.namechange.gui.pages.PageNameChange;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
public class ChatReader {

    private final NameChangeAutomation plugin;

    private HashMap<UUID, ChatContentType> contentTypes = new HashMap<>();

    public void registerNextChat(Player p, ChatContentType type) {
        contentTypes.put(p.getUniqueId(), type);
    }

    public void unregisterNextChat(Player p) {
        if (contentTypes.containsKey(p.getUniqueId())) {
            contentTypes.remove(p.getUniqueId());
        }
    }

    public boolean isRegistered(Player p) {
        return contentTypes.containsKey(p.getUniqueId());
    }

    public void onChat(Player p, String msg) {
        if (!contentTypes.containsKey(p.getUniqueId())) {
            return;
        }
        ChatContentType type = contentTypes.get(p.getUniqueId());

        if (type == ChatContentType.DISPLAY_NAME) {
            plugin.getDataContainer().getNameChangeData(p).setDisplayName(msg);
            InventoryGui gui = new InventoryGui(p);
            gui.openPage(new PageNameChange(gui));
        } else if (type == ChatContentType.LORE) {
            if (msg.contains("|")) {
                p.sendMessage(Chat.f("&c使用できない文字が使われています！ &7( &e| &7)"));
                p.sendMessage(Chat.f("&a全角にするか、他の縦棒を使用してください。"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                JSONMessage.create(Chat.f("&e⇓&a説明文を打ち込んで下さい！&e⇓  "))
                        .suggestCommand("")
                        .then(Chat.f("&b[クリックで補完]"))
                        .suggestCommand(msg)
                        .send(p);
                return;
            }
            plugin.getDataContainer().getNameChangeData(p).receiveLoreChat(msg);
            InventoryGui gui = new InventoryGui(p);
            gui.openPage(new PageEditLore(gui));
        } else if (type == ChatContentType.LOREEX) {
            plugin.getDataContainer().getNameChangeData(p).receiveLoreChatCS(msg,true);
            InventoryGui gui = new InventoryGui(p);
            gui.openPage(new PageEditLore(gui));
        }else if (type == ChatContentType.LOREEXADD) {
            plugin.getDataContainer().getNameChangeData(p).receiveLoreChatCS(msg,false);
            InventoryGui gui = new InventoryGui(p);
            gui.openPage(new PageEditLore(gui));
        }else if (type == ChatContentType.LOREEXREMOVE) {
            boolean success = plugin.getDataContainer().getNameChangeData(p).receiveLoreChatDeleteCS(msg);
            if (success != true) {
                p.sendMessage(Chat.f("&c指定された行は存在しません"));
            }
            InventoryGui gui = new InventoryGui(p);
            gui.openPage(new PageEditLore(gui));
        }else if(type == ChatContentType.AUTHOR_PLAYER_NAME) {
            InventoryGui gui = new InventoryGui(p);
            String node = new CSUtility().getWeaponTitle(p.getInventory().getItemInMainHand());
            OfflinePlayer author = Bukkit.getOfflinePlayer(msg);
            if(node != null) {
                if (author == null) {
                    p.sendMessage(Chat.f("&c指定されたプレイヤーは存在しません!!"));
                } else {
                    NameChangeAutomation.INSTANCE.getNameInfoIO().saveAuthor(node, author);
                }
            }else {
                p.sendMessage("武器を手に持って実行してください" + node);
            }
            NameChangeInfoData data = NameChangeAutomation.INSTANCE.getNameInfoIO().load(node);
            gui.openPage(new PageEditNameInfo(gui,node,data));
        }else if(type == ChatContentType.APPROVER_PLAYER_NAME) {
            InventoryGui gui = new InventoryGui(p);
            String node = new CSUtility().getWeaponTitle(p.getInventory().getItemInMainHand());
            OfflinePlayer approver = Bukkit.getOfflinePlayer(msg);
            if(node != null) {
                if (approver == null) {
                    p.sendMessage(Chat.f("&c指定されたプレイヤーは存在しません!!"));
                } else {
                    new NameChangeInfoIO().saveApprover(node, approver);
                }
            } else {
                p.sendMessage("武器を手に持って実行してください" + node);
            }

            NameChangeInfoData data = NameChangeAutomation.INSTANCE.getNameInfoIO().load(node);
            gui.openPage(new PageEditNameInfo(gui,node,data));

        }else if(type == ChatContentType.BASE_WEAPON) {
            InventoryGui gui = new InventoryGui(p);
            String node = new CSUtility().getWeaponTitle(p.getInventory().getItemInMainHand());
            ItemStack baseWeapon = new CSUtility().generateWeapon(msg);
            if(baseWeapon == null){
                p.sendMessage(Chat.f("&c指定された武器は存在しません!!"));
            }else {
                NameChangeAutomation.INSTANCE.getNameInfoIO().saveBaseWeapon(node, msg);
            }
            NameChangeInfoData data = NameChangeAutomation.INSTANCE.getNameInfoIO().load(node);
            gui.openPage(new PageEditNameInfo(gui,node,data));

        }else if(type == ChatContentType.NUMBER) {
            InventoryGui gui = new InventoryGui(p);
            String node = new CSUtility().getWeaponTitle(p.getInventory().getItemInMainHand());
            String stringNumber = msg.replaceAll("[^0-9]", "");
            if(stringNumber != null) {
                NameChangeAutomation.INSTANCE.getNameInfoIO().saveCustomModelData(node, Integer.parseInt(stringNumber));
            }
            NameChangeInfoData data = NameChangeAutomation.INSTANCE.getNameInfoIO().load(node);
            gui.openPage(new PageEditNameInfo(gui,node,data));
        }

        contentTypes.remove(p.getUniqueId());
        return;
    }
}
