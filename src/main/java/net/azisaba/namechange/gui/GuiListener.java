package net.azisaba.namechange.gui;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.gui.pages.PageNameChange;
import net.azisaba.namechange.utils.Chat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    private final GuiPage page;

    public GuiListener(GuiPage page){
        this.page = page;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getView().getTopInventory() != page.inventory) return;

        GuiItem item = page.getItem(e.getSlot());
        if(item != null){
            item.onClick(e);
            if(e.getClick() == ClickType.RIGHT){
                item.onRightClick(e);
            }else if(e.getClick() == ClickType.LEFT){
                item.onLeftClick(e);
            }
        }

        e.setCancelled(true);
        Component pagenamechange = Component.text("Name Change GUI");
        String plainTitle = PlainTextComponentSerializer.plainText().serialize(e.getView().title());
        String plainPageNameChange = PlainTextComponentSerializer.plainText().serialize(pagenamechange);

        if(plainTitle.equals(plainPageNameChange)){
            Player p = (Player) e.getWhoClicked();
            ItemStack item2 = e.getCurrentItem();
            if(e.getView().getTopInventory() == e.getClickedInventory()){
                if (e.getSlot() == 11) {
                    boolean success = returnItem(p, item2);
                    if (success) {
                        Inventory inv = e.getClickedInventory();
                        inv.setItem(e.getSlot(), null);
                        inv.setItem(e.getSlot() + 4, null);
                        playSound(p);
                        NameChangeAutomation.INSTANCE.getDataContainer().unregisterNameChangeData(p);
                        NameChangeAutomation.INSTANCE.getDataContainer().removeFile(p);
                    }
                }
            }
            if(e.getClickedInventory() == page.inventory) return;
            CSUtility csUtility = new CSUtility();
            String id = csUtility.getWeaponTitle(item2);
            CSDirector director = (CSDirector) Bukkit.getPluginManager().getPlugin("CrackShot");
            String inventoryControl = director.getString(id + ".Item_Information.Inventory_Control");

            if (inventoryControl == null) {
                return;
            }
            if (!NameChangeAutomation.INSTANCE.getPluginConfig().getNameChangeable().contains(inventoryControl)) {
                p.sendMessage(Chat.f("&cこのアイテムは名前変更できません！"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                return;
            }
            if(item2 == null){
                return;
            }
            if (item2.getAmount() > 1) {
                p.sendMessage(Chat.f("&c1つにしてからクリックしてください！"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                return;
            }

            e.getInventory().setItem(11, item2);
            e.getInventory().setItem(15, item2);
            e.getClickedInventory().setItem(e.getSlot(), null);
            playSound(p);

            NameChangeData data = new NameChangeData(p.getUniqueId(), p.getName());
            data.setPreviousWeaponID(id);

            NameChangeAutomation.INSTANCE.getDataContainer().registerNewNameChangeData(p, data);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(e.getInventory() != page.inventory) return;
        page.close();
    }

    private void playSound(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
    }

    private boolean returnItem(Player p, ItemStack item) {
        for (int i = 0; i < 36; i++) {
            ItemStack item2 = p.getInventory().getItem(i);
            if (item2 == null || item2.getType() == Material.AIR) {
                p.getInventory().setItem(i, item);
                return true;
            }
        }

        return false;
    }

}
