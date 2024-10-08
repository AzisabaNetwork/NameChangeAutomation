package net.azisaba.namechange.gui.pages;

import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiPage;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.items.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PageNameChange extends GuiPage {
    public PageNameChange(InventoryGui gui) {
        super(gui, ChatColor.translateAlternateColorCodes('&',"&eName Change GUI") , 54);
    }

    @Override
    public void setUp() {
        for (int i = 0; i < 54; i++) {
            this.setItem(i, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        }
        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        if(data != null){
            this.setItem(11, new ItemBefore(gui,data));
            this.setItem(15, new ItemAfter(gui,data));
        }else{
            this.setItem(11, new ItemStack(Material.AIR));
            this.setItem(15, new ItemStack(Material.AIR));
        }
        this.setItem(13, new ItemSign(gui));
        this.setItem(29, new ItemChangeDisplayName(gui));
        this.setItem(31, new ItemChangeLore(gui));
        this.setItem(33, new ItemComplete(gui));
        this.setItem(49, new ItemClose(gui));
    }

    @Override
    public void back() {
        gui.player.closeInventory();
    }
}
