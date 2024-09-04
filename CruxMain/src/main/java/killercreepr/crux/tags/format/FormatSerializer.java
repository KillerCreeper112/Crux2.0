package killercreepr.crux.tags.format;

import killercreepr.crux.registry.Registry;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.provider.StringListTagProvider;
import killercreepr.crux.tags.provider.StringTagProvider;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public interface FormatSerializer {
    static @NotNull Builder builder(){
        return new Format.Builder();
    }

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
    @NotNull
    TagParser tags();

    interface Builder{
        Builder addGlobalStringTag(@NotNull StringResolver tag);

        Builder addGlobalStringTags(@NotNull Collection<StringResolver> tags);

        Builder addGlobalStringListTag(@NotNull StringListResolver tag);

        Builder addGlobalStringListTags(@NotNull Collection<StringListResolver> tags);

        Builder miniMessage(MiniMessage miniMessage);

        Builder tagParser(TagParser tagParser);

        Builder stringPattern(Pattern stringPattern);

        Builder lorePattern(Pattern lorePattern);

        Builder equationPattern(Pattern equationPattern);

        Builder bEquationPattern(Pattern bEquationPattern);

        @NotNull FormatSerializer build();
    }
}
