package net.azisaba.namechange.gui.pages;

import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.GuiPage;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.items.ItemAccept;
import net.azisaba.namechange.gui.items.ItemDeny;
import net.azisaba.namechange.gui.items.ItemSign;
import org.bukkit.inventory.ItemStack;

public class PageAcceptChange extends GuiPage {
    private final WaitingAcceptData data;

    public PageAcceptChange(InventoryGui gui, WaitingAcceptData data) {
        super(gui, "&aAccept NameChange &7- &e" + data.getNewID(), 9*6);
        this.data = data;
    }

    @Override
    public void setUp() {
        CSUtility util = new CSUtility();
        ItemStack beforeItem = util.generateWeapon(data.getPreviousID());
        ItemStack afterItem = util.generateWeapon(data.getNewID());
        this.setItem(10, beforeItem);
        this.setItem(11, new ItemSign(gui));
        this.setItem(12, afterItem);
        this.setItem(14, new ItemAccept(gui,data));
        this.setItem(16, new ItemDeny(gui,data));
    }

    @Override
    public void back() {

    }
}
