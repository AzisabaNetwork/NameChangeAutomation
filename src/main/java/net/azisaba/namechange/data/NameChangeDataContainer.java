package net.azisaba.namechange.data;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.gui.NameChangeGUI;
import net.azisaba.namechange.util.NameChangeProgress;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class NameChangeDataContainer {

    private final NameChangeAutomation plugin;
    private final File dataFolder;
    private HashMap<UUID, NameChangeData> nameChangeDataMap = new HashMap<>();

    public NameChangeData getNameChangeData(Player p) {
        return nameChangeDataMap.getOrDefault(p.getUniqueId(), null);
    }

    public void registerNewNameChangeData(Player p, NameChangeData data) {
        nameChangeDataMap.put(p.getUniqueId(), data);
    }

    public void unregisterNameChangeData(Player p) {
        nameChangeDataMap.remove(p.getUniqueId());
        ((NameChangeGUI) plugin.getGuiDistributor().getGUI(NameChangeGUI.class)).deleteInventoryCache(p);
    }

    public WaitingAcceptData generateWeaponFile(NameChangeData data) {
        data.setProgress(NameChangeProgress.PROCESSING);

        File previousFile = null;
        File csFolder = new File(".").toPath().resolve("./plugins/CrackShot/weapons/").toFile();
        for (File file : Objects.requireNonNull(csFolder.listFiles())) {
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection sec = conf.getConfigurationSection("");
            if (sec == null) {
                continue;
            }
            if (sec.getKeys(false).contains(data.getPreviousWeaponID())) {
                previousFile = file;
                break;
            }
        }

        if (previousFile == null) {
            data.setProgress(NameChangeProgress.FAIL);
            return null;
        }

        data.lockNewWeaponID();

        File file = new File(".").toPath().resolve("plugins/CrackShot/weapons/NameChange/namechange_" + data.getPreviousWeaponID() + ".yml").toFile();
        YamlConfiguration previousConf = YamlConfiguration.loadConfiguration(previousFile);
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection sec = previousConf.getConfigurationSection(data.getPreviousWeaponID());
        conf.set(data.getNewWeaponID(), sec);

        if (data.getDisplayName() != null) {
            conf.set(data.getNewWeaponID() + ".Item_Information.Item_Name", data.getDisplayName());
        }
        if (data.getLore() != null && !data.getLore().isEmpty()) {
            conf.set(data.getNewWeaponID() + ".Item_Information.Item_Lore", String.join("|", data.getLore()));
        }
        conf.set(data.getNewWeaponID() + ".Crafting.Enable", false);
        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            data.setProgress(NameChangeProgress.FAIL);
            return null;
        }

        File acceptFile = new File(new File(plugin.getDataFolder(), "QueueWeapons/"), data.getNewWeaponID() + ".yml");

        /*
         * CSP
         */
        File previousCSPFile = null;
        File cspFile = new File(".").toPath().resolve("plugins/CrackShotPlus/weapons/CSP_NAME_CHANGE.yml").toFile();
        File cspFolder = new File(".").toPath().resolve("plugins/CrackShotPlus/weapons/").toFile();
        if (!cspFolder.exists()) {
            data.setProgress(NameChangeProgress.SUCCESS);
            return new WaitingAcceptData(acceptFile, file, null, data.getUuid(), data.getName(), data.getPreviousWeaponID(), data.getNewWeaponID());
        }
        for (File file2 : Objects.requireNonNull(cspFolder.listFiles())) {
            YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file2);
            ConfigurationSection sec2 = conf2.getConfigurationSection("");
            if (sec2 == null) {
                continue;
            }
            if (sec2.getKeys(false).contains(data.getPreviousWeaponID())) {
                previousCSPFile = file2;
                break;
            }
        }

        if (previousCSPFile == null) {
            data.setProgress(NameChangeProgress.SUCCESS);
            return new WaitingAcceptData(acceptFile, file, null, data.getUuid(), data.getName(), data.getPreviousWeaponID(), data.getNewWeaponID());
        }

        YamlConfiguration cspPreviousConf = YamlConfiguration.loadConfiguration(previousCSPFile);
        YamlConfiguration cspConf = YamlConfiguration.loadConfiguration(cspFile);
        cspConf.set(data.getNewWeaponID(), cspPreviousConf.getConfigurationSection(data.getPreviousWeaponID()));

        try {
            cspConf.save(cspFile);
            data.setProgress(NameChangeProgress.SUCCESS);
            return new WaitingAcceptData(acceptFile, file, cspFile, data.getUuid(), data.getName(), data.getPreviousWeaponID(), data.getNewWeaponID());
        } catch (IOException e) {
            e.printStackTrace();
            data.setProgress(NameChangeProgress.FAIL);
            return null;
        }
    }

    public void loadData(Player p) {
        File file = new File(dataFolder, p.getUniqueId().toString() + ".yml");
        if (!file.exists()) {
            return;
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        UUID uuid = UUID.fromString(file.getName().substring(0, file.getName().indexOf(".")));
        String previousWeaponID = conf.getString("PreviousWeaponID");
        String displayName = conf.getString("DisplayName");
        List<String> lore = conf.getStringList("Lore");

        NameChangeData data = new NameChangeData(uuid, p.getName());
        data.setPreviousWeaponID(previousWeaponID);
        data.setDisplayName(displayName);
        data.setLore(lore);

        nameChangeDataMap.put(uuid, data);
    }

    public void save() {
        for (NameChangeData data : nameChangeDataMap.values()) {
            YamlConfiguration conf = new YamlConfiguration();

            conf.set("PreviousWeaponID", data.getPreviousWeaponID());
            conf.set("DisplayName", data.getDisplayName());
            conf.set("Lore", data.getLore());

            try {
                conf.save(new File(dataFolder, data.getUuid().toString() + ".yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeFile(Player p) {
        File file = new File(dataFolder, p.getUniqueId().toString() + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }
}
