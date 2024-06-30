package killercreepr.crux.tags.format;

import killercreepr.crux.registry.Registry;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.provider.StringListTagProvider;
import killercreepr.crux.tags.provider.StringTagProvider;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface FormatSerializer {
    @NotNull String serialize(@NotNull Component component);
    @NotNull Component deserialize(@NotNull String text);
    @NotNull Component deserialize(@NotNull String text, @Nullable StringTagProvider tagProvider);

    @NotNull String deserializeString(@NotNull String text);
    @NotNull String deserializeString(@NotNull String text, @Nullable StringTagProvider tagProvider);

    @NotNull List<Component> deserializeList(@NotNull Collection<String> list);
    @NotNull List<Component> deserializeList(@NotNull Collection<String> list, @Nullable MergedTagContainer tagProvider);

    @NotNull List<String> deserializeStringList(@NotNull Collection<String> list);
    @NotNull List<String> deserializeStringList(@NotNull Collection<String> list, @Nullable StringListTagProvider tagProvider);

    @Nullable List<String> parseStringList(@NotNull String text);
    @Nullable List<String> parseStringList(@NotNull String text, @Nullable MergedTagContainer tagProvider);

    @NotNull
    Registry<StringResolver> globalStringResolvers();
    @NotNull
    Registry<StringListResolver> globalStringListResolvers();
}
