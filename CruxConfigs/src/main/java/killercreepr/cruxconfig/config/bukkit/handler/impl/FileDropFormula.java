package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.enchantment.DropFormula;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "drop_formula")
public class FileDropFormula extends SimpleFileHandler<DropFormula> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DropFormula object) {
        return null;
    }

    @Override
    public @Nullable DropFormula deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        Key type;
        FileObject o;
        if(e instanceof FileObject oo){
            o = oo;
            type = context.getRegistry().deserializeFromFile(Key.class, o.get("type"));
        }else{
            type = context.getRegistry().deserializeFromFile(Key.class, e);
            o = null;
        }
        if(type == null) return null;

        if(type.equals(Crux.key("ore_drops"))) return new DropFormula.OreDrops();
        if(type.equals(Crux.key("uniform"))){
            if(o == null) return null;
            return new DropFormula.Uniform(context.getRegistry().deserializeFromFile(Integer.class, o.get("bonus_multiplier")));
        }
        if(type.equals(Crux.key("binomial"))){
            if(o == null) return null;
            return new DropFormula.BinomialWithBonusCount(
                context.getRegistry().deserializeFromFile(Integer.class, o.get("extra_rounds")),
                context.getRegistry().deserializeFromFile(Float.class, o.get("probability"))
            );
        }
        return null;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "drop_formula";
    }
}
