package killercreepr.crux.tags.ab.tags;

import killercreepr.crux.tags.ab.StringListTagProvider;
import killercreepr.crux.tags.ab.StringTagProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface FormatSerializer {
    @NotNull String serialize(@NotNull Component component);

    @NotNull Component deserialize(@NotNull String text);

    @NotNull Component deserialize(@NotNull String text, @Nullable StringTagProvider tagProvider);

    @NotNull String deserializeString(@NotNull String text, @Nullable StringTagProvider tagProvider);

    @NotNull List<String> deserialize(@NotNull Collection<String> list);
    @NotNull List<String> deserialize(@NotNull Collection<String> list, @Nullable StringListTagProvider tagProvider);

    @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @NotNull String text);
    @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @NotNull String text, @Nullable StringTagProvider tagProvider);
}
