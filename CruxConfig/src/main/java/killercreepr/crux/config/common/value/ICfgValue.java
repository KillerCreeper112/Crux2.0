package killercreepr.crux.config.common.value;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICfgValue<T> extends Holder<T> {
    @NotNull
    IConfigValue<?, ?> getType();

    /**
     * @return The config path. If this is null, it will
     */
    @Nullable String getPath();
    @NotNull String @Nullable[] getComments();


    /**
     * @return The config value.
     */
    default @Nullable T get(){
        return cast(getType().getValue(), null);
    }

    /**
     * @return The config value OR, if the config value is null, returns the defaultValue.
     */
    default @Nullable T getOrDefault(@Nullable T defaultValue){
        if(getType().getValue() == null) return defaultValue;
        return cast(getType().getValue(), defaultValue);
    }

    default @Nullable T cast(@Nullable Object obj, @Nullable T classCastExceptionReturn) {
        try {
            return (T) obj;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return classCastExceptionReturn;
        }
    }

    @Override
    @Nullable
    default T value(){
        return get();
    }
}
