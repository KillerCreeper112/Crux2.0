package killercreepr.cruxconfig.config.bukkit.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import killercreepr.cruxconfig.config.common.file.ICruxJson;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.registry.DefaultJsonRegistry;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;

public class CruxJson extends CruxFolder implements ICruxJson {
    protected final @NotNull JsonRegistry jsonRegistry;
    protected JsonObject json;
    protected FileReader reader;
    protected final Gson parser = new Gson();
    protected final boolean existedBefore;

    public CruxJson(@NotNull Plugin plugin, @NotNull String path){
        this(plugin, path, DefaultJsonRegistry.REGISTRY);
    }
    public CruxJson(@NotNull File file){
        this(file, DefaultJsonRegistry.REGISTRY);
    }

    public CruxJson(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists){
        this(plugin, path, DefaultJsonRegistry.REGISTRY, reloadIfExists);
    }
    public CruxJson(@NotNull File file, boolean reloadIfExists){
        this(file, DefaultJsonRegistry.REGISTRY, reloadIfExists);
    }

   public CruxJson(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry) {
        this(plugin, path, jsonRegistry, true);
    }

    public CruxJson(@NotNull File file, @NotNull JsonRegistry jsonRegistry) {
        this(file, jsonRegistry, true);
    }

    public CruxJson(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(plugin, path + ".json");
        this.jsonRegistry = jsonRegistry;
        existedBefore = file.exists();
        if(reloadIfExists) reloadIfExists();
    }

    public CruxJson(@NotNull File file, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(file);
        this.jsonRegistry = jsonRegistry;
        existedBefore = file.exists();
        if(reloadIfExists) reloadIfExists();
    }

    @Override
    public @NotNull JsonRegistry jsonRegistry() {
        return jsonRegistry;
    }

    @Override
    public @Nullable FileReader reader() {
        return reader;
    }

    @Override
    public void reader(@Nullable FileReader reader) {
        this.reader = reader;
    }

    @Override
    public @Nullable JsonObject json() {
        return json;
    }

    @Override
    public void json(@Nullable JsonObject json) {
        this.json = json;
    }

    @Override
    public @NotNull Gson parser() {
        return parser;
    }

    @Override
    public boolean existedBefore() {
        return existedBefore;
    }
}
