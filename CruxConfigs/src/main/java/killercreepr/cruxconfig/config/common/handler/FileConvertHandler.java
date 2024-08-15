package killercreepr.cruxconfig.config.common.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileConvertHandler<F, T> {
    default @Nullable FileElement attemptSerializeToFile(@NotNull FileContext<?> context, @NotNull Object object){
        try{
            return serializeToFile(context, (F) object);
        }catch (ClassCastException ignored){ return null; }
    }
    @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull F object);
    @Nullable
    F deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e);

    F convertTo(T to);
    T convertFrom(F from);
}
