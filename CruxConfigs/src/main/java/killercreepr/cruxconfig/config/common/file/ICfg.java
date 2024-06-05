package killercreepr.cruxconfig.config.common.file;

import killercreepr.crux.util.CruxReflect;
import killercreepr.cruxconfig.config.common.annotations.Config;
import killercreepr.cruxconfig.config.common.value.IConfigValue;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public interface ICfg<T, V extends IConfigValue<?, ?>> extends ICruxConfig<T> {
    /**
     * Sets up the configuration contents and registers each values.
     */
    default void setup(){
        setup(true);
    }

    /**
     * @return Whether the config contents where changed.
     */
    default boolean setupContents(){
        return setupContents(false);
    }

    /**
     * @return Whether the config contents where changed.
     * @param useCurrentValues Whether the config should set the config up with the default config values or
     *                         the current set values.
     */
    default boolean setupContents(boolean useCurrentValues){
        Config cfg = this.getClass().getAnnotation(Config.class);
        final boolean autoUpdate = cfg != null && cfg.autoUpdate();
        boolean overwriteExisting = true;
        if(file().exists()){
            if(!autoUpdate) return false;
            overwriteExisting = false;
        }
        return setupContents(overwriteExisting, useCurrentValues);
    }

    /**
     *
     * @param overwriteExisting If false and the config values' path already exists, it will not
     *                         overwrite it.
     * @param useCurrentValues Whether the config should set the config up with the default config values or
     *                         the current set values.
     * @return If the config was changed at all, returns true. Otherwise, false.
     */
    boolean setupContents(boolean overwriteExisting, boolean useCurrentValues);

    /**
     * Convenience method for storage configs.
     */
    default boolean saveContents(){
        return saveContents(false);
    }

    default boolean saveContents(boolean overwriteExisting){
        if(setupContents(overwriteExisting, true)) return save();
        return false;
    }

    default void setup(boolean reloadAfter){
        if(setupContents()) save();
        if(!reloadAfter) return;
        reload();
        register();
    }
    /**
     * @return All of the
     */
    default @NotNull LinkedHashMap<String, V> getAllValues(){
        LinkedHashMap<String, V> map = new LinkedHashMap<>();
        CruxReflect.getNonStaticParsedDeclaredFields(this).forEach((name, v) ->{
            if(!(v instanceof IConfigValue<?, ?> value)) return;
            String path = value.getPath() == null ? name.toLowerCase() : value.getPath();
            map.put(path, (V) value);
        });
        return map;
    }

    /**
     * Loads all config values into memory.
     */
    @Override
    void register();
}
