package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDeleteLore extends GuiItem {
    int line;
    public ItemDeleteLore(InventoryGui gui,int line) {
        super(gui, new ItemStack(Material.BARRIER));
        this.setDisplayName(Chat.f("&eこの行を削除する"));
        this.line = line;
    }

    @Override
    public void onClick(InventoryClickEvent e){
        int line = ((e.getSlot() + 4) / 9);
        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        data.getLore().remove(line - 1);
    }
}
