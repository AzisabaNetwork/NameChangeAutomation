package net.azisaba.namechange.data;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.utils.FileNameUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class AcceptQueueWeapons {

    private final NameChangeAutomation plugin;
    private List<WaitingAcceptData> queueWeaponDataList = new ArrayList<>();

    public WaitingAcceptData nextAcceptWeapon() {
        if (queueWeaponDataList.isEmpty()) {
            return null;
        }
        for (WaitingAcceptData data : new ArrayList<>(queueWeaponDataList)) {
            if (!data.isCompleted()) {
                return data;
            }
            if (data.isCompleted() && data.isAbleToDelete()) {
                if (data.getFiles().getWaitingAcceptFile().exists() && !data.getFiles().getWaitingAcceptFile().delete()) {
                    plugin.getLogger().warning("Failed to delete a file (" + data.getFiles().getWaitingAcceptFile().getAbsolutePath() + ")");
                }
                queueWeaponDataList.remove(data);
            }
        }
        return null;
    }

    public void addWaitingData(WaitingAcceptData data) {
        if (!queueWeaponDataList.contains(data)) {
            queueWeaponDataList.add(data);
        }
    }

    public WaitingAcceptData getWaitingData(String id) {
        for (WaitingAcceptData data : queueWeaponDataList) {
            if (data.getNewID().equals(id)) {
                return data;
            }
        }

        return null;
    }

    public void delete(WaitingAcceptData data) {
        queueWeaponDataList.remove(data);

        if (!data.getFiles().getWaitingAcceptFile().delete()) {
            Bukkit.getLogger().info("Failed to delete a file (" + data.getFiles().getWaitingAcceptFile().getAbsolutePath() + ")");
        }
    }

    public boolean isCompleted(String id) {
        for (WaitingAcceptData data : queueWeaponDataList) {
            if (!data.getNewID().equals(id)) {
                continue;
            }
            return data.isCompleted();
        }

        return true;
    }

    public void load() {
        File folder = new File(plugin.getDataFolder(), "QueueWeapons");

        if (!folder.exists()) {
            return;
        }

        File crackShotWeaponsDir = new File("./plugins/CrackShot/weapons/");
        File crackShotPlusDir = new File("./plugins/CrackShotPlus/weapons/");
        File gsrConfigFile = new File("plugins/GunScopeandRecoil/config_utf-8.yml");
        File nameChangeDir = new File("./plugins/CrackShot/weapons/NameChange/");

        for (File file : Objects.requireNonNull(folder.listFiles(pathname -> {
            String fileName = pathname.getName().toLowerCase(Locale.ROOT);
            return fileName.endsWith(".yml") || fileName.endsWith(".yaml");
        }))) {
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

            UUID uuid = UUID.fromString(conf.getString("UUID"));
            String authorName = conf.getString("Name");
            String previousID = conf.getString("PreviousID");
            String newID = conf.getString("NewID");
            String cspFileName = conf.getString("CSPFileName", null);
            boolean completed = conf.getBoolean("Completed");
            boolean hasGSRData = conf.getBoolean("HasGSRData");

            File csFile = new File(nameChangeDir, FileNameUtils.sanitize("namechange_" + previousID + ".yml"));
            File cspFile = null;
            if (cspFileName != null) {
                cspFile = new File(crackShotPlusDir, cspFileName);
            }
            File gsrFile = null;
            if (hasGSRData) {
                gsrFile = gsrConfigFile;
            }

            WaitingAcceptData data = new WaitingAcceptData(new DataFiles(file, csFile, cspFile, gsrFile), uuid, authorName, previousID, newID);
            if (completed) {
                data.setCompleted();
            }

            queueWeaponDataList.add(data);
        }
    }

    public void save() {
        for (WaitingAcceptData data : queueWeaponDataList) {
            if (data.isCompleted() && data.isAbleToDelete()) {
                if (data.getFiles().getWaitingAcceptFile().exists() && !data.getFiles().getWaitingAcceptFile().delete()) {
                    plugin.getLogger().warning("Failed to delete file (" + data.getFiles().getWaitingAcceptFile().getAbsolutePath() + ")");
                }
                continue;
            }
            YamlConfiguration conf = new YamlConfiguration();

            conf.set("UUID", data.getAuthorUUID().toString());
            conf.set("Name", data.getAuthorName());
            conf.set("PreviousID", data.getPreviousID());
            conf.set("NewID", data.getNewID());
            conf.set("Completed", data.isCompleted());
            if (data.getFiles().getCrackShotPlusFile() != null) {
                conf.set("CSPFileName", data.getFiles().getCrackShotPlusFile().getName());
            }
            conf.set("HasGSRData", data.getFiles().getGunScopeRecoilFile() != null);

            try {
                conf.save(data.getFiles().getWaitingAcceptFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
