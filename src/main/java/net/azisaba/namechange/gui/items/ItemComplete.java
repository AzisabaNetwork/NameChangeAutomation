package net.azisaba.namechange.gui.items;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.GuiItem;
import net.azisaba.namechange.gui.InventoryGui;
import net.azisaba.namechange.gui.pages.PageLastConfirmation;
import net.azisaba.namechange.utils.Chat;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemComplete extends GuiItem {
    public ItemComplete(InventoryGui gui) {
        super(gui, new ItemStack(Material.GREEN_TERRACOTTA));
        this.setDisplayName(Chat.f("&a次へ進む &7(最終確認)"));
    }

    @Override
    public void onClick(InventoryClickEvent e){
        NameChangeData data = NameChangeAutomation.INSTANCE.getDataContainer().getNameChangeData(gui.player);
        if (data == null) return;

        if (!data.canUseThisData()) {
            gui.player.sendMessage(Chat.f("&aこのデータでは、他の銃と見分けがつきません！"));
            gui.player.sendMessage(Chat.f("&eアイテム名を変更してください。"));
            gui.player.playSound(gui.player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            gui.player.closeInventory();
            return;
        }
        gui.openPage(new PageLastConfirmation(gui));
    }
}
