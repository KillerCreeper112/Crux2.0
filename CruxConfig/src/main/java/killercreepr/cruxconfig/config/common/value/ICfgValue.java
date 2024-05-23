package killercreepr.cruxconfig.config.common.value;

import killercreepr.crux.data.Holder;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.logging.Level;

public interface ICfgValue<T> extends Holder<T> {

    /**
     * @return The config path. If this is null, it will
     */
    @Nullable String getPath();
    @NotNull String @Nullable[] getComments();


    /**
     * @return The config value.
     */
    @Nullable T get();

    /**
     * @return The config value OR, if the config value is null, returns the defaultValue.
     */
    default T getOrDefault(@Nullable T defaultValue){
        if(get() == null) return defaultValue;
        return cast(get(), defaultValue);
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

    ICfgValue<T> setValue(@Nullable T value);
    boolean attemptSetValue(@Nullable Object value);
}
