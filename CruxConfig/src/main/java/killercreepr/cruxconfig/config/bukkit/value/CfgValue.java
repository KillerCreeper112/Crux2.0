package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.common.file.ICruxConfig;
import killercreepr.cruxconfig.config.common.value.IConfigValue;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Due to Java's types erasure, if you are using a Collection or Map,
 * you must create an anonymous class for it to properly get the
 * type argument.
 * <p>
 *  For example, if you create a CommonValue that holds a String value,
 *  you can just create it like: new CommonValue<>();
 *  <p>
 *  But if you want to create one that holds a String Collection, you must create it as:
 *  new CommonValue<>(){};
 *  (Note the {} brackets)
 *  <p>
 *  This is more of a workaround than an actual proper implementation, but it does work,
 *  and it works quite well enough for this system.
 *  <p>
 *  <a href="https://stackoverflow.com/questions/3437897/how-do-i-get-a-class-instance-of-generic-type-t">
 *      Check out this stackoverflow post, "How do I get a class instance of generic type T?"</a>
 *  <p>
 *  <a href="https://www.artima.com/weblogs/viewpost.jsp?thread=208860">Reflecting generics article</a>
 *  <p>
 *  <a href="https://stackoverflow.com/questions/20918650/what-are-the-benefits-of-javas-types-erasure">
 *      Benefits of Java's types erasure</a>
 */
public abstract class CfgValue<T> implements IConfigValue<T, CruxConfig> {
    protected final T defaultValue;
    protected T value;
    protected final @Nullable String path;
    protected final @NotNull String @Nullable[] comments;
    protected @NotNull Type parameterType;

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
        Bukkit.getLogger().severe("Resolved: " + parameterType);
    }

    protected Type resolveParameterType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return parameterizedType.getActualTypeArguments()[0];
    }

    /*protected Type resolveParameterType() throws UnsupportedOperationException {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length > 0) {
            Bukkit.getLogger().severe("Type args: " + typeArguments);
            return typeArguments[0];
        }
        throw new UnsupportedOperationException(parameterizedType + " does not have any type arguments!");
    }*/

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
        attemptSetValue(cfg.deserializeObject(parameterType, path));
        return getValue();
    }

    @Override
    public abstract @Nullable T get(@NotNull CruxConfig cfg, @NotNull String path);
    @Override
    public abstract void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable T object);

    protected @NotNull String addDot(@NotNull String s){ return ICruxConfig.addDot(s); }
    protected @NotNull String removeDot(@NotNull String s){ return ICruxConfig.removeDot(s); }
}
