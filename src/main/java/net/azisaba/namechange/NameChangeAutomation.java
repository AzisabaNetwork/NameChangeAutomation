package net.azisaba.namechange;

import com.shampaggon.crackshot.CSDirector;
import lombok.Getter;
import net.azisaba.namechange.chat.ChatReader;
import net.azisaba.namechange.command.NameChangeCommand;
import net.azisaba.namechange.command.NameChangeInfoCommand;
import net.azisaba.namechange.command.NameChangeReloadCommand;
import net.azisaba.namechange.config.PluginConfig;
import net.azisaba.namechange.data.AcceptQueueWeapons;
import net.azisaba.namechange.data.DenyWeapons;
import net.azisaba.namechange.data.NameChangeDataContainer;
import net.azisaba.namechange.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

@Getter
public class NameChangeAutomation extends JavaPlugin {

    public static NameChangeAutomation INSTANCE;

    private NameChangeDataContainer dataContainer;
    private AcceptQueueWeapons acceptQueueWeapons;
    private DenyWeapons denyWeapons;
    private ChatReader chatReader;

    public static Set<String> namedWeaponDisplayName = new HashSet<>();

    private PluginConfig pluginConfig;

    @Override
    public void onEnable() {
        INSTANCE = this;

        pluginConfig = new PluginConfig(this);
        pluginConfig.loadConfig();

        dataContainer = new NameChangeDataContainer(this, new File(getDataFolder(), "NameChangeData"));
        chatReader = new ChatReader(this);

        acceptQueueWeapons = new AcceptQueueWeapons(this);
        acceptQueueWeapons.load();

        denyWeapons = new DenyWeapons(this);
        denyWeapons.load();

        Bukkit.getPluginManager().registerEvents(new ItemClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LoadWeaponsFileListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(dataContainer), this);
        Bukkit.getPluginManager().registerEvents(new PaperClickListener(this), this);

        Bukkit.getPluginCommand("namechange").setExecutor(new NameChangeCommand(this));
        Bukkit.getPluginCommand("namechangereload").setExecutor(new NameChangeReloadCommand(this));
        Bukkit.getPluginCommand("namechangeinfo").setExecutor(new NameChangeInfoCommand());

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
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

                // トップレベルのキーを取得してループ処理
                Set<String> topLevelKeys = conf.getKeys(false);
                for (String key : topLevelKeys) {
                    // "Item_Information.Item_Name" のキーから値を取得し、リストに追加
                    String itemName = conf.getString(key + ".Item_Information.Item_Name");
                    if (itemName != null) {
                        namedWeaponDisplayName.add(itemName);
                    }
                }
            }
        }
    }

    public static Set<String> getNamedWeaponDisplayName (){
        Set<String> conversionDisplayName = new HashSet<>();
        Iterator<String> iterator = NameChangeAutomation.namedWeaponDisplayName.iterator();
        CSDirector CSD = (CSDirector) Bukkit.getPluginManager().getPlugin("CrackShot");

        while(iterator.hasNext()){
            if(CSD == null){
                return null;
            }
            conversionDisplayName.add(CSD.toDisplayForm(iterator.next()));
        }

        return conversionDisplayName;
    }

}


