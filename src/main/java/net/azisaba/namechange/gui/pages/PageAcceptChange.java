package net.azisaba.namechange.gui.pages;

import net.azisaba.namechange.data.WaitingAcceptData;
import net.azisaba.namechange.gui.GuiPage;
import net.azisaba.namechange.gui.InventoryGui;

public class PageAcceptChange extends GuiPage {

    private final WaitingAcceptData data;

    public PageAcceptChange(InventoryGui gui, WaitingAcceptData data) {
        super(gui, name, size);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void back() {

    }
}
