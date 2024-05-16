package killercreepr.crux.tags.format;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RawTextFormat {
    @NotNull String parse(@Nullable OfflinePlayer player, @NotNull String text);
}
