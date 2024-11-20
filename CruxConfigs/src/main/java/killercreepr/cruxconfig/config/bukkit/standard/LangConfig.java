package killercreepr.cruxconfig.config.bukkit.standard;

import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.core.communication.lang.TranslateMsg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LangConfig extends CruxConfig {
    public LangConfig(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public LangConfig(@NotNull File file) {
        super(file);
    }

    public LangConfig(@NotNull CruxConfig cfg) {
        super(cfg);
    }

    public LangConfig(@NotNull Plugin plugin, @NotNull String path, @NotNull YamlRegistry registry) {
        super(plugin, path, registry);
    }

    public LangConfig(@NotNull File file, @NotNull YamlRegistry registry) {
        super(file, registry);
    }

    public LangConfig(@NotNull CruxConfig cfg, @NotNull YamlRegistry registry) {
        super(cfg, registry);
    }

    public LangConfig populate(@NotNull CreateLang lang){
        ConfigurationSection section = cfg.getRoot();
        if(section==null) return this;
        for(String key : section.getKeys(false)){
            Communicator value = deserialize(Communicator.class, section, key);
            if(value==null) continue;
            lang.put(key, value);
        }
        return this;
    }

    public LangConfig setDefaults(@NotNull Class<?> type){
        try{
            for (Field field : type.getDeclaredFields()) {
                if(!Modifier.isStatic(field.getModifiers())) continue;
                if(!field.canAccess(null)) continue;

                if(!(field.get(null) instanceof TranslateMsg msg)) continue;
                set(msg.id(), msg.defaultValue());
            }
        }catch (IllegalArgumentException | IllegalAccessException ignored){}
        return this;
    }
}
