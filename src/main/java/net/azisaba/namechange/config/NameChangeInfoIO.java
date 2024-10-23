package net.azisaba.namechange.config;

import net.azisaba.namechange.NameChangeAutomation;
import net.azisaba.namechange.data.NameChangeData;
import net.azisaba.namechange.data.NameChangeInfoData;
import net.azisaba.namechange.data.WaitingAcceptData;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class NameChangeInfoIO {
    File dataFolder = NameChangeAutomation.INSTANCE.getDataFolder();
    File nameChangeInfoFolder = new File(dataFolder, "NameChangeInfo");
    public void save(WaitingAcceptData data) {
        if (!nameChangeInfoFolder.exists()) {
            if (nameChangeInfoFolder.mkdirs()) {
                System.out.println("フォルダ 'NameChangeInfo' が作成されました");
            } else {
                System.out.println("フォルダの作成に失敗しました");
            }
        }

        String weaponNodeName = data.getNewID();
        File yamlFile = new File(nameChangeInfoFolder, weaponNodeName + ".yml");

        if (!yamlFile.exists()) {
            try {
                yamlFile.createNewFile(); // 新規ファイルを生成
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration namedInfo = YamlConfiguration.loadConfiguration(yamlFile);

        //データ入力
        namedInfo.set(data.getNewID() + ".PreviousID", data.getPreviousID());
        namedInfo.set(data.getNewID() + ".AuthorName", data.getAuthorName());
        namedInfo.set(data.getNewID() + ".AuthorUUID", data.getAuthorUUID().toString());
        namedInfo.set(data.getNewID() + ".ApproverName", data.getApproverName());
        namedInfo.set(data.getNewID() + ".ApproverUUID", data.getApproverUUID().toString());
        namedInfo.set(data.getNewID() + ".Custom_Model_Data", data.getCustomModelData());


        // YAMLファイルを書き込む
        try {
            namedInfo.save(yamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(NameChangeInfoData data, String weaponNodeName) {
        if (!nameChangeInfoFolder.exists()) {
            if (nameChangeInfoFolder.mkdirs()) {
                System.out.println("フォルダ 'NameChangeInfo' が作成されました");
            } else {
                System.out.println("フォルダの作成に失敗しました");
            }
        }

        File yamlFile = new File(nameChangeInfoFolder, weaponNodeName + ".yml");

        if (!yamlFile.exists()) {
            try {
                yamlFile.createNewFile(); // 新規ファイルを生成
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration namedInfo = YamlConfiguration.loadConfiguration(yamlFile);

        //データ入力
        namedInfo.set(weaponNodeName + ".PreviousID", data.getBaseWeapon());
        namedInfo.set(weaponNodeName + ".AuthorName", data.getAuthorName());
        namedInfo.set(weaponNodeName + ".AuthorUUID", data.getAuthorUUID());
        namedInfo.set(weaponNodeName + ".ApproverName", data.getApproverName());
        namedInfo.set(weaponNodeName + ".ApproverUUID", data.getApproverUUID());
        namedInfo.set(weaponNodeName + ".Custom_Model_Data", data.getCustomModelData());


        // YAMLファイルを書き込む
        try {
            namedInfo.save(yamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBaseWeapon(String weaponNodeName, String baseWeaponNodeName) {
        File yamlFile = new File(nameChangeInfoFolder, weaponNodeName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);

        config.set(weaponNodeName + ".PreviousID", baseWeaponNodeName);

        try {
            config.save(yamlFile);  // ファイルに書き込む
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveApprover(String weaponNodeName, OfflinePlayer author) {
        File yamlFile = new File(nameChangeInfoFolder, weaponNodeName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);

        config.set(weaponNodeName + ".ApproverName", author.getName());
        config.set(weaponNodeName + ".ApproverUUID", author.getUniqueId().toString());

        try {
            config.save(yamlFile);  // ファイルに書き込む
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomModelData(String weaponNodeName, int cmd) {
        File yamlFile = new File(nameChangeInfoFolder, weaponNodeName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);

        config.set(weaponNodeName + ".Custom_Model_Data", cmd);

        try {
            config.save(yamlFile);  // ファイルに書き込む
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAuthor(String weaponNodeName, OfflinePlayer author) {
        File yamlFile = new File(nameChangeInfoFolder, weaponNodeName + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);

        config.set(weaponNodeName + ".AuthorName", author.getName());
        config.set(weaponNodeName + ".AuthorUUID", author.getUniqueId().toString());

        try {
            config.save(yamlFile);  // ファイルに書き込む
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NameChangeInfoData load(String weaponNode) {
        if (!nameChangeInfoFolder.exists()) {
            System.out.println("フォルダ 'NameChangeInfo' が存在しません");
            return null;
        }
        File yamlFile = new File(nameChangeInfoFolder, weaponNode + ".yml");
        if (!yamlFile.exists()) {
            System.out.println(weaponNode + ".yml が存在しません");
            return null;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
        if(config.contains(weaponNode)){
            String baseWeaponID = config.getString(weaponNode + ".PreviousID");
            String authorName = config.getString(weaponNode + ".AuthorName");
            String authorUUID = config.getString(weaponNode + ".AuthorUUID");
            String approverName = config.getString(weaponNode + ".ApproverName");
            String approverUUID = config.getString(weaponNode + ".ApproverUUID");
            int customModelData = config.getInt(weaponNode + ".Custom_Model_Data");
            return new NameChangeInfoData(baseWeaponID,authorName,authorUUID,approverName,approverUUID,customModelData);
        }else{
            return null;
        }
    }


}
