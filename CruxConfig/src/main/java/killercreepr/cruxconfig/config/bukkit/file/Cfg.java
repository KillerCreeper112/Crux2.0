package killercreepr.cruxconfig.config.bukkit.file;

import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.common.file.ICfg;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

public class Cfg extends CruxConfig implements ICfg<MemoryConfiguration, CfgValue<?>> {
    /**
     * @param plugin The plugin to use as a base directory.
     * @param path The plath in which this file will exist.
     *             The path should not include '.yml' as it gets added on automatically.
     *             Valid path strings may look like:
     *             test -> test.yml
     *             main_folder/ayo -> main_folder/ayo.yml
     */
    public Cfg(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public Cfg(@NotNull File file) {
        super(file);
    }

    /**
     * Convenience method to use new Cfg(cfg.file())
     */
    public Cfg(@NotNull CruxConfig cfg) {
        super(cfg);
    }

    @Override
    public boolean setupContents(boolean overwriteExisting, boolean useCurrentValues){
        boolean changed = false;
        for(Map.Entry<String, CfgValue<?>> entry : getAllValues().entrySet()){
            String path = entry.getKey();
            CfgValue<?> value = entry.getValue();
            if(!overwriteExisting && contains(path)) continue;
            //set value
            if(useCurrentValues) value.getType().setCurrentValue(this, path);
            else value.getType().setDefault(this, path);
            //set comments
            this.setComments(path, value.getComments());
            changed = true;
        }
        return changed;
    }



    /**
     * Loads all config values into memory.
     */
    @Override
    public void register() {
        super.register();
        getAllValues().forEach((path, value) -> value.getType().register(this, path));
    }
}
