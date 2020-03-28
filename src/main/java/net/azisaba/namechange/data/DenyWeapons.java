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

        if (!data.getFiles().getCrackShotFile().exists()) {
            return;
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(data.getFiles().getCrackShotFile());
        if (conf.getConfigurationSection("") == null) {
            if (!data.getFiles().getCrackShotFile().delete()) {
                plugin.getLogger().warning("Failed to delete file (" + data.getFiles().getCrackShotFile().getAbsolutePath() + ")");
            }
        } else {
            if (conf.getConfigurationSection("").getKeys(false).size() > 1) {
                conf.set(data.getNewID(), null);
                try {
                    conf.save(data.getFiles().getCrackShotFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (!data.getFiles().getCrackShotFile().delete()) {
                    plugin.getLogger().warning("Failed to delete file (" + data.getFiles().getCrackShotFile().getAbsolutePath() + ")");
                }
            }
        }

        if (data.getFiles().getCrackShotPlusFile() != null) {
            YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(data.getFiles().getCrackShotPlusFile());
            if (conf2.getConfigurationSection("") == null) {
                if (!data.getFiles().getCrackShotPlusFile().delete()) {
                    plugin.getLogger().warning("Failed to delete file (" + data.getFiles().getCrackShotPlusFile().getAbsolutePath() + ")");
                }
            } else {
                if (conf2.getConfigurationSection("").getKeys(false).size() > 1) {
                    conf2.set(data.getNewID(), null);
                    try {
                        conf2.save(data.getFiles().getCrackShotPlusFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!data.getFiles().getCrackShotPlusFile().delete()) {
                        plugin.getLogger().warning("Failed to delete file (" + data.getFiles().getCrackShotPlusFile().getAbsolutePath() + ")");
                    }
                }
            }
        }

        if (data.getFiles().getGunScopeRecoilFile() != null) {
            YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(data.getFiles().getGunScopeRecoilFile());
            conf2.set("Recoil." + data.getNewID(), null);
            if (conf2.contains("Scope")) {
                List<String> strList = conf2.getStringList("Scope");
                strList.remove(data.getNewID());
                conf2.set("Scope", strList);
            }

            try {
                conf2.save(data.getFiles().getGunScopeRecoilFile());
            } catch (IOException e) {
                e.printStackTrace();
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
