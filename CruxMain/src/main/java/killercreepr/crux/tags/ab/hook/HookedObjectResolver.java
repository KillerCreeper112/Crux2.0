package killercreepr.crux.tags.ab.hook;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HookedObjectResolver<I, O> extends ObjectResolver<I, O> {
    @NotNull Holder<I> getHookedObject();
    @Override
    default @Nullable O resolve(@NotNull FormatArgs args, @NotNull TextParserContext context){
        I object = getHookedObject().value();
        if(object==null) return null;
        return resolve(object, args, context);
    }
}
