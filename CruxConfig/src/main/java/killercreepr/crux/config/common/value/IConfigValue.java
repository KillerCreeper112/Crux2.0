package killercreepr.crux.config.common.value;

import killercreepr.crux.config.common.file.ICruxConfig;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IConfigValue<T, C extends ICruxConfig<?>> {
    @NotNull Class<T> getType();

    @Nullable T getDefaultValue();

    @Nullable T getValue();

    /**
     * Returns the default value if the value is null. Otherwise returns the value.
     */
    default T getOrDefaultValue(@Nullable T defaultValue){
        T value = getValue();
        if(value == null) return defaultValue;
        return value;
    }

    /**
     * Attempts to cast the value from the provided class.
     * If the value is null or cannot be cast, null will be returned.
     */
    default @Nullable <I> I getValue(@NotNull Class<I> tryClass){
        return getValue(tryClass, null);
    }
    /**
     * Attempts to cast the value from the provided class.
     * If the value is null or cannot be cast, the default value will be returned.
     */
    default <I> I getValue(@NotNull Class<I> tryClass, @Nullable I defaultValue){
        T value = getValue();
        if(value == null) return defaultValue;
        return tryClass.isAssignableFrom(value.getClass()) ? tryClass.cast(value) : defaultValue;
    }

    void setValue(@Nullable T value);

    /**
     * Attempts to cast the object and set the value.
     */
    default void attemptSetValue(@Nullable Object object){
        try{
            setValue((T) object);
        }catch (ClassCastException ignored){
        }
    }

    /**
     * Attempts to cast the provided object.
     */
    default @Nullable T attemptCast(@Nullable Object o) {
        if(o == null) return null;
        Class<T> clazz = getType();
        return o.getClass().isAssignableFrom(clazz) ? clazz.cast(o) : null;
    }

    /**
     * Parses the value from the provided configuration and path.
     * The value should then be stored.
     */
    @Nullable T register(@NotNull C cfg, @NotNull String path);

    /**
     * Attempts to parse and deserialize the object from the provided config and path.
     */
    @Nullable T get(@NotNull C cfg, @NotNull String path);
    /**
     * Attempts to serialize the given object in the provided config and path.
     */
    void set(@NotNull C cfg, @NotNull String path, @Nullable T object);

    /**
     * Serializes the default object of this config value in the provided config and path.
     */
    default void setDefault(@NotNull C cfg, @NotNull String path){
        set(cfg, path, getDefaultValue());
    }

    //Convenience methods.
    default @Nullable String getString(){
        if(getValue() instanceof String x) return x;
        return null;
    }

    default @Nullable Number getNumber(){
        return getNumber(getValue());
    }

    default @Nullable Number getNumber(@Nullable Object o){
        if(o instanceof Number x) return x;
        if(o instanceof NumberProvider pr) return pr.value();
        return null;
    }

    default @NotNull Number getNumberOrDefault(@Nullable Number number){
        Number x = getNumber();
        if(x == null) return number == null ? 0 : number;
        return x;
    }

    default int getInt(){
        return getNumberOrDefault(getNumber(getDefaultValue())).intValue();
    }

    default double getDouble(){
        return getNumberOrDefault(getNumber(getDefaultValue())).doubleValue();
    }

    default float getFloat(){
        return getNumberOrDefault(getNumber(getDefaultValue())).floatValue();
    }

    default long getLong(){
        return getNumberOrDefault(getNumber(getDefaultValue())).longValue();
    }

    default boolean getBoolean(){
        if(getValue() instanceof Boolean x) return x;
        return getDefaultValue() instanceof Boolean x ? x : false;
    }
}
