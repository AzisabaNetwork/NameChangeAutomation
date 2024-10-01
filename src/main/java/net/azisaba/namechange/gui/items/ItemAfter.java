package net.azisaba.namechange.gui.items;

import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import org.bukkit.ChatColor;

public class ItemAfter extends GuiItem {
    public ItemAfter (InventoryGui gui, NameChangeData data) {
        super(gui,data.getNewItemStack());
    }

    public ItemAfter (InventoryGui gui, WaitingAcceptData data) {
        super(gui,new CSUtility().generateWeapon(data.getNewID()));
        this.setCMD(data.getCustomModelData());
    }
}
