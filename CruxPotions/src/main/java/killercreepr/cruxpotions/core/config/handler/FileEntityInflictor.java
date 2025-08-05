package killercreepr.cruxpotions.core.config.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxpotions.core.potions.inflictor.EntityInflictor;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FileEntityInflictor implements FileObjectHandler<EntityInflictor> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull EntityInflictor object) {
        return new FileObject()
            .add("entity_type", ctx.getRegistry().serializeToFile(object.getType()))
            .add("uuid", ctx.getRegistry().serializeToFile(object.uuid()));
    }

    @Override
    public @Nullable EntityInflictor deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        return new EntityInflictor(
            ctx.getRegistry().deserializeFromFile(UUID.class, o.get("uuid")),
            ctx.getRegistry().deserializeFromFile(Key.class, o.get("type"))
        );
    }
}
