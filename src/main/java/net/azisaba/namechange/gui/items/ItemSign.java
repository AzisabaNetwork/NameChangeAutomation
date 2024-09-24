package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemSign extends GuiItem {
    public ItemSign(InventoryGui gui) {
        super(gui, new ItemStack(Material.OAK_SIGN));
        this.setDisplayName(Chat.f("&e« &a元のアイテム"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("&a完成後のアイテム &e»"));
        this.setLore(lore);
    }
}
