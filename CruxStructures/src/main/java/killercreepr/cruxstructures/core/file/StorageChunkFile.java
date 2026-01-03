package killercreepr.cruxstructures.core.file;

import com.google.gson.JsonArray;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StorageChunkFile extends CruxJson {
    public StorageChunkFile(@NotNull File file, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(file, jsonRegistry, reloadIfExists);
    }

    public StorageChunkFile(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(plugin, path, jsonRegistry, reloadIfExists);
    }

    public StorageChunkFile(@NotNull File file, @NotNull JsonRegistry jsonRegistry) {
        super(file, jsonRegistry);
    }

    public StorageChunkFile(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry) {
        super(plugin, path, jsonRegistry);
    }

    public StorageChunkFile(@NotNull File file, boolean reloadIfExists) {
        super(file, CfgRegistries.JSON_TAGGED, reloadIfExists);
    }

    public StorageChunkFile(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists) {
        super(plugin, path, CfgRegistries.JSON_TAGGED, reloadIfExists);
    }

    public StorageChunkFile(@NotNull File file) {
        super(file, CfgRegistries.JSON_TAGGED);
    }

    public StorageChunkFile(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path, CfgRegistries.JSON_TAGGED);
    }

    public @NotNull Map<CruxPosition, StoredStructure> structures(){
        reloadIfNeeded();
        if(json==null) return new HashMap<>();
        Map<CruxPosition, StoredStructure> map = new HashMap<>();

        if(!(json.get("structures") instanceof JsonArray a)) return map;

        a.forEach(ele ->{
            Crux.logError("eleayo=" + ele);
            if(!(jsonRegistry.deserializeFromJson(ele) instanceof StoredStructure str)) return;
            map.put(str.getPosition(), str);
        });

        return map;
    }

    public StorageChunkFile structures(@NotNull Collection<StoredStructure> values){
        reloadIfNeeded();
        JsonArray a = new JsonArray();
        values.forEach(str ->{
            if(!str.shouldPersist()) return;
            a.add(jsonRegistry.serializeToJson(str));
        });
        json.add("structures", a);
        return this;
    }
}
