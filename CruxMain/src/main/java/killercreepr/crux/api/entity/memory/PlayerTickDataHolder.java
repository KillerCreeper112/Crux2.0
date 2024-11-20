package killercreepr.crux.api.entity.memory;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlayerTickDataHolder extends TickedDataHolder {
    void tick(@NotNull Player e);
    boolean shouldRemoveFromMemory(@Nullable Player e);
    void removing(@Nullable Player e);
}
