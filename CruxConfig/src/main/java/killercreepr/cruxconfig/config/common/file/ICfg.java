package killercreepr.cruxconfig.config.common.file;

import killercreepr.crux.util.CruxReflect;
import killercreepr.cruxconfig.config.common.annotations.Config;
import killercreepr.cruxconfig.config.common.value.ICfgValue;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public interface ICfg<T, V extends ICfgValue<?>> extends ICruxConfig<T> {
    /**
     * Sets up the configuration contents and registers each values.
     */
    default void setup(){
        if(setupContents()) save();
        register();
    }

    /**
     * @return Whether the config contents where changed.
     */
    default boolean setupContents(){
        Config cfg = this.getClass().getAnnotation(Config.class);
        final boolean autoUpdate = cfg != null && cfg.autoUpdate();
        boolean checkForExisting = false;
        if(file().exists()){
            if(!autoUpdate) return false;
            checkForExisting = true;
        }
        return setupContents(checkForExisting);
    }

    /**
     *
     * @param checkForExisting If true and the config values' path already exists, it will not
     *                         overwrite it.
     * @return If the config was changed at all, returns true. Otherwise, false.
     */
    boolean setupContents(boolean checkForExisting);

    /**
     * @return All of the
     */
    default @NotNull LinkedHashMap<String, V> getAllValues(){

        LinkedHashMap<String, V> map = new LinkedHashMap<>();
        CruxReflect.getNonStaticParsedDeclaredFields(this).forEach((name, v) ->{
            if(!(v instanceof ICfgValue<?> value)) return;
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
