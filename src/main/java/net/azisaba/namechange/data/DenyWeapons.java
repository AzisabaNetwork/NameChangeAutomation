package net.azisaba.namechange.data;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DenyWeapons {

    private final NameChangeAutomation plugin;

    private List<String> denyIDList = new ArrayList<>();

    public boolean isDenied(String id) {
        return denyIDList.contains(id);
    }

    public void executeDenyProcess(WaitingAcceptData data) {
        if (!denyIDList.contains(data.getNewID())) {
            denyIDList.add(data.getNewID());
        }

        if (!data.getCrackShotDataFile().exists()) {
            return;
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(data.getCrackShotDataFile());
        if (conf.getConfigurationSection("") == null) {
            if (!data.getCrackShotDataFile().delete()) {
                plugin.getLogger().warning("Failed to delete file (" + data.getCrackShotDataFile().getAbsolutePath() + ")");
            }
        } else {
            if (conf.getConfigurationSection("").getKeys(false).size() > 1) {
                conf.set(data.getNewID(), null);
                try {
                    conf.save(data.getCrackShotDataFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (!data.getCrackShotDataFile().delete()) {
                    plugin.getLogger().warning("Failed to delete file (" + data.getCrackShotDataFile().getAbsolutePath() + ")");
                }
            }
        }

        if (data.getCrackShotPlusDataFile() == null) {
            return;
        }

        YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(data.getCrackShotPlusDataFile());
        if (conf2.getConfigurationSection("") == null) {
            if (!data.getCrackShotPlusDataFile().delete()) {
                plugin.getLogger().warning("Failed to delete file (" + data.getCrackShotPlusDataFile().getAbsolutePath() + ")");
            }
        } else {
            if (conf2.getConfigurationSection("").getKeys(false).size() > 1) {
                conf2.set(data.getNewID(), null);
                try {
                    conf2.save(data.getCrackShotPlusDataFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (!data.getCrackShotPlusDataFile().delete()) {
                    plugin.getLogger().warning("Failed to delete file (" + data.getCrackShotPlusDataFile().getAbsolutePath() + ")");
                }
            }
        }
    }

    public void deleteDenyInformation(String id) {
        denyIDList.remove(id);
        plugin.getAcceptQueueWeapons().getWaitingData(id);
    }

    public void load() {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "DenyWeapons.yml"));

        denyIDList = new ArrayList<>(conf.getStringList("WeaponID"));
    }

    public void save() {
        File file = new File(plugin.getDataFolder(), "DenyWeapons.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

        conf.set("WeaponID", denyIDList);

        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
