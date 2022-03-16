package net.azisaba.namechange;

import com.shampaggon.crackshot.CSDirector;
import java.util.Locale;
import lombok.Getter;
import net.azisaba.namechange.chat.ChatReader;
import net.azisaba.namechange.command.NameChangeCommand;
import net.azisaba.namechange.config.PluginConfig;
import net.azisaba.namechange.data.AcceptQueueWeapons;
import net.azisaba.namechange.data.DenyWeapons;
import net.azisaba.namechange.data.NameChangeDataContainer;
import net.azisaba.namechange.gui.AcceptNameChangeGUI;
import net.azisaba.namechange.gui.EditLoreGUI;
import net.azisaba.namechange.gui.LastConfirmationGUI;
import net.azisaba.namechange.gui.NameChangeGUI;
import net.azisaba.namechange.gui.core.ClickableGUIDistributor;
import net.azisaba.namechange.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class NameChangeAutomation extends JavaPlugin {

    private ClickableGUIDistributor guiDistributor;
    private NameChangeDataContainer dataContainer;
    private AcceptQueueWeapons acceptQueueWeapons;
    private DenyWeapons denyWeapons;
    private ChatReader chatReader;

    private PluginConfig pluginConfig;

    @Override
    public void onEnable() {
        pluginConfig = new PluginConfig(this);
        pluginConfig.loadConfig();

        dataContainer = new NameChangeDataContainer(this, new File(getDataFolder(), "NameChangeData"));
        chatReader = new ChatReader(this);

        guiDistributor = new ClickableGUIDistributor();
        guiDistributor.registerClickableGUI(new NameChangeGUI(this));
        guiDistributor.registerClickableGUI(new EditLoreGUI(this));
        guiDistributor.registerClickableGUI(new LastConfirmationGUI(this));
        guiDistributor.registerClickableGUI(new AcceptNameChangeGUI(this));

        acceptQueueWeapons = new AcceptQueueWeapons(this);
        acceptQueueWeapons.load();

        denyWeapons = new DenyWeapons(this);
        denyWeapons.load();

        Bukkit.getPluginManager().registerEvents(new ItemClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClickableGUIListener(guiDistributor), this);
        Bukkit.getPluginManager().registerEvents(new LoadWeaponsFileListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GUIUpdateListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(dataContainer), this);
        Bukkit.getPluginManager().registerEvents(new PaperClickListener(this), this);

        Bukkit.getPluginCommand("namechange").setExecutor(new NameChangeCommand(this));

        for (Player p : Bukkit.getOnlinePlayers()) {
            dataContainer.loadData(p);
        }

        Plugin cs = Bukkit.getPluginManager().getPlugin("CrackShot");
        if (cs != null) {
            loadNameChangeWeapons((CSDirector) cs);
        }

        Bukkit.getLogger().info(getName() + " enabled.");
    }

    @Override
    public void onDisable() {
        if (guiDistributor != null) {
            guiDistributor.closeAllInventories();
        }
        if (dataContainer != null)
            dataContainer.save();
        if (acceptQueueWeapons != null)
            acceptQueueWeapons.save();
        if (denyWeapons != null)
            denyWeapons.save();

        Bukkit.getLogger().info(getName() + " disabled.");
    }

    public void loadNameChangeWeapons(CSDirector plugin) {
        File parentFile = new File(plugin.getDataFolder(), "weapons");
        File nameChangeDirectory = new File(parentFile, "NameChange");

        loadWeapons(plugin, nameChangeDirectory);

        plugin.csminion.completeList();
    }

    public void loadWeapons(CSDirector plugin, File directory) {
        File[] files = directory.listFiles(pathname -> {
            String fileName = pathname.getName().toLowerCase(Locale.ROOT);
            return fileName.endsWith(".yml") || fileName.endsWith(".yaml");
        });
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                loadWeapons(plugin, file);
                continue;
            }
            if (file.getName().toLowerCase().endsWith(".yml") || file.getName().toLowerCase().endsWith(".yaml")) {
                plugin.fillHashMaps(YamlConfiguration.loadConfiguration(file));
            }
        }
    }
}
