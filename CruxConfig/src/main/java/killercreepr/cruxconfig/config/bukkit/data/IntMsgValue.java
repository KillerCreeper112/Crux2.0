package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.data.MsgContainer;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.ConfigValue;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class IntMsgValue extends ConfigValue<Map<Integer, MsgContainer>> {
    public IntMsgValue(@Nullable Map<Integer, MsgContainer> defaultValue) {
        super((Class<Map<Integer, MsgContainer>>) (Class<?>) Map.class, defaultValue);
    }

    public IntMsgValue() {
        this(null);
    }

    @Override
    public @Nullable Map<Integer, MsgContainer> get(@NotNull CruxConfig cfg, @NotNull String path) {
        ConfigurationSection section = cfg.config().getConfigurationSection(removeDot(path));
        if(section == null) return null;
        Map<Integer, MsgContainer> map = new HashMap<>();
        for(String s : section.getKeys(false)){
            MsgContainer msg = new MsgContainerValue().get(cfg, addDot(path) + s);
            if(msg!=null) map.put(Integer.parseInt(s), msg);
        }
        return map.isEmpty() ? null : map;
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Map<Integer, MsgContainer> object) {
        cfg.set(removeDot(path), null);
        if(object == null) return;
        object.forEach((key, value) -> new MsgContainerValue().set(cfg, addDot(path) + key, value));
    }
}
