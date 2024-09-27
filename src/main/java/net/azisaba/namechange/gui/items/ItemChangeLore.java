package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.NameChangeGUI;
import net.azisaba.namechange.gui.pages.PageEditLore;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemChangeLore extends GuiItem {
    InventoryGui gui;
    public ItemChangeLore(InventoryGui gui) {
        super(gui, new ItemStack(Material.WRITABLE_BOOK));
        this.setDisplayName(Chat.f("&a説明文を変える"));
        this.gui = gui;
    }

    @Override
    public void onClick(InventoryClickEvent e){
        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        if(data == null){
            gui.player.sendMessage(ChatColor.RED + "先に銃をセットしてください！");
            gui.player.playSound(gui.player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            gui.player.openInventory(NameChangeAutomation.INSTANCE.getGuiDistributor().getGUI(NameChangeGUI.class).getInventory(gui.player));
            return;
        }
        gui.openPage(new PageEditLore(this.gui));
    }
}
