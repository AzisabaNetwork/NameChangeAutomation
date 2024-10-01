package net.azisaba.namechange.gui.pages;

import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiPage;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.items.ItemCancel;
import net.azisaba.namechange.gui.items.ItemConfirm;
import net.azisaba.namechange.utils.Chat;
import net.azisaba.namechange.utils.ItemHelper;
import org.bukkit.Material;

public class PageLastConfirmation extends GuiPage {
    NameChangeData data;
    public PageLastConfirmation(InventoryGui gui, NameChangeData data) {
        super(gui, Chat.f("&eName Change &7- &cLast Confirm"), 9*3);
        this.data = data;
    }

    @Override
    public void setUp() {
        this.setItem(11, data.getNewItemStack());
        this.setItem(13, ItemHelper.create(Material.OAK_SIGN, Chat.f("&eこの内容で申請しますか？"), Chat.f("&7この操作は取り消せません！")));
        this.setItem(15, new ItemConfirm(gui));
        this.setItem(16, new ItemCancel(gui));
    }

    @Override
    public void back() {
        gui.openPage(new PageNameChange(gui));
    }
}
