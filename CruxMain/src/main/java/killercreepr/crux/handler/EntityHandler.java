package killercreepr.crux.handler;

import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface EntityHandler {
    @NotNull
    Key getType(@NotNull Entity entity);

    class Dummy implements EntityHandler{
        @Override
        public @NotNull Key getType(@NotNull Entity entity) {
            return entity.getType().key();
        }
    }
}
