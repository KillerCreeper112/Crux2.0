package killercreepr.crux.api.text.context;

import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.context.DummyInputContext;
import killercreepr.crux.core.text.context.SimpleInputContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InputContext {
    @Deprecated
    static @NotNull InputContext simple(){
        return simple((MergedTagContainer) null);
    }

    @Deprecated
    static @NotNull InputContext simple(@Nullable StringTagProvider tags){
        return simple(Crux.format(), tags);
    }

    @Deprecated
    static @NotNull InputContext simple(@NotNull FormatSerializer format, @Nullable StringTagProvider tags){
        return new SimpleInputContext(format, tags);
    }

    @Deprecated
    static @NotNull InputContext simple(@NotNull FormatSerializer format){
        return simple(format, null);
    }

    InputContext EMPTY = new SimpleInputContext(Crux.format(), null);

    static @NotNull InputContext inputContext(){
        return EMPTY;
    }

    static @NotNull InputContext inputContext(@Nullable StringTagProvider tags){
        return inputContext(Crux.format(), tags);
    }

    static @NotNull InputContext inputContext(@NotNull FormatSerializer format, @Nullable StringTagProvider tags){
        return new SimpleInputContext(format, tags);
    }

    static @NotNull InputContext inputContext(@NotNull FormatSerializer format){
        return simple(format, null);
    }

    static @NotNull InputContext dummy(){
        return new DummyInputContext();
    }

    @NotNull String input(@NotNull String text);
}
