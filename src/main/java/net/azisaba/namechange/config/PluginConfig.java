package net.azisaba.namechange.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.azisaba.namechange.NameChangeAutomation;

import java.util.List;

@Getter
public class PluginConfig extends Config {

    private List<String> nameChangeable;

    public PluginConfig(@NonNull NameChangeAutomation plugin) {
        super(plugin, "configs/config.yml", "config.yml");
    }

    @SneakyThrows(value = {Exception.class})
    @Override
    public void loadConfig() {
        super.loadConfig();

        nameChangeable = config.getStringList("NameChangeable");
    }
}