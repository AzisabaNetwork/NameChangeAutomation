package net.azisaba.namechange.gui.pages;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiPage;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.items.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PageEditLore extends GuiPage {
    public PageEditLore(InventoryGui gui) {
        super(gui, ChatColor.translateAlternateColorCodes('&',"&eLore Edit GUI"), 54);
    }

    @Override
    public void setUp() {

        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        boolean hasLine = true;
        for (int line = 0; line < 6; line++) {
            ItemStack sign2 = new ItemStack(Material.OAK_SIGN);
            sign2.setAmount(line + 1);
            if (data != null) {
                Component lore = null;
                if (data.getLore() != null && data.getLore().size() > line) {
                    lore = data.getLore().get(line);
                }
                if (lore != null) {
                    ItemMeta meta = sign2.getItemMeta();
                    meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(lore)));
                    sign2.setItemMeta(meta);
                    hasLine = true;
                } else {
                    hasLine = false;
                }
            }

            this.setItem((line * 9) + 1, sign2);
            this.setItem((line * 9) + 2, new ItemLoreEdit(gui,line));
            if (hasLine) {
                if (line != 0)
                    this.setItem((line * 9) + 3, new ItemMoveToUp(gui,line));
                if (line != 5)
                    this.setItem((line * 9) + 4, new ItemMoveToDown(gui,line));
                this.setItem((line * 9) + 5, new ItemDeleteLore(gui,line));
            } else {
                int deleteItemSlot = (line * 9) + 4 - 9;
                if (deleteItemSlot >= 0) {
                    this.setItem(deleteItemSlot, new ItemStack(Material.AIR));
                }
            }
        }

        if (data != null) {
            this.setItem(16, new ItemAfter(gui,data));
        }

        this.setItem(43, new ItemLoreComplete(gui));
        this.setItem(0, new ItemEditLoreCS(gui));
    }

    @Override
    public void back() {
        gui.openPage(new PageNameChange(gui));
    }
}
