package net.azisaba.namechange.data;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
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
                if (data.getAcceptDataFile().exists() && !data.getAcceptDataFile().delete()) {
                    plugin.getLogger().warning("Failed to delete a file (" + data.getAcceptDataFile().getAbsolutePath() + ")");
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

        if (!data.getAcceptDataFile().delete()) {
            Bukkit.getLogger().info("Failed to delete a file (" + data.getAcceptDataFile().getAbsolutePath() + ")");
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

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

            UUID uuid = UUID.fromString(conf.getString("UUID"));
            String authorName = conf.getString("Name");
            String previousID = conf.getString("PreviousID");
            String newID = conf.getString("NewID");
            String cspFileName = conf.getString("CSPFileName", null);
            boolean completed = conf.getBoolean("Completed");

            File csFile = new File(".").toPath().resolve("plugins/CrackShot/weapons/NameChange/namechange_" + previousID + ".yml").toFile();
            File cspFile = null;
            if (cspFileName != null) {
                cspFile = new File(".").toPath().resolve("plugins/CrackShotPlus/weapons/" + cspFileName).toFile();
            }

            WaitingAcceptData data = new WaitingAcceptData(file, csFile, cspFile, uuid, authorName, previousID, newID);
            if (completed) {
                data.setCompleted();
            }

            queueWeaponDataList.add(data);
        }
    }

    public void save() {
        for (WaitingAcceptData data : queueWeaponDataList) {
            if (data.isCompleted() && data.isAbleToDelete()) {
                if (data.getAcceptDataFile().exists() && !data.getAcceptDataFile().delete()) {
                    plugin.getLogger().warning("Failed to delete file (" + data.getAcceptDataFile().getAbsolutePath() + ")");
                }
                continue;
            }
            YamlConfiguration conf = new YamlConfiguration();

            conf.set("UUID", data.getAuthorUUID().toString());
            conf.set("Name", data.getAuthorName());
            conf.set("PreviousID", data.getPreviousID());
            conf.set("NewID", data.getNewID());
            conf.set("Completed", data.isCompleted());
            if (data.getCrackShotPlusDataFile() != null) {
                conf.set("CSPFileName", data.getCrackShotPlusDataFile().getName());
            }

            try {
                conf.save(data.getAcceptDataFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
