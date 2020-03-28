package net.azisaba.namechange.data;

import lombok.RequiredArgsConstructor;
import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.gui.NameChangeGUI;
import net.azisaba.namechange.util.FactoryResponse;
import net.azisaba.namechange.util.NameChangeProgress;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class NameChangeDataContainer {

    private final NameChangeAutomation plugin;
    private final File dataFolder;
    private final NameChangeFactory factory = new NameChangeFactory();

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

        File acceptFile = new File(new File(plugin.getDataFolder(), "QueueWeapons/"), data.getNewWeaponID() + ".yml");

        FactoryResponse crackShotResponse = factory.executeForCrackShotFile(data);
        if (crackShotResponse.getStatus() == FactoryResponse.FactoryStatus.FAIL) {
            data.setProgress(NameChangeProgress.FAIL);
            return null;
        }

        FactoryResponse crackShotPlusResponse = factory.executeForCrackShotPlus(data);
        if (crackShotPlusResponse.getStatus() == FactoryResponse.FactoryStatus.FAIL) {
            data.setProgress(NameChangeProgress.FAIL);
            return null;
        }

        FactoryResponse gsrResponse = factory.executeForGunScopeRecoil(data);
        if (gsrResponse.getStatus() == FactoryResponse.FactoryStatus.FAIL) {
            data.setProgress(NameChangeProgress.FAIL);
            return null;
        }

        data.setProgress(NameChangeProgress.SUCCESS);
        DataFiles files = new DataFiles(acceptFile, crackShotResponse.getFile(), crackShotPlusResponse.getFile(), gsrResponse.getFile());
        return new WaitingAcceptData(files, data.getUuid(), data.getName(), data.getPreviousWeaponID(), data.getNewWeaponID());
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
