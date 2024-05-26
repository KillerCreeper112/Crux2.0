package killercreepr.crux.tags.container;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.FormatContext;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.format.FormatPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IObjectTagResolverContainer {
    IObjectTagResolverContainer mergePrefixBuilders(@NotNull Map<String, PrefixBuilder> prefixBuilders);

    IObjectTagResolverContainer setPrefixBuilder(@NotNull String id, @NotNull PrefixBuilder prefix);

    IObjectTagResolverContainer hookAll(@NotNull DataExchange info);

    IObjectTagResolverContainer hookAll(@NotNull FormatContext context, @NotNull DataExchange info);

    IObjectTagResolverContainer hookAll(@NotNull DataExchange info, @Nullable Map<String, PrefixBuilder> prefix);

    IObjectTagResolverContainer hookAll(@NotNull FormatContext context, @NotNull DataExchange info, @Nullable Map<String, PrefixBuilder> prefixBuilders);

    IObjectTagResolverContainer hook(@Nullable Object info);

    IObjectTagResolverContainer hook(@NotNull FormatContext context, @Nullable Object info);

    //IObjectTagResolverContainer hook(@Nullable Object info, @Nullable FormatPrefix prefix);

    IObjectTagResolverContainer hook(@NotNull FormatContext context, @Nullable Object info, @Nullable FormatPrefix prefix);
    @NotNull Map<String, PrefixBuilder> getPrefixBuilders();
    interface PrefixBuilder{
        @Nullable FormatPrefix build(@NotNull DataExchange info, @NotNull String id, @NotNull Object object);
    }
}
