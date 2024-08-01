package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileGenericEnum<T extends Enum<T>> extends SimpleFileHandler<T> {
    protected final Class<T> clazz;
    public FileGenericEnum(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        return new FilePrimitive(object.toString().toLowerCase());
    }

    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        try{
            return Enum.valueOf(clazz, s.getAsString().toUpperCase());
        }catch (IllegalArgumentException ignored){
            return null;
        }
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return clazz.getSimpleName().toLowerCase();
    }
}
