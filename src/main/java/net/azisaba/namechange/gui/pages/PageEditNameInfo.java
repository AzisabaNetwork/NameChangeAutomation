package net.azisaba.namechange.gui.pages;

import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.data.NameChangeInfoData;
import net.azisaba.namechange.gui.GuiPage;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.items.*;
import net.azisaba.namechange.utils.Chat;
import net.azisaba.namechange.utils.ItemHelper;
import org.bukkit.Material;

public class PageEditNameInfo extends GuiPage {
    String weaponNode;
    NameChangeInfoData data;
    public PageEditNameInfo(InventoryGui gui, String node, NameChangeInfoData data) {
        super(gui, Chat.f("&eName Change &7- &cNameChangeInfo"), 9);
        this.weaponNode = node;
        this.data = data;
    }

    @Override
    public void setUp() {
        String node = new CSUtility().getWeaponTitle(gui.player.getInventory().getItemInMainHand());
        this.setItem(1, gui.player.getInventory().getItemInMainHand());
        this.setItem(3, new ItemNIBaseWeapon(gui, node, data));
        this.setItem(4, new ItemNIAuthor(gui, node, data));
        this.setItem(5, new ItemNIApprover(gui, node, data));
        this.setItem(6, new ItemNICustomModelData(gui, node, data));
    }

    @Override
    public void back() {
        gui.player.closeInventory();
    }
}
