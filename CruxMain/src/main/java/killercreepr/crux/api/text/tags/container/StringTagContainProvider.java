package killercreepr.crux.api.text.tags.container;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface StringTagContainProvider extends TagContainer<StringResolver>, StringTagProvider {
    default StringTagContainProvider hookAll(@NotNull DataExchange info){
        TagContainer.super.hookAll(info);
        return this;
    }
    StringTagContainProvider hook(@Nullable Object info);
    StringTagContainProvider hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix);
    StringTagContainProvider add(@NotNull StringResolver resolver);
    StringTagContainProvider add(@NotNull StringResolver resolver, @Nullable FormatPrefix prefix);
    StringTagContainProvider addAll(@Nullable TagContainer<StringResolver> tags);
    StringTagContainProvider addAll(@Nullable Collection<StringResolver> tags);
    StringTagContainProvider addAll(@Nullable Collection<StringResolver> tags, @Nullable FormatPrefix prefix);
    StringTagContainProvider addExact(@NotNull StringResolver resolver, @NotNull String id);

    StringTagContainProvider addAll(@Nullable TagContainer<StringResolver> tags, @Nullable FormatPrefix prefix);
}
