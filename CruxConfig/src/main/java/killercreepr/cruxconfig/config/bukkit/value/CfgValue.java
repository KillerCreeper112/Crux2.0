package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.common.file.ICruxConfig;
import killercreepr.cruxconfig.config.common.value.IConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class CfgValue<T> implements IConfigValue<T, CruxConfig> {
    protected final T defaultValue;
    protected T value;
    protected final @Nullable String path;
    protected final @NotNull String @Nullable[] comments;
    protected final @NotNull Type parameterType;

    public CfgValue() {
        this(null);
    }

    public CfgValue(@Nullable T defaultValue) {
        this(defaultValue, null);
    }

    public CfgValue(@Nullable T defaultValue, @NotNull String @Nullable [] comments) {
        this(defaultValue, null, comments);
    }

    public CfgValue(@Nullable T defaultValue, @Nullable String path, @NotNull String @Nullable ... comments) {
        this.defaultValue = defaultValue;
        this.path = path;
        this.comments = comments;
        this.parameterType = resolveParameterType();
    }

    protected Type resolveParameterType() throws UnsupportedOperationException {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length > 0) {
            return typeArguments[0];
        }
        throw new UnsupportedOperationException(parameterizedType + " does not have any type arguments!");
    }

    @Override
    public @NotNull Type getParameterType() {
        return parameterType;
    }

    @Override
    public @Nullable T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public @Nullable T getValue() {
        return value;
    }

    @Override
    public @Nullable String getPath() {
        return path;
    }

    @Override
    public @NotNull String @Nullable [] getComments() {
        return comments;
    }

    @Override
    public void setValue(@Nullable T value) {
        this.value = value;
    }

    @Override
    public @Nullable T register(@NotNull CruxConfig cfg, @NotNull String path) {
        setValue(get(cfg, path));
        return getValue();
    }

    @Override
    public abstract @Nullable T get(@NotNull CruxConfig cfg, @NotNull String path);
    @Override
    public abstract void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable T object);

    protected @NotNull String addDot(@NotNull String s){ return ICruxConfig.addDot(s); }
    protected @NotNull String removeDot(@NotNull String s){ return ICruxConfig.removeDot(s); }
}
