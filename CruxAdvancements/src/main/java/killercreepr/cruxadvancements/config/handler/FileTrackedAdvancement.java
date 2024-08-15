package killercreepr.cruxadvancements.config.handler;

import killercreepr.crux.util.CruxObjects;
import killercreepr.cruxadvancements.data.TrackedAdvancement;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileTrackedAdvancement implements FileHandler<TrackedAdvancement> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TrackedAdvancement object) {
        FileRegistry registry = ctx.getRegistry();
        return new FileObject()
            .add("manager", registry.serializeToFileElement(object.getManagerKey()))
            .add("advancement", registry.serializeToFileElement(object.getAdvancementKey()))
            ;
    }

    @Override
    public @Nullable TrackedAdvancement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key manager = registry.deserializeFromFile(Key.class, o.get("manager"));
        Key advancement = registry.deserializeFromFile(Key.class, o.get("advancement"));
        if(CruxObjects.checkNull(manager, advancement)) return null;
        return new TrackedAdvancement(manager, advancement);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "tracked_advancement";
    }
}
