package net.azisaba.namechange.gui.pages;

import net.azisaba.namechange.gui.GuiPage;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PageNameChange extends GuiPage {
    public PageNameChange(InventoryGui gui) {
        super(gui, Chat.f("&eName Change GUI"), 54);
    }

    @Override
    public void setUp() {
        for (int i = 0; i < 54; i++) {
            this.setItem(i, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    @Override
    public void back() {

    }
}
