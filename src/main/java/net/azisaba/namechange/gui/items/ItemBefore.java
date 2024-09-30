package net.azisaba.namechange.gui.items;

import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ItemBefore extends GuiItem {

    public ItemBefore (InventoryGui gui, NameChangeData data) {

        super(gui, new CSUtility().generateWeapon(data.getPreviousWeaponID()));
    }
}
