package killercreepr.crux.tags.container;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.provider.StringListTagProvider;
import killercreepr.crux.tags.resolver.StringListResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface StringListTagContainProvider extends TagContainer<StringListResolver>, StringListTagProvider {
    default StringListTagContainProvider hookAll(@NotNull DataExchange info){
        TagContainer.super.hookAll(info);
        return this;
    }
    StringListTagContainProvider hook(@Nullable Object info);
    StringListTagContainProvider hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix);
    StringListTagContainProvider add(@NotNull StringListResolver resolver);
    StringListTagContainProvider add(@NotNull StringListResolver resolver, @Nullable FormatPrefix prefix);
    StringListTagContainProvider addAll(@Nullable TagContainer<StringListResolver> tags);
    StringListTagContainProvider addAll(@Nullable Collection<StringListResolver> tags);
    StringListTagContainProvider addAll(@Nullable Collection<StringListResolver> tags, @Nullable FormatPrefix prefix);
    StringListTagContainProvider addExact(@NotNull StringListResolver resolver, @NotNull String id);

    StringListTagContainProvider addAll(@Nullable TagContainer<StringListResolver> tags, @Nullable FormatPrefix prefix);
}
