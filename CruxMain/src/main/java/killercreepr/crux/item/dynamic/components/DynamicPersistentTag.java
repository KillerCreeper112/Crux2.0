package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.persistence.CruxPersistence;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxString;
import killercreepr.crux.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class DynamicPersistentTag {
    protected final @NotNull String type;
    protected final @NotNull Object key;
    protected final @NotNull Object value;

    public DynamicPersistentTag(@NotNull String type, @NotNull Object key, @NotNull Object value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public @NotNull Key parseKey(@NotNull TextParserContext ctx){
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

    public <T extends PersistentDataContainer> T apply(@NotNull T to, @NotNull Key key, @NotNull TextParserContext ctx){
        switch (type.toLowerCase()){
            case "key" ->{
                String s = ctx.deserializeString(value.toString());
                CruxTag.set(to, key, CruxPersistence.CRUX_KEY, Crux.key(s));
            }
            case "string" ->{
                String s = ctx.deserializeString(value.toString());
                CruxTag.set(to, key, PersistentDataType.STRING, s);
            }
            case "double" ->{
                CruxTag.set(to, key, PersistentDataType.DOUBLE, parseNumber(ctx).doubleValue());
            }
            case "float" ->{
                CruxTag.set(to, key, PersistentDataType.FLOAT, parseNumber(ctx).floatValue());
            }
            case "integer" ->{
                CruxTag.set(to, key, PersistentDataType.INTEGER, parseNumber(ctx).intValue());
            }
            case "long" ->{
                CruxTag.set(to, key, PersistentDataType.LONG, parseNumber(ctx).longValue());
            }
            case "short" ->{
                CruxTag.set(to, key, PersistentDataType.SHORT, parseNumber(ctx).shortValue());
            }
            case "boolean" ->{
                CruxTag.set(to, key, PersistentDataType.BOOLEAN, CruxString.parseBoolean(ctx.deserializeString(value.toString())));
            }
        }
        return to;
    }

    public @NotNull String getType() {
        return type;
    }

    public @NotNull Object getValue() {
        return value;
    }

    public @NotNull Object getKey() {
        return key;
    }
}
