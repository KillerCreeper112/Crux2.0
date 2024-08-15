package killercreepr.cruxconfig.config.bukkit.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import killercreepr.crux.data.util.Pair;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.common.file.ICruxJson;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;

public class CruxJson extends CruxFolder implements ICruxJson, DataFile {
    protected final @NotNull JsonRegistry jsonRegistry;
    protected JsonObject json;
    protected FileReader reader;
    protected final Gson parser = new Gson();
    protected final boolean existedBefore;
    protected boolean isClosed = false;

    public CruxJson(@NotNull Plugin plugin, @NotNull String path){
        this(plugin, path, CfgRegistries.JSON);
    }
    public CruxJson(@NotNull File file){
        this(file, CfgRegistries.JSON);
    }

    public CruxJson(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists){
        this(plugin, path, CfgRegistries.JSON, reloadIfExists);
    }
    public CruxJson(@NotNull File file, boolean reloadIfExists){
        this(file, CfgRegistries.JSON, reloadIfExists);
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
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void setClosed(boolean value) {
        this.isClosed = value;
    }

    @Override
    public boolean existedBefore() {
        return existedBefore;
    }

    @Override
    public @Nullable FileElement getRoot() {
        if(json==null) return null;
        return FileElement.fromJson(json);
    }

    @Override
    public void serialize(@NotNull String path, @Nullable Object value) {
        Pair<FileObject, String> element = buildElementPath(path, (object, endPath) ->{
            if(value==null){
                object.remove(endPath);
                return;
            }
            object.add(endPath, jsonRegistry.serializeToFile(value));
        });
        json.add(element.getSecond(), element.getFirst().toJson());
    }

    public @NotNull Pair<FileObject, String> buildElementPath(@NotNull String path, @NotNull BiConsumer<FileObject, String> endResult){
        FileObject start = json == null ? new FileObject() : FileObject.fromJson(json);
        FileObject built = start;
        int index = 0;
        String[] split = path.split(Character.toString(pathSeparator));
        String startPath = null;
        for(String s : split){
            index++;
            if(index==1) startPath = s;
            if(index == split.length){
                endResult.accept(built, s);
                break;
            }
            if(built.get(s) instanceof FileObject o){
                built = o;
                continue;
            }
            FileObject newObject = new FileObject();
            built.add(s, newObject);
            built = newObject;
        }
        return new Pair<>(start, startPath);
    }

    @Override
    public @Nullable FileElement getElement(@NotNull String path) {
        if(json==null) return null;
        if(path.isBlank()) return getRoot();

        FileObject previous = FileObject.fromJson(json);
        int index = 0;
        String[] split = path.split(Character.toString(pathSeparator));
        for(String s : split){
            index++;
            if(index == split.length){
                return previous.get(s);
            }
            if(!(previous.get(s) instanceof FileObject got)) return null;
            previous = got;
        }
        return null;
    }

    @Override
    public <T> @Nullable T deserialize(@NotNull String path, @NotNull Type type) {
        if(json==null) return null;
        return jsonRegistry.deserializeFromFile(type, getElement(path));
    }

    @Override
    public @NotNull FileRegistry fileRegistry() {
        return jsonRegistry;
    }

    protected char pathSeparator = '.';
    @Override
    public char getPathSeparator() {
        return pathSeparator;
    }

    @Override
    public void setPathSeparator(char separator) {
        this.pathSeparator = separator;
    }
}
