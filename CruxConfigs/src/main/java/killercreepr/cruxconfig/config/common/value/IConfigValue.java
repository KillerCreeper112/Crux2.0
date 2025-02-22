package killercreepr.cruxconfig.config.common.value;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface IConfigValue<T, C extends DataFile> extends Holder<T> {
    @NotNull Type getParameterType();
    @Nullable T getDefaultValue();

    /**
     * @return The config value.
     */
    @Nullable T getValue();
    default T getOrDefaultValue(){
        T value = value();
        return value == null ? getDefaultValue() : value;
    }


    /**
     * @return The config path. If this is null, it will
     */
    @Nullable String getPath();
    @NotNull String @Nullable[] getComments();


    /**
     * @return The config value OR, if the config value is null, returns the defaultValue.
     */
    default T getOrDefault(@Nullable T defaultValue){
        if(getValue() == null) return defaultValue;
        return cast(getValue(), defaultValue);
    }

    default @Nullable T cast(@Nullable Object obj, @Nullable T classCastExceptionReturn) {
        try {
            return (T) obj;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return classCastExceptionReturn;
        }
    }

    @Nullable
    default T value(){
        return getValue();
    }

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
    default boolean attemptSetValue(@Nullable Object object){
        try{
            setValue((T) object);
            return true;
        }catch (ClassCastException ignored){
            return false;
        }
    }

    /**
     * Attempts to cast the provided object.
     */
    default @Nullable T attemptCast(@Nullable Object o) {
        if(o == null) return null;
        try{
            return (T) o;
        }catch (ClassCastException ignored){}
        return null;
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

    default boolean attemptSet(@NotNull C cfg, @NotNull String path, @Nullable Object object){
        if(object == null){
            set(cfg, path, null);
            return true;
        }
        T o = attemptCast(object);
        if(o==null) return false;
        set(cfg, path, o);
        return true;
    }

    /**
     * Serializes the default object of this config value in the provided config and path.
     */
    default void setDefault(@NotNull C cfg, @NotNull String path){
        set(cfg, path, getDefaultValue());
    }

    /**
     * Serializes the current object of this config value in the provided config and path.
     */
    default void setCurrentValue(@NotNull C cfg, @NotNull String path){
        set(cfg, path, getValue());
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
