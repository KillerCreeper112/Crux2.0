package killercreepr.crux.item.dynamic.components;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.persistence.CruxPersistence;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxString;
import killercreepr.crux.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ToolComponentPersistTagHandler implements DynamicPersistTagHandler {
    @Override
    public <T extends PersistentDataContainer> void handle(@NotNull DynamicPersistentTag tag, @NotNull T to,
                                                           @Nullable Key key,
                                                           @NotNull Object value,
                                                           @NotNull TextParserContext ctx) {
        Objects.requireNonNull(key);
        DynamicPersistentTag.ParseResult<?> result = parseObject(to, value, ctx);
        if(result != null) result.applyTo(to, key);
    }

    @Override
    public <T extends PersistentDataContainer> DynamicPersistentTag.@Nullable ParseResult<?> parseObject(@NotNull T to,
                                                                                                         @NotNull Object value,
                                                                                                         @NotNull TextParserContext ctx) {
        List<Map<String, String>> values = parseString(ctx.deserializeString(value.toString()));
        PersistentDataContainer c = to.getAdapterContext().newPersistentDataContainer();

        List<PersistentDataContainer> list = new ArrayList<>();
        values.forEach(map -> {
            String block = map.get("blocks");
            String canHarvest = map.get("can_harvest");
            String speed = map.get("speed");

            if(block == null) return;
            PersistentDataContainer inner = to.getAdapterContext().newPersistentDataContainer();
            CruxTag.set(inner, "blocks", CruxPersistence.CRUX_KEY, Crux.key(ctx.deserializeString(block)));
            if(canHarvest != null){
                CruxTag.set(inner, "can_harvest", PersistentDataType.BOOLEAN, CruxString.parseBoolean(ctx.deserializeString(canHarvest)));
            }
            if(speed != null){
                CruxTag.set(inner, "speed", PersistentDataType.FLOAT, (float) CruxMath.evaluate(ctx.deserializeString(speed)));
            }
            list.add(inner);
        });
        CruxTag.set(c, "values", PersistentDataType.LIST.dataContainers(), list);

        return DynamicPersistentTag.ParseResult.result(PersistentDataType.TAG_CONTAINER, c);
    }

    public List<Map<String, String>> parseString(@NotNull String string){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
        return gson.fromJson(string, listType);
    }
}
