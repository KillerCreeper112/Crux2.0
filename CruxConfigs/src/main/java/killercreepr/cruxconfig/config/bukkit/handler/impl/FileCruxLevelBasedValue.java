package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.enchantment.CruxLevelBasedValue;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "crux_level_based_value")
public class FileCruxLevelBasedValue extends SimpleFileHandler<CruxLevelBasedValue> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxLevelBasedValue object) {
        return null;
    }

    @Override
    public @Nullable CruxLevelBasedValue deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        Key type = ctx.getRegistry().deserializeFromFile(Key.class, o.get("type"));

        if(type.equals(Crux.key("linear"))){
            Float base = o.getObject(Float.class, "base");
            Float per_level_above_first = o.getObject(Float.class, "per_level_above_first");
            return new CruxLevelBasedValue.Linear(base, per_level_above_first);
        }
        //"type": "minecraft:linear",
        //            "base": 0.035,
        //            "per_level_above_first": 0.01
        return null;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_level_based_value";
    }
}
