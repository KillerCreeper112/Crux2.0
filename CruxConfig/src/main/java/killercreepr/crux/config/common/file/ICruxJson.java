package killercreepr.crux.config.common.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import killercreepr.crux.config.common.json.JsonRegistry;
import killercreepr.crux.config.common.json.JsonSerializable;
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
        if(json() == null) reload();
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
            if(reader == null){
                reader = new FileReader(file);
                reader(reader);
            }
            json(parser().fromJson(reader, JsonObject.class));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
            try{ reader.close(); } catch (IOException | NullPointerException ignored){}
            return file().delete();
        }
        try {
            String jsonString = pretty ? new GsonBuilder().setPrettyPrinting().create().toJson(json) :
                    new GsonBuilder().create().toJson(json);
            FileWriter fw = new FileWriter(file());
            fw.write(jsonString);
            fw.flush();
            fw.close();
            reader.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    default void close(){
        JsonObject json = json();
        if(json == null) return;
        try{ reader().close(); } catch (IOException ignored){}
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
        return jsonRegistry().deserialize(base);
    }

    default <T> @Nullable T get(@Nullable JsonElement base, @NotNull Class<T> clazz){
        return jsonRegistry().deserialize(base, clazz);
    }

    default <T extends JsonSerializable> @NotNull JsonObject add(@NotNull String property, @NotNull T object){
        return add(json(), property, object);
    }

    /**
     * @return The first base JsonObject, for chaining.
     */
    default <T extends JsonSerializable> @NotNull JsonObject add(@NotNull JsonObject base, @NotNull String property, @NotNull T object){
        base.add(property, jsonRegistry().serialize(object));
        return base;
    }
}
