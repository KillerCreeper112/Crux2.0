package killercreepr.crux.config.bukkit.file;

import com.google.gson.JsonObject;
import killercreepr.crux.config.bukkit.value.CfgValue;
import killercreepr.crux.config.common.file.ICfg;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonCfg extends CruxJson implements ICfg<JsonObject, CfgValue<?>> {
    /**
     * @param plugin The plugin to use as a base directory.
     * @param path The plath in which this file will exist.
     *             The path should not include '.yml' as it gets added on automatically.
     *             Valid path strings may look like:
     *             test -> test.yml
     *             main_folder/ayo -> main_folder/ayo.yml
     */
    public JsonCfg(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public JsonCfg(@NotNull File file) {
        super(file);
    }

    /**
     * Convenience method to use new Cfg(cfg.file())
     */
    public JsonCfg(@NotNull CruxJson cfg) {
        super(cfg.file());
    }

    @Override
    public void setup() {
        ICfg.super.setup();
    }

    @Override
    public boolean setupContents() {
        return ICfg.super.setupContents();
    }

    /**
     *
     * @param checkForExisting If true and the config values' path already exists, it will not
     *                         overwrite it.
     * @return If the config was changed at all, returns true. Otherwise, false.
     */
    @Override
    public boolean setupContents(boolean checkForExisting){
        boolean changed = false;
        for(Map.Entry<String, CfgValue<?>> entry : getAllValues().entrySet()){
            String path = entry.getKey();
            CfgValue<?> value = entry.getValue();
            if(checkForExisting && contains(path)) continue;
            Object v = value.value();
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
    public @NotNull LinkedHashMap<String, CfgValue<?>> getAllValues() {
        return ICfg.super.getAllValues();
    }

    @Override
    public @NotNull JsonObject config() {
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
        getAllValues().forEach((path, value) ->{
            Object found = get(path);
            value.getType().attemptSetValue(found);
        });
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
