package killercreepr.cruxconfig.config.bukkit.file;

import com.google.gson.JsonObject;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.common.file.IJsonCfg;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonCfg extends CruxJson implements IJsonCfg<JsonObject, CfgValue<?>>, DataFile {
    /**
     * @param plugin The plugin to use as a base directory.
     * @param path The plath in which this file will exist.
     *             The path should not include '.yml' as it gets added on automatically.
     *             Valid path strings may look like:
     *             test -> test.yml
     *             main_folder/ayo -> main_folder/ayo.yml
     */
    public JsonCfg(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path, false);
    }

    public JsonCfg(@NotNull File file) {
        super(file, false);
    }

    /**
     * Convenience method to use new Cfg(cfg.file())
     */
    public JsonCfg(@NotNull CruxJson cfg) {
        super(cfg.file(), false);
    }

    public JsonCfg(@NotNull File file, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(file, jsonRegistry, reloadIfExists);
    }

    public JsonCfg(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(plugin, path, jsonRegistry, reloadIfExists);
    }

    public JsonCfg(@NotNull File file, @NotNull JsonRegistry jsonRegistry) {
        super(file, jsonRegistry);
    }

    public JsonCfg(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry) {
        super(plugin, path, jsonRegistry);
    }

    public JsonCfg(@NotNull File file, boolean reloadIfExists) {
        super(file, reloadIfExists);
    }

    public JsonCfg(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists) {
        super(plugin, path, reloadIfExists);
    }

    /**
     *
     * @param overwriteExisting If true and the config values' path already exists, it will not
     *                         overwrite it.
     * @return If the config was changed at all, returns true. Otherwise, false.
     */
    @Override
    public boolean setupContents(boolean overwriteExisting){
        reloadIfNeeded();
        boolean changed = false;
        for(Map.Entry<String, CfgValue<?>> entry : getAllValues().entrySet()){
            String path = entry.getKey();
            CfgValue<?> value = entry.getValue();
            if(!overwriteExisting && contains(path)) continue;
            Object v = value.getDefaultValue();
            if(v==null) continue;
            //set value
            set(path, v);
            //set comments
            this.setComments(path, value.getComments());
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean setupContents(boolean overwriteExisting, boolean useCurrentValues) {
        reloadIfNeeded();
        boolean changed = false;
        for(Map.Entry<String, CfgValue<?>> entry : getAllValues().entrySet()){
            String path = entry.getKey();
            CfgValue<?> value = entry.getValue();
            if(!overwriteExisting && contains(path)) continue;
            Object v = useCurrentValues ? value.getValue() : value.getDefaultValue();
            //set value
            set(path, v);
            //set comments
            this.setComments(path, value.getComments());
            changed = true;
        }
        return changed;
    }

    @Override
    public void setup(boolean reloadAfter) {
        IJsonCfg.super.setup(reloadAfter);
        close();
    }

    @Override
    public @NotNull JsonObject config() {
        reloadIfNeeded();
        return json();
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if(value==null){
            remove(path);
            return;
        }
        add(path, value);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value, @NotNull String @Nullable ... comments) {
        if(value==null){
            remove(path);
            return;
        }
        add(path, value);
        setComments(path, comments);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value, @Nullable List<String> comments) {
        if(value==null){
            remove(path);
            return;
        }
        add(path, value);
        setComments(path, comments==null?null:comments.toArray(new String[0]));
    }

    @Override
    public void setComments(@NotNull String path, @NotNull String @Nullable ... comments) {
        //todo maybe?
    }

    @Override
    public boolean contains(@NotNull String path) {
        return json().has(path);
    }

    /**
     * Loads all config values into memory.
     */
    @Override
    public void register() {
        reloadIfNeeded();
        getAllValues().forEach((path, value) ->{
            Object found = deserialize(path, value.getParameterType());
            value.attemptSetValue(found);
        });
    }

    @Override
    public void reload() {
        super.reload();
    }

    @Override
    public boolean createDefault() {
        return super.createDefault();
    }

    @Override
    public boolean save() {
        return super.save();
    }

    @Override
    public @Nullable Object get(@NotNull String memberName) {
        return super.get(memberName);
    }
}
