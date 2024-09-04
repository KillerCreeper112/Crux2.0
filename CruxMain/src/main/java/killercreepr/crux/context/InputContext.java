package killercreepr.crux.context;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.format.FormatSerializer;
import killercreepr.crux.tags.provider.StringTagProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InputContext {
    static @NotNull InputContext simple(@Nullable StringTagProvider tags){
        return simple(Crux.FORMAT, tags);
    }

    static @NotNull InputContext simple(@NotNull FormatSerializer format, @Nullable StringTagProvider tags){
        return new SimpleInputContext(format, tags);
    }

    static @NotNull InputContext dummy(){
        return new DummyInputContext();
    }

    @NotNull String input(@NotNull String text);
}
