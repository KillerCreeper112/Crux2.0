package killercreepr.cruxconfig.config.common.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileObjectConverter<F, T> extends FileObjectHandler<F>, FileConvertHandler<F, T>{
    @Override
    @Nullable
    default FileElement attemptSerializeToFile(@NotNull FileContext<?> context, @NotNull Object object) {
        return FileConvertHandler.super.attemptSerializeToFile(context, object);
    }
}
