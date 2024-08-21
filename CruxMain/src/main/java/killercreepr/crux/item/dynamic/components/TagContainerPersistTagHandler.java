package killercreepr.crux.item.dynamic.components;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TagContainerPersistTagHandler implements DynamicPersistTagHandler {
    @Override
    public <T extends PersistentDataContainer> void handle(@NotNull DynamicPersistentTag tag, @NotNull T to,
                                                           @Nullable Key key,
                                                           @NotNull Object value,
                                                           @NotNull TextParserContext ctx) {
        Objects.requireNonNull(key);
        List<Map<String, String>> values = parseString(ctx.deserializeString(value.toString()));
        PersistentDataContainer c = to.getAdapterContext().newPersistentDataContainer();

        values.forEach(map ->{
            String type = map.get("type");
            String tagKey = map.get("key");
            String tagValue = map.get("value");
            if(type==null || tagValue == null) return;
            DynamicPersistentTag persistentTag = new DynamicPersistentTag(type, tagKey, tagValue);
            persistentTag.apply(c, ctx);
        });

        CruxTag.set(to, key, PersistentDataType.TAG_CONTAINER, c);
    }
    public List<Map<String, String>> parseString(@NotNull String string){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
        return gson.fromJson(string, listType);
    }
}
