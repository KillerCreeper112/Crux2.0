package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.common.file.ICruxConfig;
import killercreepr.cruxconfig.config.common.value.IConfigValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigValue <T> implements IConfigValue<T, CruxConfig> {
    protected final Class<T> type;
    protected final T defaultValue;
    protected T value;

    public ConfigValue(@NotNull Class<T> type, @Nullable T defaultValue) {
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public ConfigValue(@NotNull Class<T> type) {
        this.type = type;
        this.defaultValue = null;
    }

    @Override
    public @NotNull Class<T> getType() {
        return type;
    }
    @Override
    public @Nullable T getDefaultValue() {
        return defaultValue;
    }
    @Override
    public @Nullable T getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable T value) {
        this.value = value;
    }

    @Override
    public @Nullable T register(@NotNull CruxConfig cfg, @NotNull String path) {
        setValue(get(cfg, path));
        return getValue();
    }

    @Override
    public abstract @Nullable T get(@NotNull CruxConfig cfg, @NotNull String path);
    @Override
    public abstract void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable T object);


    //Convenience methods.
    public @Nullable Component getComponent(){
        String s = getString();
        return s == null ? null : MiniMessage.miniMessage().deserialize(s);
    }

    //Convenience methods
    protected @NotNull String addDot(@NotNull String s){ return ICruxConfig.addDot(s); }
    protected @NotNull String removeDot(@NotNull String s){ return ICruxConfig.removeDot(s); }
}
