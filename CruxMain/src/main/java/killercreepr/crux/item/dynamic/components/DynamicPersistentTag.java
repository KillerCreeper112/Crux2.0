package killercreepr.crux.item.dynamic.components;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.persistence.CruxPersistence;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxString;
import killercreepr.crux.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DynamicPersistentTag {
    public static final MappedRegistry<String, DynamicPersistTagHandler> HANDLERS = new SimpleMappedRegistry<>();
    protected final @NotNull String type;
    protected final @Nullable Object key;//nullable for custom handlers
    protected final @NotNull Object value;

    public DynamicPersistentTag(@NotNull String type, @Nullable Object key, @NotNull Object value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public @Nullable Key parseKey(@NotNull TextParserContext ctx){
        if(key==null) return null;
        return Crux.key(ctx.deserializeString(key.toString()));
    }

    public @NotNull Number parseNumber(@NotNull TextParserContext ctx){
        return CruxMath.evaluate(ctx.deserializeString(value.toString()));
    }

    public <T extends PersistentDataHolder> T apply(@NotNull T to, @NotNull TextParserContext ctx){
        apply(to.getPersistentDataContainer(), ctx);
        return to;
    }

    public <T extends PersistentDataHolder> T apply(@NotNull T to, @NotNull Key key, @NotNull TextParserContext ctx){
        apply(to.getPersistentDataContainer(), key, ctx);
        return to;
    }

    public <T extends PersistentDataContainer> T apply(@NotNull T to, @NotNull TextParserContext ctx){
        Key key = parseKey(ctx);
        return apply(to, key, ctx);
    }

    public <T, E extends PersistentDataContainer> List<T> parseList(@NotNull E to, @NotNull TextParserContext ctx){
        String subType = type.toLowerCase().replaceFirst("list/", "");
        Gson gson = new Gson();
        List<String> list = gson.fromJson(value.toString(), new TypeToken<List<String>>(){}.getType());
        List<T> parsedList = new ArrayList<>();
        list.forEach(v ->{
            DynamicPersistentTag tag = new DynamicPersistentTag(subType, null, v);
            ParseResult<?> result = tag.parseObject(to, tag.getValue(), ctx);
            if(result==null) return;
            if(result.getValue()==null) return;
            parsedList.add((T) result.getValue());
        });
        return parsedList;
    }


    public <T extends PersistentDataContainer> @Nullable ParseResult<?> parseObject(@NotNull T to, @NotNull Object value, @NotNull TextParserContext ctx){
        switch (type.toLowerCase()){
            case "key" ->{
                String s = ctx.deserializeString(value.toString());
                return ParseResult.result(CruxPersistence.CRUX_KEY, Crux.key(s));
            }
            case "list/key" ->{
                return ParseResult.result(CruxPersistence.LIST.KEY, parseList(to, ctx));
            }
            case "string" ->{
                String s = ctx.deserializeString(value.toString());
                return ParseResult.result(PersistentDataType.STRING, s);
            }
            case "list/string" ->{
                Objects.requireNonNull(key, "Key must be present!");
                return ParseResult.result(PersistentDataType.LIST.strings(), parseList(to, ctx));
            }
            case "double" ->{
                return ParseResult.result(PersistentDataType.DOUBLE, parseNumber(ctx).doubleValue());
            }
            case "list/double" ->{
                return ParseResult.result(PersistentDataType.LIST.doubles(), parseList(to, ctx));
            }
            case "float" ->{
                return ParseResult.result(PersistentDataType.FLOAT, parseNumber(ctx).floatValue());
            }
            case "list/float" ->{
                return ParseResult.result(PersistentDataType.LIST.floats(), parseList(to, ctx));
            }
            case "integer" ->{
                return ParseResult.result(PersistentDataType.INTEGER, parseNumber(ctx).intValue());
            }
            case "list/integer" ->{
                return ParseResult.result(PersistentDataType.LIST.integers(), parseList(to, ctx));
            }
            case "long" ->{
                return ParseResult.result(PersistentDataType.LONG, parseNumber(ctx).longValue());
            }
            case "list/long" ->{
                return ParseResult.result(PersistentDataType.LIST.longs(), parseList(to, ctx));
            }
            case "short" ->{
                return ParseResult.result(PersistentDataType.SHORT, parseNumber(ctx).shortValue());
            }
            case "list/short" ->{
                return ParseResult.result(PersistentDataType.LIST.shorts(), parseList(to, ctx));
            }
            case "boolean" ->{
                return ParseResult.result(PersistentDataType.BOOLEAN, CruxString.parseBoolean(ctx.deserializeString(value.toString())));
            }
            case "list/boolean" ->{
                return ParseResult.result(PersistentDataType.LIST.booleans(), parseList(to, ctx));
            }
            case "list/tag_container" ->{
                return ParseResult.result(PersistentDataType.LIST.dataContainers(), parseList(to, ctx));
            }
            default -> {
                DynamicPersistTagHandler handler = HANDLERS.get(type);
                if(handler != null) return handler.parseObject(to, value, ctx);
            }
        }
        return null;
    }

    public <T extends PersistentDataContainer> T apply(@NotNull T to, @Nullable Key key, @NotNull TextParserContext ctx, @NotNull String type){
        switch (type.toLowerCase()){
            /*case "key" ->{
                Objects.requireNonNull(key, "Key must be present!");
                String s = ctx.deserializeString(value.toString());
                CruxTag.set(to, key, CruxPersistence.CRUX_KEY, Crux.key(s));
            }
            case "string" ->{
                Objects.requireNonNull(key, "Key must be present!");
                String s = ctx.deserializeString(value.toString());
                CruxTag.set(to, key, PersistentDataType.STRING, s);
            }
            case "list/string" ->{
                Objects.requireNonNull(key, "Key must be present!");
                Gson gson = new Gson();
                List<String> list = gson.fromJson(value.toString(), new TypeToken<List<String>>(){}.getType());
                CruxTag.set(to, key, PersistentDataType.LIST.strings(), list);
            }
            case "double" ->{
                Objects.requireNonNull(key, "Key must be present!");
                CruxTag.set(to, key, PersistentDataType.DOUBLE, parseNumber(ctx).doubleValue());
            }
            case "float" ->{
                Objects.requireNonNull(key, "Key must be present!");
                CruxTag.set(to, key, PersistentDataType.FLOAT, parseNumber(ctx).floatValue());
            }
            case "integer" ->{
                Objects.requireNonNull(key, "Key must be present!");
                CruxTag.set(to, key, PersistentDataType.INTEGER, parseNumber(ctx).intValue());
            }
            case "long" ->{
                Objects.requireNonNull(key, "Key must be present!");
                CruxTag.set(to, key, PersistentDataType.LONG, parseNumber(ctx).longValue());
            }
            case "short" ->{
                Objects.requireNonNull(key, "Key must be present!");
                CruxTag.set(to, key, PersistentDataType.SHORT, parseNumber(ctx).shortValue());
            }
            case "boolean" ->{
                Objects.requireNonNull(key, "Key must be present!");
                CruxTag.set(to, key, PersistentDataType.BOOLEAN, CruxString.parseBoolean(ctx.deserializeString(value.toString())));
            }*/
            default -> {
                DynamicPersistTagHandler handler = HANDLERS.get(type);
                if(handler != null){
                    handler.handle(
                        this, to, key, value, ctx
                    );
                    return to;
                }
                ParseResult<?> result = parseObject(to, value, ctx);
                if(result != null) result.applyTo(to, Objects.requireNonNull(key, "Key must be present!"));
            }
        }
        return to;
    }

    public <T extends PersistentDataContainer> T apply(@NotNull T to, @Nullable Key key, @NotNull TextParserContext ctx){
        return apply(to, key, ctx, type);
    }

    public @NotNull String getType() {
        return type;
    }

    public @NotNull Object getValue() {
        return value;
    }

    public @Nullable Object getKey() {
        return key;
    }

    public static class ParseResult<T>{
        public static <T> ParseResult<T> result(@NotNull PersistentDataType<?, T> dataType, @Nullable T value){
            return new ParseResult<>(dataType, value);
        }
        protected final @NotNull PersistentDataType<?, T> dataType;
        protected final @Nullable T value;

        public ParseResult(@NotNull PersistentDataType<?, T> dataType, @Nullable T value) {
            this.dataType = dataType;
            this.value = value;
        }

        public <E extends PersistentDataContainer> E applyTo(E to, Key key){
            CruxTag.set(to, key, dataType, value);
            return to;
        }

        public @NotNull PersistentDataType<?, T> getDataType() {
            return dataType;
        }

        @Nullable
        public T getValue() {
            return value;
        }
    }
}
