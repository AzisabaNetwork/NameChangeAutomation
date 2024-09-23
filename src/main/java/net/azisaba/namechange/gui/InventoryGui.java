package net.azisaba.namechange.gui;

import org.bukkit.entity.Player;

public class InventoryGui {

    public final Player player;
    public GuiPage currentPage;

    public InventoryGui(Player player){
        this.player = player;
    }

    public void openPage(GuiPage page){
        page.setUp();
        this.player.openInventory(page.inventory);
        this.currentPage = page;
    }

    public void back(){
        this.currentPage.back();
    }
}
