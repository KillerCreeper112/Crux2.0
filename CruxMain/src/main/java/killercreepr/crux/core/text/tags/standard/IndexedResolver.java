package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.registry.index.Index;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IndexedResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "indexed";
    }

    //<indexed:<index_key>:<key>>
    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        Index<String, String> index = CruxRegistries.STRING_INDEXES.get(Crux.key(context.deserializeString(args.get(0))));
        String key = context.deserializeString(args.get(1));
        if(args.has(2) && CruxString.parseBoolean(context.deserializeString(args.get(2)))){
            return index.key(key) + "";
        }
        return index.value(key) + "";
    }
}
