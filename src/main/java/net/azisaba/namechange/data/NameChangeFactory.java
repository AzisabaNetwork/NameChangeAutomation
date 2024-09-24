package net.azisaba.namechange.data;

import java.util.Locale;
import lombok.NoArgsConstructor;
import net.azisaba.namechange.util.FactoryResponse;
import net.azisaba.namechange.util.NameChangeProgress;
import net.azisaba.namechange.utils.FileNameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class NameChangeFactory {

    public FactoryResponse executeForCrackShotFile(NameChangeData data) {
        File previousFile = null;
        File csFolder = new File("./plugins/CrackShot/weapons/");
        for (File file : Objects.requireNonNull(csFolder.listFiles(pathname -> {
            String fileName = pathname.getName().toLowerCase(Locale.ROOT);
            return fileName.endsWith(".yml") || fileName.endsWith(".yaml");
        }))) {
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
            return new FactoryResponse(FactoryResponse.FactoryStatus.FAIL, null);
        }

        data.lockNewWeaponID();

        File file = new File(new File(csFolder, "NameChange"),
            FileNameUtils.sanitize("namechange_" + data.getPreviousWeaponID() + ".yml"));
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
            return new FactoryResponse(FactoryResponse.FactoryStatus.SUCCESS, file);
        } catch (IOException e) {
            e.printStackTrace();
            return new FactoryResponse(FactoryResponse.FactoryStatus.FAIL, null);
        }
    }

    public FactoryResponse executeForCrackShotPlus(NameChangeData data) {
        File previousFile = null;
        File file = new File("./plugins/CrackShotPlus/weapons/CSP_NAME_CHANGE.yml");
        File cspFolder = new File("./plugins/CrackShotPlus/weapons/");
        if (!cspFolder.exists()) {
            return new FactoryResponse(FactoryResponse.FactoryStatus.NO_NEED, null);
        }
        for (File file2 : Objects.requireNonNull(cspFolder.listFiles(pathname -> {
            String fileName = pathname.getName().toLowerCase(Locale.ROOT);
            return fileName.endsWith(".yml") || fileName.endsWith(".yaml");
        }))) {
            YamlConfiguration conf2 = YamlConfiguration.loadConfiguration(file2);
            ConfigurationSection sec2 = conf2.getConfigurationSection("");
            if (sec2 == null) {
                continue;
            }
            if (sec2.getKeys(false).contains(data.getPreviousWeaponID())) {
                previousFile = file2;
                break;
            }
        }

        if (previousFile == null) {
            data.setProgress(NameChangeProgress.SUCCESS);
            return new FactoryResponse(FactoryResponse.FactoryStatus.NO_NEED, null);
        }

        YamlConfiguration cspPreviousConf = YamlConfiguration.loadConfiguration(previousFile);
        YamlConfiguration cspConf = YamlConfiguration.loadConfiguration(file);
        cspConf.set(data.getNewWeaponID(), cspPreviousConf.getConfigurationSection(data.getPreviousWeaponID()));

        try {
            cspConf.save(file);
            return new FactoryResponse(FactoryResponse.FactoryStatus.SUCCESS, file);
        } catch (IOException e) {
            e.printStackTrace();
            return new FactoryResponse(FactoryResponse.FactoryStatus.FAIL, null);
        }
    }

    //GSRを使わなくなったため未使用
    public FactoryResponse executeForGunScopeRecoil(NameChangeData data) {
        File file = new File("./plugins/GunScopeandRecoil/config_utf-8.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        boolean updated = false;
        if (conf.getConfigurationSection("Recoil." + data.getPreviousWeaponID()) != null) {
            conf.set("Recoil." + data.getNewWeaponID(), conf.getConfigurationSection("Recoil." + data.getPreviousWeaponID()));
            updated = true;
        }
        if (conf.getStringList("Scope").contains(data.getPreviousWeaponID())) {
            List<String> strList = conf.getStringList("Scope");
            strList.add(data.getNewWeaponID());
            conf.set("Scope", strList);
            updated = true;
        }

        if (!updated) {
            return new FactoryResponse(FactoryResponse.FactoryStatus.NO_NEED, null);
        }

        try {
            conf.save(file);
            return new FactoryResponse(FactoryResponse.FactoryStatus.SUCCESS, file);
        } catch (IOException e) {
            e.printStackTrace();
            return new FactoryResponse(FactoryResponse.FactoryStatus.FAIL, null);
        }
    }
}
