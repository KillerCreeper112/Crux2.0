package killercreepr.cruxattributes.core.config.handler;

import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCruxSlotGroup implements FileObjectHandler<CruxSlotGroup> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxSlotGroup o) {
        return ctx.getRegistry().serializeToFile(o.key());
    }

    @Override
    public @Nullable CruxSlotGroup deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        Key key = ctx.getRegistry().deserializeFromFile(Key.class, e);
        if(key == null) return null;
        return CruxAttributeRegistries.SLOT_GROUP.get(key);
    }
}
