package killercreepr.cruxstructures.file;

import com.google.gson.JsonArray;
import killercreepr.crux.Crux;
import killercreepr.crux.data.BlockPos;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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
        super(file, reloadIfExists);
    }

    public StorageChunkFile(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists) {
        super(plugin, path, reloadIfExists);
    }

    public StorageChunkFile(@NotNull File file) {
        super(file);
    }

    public StorageChunkFile(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public @NotNull Map<BlockPos, StoredStructure> structures(){
        reloadIfExists();
        if(json==null) return new HashMap<>();
        Map<BlockPos, StoredStructure> map = new HashMap<>();

        if(!(json.get("structures") instanceof JsonArray a)) return map;

        a.forEach(ele ->{
            if(!(jsonRegistry.deserialize(ele) instanceof StoredStructure str)) return;
            map.put(str.getBlockPos(), str);
        });

        return map;
    }

    public StorageChunkFile structures(@NotNull Collection<StoredStructure> values){
        reloadIfNeeded();
        JsonArray a = new JsonArray();
        values.forEach(str ->{
            if(!str.shouldPersist()) return;
            Crux.log(Level.WARNING, str.getClass().getName() + " TEST TEST TEST");
            a.add(jsonRegistry.serializeObject(str));
        });
        json.add("structures", a);
        return this;
    }
}
