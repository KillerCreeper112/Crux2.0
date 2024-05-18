package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CollectionValue<T> extends CommonValue<Collection<T>> {
    protected final Class<?> firstType;
    public CollectionValue(@Nullable Collection<T> defaultValue, Class<?> firstType) {
        super((Class<Collection<T>>) (Class<?>) Collection.class, defaultValue);
        this.firstType = firstType;
    }

    public CollectionValue(@NotNull Class<?> firstType) {
        this(null, firstType);
    }

    @Override
    public @Nullable Collection<T> get(@NotNull CruxConfig cfg, @NotNull String path) {
        return (Collection<T>) cfg.deserializeObject(firstType, path);
    }
}
