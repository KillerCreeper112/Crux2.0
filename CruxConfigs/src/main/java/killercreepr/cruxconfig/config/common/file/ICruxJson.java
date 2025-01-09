package killercreepr.cruxconfig.config.common.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface ICruxJson extends ICruxFile {
    @NotNull
    JsonRegistry jsonRegistry();
    @Nullable FileReader reader();
    void reader(@Nullable FileReader reader);
    @Nullable
    JsonObject json();
    void json(@Nullable JsonObject json);

    @NotNull Gson parser();

    default void reloadIfNeeded(){
        if(json() == null || isClosed()) reload();
    }

    default JsonObject reloadIfExists(){
        if(!file().exists()) return null;
        reload();
        return json();
    }

    default void reload() {
        File file = file();
        try {
            if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if(!file.exists()) {
                PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8);
                pw.print("{");
                pw.print("}");
                pw.flush();
                pw.close();
            }
            FileReader reader = reader();
            if(reader != null){
                reader.close();
                setClosed(true);
            }
            if(reader == null || isClosed()){
                reader = new FileReader(file);
                reader(reader);
            }
            json(parser().fromJson(reader, JsonObject.class));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    boolean isClosed();
    void setClosed(boolean value);

    default boolean createDefault(){
        return createDefault(false);
    }

    default boolean createDefault(boolean pretty){
        if(existedBefore()) return false;
        setDefaults();
        return save(pretty);
    }

    default void setDefaults(){}

    default boolean save(){
        return save(false);
    }

    boolean existedBefore();

    default boolean save(boolean pretty) {
        JsonObject json = json();
        if(json == null) return false;
        FileReader reader = reader();
        if(json.isEmpty()){
            close();
            try{
                FileUtils.delete(file());
                return true;
            }catch (IOException ignored){
                return false;
            }
        }
        try {
            String jsonString = pretty ? new GsonBuilder().setPrettyPrinting().create().toJson(json) :
                    new GsonBuilder().create().toJson(json);
            FileWriter fw = new FileWriter(file());
            fw.write(jsonString);
            fw.flush();
            fw.close();
            reader.close();
            setClosed(true);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    default void close(){
        FileReader reader = reader();
        if(reader == null) return;
        try{ reader.close(); } catch (IOException ignored){}
        setClosed(true);
    }

    default @Nullable Object get(@NotNull String memberName){
        return get(json(), memberName);
    }

    default <T> @Nullable T get(@NotNull String memberName, @NotNull Class<T> clazz){
        return get(json(), memberName, clazz);
    }

    default @Nullable Object get(@NotNull JsonObject base, @NotNull String memberName){
        return get(base.get(memberName));
    }

    default <T> @Nullable T get(@NotNull JsonObject base, @NotNull String memberName, @NotNull Class<T> clazz){
        return get(base.get(memberName), clazz);
    }

    default @Nullable Object get(@Nullable JsonElement base){
        return jsonRegistry().deserializeFromJson(base);
    }

    default <T> @Nullable T get(@Nullable JsonElement base, @NotNull Class<T> clazz){
        return jsonRegistry().deserializeFromJson(clazz, base);
    }

    default <T> @NotNull JsonObject add(@NotNull String property, @NotNull T object){
        return add(json(), property, object);
    }

    default <T> @Nullable JsonElement remove(@NotNull String property){
        return remove(json(), property);
    }

    default <T> @Nullable JsonElement remove(@NotNull JsonObject base, @NotNull String property){
        return base.remove(property);
    }

    /**
     * @return The first base JsonObject, for chaining.
     */
    default <T> @NotNull JsonObject add(@NotNull JsonObject base, @NotNull String property, @NotNull T object){
        base.add(property, jsonRegistry().serializeToJson(object));
        return base;
    }
}
