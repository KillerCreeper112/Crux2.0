package killercreepr.cruxmenus.api.menu.config;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileDataProvider {
    static @NotNull FileDataProvider generic(@NotNull Class<?> parseClass){
        return new FileDataProvider() {
            @Override
            public @Nullable Object deserialize(@NotNull FileContext<?> context, @NotNull FileElement base, @Nullable FileObject menuContext,
                                                @NotNull FileElement e) {
                return context.getRegistry().deserializeFromFile(parseClass, e, context);
                //return context.getRegistry().deserializeObject(parseClass, e, context);
            }
        };
    }

    @Nullable Object deserialize(@NotNull FileContext<?> context, @NotNull FileElement base, @Nullable FileObject menuContext,
                                 @NotNull FileElement e);
}
