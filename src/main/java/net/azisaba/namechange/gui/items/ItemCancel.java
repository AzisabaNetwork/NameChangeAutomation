package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemCancel extends GuiItem {
    public ItemCancel(InventoryGui gui) {
        super(gui, new ItemStack(Material.BARRIER));
        this.setDisplayName(Chat.f("&c却下する"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("&7これを押すとデータは削除され、復旧できません！"));
        this.setLore(lore);
    }

    @Override
    public void onClick(){
        gui.currentPage.back();
    }
}
