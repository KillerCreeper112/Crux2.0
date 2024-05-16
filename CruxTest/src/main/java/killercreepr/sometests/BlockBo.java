package killercreepr.sometests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import killercreepr.crux.config.common.json.JsonSerializable;
import killercreepr.crux.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id= "block_bo")
public record BlockBo(int x, int y, int z) implements JsonSerializable {

    @Override
    public @NotNull JsonElement serializeToJson() {
        JsonObject o =new JsonObject();
        o.addProperty("x", x);
        o.addProperty("y", y);
        o.addProperty("z", z);
        return o;
    }

    public static @Nullable BlockBo deserializeFromJson(@Nullable JsonElement json){
        if(!(json instanceof JsonObject o)) return null;
        try{
            return new BlockBo(
                    o.get("x").getAsInt(),
                    o.get("y").getAsInt(),
                    o.get("z").getAsInt()
            );
        }catch (NullPointerException d){ return null; }
    }
}
