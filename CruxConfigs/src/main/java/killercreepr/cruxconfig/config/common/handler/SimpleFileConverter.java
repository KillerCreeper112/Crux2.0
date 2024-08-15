package killercreepr.cruxconfig.config.common.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public abstract class SimpleFileConverter<F, T> implements FileConvertHandler<F, T>{
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull F object) {
        return ctx.getRegistry().serializeToFile(convertFrom(object), ctx);
    }

    @Override
    public @Nullable F deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        T from = ctx.getRegistry().deserializeFromFile(toType(), e, ctx);
        if(from==null) return null;
        return convertTo(from);
    }

    public abstract Type toType();
}
